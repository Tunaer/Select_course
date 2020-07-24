package com.example.selectcourse.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.util.HttpSender;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author tunaer
 * 07.23 22:30
 * 课程实体类
 */
public class SelectService {
    // 选课，阻塞到请求返回
    public static String selectCourse(List<Course> toUpload) {
        String url, method;
        url = "/opt/select";
        method = "POST";
        String courses = coursesToJsonString(toUpload);
        HashMap<String, String> idMap = new HashMap<>(1);
        idMap.put("courses", courses);
        Semaphore sem = new Semaphore(0);
        AtomicReference<String> result = new AtomicReference<>();
        HttpSender.requestForJson(url, method, idMap, json -> {
            if (json == null) result.set("网络故障");
            else if (json.getInteger("state") == 0) result.set("登录无效");
            else result.set("新选了" + json.getString("msg") + "门课");
            sem.release();
        });
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result.get();
    }

    // 获取已选课程
    public static void getAllCourses(Consumer<List<Course>> callback) {
        HttpSender.requestForJson("/opt/selected", "GET", null, json -> {
            if (json == null || json.getJSONArray("object") == null) callback.accept(null);
            else {
                JSONArray rawCourses = json.getJSONArray("object");
                List<Course> courses = rawCourses.stream().
                        map(rawObj -> Course.fromJsonObject((JSONObject) (rawObj))).
                        collect(Collectors.toList());
                callback.accept(courses);
            }
        });
    }

    // 退课请求，调用后会阻塞直到请求返回
    public static String cancelCourses(List<Course> toDelete) {
        Semaphore sem = new Semaphore(0);
        String arrString = coursesToJsonString(toDelete);
        Map<String, String> idMap = new HashMap<>();
        idMap.put("courses", arrString);
        AtomicReference<String> result = new AtomicReference<>();
        HttpSender.requestForJson("/opt/cancel", "DELETE", idMap, json -> {
            if (json == null) result.set("网络故障");
            else if (json.getInteger("state") == 0) result.set("登录无效");
            else result.set("已删除" + json.getString("msg") + "门课程");  // 成功的情况
            sem.release();
        });
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result.get();
    }

    private static String coursesToJsonString(List<Course> toParse) {
        JSONArray toCancelArr = new JSONArray();
        toParse.forEach(course -> toCancelArr.add(course.getType().getId() + course.getId()));
        String arrString = "[]";
        try {
            arrString = URLEncoder.encode(toCancelArr.toJSONString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return arrString;
    }
}

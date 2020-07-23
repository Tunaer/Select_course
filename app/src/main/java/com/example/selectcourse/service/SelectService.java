package com.example.selectcourse.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.util.HttpSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;
/**
 * @author tunaer
 * 07.23 22:30
 * 课程实体类
 */
public class SelectService {
    // 选课
    public static void selectCourse(Course toUpload, Consumer<String> callback) {
        String url, method;
        url = "/opt/select";
        method = "POST";

        HttpSender.requestForJson(url, method, toUpload.toParamMap(),
                json -> {
                    if (json == null) callback.accept("网络故障");
                    else if (json.getInteger("state") == 0) callback.accept("权限不足或登录过期");
                    else callback.accept(null);
                });
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

    public static String cancelCourses(List<Course> toDelete) {
        CountDownLatch countDown = new CountDownLatch(toDelete.size());
        StringBuilder sb = new StringBuilder();
        toDelete.forEach(course->{
            Map<String, String> idMap = new HashMap<>();
            idMap.put("course_id", course.getType().getId()+course.getId());
//            idMap.put("course_id", course.getType().getId()+course.getId());
            HttpSender.requestForJson("/opt/cancel", "DELETE", idMap, json->{
                countDown.countDown();
//                if(json == null || json.getInteger("state") != 1)
//                    sb.append(course.getId()).append(" ");
            });
        });
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(sb.length()!=0){
            sb.append("课程退课失败");
            return sb.toString();
        }else{
            return "退课成功";
        }
    }
}

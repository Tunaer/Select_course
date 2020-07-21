package com.example.selectcourse.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.util.HttpSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author cxlm
 * 07.20 23:26
 * 课程相关业务处理逻辑，静态类
 */
public class CourseService {

    // 检查表单，返回错误信息，为 null 时则检查通过
    public static String checkForm(Course course) {
        int temp;
        if ((temp = course.getCourseName().length()) == 0 || temp > 30)
            return "课程名过于诡异";
        if (course.getHour() == 0)
            return "学时不能为0";
        if (course.getId().length() != 4)
            return "课程编号必须为四位";
        if (course.getWay() == null || course.getWay().isEmpty())
            return "考核方式不能为空";
        return null;
    }

    // 修改、新增课程
    public static void uploadCourse(Course toUpload, boolean modify, Consumer<String> callback) {
        String url, method;
        if (modify) {
            url = "/opt/modify";
            method = "PUT";
        } else {
            url = "/opt/new";
            method = "POST";
        }
        HttpSender.requestForJson(url, method, toUpload.toParamMap(),
                json -> {
                    if (json == null) callback.accept("网络故障");
                    else if (json.getInteger("state") == 0) callback.accept("权限不足或登录过期");
                    else callback.accept(null);
                });
    }

    // 获取所有课程
    public static void getAllCourses(Consumer<List<Course>> callback) {
        HttpSender.requestForJson("/opt/list", "GET", null, json -> {
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
}

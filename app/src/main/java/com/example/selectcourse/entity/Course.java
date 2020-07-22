package com.example.selectcourse.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author cxlm
 * 07.20 22:46
 * 课程实体类
 */
public class Course implements Serializable {
    private String courseName;
    private String id;  // 课程编号后四位
    private int hour;
    private String way;
    private CourseType type;

    public String getTeacherName() {
        return teacherName;
    }

    private String teacherName;

    public String getCourseName() {
        return courseName;
    }

    public String getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public String getHourStr() {
        return hour + "";
    }

    public String getWay() {
        return way;
    }

    public CourseType getType() {
        return type;
    }

    public Course(String courseName, String id, int hour, String way, String type) {
        this.courseName = courseName;
        this.id = id;
        this.hour = hour;
        this.way = way;
        this.type = CourseType.parse(type);
    }

    private Course() {
    }

    /**
     * 将本对象转化为参数 Map，如果实体类较多的话应考虑使用反射
     */
    public HashMap<String, String> toParamMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", type.getId() + id);
        result.put("courseName", courseName);
        result.put("hour", hour + "");
        result.put("way", way);
        return result;
    }

    public static Course fromJsonObject(JSONObject obj) {
        Course res = new Course();
        String rawId = obj.getString("id");
        res.type = CourseType.parse(rawId.substring(0, 2));
        res.id = rawId.substring(2);
        res.courseName = obj.getString("courseName");
        res.hour = obj.getInteger("hour");
        res.way = obj.getString("way");
        res.teacherName = obj.getJSONObject("teacher").getString("userName");
        return res;
    }
}

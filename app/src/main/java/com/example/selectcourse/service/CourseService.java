package com.example.selectcourse.service;

import com.example.selectcourse.entity.Course;

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
        if (course.getId().length() != 4)
            return "课程编号必须为四位";
        if (course.getHour() == 0)
            return "学时不能为0";
        if (course.getWay().isEmpty())
            return "考核方式不能为空";
        return null;
    }

    // TODO: 发布课程请求
    // TODO： 修改课程请求
}

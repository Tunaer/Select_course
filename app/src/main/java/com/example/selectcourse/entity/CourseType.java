package com.example.selectcourse.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cxlm
 * 07.20 22:49
 * 写死的课程类别
 */
public enum CourseType implements Serializable {
    SCIENCE("01", "理工类"),
    LITERATURE("02", "文史类"),
    ART("03", "艺术类"),
    GENERAL("04", "通识类"),
    INNOVATION("05", "创新类");

    private String id;
    private String name;

    CourseType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 字符串转枚举
    public static CourseType parse(String val) {
        switch (val) {
            case "01":
            case "理工类":
                return SCIENCE;
            case "02":
            case "文史类":
                return LITERATURE;
            case "03":
            case "艺术类":
                return ART;
            case "04":
            case "通识类":
                return GENERAL;
            case "05":
            case "创新类":
                return INNOVATION;
            default:
                return null;
        }
    }

    // 获取所有课程类别名称
    public static List<String> getTypeList(){
        CourseType []types = CourseType.values();
        ArrayList<String> typeList = new ArrayList<>(types.length);
        for (CourseType value : types) {
            typeList.add(value.name);
        }
        return typeList;
    }
}

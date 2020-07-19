package com.example.selectcourse.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * @author cxlm
 * 07.19 12:39
 * Session，静态类，对 HashMap<String, String> 的简单封装
 */
public class Session {
    private static HashMap<String, String> data;

    /**
     * 数据备份到文件
     */
    public static void backup(FileOutputStream fos) {
        FileUtil.write(fos, data);
    }

    public static String get(String key) {
        if (data == null) return null;
        return data.get(key);
    }

    public static void set(String key, String value) {
        if (data == null) data = new HashMap<>();
        data.put(key, value);
    }

    public static void load(String dataStr) {
        data = new HashMap<>();
        for (String line : dataStr.split(FileUtil.BR)) {
            String[] kv = line.split(":");
            if (kv.length != 2) continue;
            data.put(kv[0], kv[1]);
        }
    }
}

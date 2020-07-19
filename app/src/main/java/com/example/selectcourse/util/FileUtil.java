package com.example.selectcourse.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cxlm
 * 07.19 12:42
 * 文件读写工具，静态类，操作的文件内容格式如下：
 * key1:value1
 * key2:value2
 */
public class FileUtil {
    public static final String BR = "\n";  // 换行符
    public static final String FILENAME = "user.data";

    /**
     * 数据写入文件
     */
    public static void write(FileOutputStream fos, HashMap<String, String> data) {
        if (fos == null) return;
        try {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                fos.write((entry.getKey() + ":" + entry.getValue() + BR).getBytes());
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据读取
     */
    public static String read(FileInputStream fis) {
        if (fis == null) return null;
        StringBuilder sb = new StringBuilder();
        try {
            byte[] buffer = new byte[256];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

package com.example.selectcourse.http;


import android.os.Build;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpSender {
    private static final String SERVER_BASE = "http://cxlm.work/course";
    private static final HashMap<String, List<Cookie>> COOKIE_MAP = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void request(String urlSuffix, String method, Map<String, String> parameters,
                                     final Callback callback) {
        // 不要跟老夫谈什么请求正文，管他什么用户信息还是密码，通通明文传输（有空可以尝试做一下，使用 RequestBodyBuilder 构建）
        String urlParam = "";
        if(parameters.size() != 0){  // 如果传入了参数，需要拼接 URL
            StringBuilder sb = new StringBuilder("?");
            parameters.forEach((k, v)->{
                try {
                    // 构建成 “?a=1&b=2&” 的形式
                    sb.append(k).append("=").append(URLEncoder.encode(v, "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            // 去掉结尾多余的 &
            sb.deleteCharAt(sb.length()-1);
            urlParam = sb.toString();
        }
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            // 保存 Cookie
            @Override
            public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                COOKIE_MAP.put(httpUrl.host(), list);
            }
            // 读取 Cookie
            @NotNull
            @Override
            public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                List<Cookie> cookies = COOKIE_MAP.get(httpUrl.host());
                return cookies == null ? new LinkedList<>(): cookies;
            }
        }).build();
        Request request = new Request.Builder().url(SERVER_BASE + urlSuffix + urlParam).method(method, null).build();
        Call call = client.newCall(request);  // 发送请求
        call.enqueue(callback);  // 回调
    }
}

package com.example.selectcourse.util;


import android.os.Build;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpSender {
    private static final String SERVER_BASE = "http://cxlm.work/course";
    private static final HashMap<String, List<Cookie>> COOKIE_MAP = new HashMap<>();
    private static final String COOKIE_HOST = "cxlm.work";

    static {
        // 从文件中读取 Cookie
        String cookieStr = Session.get("JSESSIONID");
        if (cookieStr != null) {
            Cookie loginCookie = new Cookie.Builder().domain(COOKIE_HOST).
                    name("JSESSIONID").value(cookieStr).path("/").httpOnly().build();
            LinkedList<Cookie> cookies = new LinkedList<>();
            cookies.add(loginCookie);
            COOKIE_MAP.put(COOKIE_HOST, cookies);
        }
    }

    /**
     * 清空 COOKIE，在重新登录时可能需要，调用后，本地 COOKIE 全部失效，需要重新登录，否则大部分接口都将返回错误
     */
    public static void cleanCookie() {
        COOKIE_MAP.clear();
    }

    /**
     * 发送请求，需要传入回调函数，需要自己解析响应报文。
     * 自动管理 Cookie
     */
    public static void request(String urlSuffix, String method, Map<String, String> parameters,
                               final Callback callback) {
        // 不要跟老夫谈什么请求正文，管他什么用户信息还是密码，通通明文传输（有空可以尝试做一下，使用 RequestBodyBuilder 构建）
        String urlParam = "";
        if (parameters.size() != 0) {  // 如果传入了参数，需要拼接 URL
            StringBuilder sb = new StringBuilder("?");
            parameters.forEach((k, v) -> {
                try {
                    // 构建成 “?a=1&b=2&” 的形式
                    sb.append(k).append("=").append(URLEncoder.encode(v, "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            // 去掉结尾多余的 &
            sb.deleteCharAt(sb.length() - 1);
            urlParam = sb.toString();
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)  // 处理 BUG： 不可预知的响应结尾
                .cookieJar(new CookieJar() {
                    // 保存 Cookie
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        COOKIE_MAP.put(httpUrl.host(), list);
                        list.forEach(item -> {  // 登录凭证特殊处理
                            if (item.name().equals("JSESSIONID")) {
                                Session.set("JSESSIONID", item.value());
                            }
                        });
                    }

                    // 读取 Cookie
                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies = COOKIE_MAP.get(httpUrl.host());
                        return cookies == null ? new LinkedList<>() : cookies;
                    }
                }).build();
        Request.Builder builder = new Request.Builder();
        builder.url(SERVER_BASE + urlSuffix + urlParam);
        // builder.addHeader("Connection", "close");
        if(method.equals("PUT") || method.equals("POST")){
            RequestBody body = new FormBody.Builder().build();  // 空的请求正文
            builder.method(method, body);
            builder.addHeader("Content-Length", "0");
        }else{
            builder.method(method, null);
        }
        Request request = builder.build();
        Call call = client.newCall(request);  // 发送请求
        call.enqueue(callback);  // 回调
    }

    /**
     * 简单封装，将报文解析成 JSONObject，请求失败时传入 null，仍需要传入回调函数
     *
     * @param urlSuffix  接口后缀，http://cxlm.work/course 后到参数列表前的部分，如 "/user/login"
     * @param method     请求方式，GET、POST、PUT、DELETE
     * @param parameters 参数列表
     * @param callback   处理 JSONObject 的回调函数
     */
    public static void requestForJson(String urlSuffix, String method, Map<String, String> parameters,
                                      final Consumer<JSONObject> callback) {
        request(urlSuffix, method, parameters, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.accept(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = Objects.requireNonNull(response.body()).string();
                JSONObject obj = JSONObject.parseObject(responseStr);
                callback.accept(obj);
            }
        });
    }
}

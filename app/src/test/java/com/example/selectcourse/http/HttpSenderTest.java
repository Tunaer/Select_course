package com.example.selectcourse.http;

import com.example.selectcourse.util.HttpSender;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HttpSenderTest {
    /*
    @Test
    public void request() throws InterruptedException {
        HashMap<String, String> loginPar = new HashMap<>();
        loginPar.put("email", "t@t.t");
        loginPar.put("pwd", "5");
        Semaphore sem = new Semaphore(0);  // 使用信号量，等待回调函数执行
        HttpSender.request("/user/login", "PUT", loginPar, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.err.println("请求失败");
                e.printStackTrace();
                sem.release();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = Objects.requireNonNull(response.body()).string();
                System.out.println(responseStr);
                sem.release();
            }
        });
        // 等待结果
        sem.acquire();
    }
     */
}
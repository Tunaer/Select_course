package com.example.selectcourse.service;

import com.alibaba.fastjson.JSONObject;
import com.example.selectcourse.util.HttpSender;

import java.util.HashMap;
import java.util.function.Consumer;


/**
 * @author cxlm
 * 07.19 11:17
 * 用户相关业务逻辑，静态类
 * 针对用户忘记了创建实体类，但是不想改了
 */
public class UserService {

    public static void login(String username, String pwd, Consumer<JSONObject> callback) {
        HttpSender.cleanCookie();  // 清除原有登录凭证
        // 构建请求参数
        HashMap<String, String> params = new HashMap<>();
        params.put("email", username);
        params.put("pwd", pwd);
        // 发送请求，callback.accept(data) 表示将数据传递到 callback 中，由 callback 进行处理
        // Consumer<T> callback 表示接受一个参数的函数，参数类型为T，无返回值
        HttpSender.requestForJson("/user/login", "GET", params,
                json -> callback.accept(json == null ? null : json.getJSONObject("object")));
    }

//    public static void signin(String username,String pwd,String email,String admin,Consumer<JSONObject> callback){
//        HashMap<String, String> params = new HashMap<>();
//        params.put("username", username);
//        params.put("pwd", pwd);
//        params.put("email",email);
//        params.put("admin",admin);

//        HttpSender.requestForJson("/user/signin", "PUSH", params,
//                json -> callback.accept(json == null ? null : json.getJSONObject("object")));
//    }



}

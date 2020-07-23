package com.example.selectcourse.service;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.example.selectcourse.ui.RegisterActivity;
import com.example.selectcourse.ui.popup.ToastUtil;
import com.example.selectcourse.util.HttpSender;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;


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

    public static void signIn(String username, String pwd1, String pwd2, String email,
                              String adminKey, Context that, Consumer<Boolean> callback) {
        Pattern emailPattern = Pattern.compile("^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        if (username.isEmpty()) {
            ToastUtil.show(that, "名字太短");
        } else if (!emailPattern.matcher(email).matches()) {
            ToastUtil.show(that, "邮箱格式错误");
        } else if (!pwd1.equals(pwd2)) {
            ToastUtil.show(that, "两次密码输入不一致");
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("pwd", pwd1);
            params.put("email", email);
            params.put("key", adminKey);

            HttpSender.requestForJson("/user/signin", "POST", params, json -> {
                if (json == null) {
                    callback.accept(false);
                    return;
                }
                if (json.getInteger("state") == 0) {
                    callback.accept(false);
                    ToastUtil.show(that, json.getString("msg"));
                } else if (json.getJSONObject("object").getBoolean("admin")) {
                    callback.accept(true);
                    ToastUtil.show(that, "注册身份为管理员");
                } else {
                    callback.accept(true);
                    ToastUtil.show(that, "注册身份为学生");
                }
            });
        }
    }

    public static void sendCode(String email, Consumer<String> cb) {
        HashMap<String, String> m = new HashMap<>(1);
        m.put("email", email);
        HttpSender.requestForJson("/user/code", "GET", m, json -> {
            if (json == null) cb.accept("网络故障");
            else if (json.getInteger("state") == 0) cb.accept(json.getString("msg"));
            else cb.accept("已发送");
        });
    }

    public static void updatePwd(String email, String newPwd, String code, Consumer<String> cb) {
        HashMap<String, String> m = new HashMap<>(3);
        m.put("email", email);
        m.put("newPwd", newPwd);
        m.put("code", code);
        HttpSender.requestForJson("/user/change", "PUT", m, json -> {
            if (json == null) cb.accept("网络故障");
            else if (json.getInteger("state") == 0) cb.accept(json.getString("msg"));
            else cb.accept(null);
        });
    }

}

package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selectcourse.R;
import com.example.selectcourse.service.UserService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.util.FileUtil;
import com.example.selectcourse.util.Session;
import com.example.selectcourse.ui.popup.ToastUtil;

import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {
    EditText usernameInput, pwdInput;
    Button register, login, manage;
    TextView getpwd;

    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button) findViewById(R.id.login_btn_signin);
        login = (Button) findViewById(R.id.login_btn_login);
        usernameInput = (EditText) findViewById(R.id.login_inp_name);
        pwdInput = (EditText) findViewById(R.id.login_inp_pwd);
        getpwd = (TextView) findViewById(R.id.login_text_forget);

        //跳转注册
        register.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, register.class);
            startActivity(intent);
        });

        //密码找回
        getpwd.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, getpwd.class);
            startActivity(intent);
        });

        // 初始化加载动画
        dialog = LoadingDialog.createLoading(LoginActivity.this);

        // 这一部分用于检查用户登录记录，如果已有登录记录则直接读取并跳转，省去重复登录
        // 这样可能需要在用户主界面添加重新登录选项，如果不想要此功能，直接注释掉这一部分即可
        String adminStr = Session.get("admin");
        if(adminStr != null){
            boolean isAdmin = Boolean.parseBoolean(adminStr);
            loginSuccess(isAdmin);
        }
    }

    /**
     * 检查登录凭证并登录
     */
    public void onLogin(View view) {
        String username = usernameInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        // 请求登录，箭头函数用法与 ES6 类似，这里表示 JDK8 的 Consumer<T>
        // 这里的 res 表示在 login 函数中 accept 的参数，为一个 JSONObject
        UserService.login(username, pwd, res -> {
            dialog.close();
            if (res != null) {
                boolean admin = res.getBoolean("admin");
                Session.set("username", res.getString("userName"));
                Session.set("email", res.getString("email"));
                Session.set("admin", admin + "");
                loginSuccess(admin);
                try {
                    Session.backup(openFileOutput(FileUtil.FILENAME, Context.MODE_PRIVATE));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.show(LoginActivity.this, "登录失败");
            }
        });
        dialog.show("登录中");
    }

    /** 跳转 */
    public void loginSuccess(boolean admin) {
        Intent intent;
        if (admin) { // 如果是管理员则登录到管理页面
            intent = new Intent(LoginActivity.this, ManageActivity.class);
        } else {  // 否则登录到学生页面
            intent = new Intent(LoginActivity.this, StudentView.class);
        }
        startActivity(intent);
    }

}
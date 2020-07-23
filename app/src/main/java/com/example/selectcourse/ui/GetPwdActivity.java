package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.selectcourse.R;
import com.example.selectcourse.service.UserService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.ui.popup.ToastUtil;

public class GetPwdActivity extends AppCompatActivity {
    EditText email,pwd1,pwd2, codeInp;
    Button confirm;

    private LoadingDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpwd);

        email = (EditText) findViewById(R.id.login_inp_pwd);
        pwd1 = (EditText) findViewById(R.id.editText3);
        pwd2 = (EditText) findViewById(R.id.reg_username);
        confirm = (Button) findViewById(R.id.button);
        codeInp = findViewById(R.id.pwd_code_inp);
        loading = LoadingDialog.createLoading(GetPwdActivity.this);

        //找回成功
        confirm.setOnClickListener(view -> {
            loading.show("请求中，请稍候");
            String emailAdd = email.getText().toString();
            String p1 = pwd1.getText().toString();
            String p2 = pwd2.getText().toString();
            if(!p1.equals(p2)){
                loading.close();
                ToastUtil.show(GetPwdActivity.this, "两次密码不一致");
                return;
            }
            String code = codeInp.getText().toString();
            UserService.updatePwd(emailAdd, p1, code, msg->{
                loading.close();
                if(msg==null){
                    finish();
                    ToastUtil.show(GetPwdActivity.this, "修改成功");
                }else{
                    ToastUtil.show(GetPwdActivity.this, msg);
                }
            });
        });
    }

    // 发送验证码
    public void sendCode(View view){
        String emailAdd = email.getText().toString();
        UserService.sendCode(emailAdd, msg->{
            loading.close();
            ToastUtil.show(GetPwdActivity.this, msg);
        });
        loading.show("请求中，请稍候");
    }
}
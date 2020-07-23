package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.selectcourse.R;
import com.example.selectcourse.service.UserService;
import com.example.selectcourse.ui.popup.ToastUtil;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText username,pwd1,pwd2,email,key;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //获取
        confirm = (Button) findViewById(R.id.button15);
        username = (EditText) findViewById(R.id.reg_username);
        pwd1 = (EditText) findViewById(R.id.editText5);
        pwd2 = (EditText) findViewById(R.id.editText6);
        email = (EditText) findViewById(R.id.editText7);
        key = (EditText) findViewById(R.id.editText8);
    }

    public void onConfirm(View view){
        String name=username.getText().toString();
        String emailStr=email.getText().toString();
        String pwd1Str = pwd1.getText().toString();
        String pwd2Str = pwd2.getText().toString();
        String adminKey = key.getText().toString();
        UserService.signIn(name, pwd1Str, pwd2Str, emailStr, adminKey, this, success->{
            if(success) finish();
        });
    }
}
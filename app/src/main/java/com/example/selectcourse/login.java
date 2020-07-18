package com.example.selectcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    EditText username,pwd;
    Button register,login,manage;
    TextView getpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button) findViewById(R.id.button6);
        login = (Button) findViewById(R.id.button5);
        username = (EditText) findViewById(R.id.editText);
        pwd = (EditText) findViewById(R.id.editText2);
        getpwd = (TextView) findViewById(R.id.textView4);
        manage = (Button) findViewById(R.id.button7);

        //转到管理员页面
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(com.example.selectcourse.login.this,Manage.class);
                Toast.makeText(com.example.selectcourse.login.this,"管理员页面",Toast.LENGTH_SHORT);
                startActivity(intent);
            }
        });

        //跳转注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(com.example.selectcourse.login.this,register.class);
                startActivity(intent);
            }
        });
        //密码找回
        getpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(com.example.selectcourse.login.this,getpwd.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.selectcourse.R;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.entity.CourseType;

public class ManageActivity extends AppCompatActivity {
    EditText courseid,coursername;
    Button select,insert,update,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        courseid = (EditText) findViewById(R.id.editText1);
        coursername = (EditText) findViewById(R.id.login_inp_pwd);
        select = (Button) findViewById(R.id.button2);
        insert = (Button) findViewById(R.id.admin_btn_insert);
        update = (Button) findViewById(R.id.button3);
        delete = (Button) findViewById(R.id.button4);

        //跳转添加
        insert.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(ManageActivity.this, CourseUpdateActivity.class);
            startActivity(intent);
        });

    }
}
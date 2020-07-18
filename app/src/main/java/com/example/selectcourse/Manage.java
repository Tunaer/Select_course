package com.example.selectcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Manage extends AppCompatActivity {
    EditText courseid,coursername;
    Button select,insert,update,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        courseid = (EditText) findViewById(R.id.editText1);
        coursername = (EditText) findViewById(R.id.editText2);
        select = (Button) findViewById(R.id.button2);
        insert = (Button) findViewById(R.id.button1);
        update = (Button) findViewById(R.id.button3);
        delete = (Button) findViewById(R.id.button4);

        //跳转添加
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Manage.this,course_update.class);
                startActivity(intent);
            }
        });

    }
}
package com.example.selectcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class getpwd extends AppCompatActivity {
    EditText id,email,pwd1,pwd2;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpwd);

        id = (EditText) findViewById(R.id.editText1);
        email = (EditText) findViewById(R.id.editText2);
        pwd1 = (EditText) findViewById(R.id.editText3);
        pwd2 = (EditText) findViewById(R.id.editText4);
        confirm = (Button) findViewById(R.id.button);

        //找回成功
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(getpwd.this,activity_login.class);
//                startActivity(intent);
//            }
//        });

    }
}
package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.selectcourse.R;

public class getpwd extends AppCompatActivity {
    EditText id,email,pwd1,pwd2;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpwd);

        id = (EditText) findViewById(R.id.editText1);
        email = (EditText) findViewById(R.id.login_inp_pwd);
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
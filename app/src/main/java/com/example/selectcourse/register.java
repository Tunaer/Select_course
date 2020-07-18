package com.example.selectcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class register extends AppCompatActivity {
    EditText username,pwd1,pwd2,email,key;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        confirm = (Button) findViewById(R.id.button15);
        username = (EditText) findViewById(R.id.editText4);
        pwd1 = (EditText) findViewById(R.id.editText5);
        pwd2 = (EditText) findViewById(R.id.editText6);
        email = (EditText) findViewById(R.id.editText7);
        key = (EditText) findViewById(R.id.editText8);
    }
}
package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.selectcourse.R;
import com.example.selectcourse.service.UserService;
import com.example.selectcourse.ui.popup.ToastUtil;
import com.example.selectcourse.util.FileUtil;
import com.example.selectcourse.util.Session;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


public class StudentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


    }

}
package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.selectcourse.R;
import com.example.selectcourse.util.FileUtil;
import com.example.selectcourse.util.Session;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    TextView hop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 申请权限
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 404);
        }
        init();
        hop = (TextView) findViewById(R.id.textView10);
        hop.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // 初始化工具
    private void init() {
        try {
            // 数据文件路径：/data/user/0/com.example.selectcourse/files/user.data
            FileInputStream fis = openFileInput(FileUtil.FILENAME);
            Session.load(FileUtil.read(fis));
        }catch (FileNotFoundException e) {
            System.out.println("数据文件不存在");
        }
    }
}
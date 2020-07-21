package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.selectcourse.R;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.entity.CourseType;
import com.example.selectcourse.service.CourseService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.ui.popup.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ManageActivity extends AppCompatActivity {
    EditText courseid, coursername;
    Button select, insert, update, delete;

    private ListView courseListView;

    private String[] keys = new String[]{"title", "sub"};
    List<Map<String, String>> coursesToShow = new LinkedList<>();
    SimpleAdapter adapter;

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
        courseListView = findViewById(R.id.course_list);

        adapter = new SimpleAdapter(ManageActivity.this, coursesToShow,
                R.layout.course_list_item, keys, new int[]{R.id.course_title, R.id.course_sub});
        initList();

        //跳转添加
        insert.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(ManageActivity.this, CourseUpdateActivity.class);
            startActivity(intent);
        });

    }

    private void initList() {
        LoadingDialog dialog = LoadingDialog.createLoading(ManageActivity.this);
        dialog.show("请求数据中");
        Semaphore lock = new Semaphore(0);  // 线程同步锁
        CourseService.getAllCourses(courses -> {
            if(courses == null){
                dialog.close();
                finish();
                ToastUtil.show(ManageActivity.this, "登录已过期或网络故障");
                return;
            }
            courses.forEach(course -> {
                Map<String, String> courseData = new HashMap<>();
                courseData.put(keys[0], course.getCourseName() + " - " + course.getTeacherName());
                courseData.put(keys[1], course.getId() + " | " + course.getWay() + " | " + course.getHour() + "学时");
                coursesToShow.add(courseData);
            });
            lock.release();
            dialog.close();
        });
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        courseListView.setAdapter(adapter);
    }
}
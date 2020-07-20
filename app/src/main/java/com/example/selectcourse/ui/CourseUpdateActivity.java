package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.selectcourse.R;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.entity.CourseType;

import java.util.List;

/** 创建、修改课程共用页面 */
public class CourseUpdateActivity extends AppCompatActivity {

    private EditText nameInput, hourInput, idInput, wayInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_update);

        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = CourseType.getTypeList();

        Button submitBtn = findViewById(R.id.course_publish);
        Intent fromIntent = getIntent();  // 从这里读取原有的课程信息，这将用于修改课程信息
        Course toModify = (Course) fromIntent.getSerializableExtra("course");  // 取得要修改的课程
        if(toModify == null){  // 新建课程
            submitBtn.setText("发布课程");
        }else{
            submitBtn.setText("修改课程");
        }
        // TODO： 为其他输入框自动填入原 Course 的值，并将 ID 设为不可更改

        //为下拉列表定义一个适配器
        final ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉菜单样式。
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //添加数据
        mSpinner.setAdapter(ad);
        //点击响应事件，修改课程信息时，需要为 spinner 设置默认值，并在点击时清空该值
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    public void onSubmit(View view){
        // TODO： 获取表单内容并进行检验，如果有错给出提示，否则弹出家在动画，将数据传到后端，等待响应。
    }
}
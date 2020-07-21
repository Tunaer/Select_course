package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.selectcourse.R;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.entity.CourseType;
import com.example.selectcourse.service.CourseService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.ui.popup.ToastUtil;

import java.util.List;

/**
 * 创建、修改课程共用页面
 */
public class CourseUpdateActivity extends AppCompatActivity {

    private EditText nameInput, hourInput, idInput, wayInput;
    private Spinner courseType;
    private boolean editMode; // 模式，true 表示修改，false 表示新增
    private Button submitBtn;
    LoadingDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_update);

        courseType = (Spinner) findViewById(R.id.spinner);
        submitBtn = findViewById(R.id.course_publish);
        nameInput = findViewById(R.id.course_publish_name);
        hourInput = findViewById(R.id.course_publish_hour);
        idInput = findViewById(R.id.course_publish_id);
        wayInput = findViewById(R.id.course_publish_examine);
        List<String> list = CourseType.getTypeList();

        loading = LoadingDialog.createLoading(CourseUpdateActivity.this);  // 构建加载弹窗

        Intent fromIntent = getIntent();  // 从这里读取原有的课程信息，这将用于修改课程信息
        Course toModify = (Course) fromIntent.getSerializableExtra("course");  // 取得要修改的课程
        if (toModify == null) {  // 新建课程
            editMode = false;
            submitBtn.setText("发布课程");
        } else {
            editMode = true;
            // 修改课程模式，自动填充课程已有信息
            nameInput.setText(toModify.getCourseName());
            hourInput.setText(toModify.getHour());
            idInput.setText(toModify.getId());
            wayInput.setText(toModify.getWay());
            // 调整下拉列表显示顺序，达到默认选项的效果
            int oriIndex = list.indexOf(toModify.getType().getName());
            list.set(oriIndex, list.get(0));
            list.set(0, toModify.getType().getName());

            submitBtn.setText("修改课程");
        }

        //为下拉列表定义一个适配器
        final ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉菜单样式。
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //添加数据
        courseType.setAdapter(ad);

        courseType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void onSubmit(View view) {
        Course newCourse = new Course(nameInput.getText().toString(),
                idInput.getText().toString(), Integer.parseInt(hourInput.getText().toString()),
                wayInput.getText().toString(), courseType.toString());
        String errMsg = CourseService.checkForm(newCourse);
        if (errMsg != null) {
            ToastUtil.show(CourseUpdateActivity.this, errMsg);
        } else {
            loading.show("数据上传中");
            CourseService.uploadCourse(newCourse, editMode, errMsgStr -> {
                loading.close();
                if (errMsgStr == null) {
                    // 设置 ID 字段只读
                    idInput.setInputType(InputType.TYPE_NULL);
                    idInput.setTextColor(Color.GRAY);
                    editMode = true;
                    submitBtn.setText("修改课程");
                    ToastUtil.show(CourseUpdateActivity.this, "完成处理");
                } else {
                    ToastUtil.show(CourseUpdateActivity.this, errMsgStr);
                }
            });
        }
    }
}
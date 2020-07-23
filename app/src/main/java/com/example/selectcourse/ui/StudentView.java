package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.selectcourse.R;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.service.CourseService;
import com.example.selectcourse.service.SelectService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.ui.popup.ToastUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


public class StudentView extends AppCompatActivity {
    Button select, selected;

    private ListView courseListView;

    private String[] keys = new String[]{"title", "sub"};
    private List<Map<String, String>> coursesToShow = new LinkedList<>();
    private List<Course> pageCourses;
    private List<Course> selectedCourse;
    private SimpleAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        select = (Button) findViewById(R.id.button7);//选课
        selected = (Button) findViewById(R.id.button5);//查询已选课程
        courseListView = findViewById(R.id.course_list);

        // 列表上拉刷新，下拉加载
        SmartRefreshLayout smartRefresh = findViewById(R.id.smart_refresh);
        smartRefresh.setOnRefreshListener(layout -> {
            initList();
            smartRefresh.finishRefresh();
            ToastUtil.show(StudentView.this, "已刷新");
        });
        smartRefresh.setOnLoadMoreListener(layout-> {
            smartRefresh.finishLoadMore();
            ToastUtil.show(StudentView.this, "没有更多了");
        });
        selectedCourse = new LinkedList<>();

        adapter = new SimpleAdapter(StudentView.this, coursesToShow,
                R.layout.course_list_item, keys, new int[]{R.id.course_title, R.id.course_sub});
        // 列表项点击事件监听、记录选中的项
        courseListView.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckBox itemBox = view.findViewById(R.id.course_checkBox);
            boolean check = !itemBox.isChecked();
            itemBox.setChecked(check);
            String selectedCourseId = ((TextView) view.findViewById(R.id.course_sub)).getText().toString().substring(0, 4);
            final boolean[] correct = {false};
            pageCourses.forEach(c -> correct[0] |= c.getId().equals(selectedCourseId));
            if (!correct[0]) ToastUtil.show(StudentView.this, "页面数据已过期，请刷新后重试");
            if (check && correct[0]) {
                selectedCourse.add(pageCourses.get(i));
            } else {
                selectedCourse.removeIf(course -> course.getId().equals(pageCourses.get(i).getId()));
            }
            Log.d("checked", selectedCourse.toString());
        });

        initList();
        courseListView.setAdapter(adapter);

        //跳转已选课程界面
        selected.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(StudentView.this, Student_cancel.class);
            startActivity(intent);
        });
    }
//
    public void modifySelect(View view) {
        if (selectedCourse.size() == 0) {
            ToastUtil.show(StudentView.this, "请选择要选的课程");
        } else if (selectedCourse.size() != 1) {
            ToastUtil.show(StudentView.this, "一次只能选一门课程");
        } else {

        }
    }

    // 刷新列表，并清除列表选中状态
    private void initList() {
        // 清空已选项，因为删除操作导致其在数组内发生了移位，不清空也基本是错的
        int childCount = courseListView.getChildCount();
        for(int i=0; i<childCount; i++){
            CheckBox nowBox = courseListView.getChildAt(i).findViewById(R.id.course_checkBox);
            nowBox.setChecked(false);
        }
        coursesToShow.clear();
        selectedCourse.clear();
        // 刷新数据
        Semaphore lock = new Semaphore(0);  // 线程同步锁
        CourseService.getAllCourses(courses -> {
            if (courses == null) {
                finish();
                lock.release();  // 页面死锁
                ToastUtil.show(StudentView.this, "登录已过期或网络故障");
                return;
            }
            pageCourses = courses;
            courses.forEach(course -> {
                Map<String, String> courseData = new HashMap<>();
                courseData.put(keys[0], course.getCourseName() + " - " + course.getTeacherName());
                courseData.put(keys[1], course.getId() + " | " + course.getWay() + " | " +
                        course.getHour() + "学时 | " + course.getType().getName());
                coursesToShow.add(courseData);
            });
            lock.release();
        });
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
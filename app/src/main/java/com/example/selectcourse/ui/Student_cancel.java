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
import com.example.selectcourse.service.SelectService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.ui.popup.ToastUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Student_cancel extends AppCompatActivity {
    Button cancel;

    private ListView courseListView;

    private String[] keys = new String[]{"title", "sub"};
    private List<Map<String, String>> coursesToShow = new LinkedList<>();
    private List<Course> pageCourses;
    private List<Course> selectedCourse;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cancel = (Button) findViewById(R.id.button7);//选课

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_cancel);

        courseListView = findViewById(R.id.course_list);

        // 列表上拉刷新，下拉加载
        SmartRefreshLayout smartRefresh = findViewById(R.id.smart_refresh);
        smartRefresh.setOnRefreshListener(layout -> {
            initList();
            smartRefresh.finishRefresh();
            ToastUtil.show(Student_cancel.this, "已刷新");
        });
        smartRefresh.setOnLoadMoreListener(layout-> {
            smartRefresh.finishLoadMore();
            ToastUtil.show(Student_cancel.this, "没有更多了");
        });
        selectedCourse = new LinkedList<>();

        adapter = new SimpleAdapter(Student_cancel.this, coursesToShow,
                R.layout.course_list_item, keys, new int[]{R.id.course_title, R.id.course_sub});
        // 列表项点击事件监听、记录选中的项
        courseListView.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckBox itemBox = view.findViewById(R.id.course_checkBox);
            boolean check = !itemBox.isChecked();
            itemBox.setChecked(check);
            String selectedCourseId = ((TextView) view.findViewById(R.id.course_sub)).getText().toString().substring(0, 4);
            final boolean[] correct = {false};
            pageCourses.forEach(c -> correct[0] |= c.getId().equals(selectedCourseId));
            if (!correct[0]) ToastUtil.show(Student_cancel.this, "页面数据已过期，请刷新后重试");
            if (check && correct[0]) {
                selectedCourse.add(pageCourses.get(i));
            } else {
                selectedCourse.removeIf(course -> course.getId().equals(pageCourses.get(i).getId()));
            }
            Log.d("checked", selectedCourse.toString());
        });

        initList();
        courseListView.setAdapter(adapter);
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
        SelectService.getAllCourses(courses -> {
            if (courses == null) {
                finish();
                lock.release();  // 页面死锁
                ToastUtil.show(Student_cancel.this, "登录已过期或网络故障");
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

    public void multiCancel(View view) {
        LoadingDialog loadingDialog = LoadingDialog.createLoading(Student_cancel.this);
        if (selectedCourse.size() == 0) {
            ToastUtil.show(Student_cancel.this, "请选择要退的课程");
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_launcher_foreground)  // 标题的图片
                    .setTitle("注意")
                    .setMessage("是否要退" + selectedCourse.size() + "门课程？该操作不可逆")
                    .setNegativeButton("取消", (d1, which) -> {
                        d1.dismiss();
                    }).setPositiveButton("确定", (d2, which) -> {
                        d2.dismiss();
                        loadingDialog.show("退课中...");
                        String msg = SelectService.cancelCourses(selectedCourse);
                        loadingDialog.close();
                        pageCourses.removeAll(selectedCourse);  // 从列表中移除已删除的课程，防止页面未刷新导致重复提交
                        selectedCourse.clear();  // 清空选项，但无法在子线程中更新 UI，锁控制并不可以，效果就是删除后无法从列表中移除
                        ToastUtil.show(Student_cancel.this, msg);
                    }).create();
            dialog.show();
        }
    }
}
package com.example.selectcourse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.selectcourse.R;
import com.example.selectcourse.entity.Course;
import com.example.selectcourse.service.CourseService;
import com.example.selectcourse.ui.popup.LoadingDialog;
import com.example.selectcourse.ui.popup.ToastUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class ManageActivity extends AppCompatActivity {
    EditText courseid, coursername;
    Button select, insert, update, delete;

    private ListView courseListView;

    private String[] keys = new String[]{"title", "sub"};
    private List<Map<String, String>> coursesToShow = new LinkedList<>();
    private List<Course> pageCourses;
    private List<Course> selectedCourse;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

//        courseid = (EditText) findViewById(R.id.editText1);
//        coursername = (EditText) findViewById(R.id.login_inp_pwd);
//        select = (Button) findViewById(R.id.button2);
        insert = (Button) findViewById(R.id.admin_btn_insert);
        update = (Button) findViewById(R.id.button3);
        delete = (Button) findViewById(R.id.button4);
        courseListView = findViewById(R.id.course_list);

        // 列表上拉刷新，下拉加载
        SmartRefreshLayout smartRefresh = findViewById(R.id.smart_refresh);
        smartRefresh.setOnRefreshListener(layout -> {
            initList();
            smartRefresh.finishRefresh();
            ToastUtil.show(ManageActivity.this, "已刷新");
        });
        smartRefresh.setOnLoadMoreListener(layout-> {
            smartRefresh.finishLoadMore();
            ToastUtil.show(ManageActivity.this, "没有更多了");
        });

        selectedCourse = new LinkedList<>();

        adapter = new SimpleAdapter(ManageActivity.this, coursesToShow,
                R.layout.course_list_item, keys, new int[]{R.id.course_title, R.id.course_sub});
        // 列表项点击事件监听、记录选中的项
        courseListView.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckBox itemBox = view.findViewById(R.id.course_checkBox);
            boolean check = !itemBox.isChecked();
            itemBox.setChecked(check);
            String selectedCourseId = ((TextView) view.findViewById(R.id.course_sub)).getText().toString().substring(0, 4);
            final boolean[] correct = {false};
            pageCourses.forEach(c -> correct[0] |= c.getId().equals(selectedCourseId));
            if (!correct[0]) ToastUtil.show(ManageActivity.this, "页面数据已过期，请刷新后重试");
            if (check && correct[0]) {
                selectedCourse.add(pageCourses.get(i));
            } else {
                selectedCourse.removeIf(course -> course.getId().equals(pageCourses.get(i).getId()));
            }
            Log.d("checked", selectedCourse.toString());
        });
        initList();
        courseListView.setAdapter(adapter);

        //跳转添加
        insert.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(ManageActivity.this, CourseUpdateActivity.class);
            startActivity(intent);
        });
    }

    public void modifyOne(View view) {
        if (selectedCourse.size() == 0) {
            ToastUtil.show(ManageActivity.this, "请选择要修改的课程");
        } else if (selectedCourse.size() != 1) {
            ToastUtil.show(ManageActivity.this, "一次只能修改一门课程");
        } else {
            Course toModify = selectedCourse.get(0);
            Intent modifyIntent = new Intent();
            modifyIntent.setClass(ManageActivity.this, CourseUpdateActivity.class);
            modifyIntent.putExtra("course", toModify);
            startActivity(modifyIntent);
        }
    }

    public void multiDelete(View view) {
        LoadingDialog loadingDialog = LoadingDialog.createLoading(ManageActivity.this);
        if (selectedCourse.size() == 0) {
            ToastUtil.show(ManageActivity.this, "请选择要删除的课程");
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_launcher_foreground)  // 标题的图片
                    .setTitle("注意")
                    .setMessage("是否删除" + selectedCourse.size() + "门课程？该操作不可逆")
                    .setNegativeButton("取消", (d1, which) -> {
                        d1.dismiss();
                    }).setPositiveButton("确定", (d2, which) -> {
                        d2.dismiss();
                        loadingDialog.show("删除中...");
                        String msg = CourseService.deleteCourses(selectedCourse);
                        loadingDialog.close();
                        pageCourses.removeAll(selectedCourse);  // 从列表中移除已删除的课程，防止页面未刷新导致重复提交
                        selectedCourse.clear();  // 清空选项，但无法在子线程中更新 UI，锁控制并不可以，效果就是删除后无法从列表中移除
                        // adapter.notifyDataSetChanged()
                        ToastUtil.show(ManageActivity.this, msg);
                    }).create();
            dialog.show();
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
                ToastUtil.show(ManageActivity.this, "登录已过期或网络故障");
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
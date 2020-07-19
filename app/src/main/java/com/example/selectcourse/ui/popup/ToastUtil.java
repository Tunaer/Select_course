package com.example.selectcourse.ui.popup;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author cxlm
 * 07.19 16:57
 * 封装 Toast，可以避免在子线程中使用 Toast 造成的报错问题
 * <p>
 * 使用示例：
 * ToastUtil.show(xxActivity.this, "提示信息");
 */
public class ToastUtil {

    public static void show(Context context, String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
}

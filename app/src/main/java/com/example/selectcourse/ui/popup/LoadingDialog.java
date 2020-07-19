package com.example.selectcourse.ui.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.selectcourse.R;

/**
 * @author cxlm
 * 07.19 11:46
 * 加载遮罩层，含工厂方法
 * <p>
 * 使用示例：
 * LoadingDialog dialog = LoadingDialog.createLoading(xxxActivity.this);
 * dialog.show("提示文本");  // 显示
 * dialog.close();  // 关闭
 */
public class LoadingDialog {

    private final Dialog dialog;
    private View view;

    private LoadingDialog(Dialog dialog, View view) {
        this.dialog = dialog;
        this.view = view;
    }

    /**
     * 关闭加载遮罩
     */
    public void close() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 显示加载遮罩
     */
    public void show(String msg) {
        if (dialog != null && !dialog.isShowing()) {
            TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);// 提示文字
            tipTextView.setText(msg);// 设置加载信息
            dialog.show();
        }
    }

    public static LoadingDialog createLoading(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(false); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        // 显示 Dialog
        Window window = loadingDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);

        return new LoadingDialog(loadingDialog, v);
    }
}

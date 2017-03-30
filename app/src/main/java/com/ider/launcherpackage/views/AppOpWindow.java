package com.ider.launcherpackage.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.ProcessManager;

/**
 * Created by ider-eric on 2017/2/22.
 */

public class AppOpWindow implements View.OnClickListener{
    private static final String TAG = "AppOpWindow";
    private Context context;
    private PopupWindow popupWindow;
    private String packageName;

    private static AppOpWindow editorWindow;
    private AppOpWindow(Context context) {
        this.context = context;
    }

    public static AppOpWindow getInstance(Context context) {
        if(editorWindow == null) {
            editorWindow = new AppOpWindow(context);
        }
        return editorWindow;
    }

    public void showAppOpWindow(View parent, String packageName) {
        this.packageName = packageName;
        View view = View.inflate(context, R.layout.app_operate_layout, null);
        view.findViewById(R.id.app_op_clear_data).setOnClickListener(this);
        view.findViewById(R.id.app_op_force_stop).setOnClickListener(this);
        view.findViewById(R.id.app_op_uninstall).setOnClickListener(this);
        popupWindow = new PopupWindow(view, -1, -1, true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_op_clear_data:
                ProcessManager.clearDataForPackage(context, packageName);
                break;
            case R.id.app_op_force_stop:
                ProcessManager.forceStop(context, packageName);
                break;
            case R.id.app_op_uninstall:
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                popupWindow.dismiss();
                break;
        }
    }
}

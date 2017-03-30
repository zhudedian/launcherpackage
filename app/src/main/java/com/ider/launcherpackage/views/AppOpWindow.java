package com.ider.launcherpackage.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.ProcessManager;

/**
 * Created by ider-eric on 2017/2/22.
 */

public class AppOpWindow implements View.OnKeyListener,View.OnClickListener{
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
        view.findViewById(R.id.app_op_clear_data).setOnKeyListener(this);
        view.findViewById(R.id.app_op_force_stop).setOnClickListener(this);
        view.findViewById(R.id.app_op_force_stop).setOnKeyListener(this);
        view.findViewById(R.id.app_op_uninstall).setOnClickListener(this);
        view.findViewById(R.id.app_op_uninstall).setOnKeyListener(this);
        this.popupWindow = new PopupWindow(view, -1, -1, true);
        this.popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        this.popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
//            Log.i("appop","appop");
            this.popupWindow.dismiss();
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_op_clear_data:
                ProcessManager.clearDataForPackage(context, packageName);
                popupWindow.dismiss();
                break;
            case R.id.app_op_force_stop:
                ProcessManager.forceStop(context, packageName);
                PackageManager pm = context.getPackageManager();
                String packageLabel = null;
                try {
                    packageLabel = pm.getApplicationLabel(pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String info= context.getString(R.string.app_data_forced);
                Toast.makeText(context, packageLabel+" "+info, Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
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

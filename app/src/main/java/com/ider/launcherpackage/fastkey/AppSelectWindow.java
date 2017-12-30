package com.ider.launcherpackage.fastkey;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.AppAdapter;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.ArrayList;
import java.util.List;


public class AppSelectWindow implements View.OnKeyListener, AdapterView.OnItemClickListener{

    private Context mContext;
    private List<PackageHolder> allApps;
    private View baseView;
    private PopupWindow popWindow;
    private static AppSelectWindow INSTANCE;
    private OnAppSelectListener onAppSelectListener;
    private List<PackageHolder> locals = null;
    public interface OnAppSelectListener{
        void onAppSelected(PackageHolder holder);
    }
    public void setOnAppSelectListener(OnAppSelectListener onAppSelectListener) {
        this.onAppSelectListener = onAppSelectListener;
    }


    private AppSelectWindow(Context context) {
        this.mContext = context;
    }

    public static AppSelectWindow getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new AppSelectWindow(context);
        }
        return INSTANCE;
    }


    public void showAppPopWindow(View baseView) {
        this.baseView = baseView;
        View view = View.inflate(mContext, R.layout.app_select_window, null);
        view.setOnKeyListener(this);

        GridView gridView = (GridView) view.findViewById(R.id.app_select_grid);
        gridView.setOnKeyListener(this);
        gridView.setOnItemClickListener(this);
        setupAppGrid(gridView);

        this.popWindow = new PopupWindow(view, -1, -1, true);
        this.popWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        this.popWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
    }


    private void setupAppGrid(GridView gridView) {
        allApps =queryApplication(mContext);
        if(allApps.size()==0) {
            allApps.add(new PackageHolder(0L, "add", null));
        }
            AppAdapter adapter = new AppAdapter(mContext, R.layout.app_select_item, allApps);
            gridView.setAdapter(adapter);

    }
    public void setData(List<PackageHolder> locals){
        this.locals = locals;
    }
    private  List<PackageHolder> queryApplication(Context context) {
        List<PackageHolder> enties = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            if(!packageName.equals("com.yidian.calendar")&&!packageName.equals("com.android.quicksearchbox")
                    && !packageName.equals("com.android.email")&&!packageName.equals("com.android.calculator2")&&
                    !packageName.equals("com.android.soundrecorder")&&!packageName.equals("com.android.deskclock")&&
                    !packageName.equals("com.android.calendar")&&!packageName.equals("com.android.contacts")&&
                    !packageName.equals("com.android.gallery3d")&&!packageName.equals("com.android.providers.downloads.ui")&&
                    !packageName.equals("com.android.camera2")&&!packageName.equals("com.android.browser")){
                if(locals==null){
                    enties.add(new PackageHolder(packageName, null));
                }else if(!locals.contains(new PackageHolder(packageName, null))) {
                    enties.add(new PackageHolder(packageName, null));
                }
            }
        }
        return enties;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.popWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if(baseView instanceof ShortcutView) {
//            ((ShortcutView) baseView).mItemEntry = new ItemEntry(allApps.get(i).getPackageName());
//            ((ShortcutView) baseView).updateSelf();
//            ((ShortcutView) baseView).save();
//        } else
//        if(baseView instanceof GridView) {
        try {
            if(onAppSelectListener != null) {
                    onAppSelectListener.onAppSelected(allApps.get(i));
            }

//        }

            popWindow.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

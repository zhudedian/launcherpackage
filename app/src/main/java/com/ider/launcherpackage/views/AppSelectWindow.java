package com.ider.launcherpackage.views;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.ApplicationUtil;
import com.ider.launcherpackage.launcher.AppAdapter;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.PackageHolder;
import com.ider.launcherpackage.util.SelectAdapter;

import java.util.List;


public class AppSelectWindow implements View.OnKeyListener, AdapterView.OnItemClickListener{

    private Context mContext;
    private List<PackageHolder> allApps,apps;
    private View baseView;
    private PopupWindow popWindow;
    private static AppSelectWindow INSTANCE;
    private OnAppSelectListener onAppSelectListener;
    private SelectAdapter adapter;

    public interface OnAppSelectListener{
        void onAppSelected(boolean isAdd,PackageHolder holder);
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


    public void showAppPopWindow(List<PackageHolder> apps,View baseView) {
        this.baseView = baseView;
        this.apps = apps;
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
        allApps = ApplicationUtil.queryApplication(mContext);
        adapter = new SelectAdapter(mContext, R.layout.app_select_item, allApps,apps);
        gridView.setAdapter(adapter);
    }



    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//        Log.i("selectwindow","onKey");
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.popWindow.dismiss();
            return true;
        }
        return false;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(baseView instanceof ShortcutView) {
            ((ShortcutView) baseView).mItemEntry = new ItemEntry(allApps.get(i).getPackageName());
            ((ShortcutView) baseView).updateSelf();
            ((ShortcutView) baseView).save();
            popWindow.dismiss();
        } else if(baseView instanceof GridView) {
            if(onAppSelectListener != null) {
                if (apps.contains(allApps.get(i))){
//                    apps.remove(allApps.get(i));
                   outer:for (int j=0;j<apps.size();j++){
                        if (allApps.get(i).equals(apps.get(j))){
                            Log.i("selectwindow",allApps.get(i).getPackageName());
                            onAppSelectListener.onAppSelected(false,apps.get(j));
                            apps.remove(j);
                            break outer;
                        }
                    }
                    adapter.notifyDataSetChanged();

                }else {
                    Log.i("selectwindow",allApps.get(i).getPackageName());
                    onAppSelectListener.onAppSelected(true,allApps.get(i));
                    apps.add(apps.size() - 1, allApps.get(i));
                    adapter.notifyDataSetChanged();
                }

            }

        }
//        popWindow.dismiss();
    }
}

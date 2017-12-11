package com.ider.launcherpackage.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.ApplicationUtil;
import com.ider.launcherpackage.views.AppSelectWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ider-eric on 2017/1/4.
 */

public class FolderActivity extends FullscreenActivity {

    private static final String TAG = "FolderActivity";
    private Handler mHandler;
    private GridView gridView;
    private AppAdapter appAdapter;
    private List<PackageHolder> packages;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        ActionBar actionBar= getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        mHandler = new Handler();

        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        packages = DbManager.getInstance(getApplicationContext()).queryPackages(tag);
        List<PackageHolder> allApp= ApplicationUtil.queryApplication(this);
        List<PackageHolder> removeApp= new ArrayList<>();
        for (PackageHolder pa:packages){
            if (!allApp.contains(pa)){
                removeApp.add(pa);
                DbManager.getInstance(getApplicationContext()).removePackage(pa);
            }
        }
        packages.removeAll(removeApp);
        packages.add(new PackageHolder(0L, "add", tag));
        appAdapter = new AppAdapter(this, packages);
        gridView = (GridView) findViewById(R.id.folder_grid);

        mHandler.postDelayed(showApp, 100);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PackageHolder holder = packages.get(i);
                if(holder.getPackageName().equals("add")) {
                    showAppSelectWindow();
                } else {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(holder.getPackageName());
                    Log.i(TAG, "onItemClick: " + holder.getPackageName());
                    if(intent != null) {
                        startActivity(intent);
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position >= packages.size()-1) {
                    return true;
                }
                // 数据库操作类对象
                DbManager.getInstance(getApplicationContext()).removePackage(packages.get(position));
                packages.remove(position);
                appAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }


    Runnable showApp = new Runnable() {
        @Override
        public void run() {
            gridView.setAdapter(appAdapter);

        }
    };

    Runnable hideApp = new Runnable() {
        @Override
        public void run() {
            packages.clear();
            appAdapter.notifyDataSetChanged();
            FolderActivity.super.onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
//        Log.i("selectwindow","onBackPressed");
        mHandler.postDelayed(hideApp, 0);
    }

    public void showAppSelectWindow() {
        AppSelectWindow appSelectWindow = AppSelectWindow.getInstance(this);
        appSelectWindow.setOnAppSelectListener(new AppSelectWindow.OnAppSelectListener() {
            @Override
            public void onAppSelected(boolean isAdd,PackageHolder holder) {
                if (isAdd) {
                    appAdapter.notifyDataSetChanged();
                    holder.setTag(tag);
                    DbManager.getInstance(getApplicationContext()).insertPackage(holder);
                }else {
                    for (int k=0;k<packages.size();k++){
                        if (packages.get(k).getPackageName().equals(holder.getPackageName())){
//                            Log.i("selectwindow", packages.get(k)+"");
                            Log.i("selectwindow", holder.getPackageName());
                            DbManager.getInstance(getApplicationContext()).removePackage(holder);
                        }
                    }
                    appAdapter.notifyDataSetChanged();
                }

            }
        });
        appSelectWindow.showAppPopWindow(packages,gridView);
    }

}
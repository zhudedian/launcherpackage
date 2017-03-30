package com.ider.launcherpackage.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ider.launcherpackage.R;
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

        mHandler = new Handler();

        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        packages = DbManager.getInstance(getApplicationContext()).queryPackages(tag);
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
        mHandler.postDelayed(hideApp, 0);
    }

    public void showAppSelectWindow() {
        AppSelectWindow appSelectWindow = AppSelectWindow.getInstance(this);
        appSelectWindow.setOnAppSelectListener(new AppSelectWindow.OnAppSelectListener() {
            @Override
            public void onAppSelected(PackageHolder holder) {
                packages.add(packages.size() - 1, holder);
                appAdapter.notifyDataSetChanged();
                holder.setTag(tag);
                DbManager.getInstance(getApplicationContext()).insertPackage(holder);
            }
        });
        appSelectWindow.showAppPopWindow(gridView);
    }

}
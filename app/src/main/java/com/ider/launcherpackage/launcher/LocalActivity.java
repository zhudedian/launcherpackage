package com.ider.launcherpackage.launcher;

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

public class LocalActivity extends FullscreenActivity {


    private static final String TAG = "FolderActivity";
    private Handler mHandler;
    private GridView gridView;
    private AppAdapter appAdapter;
    private List<PackageHolder> packages = new ArrayList<>();
    String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        ActionBar actionBar= getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        mHandler = new Handler();

        packages.add(new PackageHolder("com.android.music", "10"));
        packages.add(new PackageHolder("android.rk.RockVideoPlayer", "10"));
        packages.add(new PackageHolder("com.android.gallery3d", "10"));
        packages.add(new PackageHolder("com.android.rockchip", "10"));
        packages.add(new PackageHolder("com.estrongs.android.pop", "10"));
        appAdapter = new AppAdapter(this, packages);
        gridView = (GridView) findViewById(R.id.local_grid);

        mHandler.postDelayed(showApp, 100);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PackageHolder holder = packages.get(i);
                Intent intent = getPackageManager().getLaunchIntentForPackage(holder.getPackageName());
                Log.i(TAG, "onItemClick: " + holder.getPackageName());
                if(intent != null) {
                    startActivity(intent);
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
            LocalActivity.super.onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
//        Log.i("selectwindow","onBackPressed");
        mHandler.postDelayed(hideApp, 0);
    }

}

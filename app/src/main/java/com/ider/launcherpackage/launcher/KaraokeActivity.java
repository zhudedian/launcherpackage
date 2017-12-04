package com.ider.launcherpackage.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.db.KaraokeApp;
import com.ider.launcherpackage.db.MovieApp;
import com.ider.launcherpackage.views.AppSelectWindow;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class KaraokeActivity extends AppCompatActivity {

    private static final String TAG = "KaraokeActivity";
    private Handler mHandler;
    private GridView gridView;
    private AppAdapter appAdapter;
    private static List<PackageHolder> packages = new ArrayList<>();
    private static List<KaraokeApp> apps;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke);
        ActionBar actionBar= getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        mHandler = new Handler();

        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        if (apps == null) {
            apps = DataSupport.findAll(KaraokeApp.class);
            if (apps.size() > 0)
                for (KaraokeApp app : apps) {
                    packages.add(new PackageHolder(0L, app.getPackageName(), tag));
                }
            packages.add(new PackageHolder(0L, "add", tag));
        }
        appAdapter = new AppAdapter(this, packages);
        gridView = (GridView) findViewById(R.id.folder_grid);

        mHandler.postDelayed(showApp, 150);

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
//                DbManager.getInstance(getApplicationContext()).removePackage(packages.get(position));
                DataSupport.deleteAll(KaraokeApp.class,"packageName = ?",packages.get(position).getPackageName());
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
            appAdapter.notifyDataSetChanged();
            KaraokeActivity.super.onBackPressed();
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
                    KaraokeApp app = new KaraokeApp(holder.getPackageName());
                    app.save();
//                    holder.setTag(tag);
//                    DbManager.getInstance(getApplicationContext()).insertPackage(holder);
                }else {
                    for (int k=0;k<packages.size();k++){
                        if (packages.get(k).getPackageName().equals(holder.getPackageName())){
//                            Log.i("selectwindow", packages.get(k)+"");
                            Log.i("selectwindow", holder.getPackageName());
                            DataSupport.deleteAll(KaraokeApp.class,"packageName = ?",holder.getPackageName());
//                            DbManager.getInstance(getApplicationContext()).removePackage(holder);
                        }
                    }
                    appAdapter.notifyDataSetChanged();
                }

            }
        });
        appSelectWindow.showAppPopWindow(packages,gridView);
    }
}

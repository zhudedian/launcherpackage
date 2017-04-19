package com.ider.launcherpackage.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.ApplicationUtil;
import com.ider.launcherpackage.launcher.DbManager;
import com.ider.launcherpackage.launcher.LocalActivity;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Eric on 2017/4/18.
 */

public class ShortcutLocal extends LocalEntryView {

    private static final String TAG = "ShortcutLocal";

    // 数据库操作类
    private DbManager dbManager;
    // 用于保存用户存储的package信息
    private List<PackageHolder> packages;
    private ImageView thumbnailGrid;
    private TextView title;

    public ShortcutLocal(Context context) {
        this(context, null);
    }

    public ShortcutLocal(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.home_large_view, this);
        thumbnailGrid = (ImageView) findViewById(R.id.home_large_image);
        title = (TextView) findViewById(R.id.home_large_text);
    }

    


    @Override
    public void onClick() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), thumbnailGrid, "FolderGrid");
        Intent intent = new Intent(getContext(), LocalActivity.class);
        intent.putExtra("tag", (String) getTag());
        ActivityCompat.startActivity(getContext(), intent, compat.toBundle());

    }

    @Override
    public void onLongClick() {

    }
}

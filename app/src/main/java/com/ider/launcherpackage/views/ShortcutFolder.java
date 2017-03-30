package com.ider.launcherpackage.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.FolderActivity;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.ArrayList;
import java.util.List;

import com.ider.launcherpackage.launcher.DbManager;

/**
 * Created by ider-eric on 2016/12/29.
 */

public class ShortcutFolder extends BaseEntryView {
    private static final String TAG = "ShortcutFolder";

    // 数据库操作类
    private DbManager dbManager;
    // 用于保存用户存储的package信息
    private List<PackageHolder> packages;
    private ImageView thumbnailGrid;
    private TextView title;

    public ShortcutFolder(Context context) {
        this(context, null);
    }

    public ShortcutFolder(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.shortcut_folder_small, this);
        thumbnailGrid = (ImageView) findViewById(R.id.shortcut_folder_thumbnail_grid);
        title = (TextView) findViewById(R.id.shortcut_folder_text);
        title.setText("Favorite");
    }


    @Override
    public void updateSelf() {
        dbManager = DbManager.getInstance(getContext());
        packages = dbManager.queryPackages((String) getTag());

        Bitmap bitmap = mBitmapTools.getFolderThumbnailBitmap(getContext(), packages, thumbnailGrid.getWidth(), thumbnailGrid.getHeight());
        thumbnailGrid.setImageBitmap(bitmap);

    }


    @Override
    public void setDefault() {
        ArrayList<PackageHolder> list = new ArrayList<>();
        if(getTag().equals("14")) {
            list.add(new PackageHolder("com.android.settings", "14"));
            list.add(new PackageHolder("com.android.browser", "14"));
            list.add(new PackageHolder("org.xbmc.kodi", "14"));
        }
        Bitmap bitmap = mBitmapTools.getFolderThumbnailBitmap(getContext(), list, thumbnailGrid.getWidth(), thumbnailGrid.getHeight());
        thumbnailGrid.setImageBitmap(bitmap);

        DbManager.getInstance(getContext()).insertPackages(list);

    }

    @Override
    public void onClick() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), thumbnailGrid, "FolderGrid");
        Intent intent = new Intent(getContext(), FolderActivity.class);
        intent.putExtra("tag", (String) getTag());
        ActivityCompat.startActivity(getContext(), intent, compat.toBundle());

    }

    @Override
    public void onLongClick() {

    }

}

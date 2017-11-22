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
import com.ider.launcherpackage.db.KaraokeApp;
import com.ider.launcherpackage.db.MovieApp;
import com.ider.launcherpackage.db.SportsApp;
import com.ider.launcherpackage.db.VipApp;
import com.ider.launcherpackage.launcher.DbManager;
import com.ider.launcherpackage.launcher.FolderActivity;
import com.ider.launcherpackage.launcher.KaraokeActivity;
import com.ider.launcherpackage.launcher.MovieActivity;
import com.ider.launcherpackage.launcher.PackageHolder;
import com.ider.launcherpackage.launcher.SportsActivity;
import com.ider.launcherpackage.launcher.VipActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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
    private String tag;

    public ShortcutFolder(Context context) {
        this(context, null);
    }

    public ShortcutFolder(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.shortcut_folder_small, this);
        //thumbnailGrid = (ImageView) findViewById(R.id.shortcut_folder_thumbnail_grid);
        thumbnailGrid = (ImageView) findViewById(R.id.shortcut_folder_thumbnail_grid2);
        title = (TextView) findViewById(R.id.shortcut_folder_text);
        tag = (String) getTag();
    }


    @Override
    public void updateSelf() {
        dbManager = DbManager.getInstance(getContext());
        packages = dbManager.queryPackages((String) getTag());
//        List<PackageHolder> allApp= ApplicationUtil.queryApplication(getContext());
//        List<PackageHolder> removeApp= new ArrayList<>();
//        for (PackageHolder pa:packages){
//            if (!allApp.contains(pa)){
//                removeApp.add(pa);
//                dbManager.removePackage(pa);
//            }
//        }
//        packages.removeAll(removeApp);
//        Bitmap bitmap = mBitmapTools.getFolderThumbnailBitmap(getContext(), packages, thumbnailGrid.getWidth(), thumbnailGrid.getHeight());
//        thumbnailGrid.setImageBitmap(bitmap);
        if (tag.equals("13")){
            thumbnailGrid.setImageResource(R.drawable.tiyusaishi);
            title.setText(getResources().getString(R.string.file));
        }else if (tag.equals("12")){
            title.setText(getResources().getString(R.string.youtube));
            thumbnailGrid.setImageResource(R.drawable.keigeyule);
        }else if (tag.equals("11")){
            title.setText(getResources().getString(R.string.kodi));
            thumbnailGrid.setImageResource(R.drawable.vedio);
        }else if (tag.equals("10")){
            title.setText(getResources().getString(R.string.applications));
            thumbnailGrid.setImageResource(R.drawable.vip);
        }

    }


    @Override
    public void setDefault() {
        ArrayList<PackageHolder> list = new ArrayList<>();
        List<PackageHolder> allApp= ApplicationUtil.queryApplication(getContext());
        if(getTag().equals("14")) {
            if (allApp.contains(new PackageHolder("com.rk_itvui.settings", "14"))) {
                list.add(new PackageHolder("com.rk_itvui.settings", "14"));
            }
            if (allApp.contains(new PackageHolder("com.android.music", "14"))) {
                list.add(new PackageHolder("com.android.music", "14"));
            }
            if (allApp.contains(new PackageHolder("com.android.browser", "14"))) {
                list.add(new PackageHolder("com.android.browser", "14"));
            }
            if (allApp.contains(new PackageHolder("org.xbmc.kodi", "14"))) {
                list.add(new PackageHolder("org.xbmc.kodi", "14"));
            }
        }
//        Bitmap bitmap = mBitmapTools.getFolderThumbnailBitmap(getContext(), list, thumbnailGrid.getWidth(), thumbnailGrid.getHeight());
//        thumbnailGrid.setImageBitmap(bitmap);
        VipApp vipApp = new VipApp("app");
        vipApp.save();
        DataSupport.deleteAll(VipApp.class);

        SportsApp sportsApp = new SportsApp("com.pptv.tvsports");
        sportsApp.save();


        MovieApp movieApp = new MovieApp("com.qiyi.tv.tw");
        movieApp.save();
        movieApp = new MovieApp("com.vcinema.client.tv");
        movieApp.save();
        movieApp = new MovieApp("com.qianxun.tvbox");
        movieApp.save();
        movieApp = new MovieApp("com.jyzx8.fireplayer");
        movieApp.save();
        movieApp = new MovieApp("com.moretv.android");
        movieApp.save();

        KaraokeApp karaokeApp = new KaraokeApp("com.SingerKing");
        karaokeApp.save();
        karaokeApp = new KaraokeApp("com.kgeking.client");
        karaokeApp.save();
        karaokeApp = new KaraokeApp("com.Kalatech.Kalatech");
        karaokeApp.save();




        DbManager.getInstance(getContext()).insertPackages(list);
        if (tag.equals("13")){
            thumbnailGrid.setImageResource(R.drawable.tiyusaishi);
            title.setText(getResources().getString(R.string.file));
        }else if (tag.equals("12")){
            title.setText(getResources().getString(R.string.youtube));
            thumbnailGrid.setImageResource(R.drawable.keigeyule);
        }else if (tag.equals("11")){
            title.setText(getResources().getString(R.string.kodi));
//            thumbnailGrid.setImageResource(R.drawable.keigeyule);
        }else if (tag.equals("10")){
            title.setText(getResources().getString(R.string.applications));
            thumbnailGrid.setImageResource(R.drawable.vip);
        }

    }

    @Override
    public void onClick() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), thumbnailGrid, "FolderGrid");
        Intent intent = new Intent(getContext(), FolderActivity.class);
        intent.putExtra("tag", (String) getTag());
        if (tag.equals("14")){
            intent = new Intent(getContext(), FolderActivity.class);
        }else if (tag.equals("13")){
            intent = new Intent(getContext(), SportsActivity.class);
        }else if (tag.equals("12")){
            intent = new Intent(getContext(), KaraokeActivity.class);
        }else if (tag.equals("11")){
            intent = new Intent(getContext(),MovieActivity.class);
        }else if (tag.equals("10")){
            intent = new Intent(getContext(),VipActivity.class);
        }

        ActivityCompat.startActivity(getContext(), intent, compat.toBundle());

    }

    @Override
    public void onLongClick() {

    }

}

package com.ider.launcherpackage.common;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.ArrayList;
import java.util.List;

public class BitmapTools {

    private static BitmapTools INSTANCE;
    private BitmapTools() {

    }

    public static BitmapTools getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BitmapTools();
        }
        return INSTANCE;
    }

    public Bitmap getResourcecBitmap(Context mContext, int resourceId) {
        Drawable drawable = mContext.getResources().getDrawable(resourceId);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    public Bitmap getFolderThumbnailBitmap(Context mContext, List<PackageHolder> packages, int width, int height) {
        // 创建一个grid视图
        View view = View.inflate(mContext, R.layout.folder_thumbnail_grid, null);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);

        GridLayout gridLayout = (GridLayout) view;
        setupThumbnailGrid(gridLayout, packages);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private void setupThumbnailGrid(GridLayout gridLayout, List<PackageHolder> list) {
        if(list.size() > 9) {
            list = list.subList(0, 9);
        }
        for(int i = 0; i < list.size(); i++) {
            Drawable drawable;
            if (list.get(i).getPackageName().equals("add")) {
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.add_item_white);
            } else {
                drawable = ItemEntry.loadImage(gridLayout.getContext(), list.get(i).getPackageName());
            }
            ((ImageView) gridLayout.getChildAt(i)).setImageDrawable(drawable);
        }


    }


}

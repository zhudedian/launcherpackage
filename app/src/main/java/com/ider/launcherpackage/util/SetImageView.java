package com.ider.launcherpackage.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.LauncherApplication;

import static android.R.attr.id;
import static android.R.attr.width;

/**
 * Created by Eric on 2017/3/14.
 */

public class SetImageView {
    public static Bitmap setSmallImageView(int id,String title,int id2){
        int width =300;
        int height=300;
        View view = View.inflate(LauncherApplication.getContext(), R.layout.home_small_view, null);
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);
        RelativeLayout relativeLayout = (RelativeLayout)view;
        if(id!=-1) {
            ((ImageView) relativeLayout.getChildAt(0)).setImageBitmap(BitmapFactory.decodeResource(LauncherApplication.getContext().getResources(), id));
        }else {
            if (id2!=-1)
            ((ImageView) relativeLayout.getChildAt(2)).setImageBitmap(BitmapFactory.decodeResource(LauncherApplication.getContext().getResources(), id2));
        }
        ((TextView)relativeLayout.getChildAt(1)).setText(title);
        ((TextView)relativeLayout.getChildAt(1)).bringToFront();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }
    public static Bitmap setLargeImageView(){
        int width =370;
        int height=523;
        View view = View.inflate(LauncherApplication.getContext(), R.layout.home_large_view, null);
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }
}

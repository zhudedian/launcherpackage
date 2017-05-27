package com.ider.launcherpackage.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.LauncherApplication;

import static android.R.attr.id;
import static android.R.attr.width;

/**
 * Created by Eric on 2017/3/14.
 */

public class SetImageView {
    public static Bitmap setSmallImageView(int id,String title){
        int width =300;
        int height=300;
        if (id==13||id==15||id==16||id==17){
            width =330;
            height=330;
        }
        View view = View.inflate(LauncherApplication.getContext(), R.layout.home_small_view, null);
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);
        RelativeLayout relativeLayout = (RelativeLayout)view;
        if (id==13){
            ((ImageView) relativeLayout.getChildAt(0)).setImageDrawable(ItemEntry.loadImage(LauncherApplication.getContext(), "com.netflix.mediaclient"));
        }else if (id==15){
            ((ImageView) relativeLayout.getChildAt(0)).setImageDrawable(ItemEntry.loadImage(LauncherApplication.getContext(), "android.rk.RockVideoPlayer"));
        } else if (id==16){
            ((ImageView) relativeLayout.getChildAt(0)).setImageDrawable(ItemEntry.loadImage(LauncherApplication.getContext(), "com.android.music"));
        }else if (id==17){
            ((ImageView) relativeLayout.getChildAt(0)).setImageDrawable(ItemEntry.loadImage(LauncherApplication.getContext(), "com.android.gallery3d"));
        }  else{
            ((ImageView) relativeLayout.getChildAt(0)).setImageBitmap(BitmapFactory.decodeResource(LauncherApplication.getContext().getResources(), id));
        }
        ((TextView)relativeLayout.getChildAt(1)).setText(title);
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

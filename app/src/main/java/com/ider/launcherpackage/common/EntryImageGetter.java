package com.ider.launcherpackage.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.LauncherApplication;

/**
 * Created by ider-eric on 2016/12/28.
 */

public class EntryImageGetter {


    public static Bitmap getEntryImage(String packageName) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ItemEntry.loadImage(LauncherApplication.getContext(), packageName);
        if(bitmapDrawable != null) {
            return bitmapDrawable.getBitmap();
        }
        return null;
    }


    public static Bitmap getDefaultImage() {
        return BitmapFactory.decodeResource(LauncherApplication.getContext().getResources(), R.mipmap.add_item_white);
    }


}

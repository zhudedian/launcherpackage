package com.ider.launcherpackage.common;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ider-eric on 2016/12/20.
 */

public class DimenUtil {

    public static float dp2px(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}

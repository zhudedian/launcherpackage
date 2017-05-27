package com.ider.launcherpackage.views;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.EntryAnimation;
import com.ider.launcherpackage.launcher.AppListActivity;

/**
 * Created by ider-eric on 2017/2/21.
 */

public class BaseImageView extends ImageView implements View.OnClickListener{

    private Animator animator;
    private String packageName;
    private String manufature;
    private int sdkVersion;

    public BaseImageView(Context context) {
        this(context, null);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.FIT_XY);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseImageView);
        this.packageName = typedArray.getString(R.styleable.BaseImageView_package_name);
        typedArray.recycle();
        manufature = Build.MANUFACTURER.toLowerCase();

        setOnClickListener(this);

    }
    


    @Override
    public void onClick(View view) {
        if(packageName != null) {
            Intent intent = new Intent();
            if (packageName.equals("com.android.tv.settings")) {
                if(manufature.equals("Amlogic")) {
                    intent.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings"));
                    if (intent != null) {
                        getContext().startActivity(intent);
                    } else {
                        intent = getContext().getPackageManager().getLaunchIntentForPackage("com.mbx.settingsmbox");
                        if (intent != null) {
                            getContext().startActivity(intent);
                        } else {
                            intent = getContext().getPackageManager().getLaunchIntentForPackage("com.android.settings");
                            if (intent != null) {
                                getContext().startActivity(intent);
                            }
                        }
                    }
                }else if (manufature.equals("rockchip")){
                    intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.Settings"));
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }
                } else {
                    intent.setAction("com.android.settings.Settings");
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }
                }
            }else if (packageName.equals("com.droidlogic.mediacenter")) {
                if(manufature.equals("Amlogic")) {
                    intent = getContext().getPackageManager().getLaunchIntentForPackage("com.amlogic.mediacenter");
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), getResources().getString(R.string.havent_notice), Toast.LENGTH_SHORT).show();
                        intent = new Intent(getContext(), AppListActivity.class);
                        getContext().startActivity(intent);
                    }
                }else if (manufature.equals("rockchip")){
                    intent = getContext().getPackageManager().getLaunchIntentForPackage("com.rockchip.mediacenter");
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), getResources().getString(R.string.havent_notice), Toast.LENGTH_SHORT).show();
                        intent = new Intent(getContext(), AppListActivity.class);
                        getContext().startActivity(intent);
                    }
                }
            } else if (packageName.equals("com.google.android.youtube")) {
                if(manufature.equals("Amlogic")) {
                    intent = getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), getResources().getString(R.string.havent_notice), Toast.LENGTH_SHORT).show();
                        intent = new Intent(getContext(), AppListActivity.class);
                        getContext().startActivity(intent);
                    }
                }else if (manufature.equals("rockchip")){
                    intent = getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.youtube.tv");
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), getResources().getString(R.string.havent_notice), Toast.LENGTH_SHORT).show();
                        intent = new Intent(getContext(), AppListActivity.class);
                        getContext().startActivity(intent);
                    }
                }
            } else {
                intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    getContext().startActivity(intent);
                } else if (packageName.equals("com.droidlogic.FileBrower")) {
                    intent = getContext().getPackageManager().getLaunchIntentForPackage("com.fb.FileBrower");
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }else {
                        intent = getContext().getPackageManager().getLaunchIntentForPackage("com.android.rockchip");
                        if (intent != null) {
                            getContext().startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), getResources().getString(R.string.havent_notice), Toast.LENGTH_SHORT).show();
                            intent = new Intent(getContext(), AppListActivity.class);
                            getContext().startActivity(intent);
                        }
                    }
                }else {
                    Toast.makeText(getContext(), getResources().getString(R.string.havent_notice), Toast.LENGTH_SHORT).show();
                    intent = new Intent(getContext(), AppListActivity.class);
                    getContext().startActivity(intent);
                }
            }
        } else {
            Intent intent = new Intent(getContext(), AppListActivity.class);
            getContext().startActivity(intent);
        }
    }


    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

        if (gainFocus) {
            animator = EntryAnimation.createFocusAnimator(this);
        } else {

            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
            animator = EntryAnimation.createLoseFocusAnimator(this);

        }
        animator.start();
    }
}

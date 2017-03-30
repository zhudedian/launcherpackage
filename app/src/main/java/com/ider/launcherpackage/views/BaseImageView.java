package com.ider.launcherpackage.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
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

    public BaseImageView(Context context) {
        this(context, null);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.FIT_XY);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseImageView);
        this.packageName = typedArray.getString(R.styleable.BaseImageView_package_name);
        typedArray.recycle();

        setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(packageName != null) {
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if(intent != null) {
                getContext().startActivity(intent);
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

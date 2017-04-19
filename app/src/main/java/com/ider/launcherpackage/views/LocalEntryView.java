package com.ider.launcherpackage.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ider.launcherpackage.common.BitmapTools;
import com.ider.launcherpackage.common.EntryAnimation;
import com.ider.launcherpackage.launcher.MainActivity;

/**
 * Created by Eric on 2017/4/19.
 */

public abstract class LocalEntryView extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "LocalEntryview";

    public BitmapTools mBitmapTools;
    public Context mContext;
    private ObjectAnimator animator = null;

    public LocalEntryView(Context context) {
        this(context, null);
    }

    public LocalEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(this);
        setOnLongClickListener(this);
        mBitmapTools = BitmapTools.getInstance();
    }



    public abstract void onClick();

    public abstract void onLongClick();

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

        if(gainFocus) {
            animator = EntryAnimation.createFocusAnimator(this);
        } else {

            if(animator != null && animator.isRunning()) {
                animator.cancel();
            }
            animator = EntryAnimation.createLoseFocusAnimator(this);

        }
        animator.start();
    }

    @Override
    public void onClick(View view) {
        onClick();
    }

    @Override
    public boolean onLongClick(View view) {
        onLongClick();
        return true;
    }
}

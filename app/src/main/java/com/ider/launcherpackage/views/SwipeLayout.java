package com.ider.launcherpackage.views;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ider.launcherpackage.R;


/**
 * Created by ider-eric on 2017/2/23.
 */

public class SwipeLayout extends LinearLayout implements View.OnFocusChangeListener{

    private static final String TAG = "SwipeLayout";
    private boolean shown;
    private SwipeListener swipeListener;

    public interface SwipeListener {
        void onSwipeOpen();
        void onSwipeClose();
    }

    public void setSwipeListener(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for(int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setOnFocusChangeListener(this);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            if(!shown) {
                show();
            }
        } else {
            if(shown && allLostFocus()) {
                close();
            }
        }
    }

    private void show() {
        this.shown = true;
        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.swipe_layout_in);
        animator.setTarget(this);
        animator.start();
        this.swipeListener.onSwipeOpen();
    }

    public boolean isSwipeShown() {
        return this.shown;
    }

    private void close() {
        this.shown = false;
        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.swipe_layout_out);
        animator.setTarget(this);
        animator.start();
        this.swipeListener.onSwipeClose();
    }

    private boolean allLostFocus() {
        for(int i = 0; i < getChildCount(); i++) {
            if(getChildAt(i).hasFocus()) {
                return false;
            }
        }
        return true;
    }

    public void enableFocusable() {
        for(int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setFocusable(true);
        }
    }


}

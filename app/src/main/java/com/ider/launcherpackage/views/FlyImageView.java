package com.ider.launcherpackage.views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

@SuppressLint("NewApi")
public class FlyImageView extends ImageView {
	ViewPropertyAnimator anim;
	int animDuration = 300;


	public FlyImageView(Context context) {
		super(context);
		this.anim = animate();

	}

	public FlyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.anim = animate();

	}

	public FlyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.anim = animate();

	}



	public void flyTo(final int width, final int height, final int toX, final int toY) {
		ValueAnimator animator = ValueAnimator.ofInt(0, 100);
		final LayoutParams params = (LayoutParams) getLayoutParams();
		final int currentWidth = params.width;
		final int currentHeight = params.height;
		final int currentLeftMargin = params.leftMargin;
		final int currentTopMargin = params.topMargin;
		animator.addUpdateListener(new AnimatorUpdateListener() {

			private IntEvaluator mEvaluator = new IntEvaluator();
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {

				float fraction = animator.getAnimatedFraction();
				params.width = mEvaluator.evaluate(fraction, currentWidth, width);
				params.height = mEvaluator.evaluate(fraction, currentHeight, height);
				params.leftMargin = mEvaluator.evaluate(fraction, currentLeftMargin, toX);
				params.topMargin = mEvaluator.evaluate(fraction, currentTopMargin, toY);
				requestLayout();
				
			}
		});
		
		animator.setDuration(animDuration);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.start();
	}
	
	

	
	public void hide() {
		setVisibility(View.INVISIBLE);
	}
	
	public boolean isHidden() {
		return getVisibility() == View.INVISIBLE || getVisibility() == View.GONE;
	}
	
	public void show() {
		setVisibility(View.VISIBLE);
	}

}

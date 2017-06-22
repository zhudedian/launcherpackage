package com.ider.launcherpackage.clean;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcherfun.ProcessManager;
import com.ider.launcherpackage.R;

import java.text.DecimalFormat;

public class CleanActivity extends Activity {
    private static final String TAG = "CleanActivity";
    private TextView cleaned_memory = null;
    private ImageView rotation = null;
    private float oldMemory;
    private Animation rotationAnim = null;
    private DecimalFormat df;
    private Handler mHandler;
    Runnable clean = new Runnable() {
        public void run() {
            com.ider.launcherpackage.clean.CleanActivity.this.cleanMemory();
            float newMemory = (float) com.ider.launcherpackage.clean.CleanActivity.this.getAvailableMemory() / 1024.0F / 1024.0F;
            String newAvailableMemory = com.ider.launcherpackage.clean.CleanActivity.this.df.format((double)newMemory);
            Log.i("CleanActivity", "run: new = " + newMemory);
            com.ider.launcherpackage.clean.CleanActivity.this.cleaned_memory.setVisibility(0);
            String mMemoryFormat = com.ider.launcherpackage.clean.CleanActivity.this.getString(com.ider.launcherfun.R.string.CleanedMemory);
            float memorys = newMemory - com.ider.launcherpackage.clean.CleanActivity.this.oldMemory;
            String cleaned;
            if(memorys < 0.0F) {
                cleaned = "0";
            } else {
                cleaned = com.ider.launcherpackage.clean.CleanActivity.this.df.format((double)memorys);
            }

            com.ider.launcherpackage.clean.CleanActivity.this.cleaned_memory.setText(String.format(mMemoryFormat, new Object[]{cleaned, newAvailableMemory}));
        }
    };
    Runnable finishSelf = new Runnable() {
        public void run() {
            com.ider.launcherpackage.clean.CleanActivity.this.finish();
        }
    };

    public CleanActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(com.ider.launcherfun.R.layout.clean_layout);
        this.df = new DecimalFormat("##0.0");
        this.mHandler = new Handler();
        this.cleaned_memory = (TextView)this.findViewById(com.ider.launcherfun.R.id.cleanInfo);
        this.rotation = (ImageView)this.findViewById(com.ider.launcherfun.R.id.rotation_image);
        this.initAnimation();
        this.startAnimation();

    }

    public void cleanMemory() {
        ProcessManager.cleanMemory(this, false);
    }

    public long getAvailableMemory() {
        ActivityManager manager = (ActivityManager)this.getSystemService("activity");
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(mi);
        return mi.availMem;
    }

    private void initAnimation() {
        this.rotationAnim = new RotateAnimation(0.0F, 720.0F, 1, 0.5F, 1, 0.5F);
        this.rotationAnim.setDuration(700L);
        this.rotationAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                com.ider.launcherpackage.clean.CleanActivity.this.oldMemory = (float) com.ider.launcherpackage.clean.CleanActivity.this.getAvailableMemory() / 1024.0F / 1024.0F;
                Log.i("CleanActivity", "run: old = " + com.ider.launcherpackage.clean.CleanActivity.this.oldMemory);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                com.ider.launcherpackage.clean.CleanActivity.this.mHandler.post(com.ider.launcherpackage.clean.CleanActivity.this.clean);
                com.ider.launcherpackage.clean.CleanActivity.this.mHandler.postDelayed(com.ider.launcherpackage.clean.CleanActivity.this.finishSelf, 3000L);
            }
        });
    }

    public void startAnimation() {
        this.rotation.startAnimation(this.rotationAnim);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 23) {
            this.startAnimation();
            this.mHandler.removeCallbacks(this.finishSelf);
        }

        return super.onKeyDown(keyCode, event);
    }
}

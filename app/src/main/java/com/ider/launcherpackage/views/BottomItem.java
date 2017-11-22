package com.ider.launcherpackage.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.clean.CleanActivity;
import com.ider.launcherpackage.common.EntryAnimation;
import com.ider.launcherpackage.common.IntentCreator;
import com.ider.launcherpackage.launcher.HelpActivity;
import com.ider.launcherpackage.launcher.MainActivity;

import static com.ider.launcherpackage.R.drawable.ic_jiasu;

/**
 * Created by Eric on 2017/11/7.
 */

public class BottomItem extends LinearLayout implements View.OnClickListener{

    private String TAG = "BottomItem";
    private boolean isSelect = false;

    private ImageView image;
    private TextView text;

    public BottomItem(Context context) {
        this(context, null);
    }
    public BottomItem (Context context, AttributeSet attrs){
        super(context,attrs);
        this.setFocusable(true);
        LayoutInflater.from(context).inflate(R.layout.bottom_item, this);
        image = (ImageView)findViewById(R.id.image);
        text = (TextView)findViewById(R.id.text);
        freshView();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

        if (gainFocus) {
            setSelect(true);
        } else {
            setSelect(false);
        }

    }
    @Override
    public void onClick(View view) {
        Log.i(TAG,"onClick");
        if (getTag().equals("1")){
            Intent intent = new Intent(getContext(), CleanActivity.class);
            getContext().startActivity(intent);
        }else if (getTag().equals("2")){
            Intent intent = new IntentCreator(getContext()).createWifiIntent();
            getContext().startActivity(intent);
        }else if (getTag().equals("3")){
            Intent intent = new Intent();
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
        }else if (getTag().equals("4")){
            Intent intent = new Intent(getContext(),HelpActivity.class);
            getContext().startActivity(intent);
        }
    }

    public void freshView(){
        if (getTag().equals("1")){
            if (isSelect){
                image.setImageResource(R.drawable.ic_jiasu);
                text.setVisibility(VISIBLE);
            }else {
                image.setImageResource(R.drawable.ic_jiasu_grey);
                text.setVisibility(GONE);
            }
            text.setText(R.string.clean);
        }else if (getTag().equals("2")){
            if (isSelect){
                text.setVisibility(VISIBLE);
                image.setImageResource(R.drawable.ic_wlan);
            }else {
                text.setVisibility(GONE);
                image.setImageResource(R.drawable.ic_wlan_grep);
            }
            text.setText(R.string.wifi);
        }else if (getTag().equals("3")){
            if (isSelect){
                text.setVisibility(VISIBLE);
                image.setImageResource(R.drawable.ic_setting);
            }else {
                text.setVisibility(GONE);
                image.setImageResource(R.drawable.ic_setting_grey);
            }
            text.setText(R.string.settings);
        }else if (getTag().equals("4")){
            if (isSelect){
                text.setVisibility(VISIBLE);
                image.setImageResource(R.drawable.ic_help);
            }else {
                text.setVisibility(GONE);
                image.setImageResource(R.drawable.ic_help_grey);
            }
            text.setText(R.string.help);
        }
    }
    public void setSelect(boolean isSelect){
        this.isSelect = isSelect;
        freshView();
    }
}

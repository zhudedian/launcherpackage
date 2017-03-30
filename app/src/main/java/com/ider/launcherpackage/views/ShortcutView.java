package com.ider.launcherpackage.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcherpackage.common.EntryImageGetter;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.LauncherApplication;
import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.PreferenceManager;


public class ShortcutView extends BaseEntryView {

    private static final String TAG = "ShortcutView";

    public ItemEntry mItemEntry;
    public AppSelectWindow appSelectWindow;
    public ShortcutEdit editWindow;
    public PreferenceManager preferenceManager;
    private TextView shortcutText;
    private ImageView floatImage;


    public ShortcutView(Context context) {
        this(context, null);
    }

    public ShortcutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.default_shortcut, this);
        floatImage = (ImageView) findViewById(R.id.shortcut_image);
        shortcutText = (TextView) findViewById(R.id.shortcut_text);
        preferenceManager = PreferenceManager.getInstance(LauncherApplication.getContext());
        String savedPackage = preferenceManager.getString((String) getTag());
        if(savedPackage != null) {
            this.mItemEntry = new ItemEntry(savedPackage);
        } else {
            setDefault();
        }
    }

    @Override
    public void setDefault() {

    }



    @Override
    public void updateSelf() {
        if (mItemEntry != null) {
            Bitmap bitmap = EntryImageGetter.getEntryImage(mItemEntry.getPackageName());
            if(bitmap != null) {
                floatImage.setImageBitmap(bitmap);
            }
            shortcutText.setText(ItemEntry.loadLabel(getContext(), mItemEntry.getPackageName()));
        } else {
            // 加号
            floatImage.setImageBitmap(EntryImageGetter.getDefaultImage());
            shortcutText.setText(R.string.shortcut_default_title);

            ViewOutlineProvider provider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = 30;
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            };
            floatImage.setOutlineProvider(provider);
        }
    }


    @Override
    public void onClick() {
        if(this.mItemEntry == null) {
            showAppSelectWindow();
        } else {
            String mPackageName = this.mItemEntry.getPackageName();
            if(mPackageName != null) {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mPackageName);
                if(intent != null) {
                    mContext.startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onLongClick() {
        showEditWindow();
    }

    public void save() {
        preferenceManager.putString((String) getTag(), mItemEntry.getPackageName());
    }
    public void remove() {
        preferenceManager.remove((String) getTag());
    }


    public void showEditWindow() {
        if(this.editWindow == null) {
            editWindow = ShortcutEdit.getInstance(LauncherApplication.getContext());
        }
        this.editWindow.showEditWindow(this);
    }


    public void showAppSelectWindow() {
        if(this.appSelectWindow == null) {
            this.appSelectWindow = AppSelectWindow.getInstance(LauncherApplication.getContext());
        }
        this.appSelectWindow.showAppPopWindow(this);
    }

}
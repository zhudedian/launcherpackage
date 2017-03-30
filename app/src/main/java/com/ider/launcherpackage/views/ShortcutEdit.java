package com.ider.launcherpackage.views;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;
import com.ider.launcherpackage.R;


public class ShortcutEdit implements View.OnKeyListener, View.OnClickListener {

    private Context mContext;
    private PopupWindow editWindow;
    private ShortcutView baseView;
    private static ShortcutEdit INSTANCE;
    private ShortcutEdit(Context context) {
        this.mContext = context;
    }

    public static ShortcutEdit getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new ShortcutEdit(context);
        }
        return INSTANCE;
    }

    public void showEditWindow(ShortcutView baseView) {
        this.baseView = baseView;
        View view = View.inflate(mContext, R.layout.shortcut_edit_window, null);
        view.setOnKeyListener(this);

        view.findViewById(R.id.shortcut_edit_replace).setOnKeyListener(this);
        view.findViewById(R.id.shortcut_edit_delete).setOnKeyListener(this);
        view.findViewById(R.id.shortcut_edit_replace).setOnClickListener(this);
        view.findViewById(R.id.shortcut_edit_delete).setOnClickListener(this);

        int width = (int) (baseView.getWidth() * baseView.getScaleX());
        int height = (int) (baseView.getHeight() * baseView.getScaleY());
        editWindow = new PopupWindow(view, width, height, true);
        editWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        editWindow.showAsDropDown(baseView, 0, -baseView.getHeight());
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            editWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.shortcut_edit_replace) {
            editWindow.dismiss();
            this.baseView.showAppSelectWindow();
        } else if (view.getId() == R.id.shortcut_edit_delete) {
            editWindow.dismiss();
            baseView.mItemEntry = null;
            baseView.remove();
            baseView.updateSelf();
        }
    }
}

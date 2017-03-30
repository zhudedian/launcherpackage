package com.ider.launcherpackage.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by ider-eric on 2017/2/24.
 */

public class IntentCreator {

    private Context context;
    private String manufature;
    private int sdkVersion;

    public IntentCreator(Context context) {
        this.context = context;
        manufature = Build.MANUFACTURER.toLowerCase();
        sdkVersion = Build.VERSION.SDK_INT;
    }

    public Intent createWifiIntent() {
        Intent intent = new Intent();

        if(manufature.equals("Amlogic")) {
            if(sdkVersion >= 21) {
                intent.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.connectivity.NetworkActivity"));
            } else {
                intent.setComponent(new ComponentName("com.mbx.settingsmbox", "com.mbx.settingsmbox.SettingsMboxActivity"));
                intent.putExtra("position", 1);
            }
        } else if(manufature.equals("rockchip")) {
            intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.network_settingnew"));
        } else {
            intent.setAction("android.settings.WIFI_SETTINGS");
        }
        return intent;
    }

    public Intent createDisplayIntent() {
        Intent intent = new Intent();
        if(manufature.equals("amlogic")) {
            if(sdkVersion >= 21) {
                intent.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.device.display.DisplayActivity"));
            } else {
                intent.setComponent(new ComponentName("com.mbx.settingsmbox", "com.mbx.settingsmbox.SettingsMboxActivity"));
                intent.putExtra("position", 2);
            }
        } else if(manufature.equals("rockchip")) {
            intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.ScreensSettings"));
        } else {
            intent.setAction("com.android.settings.DISPLAY_SETTINGS");
        }
        return intent;
    }

    public Intent createAudioIntent() {
        Intent intent = new Intent();
        if (manufature.equals("amlogic")) {
            if(sdkVersion >= 21) {
                intent.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.device.sound.SoundActivity"));
            }
        } else if(manufature.equals("rockchip")) {
            intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.sound.SoundSetting"));
        } else {
            intent.setAction("com.android.settings.SOUND_SETTINGS");
        }
        return intent;
    }

    public Intent createAppsIntent() {
        Intent intent = new Intent();
        if(manufature.equals("amlogic")) {
            if(sdkVersion >= 21) {
                intent.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.device.apps.AppsActivity"));
            }
        } else if(manufature.equals("rockchip")) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setComponent(new ComponentName("com.rk_itvui.settings", "com.rk_itvui.settings.dialog.ManageApplications"));
        } else {
            intent.setAction("android.settings.MANAGE_APPLICATIONS_SETTINGS");
        }
        return intent;
    }

}

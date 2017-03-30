package com.ider.launcherpackage.launcher;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.ider.launcherfun.CleanActivity;
import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.IntentCreator;
import com.ider.launcherpackage.views.BaseImageView;
import com.ider.launcherpackage.views.ShortcutFolder;
import com.ider.launcherpackage.views.SwipeLayout;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends FullscreenActivity {

    private static final String TAG = "MainActivity";

    private ImageView stateWifi, stateBluetooth, stateUsb;
    private View mainContainer;
    private BaseImageView vApps;
    private ShortcutFolder vFolder;
    private SwipeLayout functionContainer;
    private ImageView vSwipClean, vSwipeWifi, vSwipeDisplay, vSwipeAudio, vSwipeApps;
    private boolean focusFlag = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (focusFlag) {
            vApps.requestFocus();
            functionContainer.enableFocusable();
            focusFlag = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContainer = findViewById(R.id.content_main);
        vApps = (BaseImageView) findViewById(R.id.main_apps);
        vFolder = (ShortcutFolder) findViewById(R.id.folder14);
        stateWifi = (ImageView) findViewById(R.id.state_wifi);
        stateBluetooth = (ImageView) findViewById(R.id.state_bluetooth);
        stateUsb = (ImageView) findViewById(R.id.state_usb);

        functionContainer = (SwipeLayout) findViewById(R.id.function_main);
        vSwipClean = (ImageView) findViewById(R.id.setting_cleanup);
        vSwipeWifi = (ImageView) findViewById(R.id.setting_network);
        vSwipeDisplay = (ImageView) findViewById(R.id.setting_display);
        vSwipeAudio = (ImageView) findViewById(R.id.setting_sound);
        vSwipeApps = (ImageView) findViewById(R.id.setting_apps);

        setListeners();
        bindReceivers();
        setUsbState();

        BluetoothManager btManager = (BluetoothManager) getSystemService(Service.BLUETOOTH_SERVICE);
        setBtState(btManager.getAdapter().getState());
    }

    private void setListeners() {
        //最右边五个快捷划开功能的回调
        functionContainer.setSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onSwipeOpen() {
                Log.i(TAG, "onSwipeOpen: ");
                AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.content_swipe_off);
                animator.setTarget(mainContainer);
                animator.start();
            }

            @Override
            public void onSwipeClose() {
                Log.i(TAG, "onSwipeClose: ");
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.content_swipe_on);
                set.setTarget(mainContainer);
                set.start();
            }
        });
        vSwipClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CleanActivity.class);
                startActivity(intent);
            }
        });
        vSwipeWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new IntentCreator(MainActivity.this).createWifiIntent();
                startActivity(intent);
            }
        });
        vSwipeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new IntentCreator(MainActivity.this).createDisplayIntent();
                startActivity(intent);
            }
        });
        vSwipeAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new IntentCreator(MainActivity.this).createAudioIntent();
                startActivity(intent);
            }
        });
        vSwipeApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new IntentCreator(MainActivity.this).createAppsIntent();
                startActivity(intent);
            }
        });
    }


    private void setUsbState() {
        if(isUsbExists()) {
            stateUsb.setVisibility(View.VISIBLE);
        } else {
            stateUsb.setVisibility(View.GONE);
        }
    }

    private boolean isUsbExists() {
        if(Build.MANUFACTURER.toLowerCase().equals("amlogic")) {
            if(Build.VERSION.SDK_INT >= 23) {
                return checkAmlogic6Usb();
            }
        }
        return checkNormalUsbExists();
    }

    private void setBtState(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                stateBluetooth.setVisibility(View.VISIBLE);
                stateBluetooth.setImageResource(R.drawable.ic_bluetooth_white_36dp);
                break;
            case BluetoothAdapter.STATE_OFF:
                stateBluetooth.setVisibility(View.GONE);
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                stateBluetooth.setVisibility(View.VISIBLE);
                stateBluetooth.setImageResource(R.drawable.ic_bluetooth_connected_white_36dp);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
    }

    public void unregisterReceivers() {
        try {
            unregisterReceiver(netReceiver);
            unregisterReceiver(mediaReciever);
            unregisterReceiver(btReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bindReceivers() {
        IntentFilter filter;
        // 外接u盘广播
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mediaReciever, filter);

        filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(netReceiver, filter);

        filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btReceiver, filter);
    }

    public static boolean isFirstIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("launcher_prefers", Context.MODE_PRIVATE);
        boolean firstIn = preferences.getBoolean("first_in", true);
        if (firstIn) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_in", false);
            editor.apply();
        }
        return firstIn;
    }

    BroadcastReceiver netReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            updateNetInfo();
        }
    };

    public void updateNetInfo() {
        if (isEthernetConnected()) {
            stateWifi.setImageResource(R.drawable.ic_settings_ethernet_white_36dp);
            return;
        }
        if (isWifiConnected()) {
            stateWifi.setImageResource(R.drawable.ic_wifi_white_36dp);
            return;
        }
        stateWifi.setImageResource(R.drawable.ic_signal_wifi_off_white_36dp);
    }

    public boolean isEthernetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        return info.isConnected() && info.isAvailable();
    }

    public boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info.isConnected() && info.isAvailable();

    }

    BroadcastReceiver mediaReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUsbState();
        }
    };

    BroadcastReceiver btReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                setBtState(blueState);
            }
        }
    };


    /* Amlogic平台6.0以上系统 */
    private boolean checkAmlogic6Usb() {
        StorageManager mStorageManager = (StorageManager) getSystemService(Service.STORAGE_SERVICE);
        try {
            Class StorageManager = Class.forName("android.os.storage.StorageManager");
            Class VolumeInfo = Class.forName("android.os.storage.VolumeInfo");
            Class DiskInfo = Class.forName("android.os.storage.DiskInfo");

            Method getVolumes = StorageManager.getMethod("getVolumes");
            Method isMountedReadable = VolumeInfo.getMethod("isMountedReadable");
            Method getType = VolumeInfo.getMethod("getType");
            Method getDisk = VolumeInfo.getMethod("getDisk");

            Method isUsb = DiskInfo.getMethod("isUsb");
            Method getDescription = DiskInfo.getMethod("getDescription");
            List volumes = (List) getVolumes.invoke(mStorageManager);
            for(int i = 0; i < volumes.size(); i++) {
                if(volumes.get(i) != null && (boolean) isMountedReadable.invoke(volumes.get(i))
                        && (int) getType.invoke(volumes.get(i)) == 0) {
                    Object diskInfo = getDisk.invoke(volumes.get(i));
                    boolean usbExists = (boolean) isUsb.invoke(diskInfo);
                    String description = (String) getDescription.invoke(diskInfo);
                    Log.i(TAG, "isUsbExists: " + usbExists + ":" + description);
                    if(usbExists) {
                        return true;
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean checkNormalUsbExists() {
        String[] usbPaths = {"/storage/external_storage"};
        for (String usbPath : usbPaths) {
            File dir = new File(usbPath);
            if (dir.exists() && dir.isDirectory()) {
                if (dir.listFiles() != null) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if(functionContainer.isSwipeShown()) {
            vFolder.requestFocus();
        } else {
            vApps.requestFocus();
        }

    }
}

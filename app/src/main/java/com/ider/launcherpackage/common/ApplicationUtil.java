package com.ider.launcherpackage.common;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.ider.launcherpackage.fastkey.Application;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ApplicationUtil {

    public static List<PackageHolder> queryApplication(Context context) {
        List<PackageHolder> enties = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (!packageName.equals("com.ider.launcherpackage")&&!packageName.equals("com.ider.boxlauncher_3368")) {
                enties.add(new PackageHolder(packageName, null));
            }
        }
        HashSet h = new HashSet(enties);//删除重复元素
        enties.clear();
        enties.addAll(h);
        return enties;
    }
    public static void startApp(Context context, Application app) {
        PackageManager pm = context.getPackageManager();
        String pkgName = app.getPackageName();
        Intent intent = pm.getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }
    public static Application doApplication(Context context, String pkgName) {

        if (pkgName != null) {
            PackageManager pm = context.getPackageManager();
            if (pm == null) {
                pm = context.getPackageManager();
            }
            Application app = null;
            ApplicationInfo info;
            try {
                info = pm.getApplicationInfo(pkgName, 0);
                app = new Application(pkgName);
                app.setIntent(pm.getLaunchIntentForPackage(pkgName));
                app.setLabel(info.loadLabel(pm).toString());
                app.setIcon(info.loadIcon(pm));
                String dir = info.publicSourceDir;
                Double size = Double.valueOf((int) new File(dir).length());
                app.setSize((double) Math.round(size / 1024 / 1024 * 100) / 100);
            } catch (PackageManager.NameNotFoundException e) {
                app = null;
                e.printStackTrace();
            }
            return app;
        }
        return null;
    }
}

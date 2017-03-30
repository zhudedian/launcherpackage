package com.ider.launcherpackage.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.R.id.list;


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
            if (!packageName.equals("com.ider.launcherpackage")&&!packageName.equals("com.ider.tools")&&!packageName.equals("com.ider.boxlauncher_3368")) {
                enties.add(new PackageHolder(packageName, null));
            }
        }
        HashSet h = new HashSet(enties);//删除重复元素
        enties.clear();
        enties.addAll(h);
        return enties;
    }

}

package com.ider.launcherpackage.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.ArrayList;
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
            enties.add(new PackageHolder(packageName, null));
        }
        return enties;
    }

}

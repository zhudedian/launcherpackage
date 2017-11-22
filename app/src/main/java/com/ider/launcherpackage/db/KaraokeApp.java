package com.ider.launcherpackage.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Eric on 2017/10/28.
 */

public class KaraokeApp extends DataSupport {

    private String packageName;

    public KaraokeApp(String packageName){
        this.packageName = packageName;
    }

    public String getPackageName(){
        return this.packageName;
    }
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
}

package com.ider.launcherpackage.launcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ider-eric on 2017/2/22.
 */

public class DbManager {

    private Context context;
    private String dbName = "app.db";
    private DaoMaster.DevOpenHelper helper;

    private static DbManager dbManager;
    private DbManager(Context context) {
        this.context = context;
    }

    public static DbManager getInstance(Context context) {
        synchronized (DbManager.class) {
            if(dbManager == null) {
                dbManager = new DbManager(context);
            }
        }
        return dbManager;
    }



    private SQLiteDatabase getReadableDatabase() {
        if(helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return helper.getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        if(helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return helper.getWritableDatabase();
    }

    public void insertPackage(PackageHolder holder) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            dao.insert(holder);
        }
    }

    public void insertPackages(ArrayList<PackageHolder> holders) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            dao.insertInTx(holders);
        }
    }

    public List<PackageHolder> queryPackages(String tag) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            QueryBuilder<PackageHolder> queryBuilder = dao.queryBuilder();
            return queryBuilder.list();
        }
    }

    public void removePackage(PackageHolder holder) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            dao.delete(holder);
        }
    }

}

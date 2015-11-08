package com.ydhl.cjl.db;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

/**
 * Created by Lianxw on 2015/5/4.
 */
public class DbUtilsFactory implements DbUtils.DbUpgradeListener {

    private  static final String DB_NAME ="cjl";
    private static final int DB_VERSION = 0;
    private static DbUtils.DbUpgradeListener UPGRADE_LISTENER = new DbUtilsFactory();

    public static DbUtils create(Context context) {
        return DbUtils.create(context,DB_NAME,DB_VERSION,UPGRADE_LISTENER);
    }

    @Override
    public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {

    }
}

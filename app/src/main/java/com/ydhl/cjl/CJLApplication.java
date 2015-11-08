package com.ydhl.cjl;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ydhl.cjl.activity.SplashActivity;
import com.ydhl.cjl.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用程序对象
 */
public class CJLApplication extends Application {

    public static final String KEY_UID = "uid";
    public static final String KEY_CLIENT_LIST = "client_list";
    private Map<String,Object> data = new HashMap();



    @Override
    public void onCreate() {
        super.onCreate();

    }




    /**
     * 获取应用程序数据
     * @param key 数据的键
     * @return 数据的值
     */
    public Object getAppData(String key) {
        return data.get(key);
    }

    /**
     * 设置应用程序数据
     * @param key 数据的键
     * @param val 数据的值
     */
    public void setAppdata(String key,Object val) {
        data.put(key,val);
    }






}

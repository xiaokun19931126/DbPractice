package com.xiaokun.dbpractice;

import android.app.Application;

import com.xiaokun.dbpractice.util.AppUtils;
import com.xiaokun.dbpractice.util.PrefsUtils;
import com.xiaokun.dbpractice.util.Toasts;

/**
 * @author xiaokun
 * @date 2017/12/21
 */

public class App extends Application
{
    public static PrefsUtils mPref;

    @Override
    public void onCreate()
    {
        super.onCreate();
        AppUtils.init(this);
        Toasts.register(this);//初始化toast
        mPref = new PrefsUtils(this, "app_data");//初始化sp
    }
}

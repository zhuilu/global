package com.global.system;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

public class SystemUtil
{

    /**
     * 客户端退出
     * 
     * @param context
     */
    public static void exitClient(Context context)
    {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        android.os.Process.killProcess(android.os.Process.myPid());
        activityManager.restartPackage("com.nbcb.move");

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

            }
        }).start();

        System.exit(0);
    }
}

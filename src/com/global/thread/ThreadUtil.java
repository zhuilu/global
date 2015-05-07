package com.global.thread;

import android.os.Looper;

/**
 * 线程工具
 * 
 * @author daishulin@163.com
 *
 */
public class ThreadUtil {
    /**
     * 检查是否在主线程
     * 
     * @return 是否在主线程
     */
    public static boolean checkMainThread() {
        return Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper();
    }
}

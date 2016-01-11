package com.nero.toolbox;

/**
 * Created by Nero on 2015/8/14.
 */

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.Date;

/**
 * <uses-permission android:name="android.permission.WAKE_LOCK" />
 */
public class MyService extends Service {
    public PowerManager.WakeLock wakeLock = null;
    public static String TAG=MyService.class.getSimpleName();
    public static boolean D=false;
    private static Handler handler = new Handler();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        acquireWakeLock();
        handler.post(showTime);
    }
    private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
            Log.i("time:", new Date().toString());
            //每隔一秒後執行一次
            handler.postDelayed(this, 1000);
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand called");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy callded");
        super.onDestroy();
        releaseWakeLock();
        //當被OS回收資源時自動重啟
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    /**
     *取得裝置待機時執行權限
     */
    private void acquireWakeLock() {
        if (null == wakeLock){
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock){
                wakeLock.acquire();
            }
        }
    }
    /**
     * 釋放裝置待機時執行權限
     */
    private void releaseWakeLock() {
        if (null != wakeLock){
            wakeLock.release();
            wakeLock = null;
        }
    }


}


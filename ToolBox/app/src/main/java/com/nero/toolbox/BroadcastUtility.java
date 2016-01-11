package com.nero.toolbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by Nero on 2016/1/11.
 */
public class BroadcastUtility {
    public static String TAG=BroadcastUtility.class.getSimpleName();
    public static boolean D=true;
    private static Context context;
    public static Intent intent;
    public BroadcastUtility(Context context) {
        BroadcastUtility.context = context;
    }
    public final static String EXTRA_DATA ="com.nero.Service.EXTRA_DATA";
    public final static String ACTION_DATA_AVAILABLE = "com.nero.Service.ACTION_DATA_AVAILABLE";

    /**
     * 事件過濾器
     * @return
     */
    public static IntentFilter mIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        intentFilter.addAction(EXTRA_DATA);
        return intentFilter;
    }
    public static BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_DATA_AVAILABLE.equals(action)) {
                String sender = intent.getStringExtra(EXTRA_DATA);
                if(D)Log.i(TAG,"get:"+sender);
            }

        }
    };
    public static void sendMessage(String msg){
        if(D)Log.i(TAG,"msg:"+msg);
        intent = new Intent(ACTION_DATA_AVAILABLE);
        intent.putExtra(EXTRA_DATA,msg);
        context.sendBroadcast(intent);
    }
}

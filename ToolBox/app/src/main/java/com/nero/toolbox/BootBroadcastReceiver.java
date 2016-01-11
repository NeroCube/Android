package com.nero.toolbox;

/**
 * Created by Nero on 2015/8/14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public static String TAG=BootBroadcastReceiver.class.getSimpleName();
    public static boolean D=false;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"ssasasas",Toast.LENGTH_LONG).show();
        if(D)Log.i(TAG, "recevie boot completed ... ");
        Intent startServiceIntent = new Intent(context, MyService.class);
        startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(startServiceIntent);
    }
}

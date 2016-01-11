package com.nero.toolbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Nero on 2016/1/8.
 */
public class NetworkUtility {
    public String TAG=NetworkUtility.class.getSimpleName();
    public boolean D=false;
    public enum Requestmethod {POST,GET}
    public enum Responsemethod {JSON,DIRECT}
    public static Context context;
    public NetworkUtility(Context context) {
        NetworkUtility.context = context;
    }
    /**
     *<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
    public String NetworkState() {
        ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo Info = CM.getActiveNetworkInfo();
        if (Info == null || !Info.isConnectedOrConnecting()){
            if(D) Log.i(TAG, "No connection");
            return "NO";
        }
        else{
            int netType = Info.getType();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                if(D) Log.i(TAG, "Wifi connection");
//	            // Need to get wifi strength
//	            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//	            List<ScanResult> scanResult = wifiManager.getScanResults();
//	            for (int i = 0; i < scanResult.size(); i++) {
//	                Log.d("scanResult", "Speed of wifi"+scanResult.get(i).level);//The db level of signal
//	            }
                return "WIFI";
            }
            else if (netType == ConnectivityManager.TYPE_MOBILE) {
                if(D) Log.i(TAG, "GPRS/3G connection");
                return "3G";
            }
            else{
                return "UN";
            }
        }
    }
    /**
     *
     */
    public boolean isNetwork(){
        if(NetworkState().equals("3G")||NetworkState().equals("WIFI")){
            return true;
        }
        else{
            if(D) Log.i(TAG, "can`t get Network");
            return false;
        }
    }
}

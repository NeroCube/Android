package com.nero.toolbox;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String TAG=MainActivity.class.getSimpleName();
    public static boolean D=true;
    private static String msg="";
    private Button run;
    private static TextView ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        run=(Button)findViewById(R.id.button);
        ans=(TextView)findViewById(R.id.textView);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                How2SendBroadcast();
            }
        });
    }
    /**
     * 實現用Android對MySQL執行SQL指令
     */
    public void How2SQLExcute(){
        new SQLExcute().execute();
    }
    /**
     * 實現用Android發起HTTPRequest取的資料
     * 提供POST、GET請求方式
     * JSON、DIRECT直接回傳完整html內容方式
     */
    public void How2makeHttpRequest(){
        new makeHttpRequest().execute();
    }
    /**
     *
     */
    public class makeHttpRequest extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String var="";
            String RequestUrl="http://opendata.epa.gov.tw/ws/Data/REWXQA/?$select=SiteName,County,PM2.5,PublishTime&$orderby=SiteName&$skip=0&$top=1000&format=json&sort=County";
            msg= HttpUtility.makeHttpRequest(var,RequestUrl, HttpUtility.Request.GET, HttpUtility.Response.DIRECT);
            return "Execute";
        }
        protected void onPostExecute(String result) {
            List<Object> returnList = new ArrayList<Object>();
            try {
                returnList=JSONParser.getListFromJsonStr(msg);
                for (int i = 0; i < returnList.size(); i++) {
                    Map<String, String> temp = new HashMap<String, String>();
                    temp=(Map<String, String>) returnList.get(i);
                    String t="";
                    t="地點:"+temp.get("SiteName")+" 縣市:"+temp.get("County")+" PM2.5:"+temp.get("PM2.5")+" 測量時間:"+temp.get("PublishTime")+" ";
                    if(D)Log.i(TAG, "t:" + t);
                }
            } catch (JSONException e) {e.printStackTrace();}

        }
        public void onPreExecute() {}
        protected void onProgressUpdate(Integer... progress) {}
    }
    /**
     *
     */
    public class SQLExcute extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String sql="SELECT * FROM `Users`";
            msg=HttpUtility.SQLExcute(Config.host,Config.path,Config.database,sql).toString();
            if(D) Log.i(TAG, "Response:" + msg);
//            String var="db="+Config.database+",sql="+sql+",mode=4";
//            String RequestUrl="http://host/FilePath.php";
//            msg= HttpUtility.makeHttpRequest(var, RequestUrl, HttpUtility.Request.POST, HttpUtility.Response.JSON);
            return "Execute";
        }
        protected void onPostExecute(String result) {
            ans.setText(msg);}
        public void onPreExecute() {}
        protected void onProgressUpdate(Integer... progress) {}
    }
    /**
     * 執行Service輪巡
     */
    public void How2ServicePolling(){
        if(!isServiceRunning("MyService")){
            Intent intent = new Intent(MainActivity.this, MyService.class);
            MainActivity.this.startService(intent);
            Log.i(TAG, "is MyService running :" + isServiceRunning("MyService"));
        }
    }
    /**
     * 檢查Service是否執行
     * @param ServiceName
     * @return
     */
    public boolean isServiceRunning(String ServiceName){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            String allServiceName = service.service.getClassName();
            if (allServiceName.equals(ServiceName)){ return true;}
        }
        return false;
    }
    /**
     *如何接收傳送廣播
     */
    public void How2SendBroadcast(){
        registerReceiver(mReceiver, BroadcastUtility.mIntentFilter());
        BroadcastUtility b=new BroadcastUtility(getApplication());
        b.sendMessage("123");

    }
    /**
     *
     */
    public static BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BroadcastUtility.ACTION_DATA_AVAILABLE.equals(action)) {
                String sender = intent.getStringExtra(BroadcastUtility.EXTRA_DATA);
                Log.i(TAG,"get:"+sender);
                ans.setText("get:"+sender);

            }
        }
    };

}

package com.nero.toolbox;

import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Nero on 2016/1/8.
 */
public class HttpUtility {
    public static String TAG=HttpUtility.class.getSimpleName();
    public static boolean D=false;
    public enum Request {POST,GET}
    public enum Response {JSON,DIRECT}
    /**
     *
     */
    public static String makeHttpRequest(String params,String RequestUrl,Request request,Response response){
        if(D)Log.i(TAG,"params:"+params+"，RequestUrl:" + RequestUrl + "，Requestmethod:" + request + "，Responsemethod:" + response);
        List<NameValuePair> paramslist=ParamsToList(params);
        String responsString="";
        InputStream is = null;
        try {
            //Requestmethod
            HttpClient HttpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            if(request == Request.POST){
                HttpPost HttpPost = new org.apache.http.client.methods.HttpPost(RequestUrl);
                HttpPost.setEntity(new UrlEncodedFormEntity(paramslist));
                httpResponse = HttpClient.execute(HttpPost);
            }
            else if(request == Request.GET){
                String paramString = URLEncodedUtils.format(paramslist, "utf-8");
                RequestUrl += "?" + paramString;
                HttpGet HttpGet = new org.apache.http.client.methods.HttpGet(RequestUrl);
                httpResponse = HttpClient.execute(HttpGet);
            }
            else{
                if(D) Log.i(TAG, "using unknown method");
            }
            //Responsemethod
            if(httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK ){
                if(response == Response.JSON) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");}
                        is.close();
                        responsString = sb.toString();
                    } catch (Exception e) {
                        if(D) Log.i(TAG, "Error converting result " + e.toString());}
                }
                else if(response == Response.DIRECT) {
                    responsString = EntityUtils.toString(httpResponse.getEntity());
                }
                else{
                    if(D) Log.i(TAG, "using unknown method");
                }
            }
            else {if(D) Log.i(TAG, "HttpCode:" + httpResponse.getStatusLine().getStatusCode());}
        }
        catch (Exception e) {
            if(D) Log.i(TAG, "Exception when try:" + e.toString());
        }
        if(D) Log.i(TAG, "responsString:" + responsString);
        return responsString;
    }
    /**
     *
     */
    public static List<NameValuePair> ParamsToList(String p) {
        String[] tokens = p.split(",");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if(!p.equals("")){
            for (int j = 0; j < tokens.length; j++) {
                try{
                    String str=tokens[j];
                    String[] Character = str.split("=");
                    String tag=Character[0];
                    String value=Character[1];
                    params.add(new BasicNameValuePair(tag, value));
                } catch (Exception e) { if(D) Log.i(TAG, "Exception when try ParamsToList :" + e.toString());}
            }
        }
        return params;
    }

    /**
	 *
	 */
    public static String makeHttpRequest(List<NameValuePair> params,String RequestUrl,Request request,Response response){
		if(D)Log.i(TAG,"，RequestUrl:" + RequestUrl + "，Requestmethod:" + request + "，Responsemethod:" + response);
        String responsString="";
        InputStream is = null;
        try {
            //Requestmethod
            HttpClient HttpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            if(request == Request.POST){
                HttpPost HttpPost = new org.apache.http.client.methods.HttpPost(RequestUrl);
                HttpPost.setEntity(new UrlEncodedFormEntity(params));
                httpResponse = HttpClient.execute(HttpPost);
            }
            else if(request == Request.GET){
                String paramString = URLEncodedUtils.format(params, "utf-8");
                RequestUrl += "?" + paramString;
                HttpGet HttpGet = new org.apache.http.client.methods.HttpGet(RequestUrl);
                httpResponse = HttpClient.execute(HttpGet);
            }
            else{
                if(D) Log.i(TAG, "using unknown method");
            }
            //Responsemethod
            if(httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK ){
                if(response == Response.JSON) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");}
                        is.close();
                        responsString = sb.toString();
                    } catch (Exception e) {
                        if(D) Log.i(TAG, "Error converting result " + e.toString());}
                }
                else if(response == Response.DIRECT) {
                    responsString = EntityUtils.toString(httpResponse.getEntity());
                }
                else{
                    if(D) Log.i(TAG, "using unknown method");
                }
            }
            else {if(D) Log.i(TAG, "HttpCode:" + httpResponse.getStatusLine().getStatusCode());}
        }
        catch (Exception e) {
            if(D) Log.i(TAG, "Exception when try:" + e.toString());
        }
        return responsString;
    }
    /**
	 * SQLComand 轉List<NameValuePair>
	 * localhost=210.61.88.210(不含http://)
	 * RequestUrl=http://210.61.88.210/guaTest/ADBC.php
	 * FileUrl=guaTest/ADBC.php
	 */
    public static List<Object> SQLExcute(String host, String FilePath, String database, String sql) {
        String RequestUrl="http://"+host+"/"+FilePath;
        try {
            sql = new String(sql.getBytes(),"ISO8859_1");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        List<NameValuePair> params = SQLComandToList(host,database,sql);
        List<Object> returnList = new ArrayList<Object>();
        int result=0;
        String message=null;
        try {
            String Response=makeHttpRequest(params, RequestUrl, Request.POST, HttpUtility.Response.JSON);
            if(D) Log.i(TAG, "Response:" + Response);
            JSONObject json =JSONParser.StringToJSONObject(Response);
            result = json.getInt("success");
            if(D) Log.i(TAG, "result:" + result);
            if(result==4) {
                String objects = json.getString("objects");
                if(D) Log.i(TAG, "objects:" + objects);
                returnList=JSONParser.getListFromJsonStr(objects);
            }
            else{
                try{
                    message = json.getString("message");
                    if(D) Log.i(TAG, "message:" + message);
                    returnList=getListFromResponseStr(""+result,message);
                }
                catch (Exception e) {
                    if(D) Log.i(TAG, "Exception:" + e.toString());}
            }
        }
        catch (Exception e) {
            if(D) Log.i(TAG, "Exception:" + e.toString());}
        return returnList;
    }
    /**
	 * SQLComand 轉List<NameValuePair>
	 */
    public static List<NameValuePair> SQLComandToList(String host,String dbname,String sql) {
        StringTokenizer st = new StringTokenizer(sql, " ");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String mode=st.nextToken();
        if(mode.equalsIgnoreCase("insert")){mode="1";}
        else if(mode.equalsIgnoreCase("delete")){mode="2";}
        else if(mode.equalsIgnoreCase("update")){mode="3";}
        else if(mode.equalsIgnoreCase("select")){mode="4";}
        else{return params;}
        try{
            params.add(new BasicNameValuePair("ad", host));
            params.add(new BasicNameValuePair("db", dbname));
            params.add(new BasicNameValuePair("sql", sql));
            params.add(new BasicNameValuePair("mode", mode));
        }
        catch (Exception e) {return params;}
        return params;
    }
    /**
     *
     */
    public static List<Object> getListFromResponseStr(String success,String massage){
        List<Object> returnList = new ArrayList<Object>();
        Map<String, String> nestedList;
        try {
            nestedList = new HashMap<String, String>();
            nestedList.put("success",success);
            nestedList.put("massage",massage);
            returnList.add(nestedList);

        }
        catch (Exception e) {return returnList;}
        return returnList;
    }
}

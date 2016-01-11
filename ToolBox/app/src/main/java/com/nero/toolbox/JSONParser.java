package com.nero.toolbox;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Nero on 2016/1/11.
 */
public class JSONParser {
    public static String TAG=JSONParser.class.getSimpleName();
    public static boolean D=false;
    /**
     * JSONStr String 轉JSONObject
     */
    public static JSONObject StringToJSONObject(String JSONStr) {
        JSONObject jObj = null;
        try {jObj = new JSONObject(JSONStr);
        }catch (JSONException e) {
            if(D)Log.i(TAG, "JSONObject:" + e.toString());}
        return jObj;
    }
    /**
     *
     */
    public static List<Object> getListFromJsonStr(String jsonstr) throws JSONException {
        if(D)Log.i(TAG, "jsonstr:" + jsonstr);
        jsonstr=jsonstr.replace("[", "");
        jsonstr=jsonstr.replace("]", "");
        List<Object> returnList = new ArrayList<Object>();
        Map<String, String> nestedList;
        StringTokenizer st = new StringTokenizer(jsonstr, "}");
        JSONObject json =new JSONObject();
        try{
            for (int i=0; st.hasMoreTokens();i++) {
                nestedList = new HashMap<String, String>();
                String t=st.nextToken()+"}";
                if(i>0){
                    t=""+t.replace(",{", "{");
                }
                json =StringToJSONObject(t);
                nestedList=JSONObjectToList(json);
                returnList.add(nestedList);
            }
        }
        catch (Exception e) {if(D)Log.i(TAG, "getListFromJsonStr:" + e.toString());}
        return returnList;
    }

    /**
     *
     */
    public static Object convertJsonItem(Object o) throws JSONException {
        if (o == null) { return "null";}
        else if (o instanceof JSONObject) {return getListFromJsonObject((JSONObject) o);}
        else if (o instanceof JSONArray) {return getListFromJsonArray((JSONArray) o);}
        else if (o.equals(Boolean.FALSE) || (o instanceof String && ((String) o).equalsIgnoreCase("false"))) {return false;}
        else if (o.equals(Boolean.TRUE) || (o instanceof String && ((String) o).equalsIgnoreCase("true"))) {return true;}
        else if (o instanceof Number) {return o;}
        else{}
        return o.toString();
    }
    /**
     *
     */
    public static List<Object> getListFromJsonArray(JSONArray jArray) throws JSONException {
        List<Object> returnList = new ArrayList<Object>();
        for (int i = 0; i < jArray.length(); i++) {
            returnList.add(convertJsonItem(jArray.get(i)));
        }
        return returnList;
    }
    /**
     *
     */
    public static List<Object> getListFromJsonObject(JSONObject jObject) throws JSONException {
        List<Object> returnList = new ArrayList<Object>();
        @SuppressWarnings("unchecked")
        Iterator<String> keys = jObject.keys();
        List<String> keysList = new ArrayList<String>();
        while (keys.hasNext()) {
            keysList.add(keys.next());
        }
        Collections.sort(keysList);
        for (String key : keysList) {
            List<Object> nestedList = new ArrayList<Object>();
            nestedList.add(key);
            nestedList.add(convertJsonItem(jObject.get(key)));
            returnList.add(nestedList);
        }
        return returnList;
    }
    /**
     *
     */
    public static Map<String, String> JSONObjectToList(JSONObject jObject) throws JSONException {
        @SuppressWarnings("unchecked")
        Iterator<String> keys = jObject.keys();
        Map<String,String> Map = new HashMap<String, String>();
        List<String> keysList = new ArrayList<String>();
        while (keys.hasNext()) {
            keysList.add(keys.next());
        }
        Collections.sort(keysList);
        for (String key : keysList) {
            Map.put(key, jObject.get(key).toString());
        }
        return Map;
    }
}

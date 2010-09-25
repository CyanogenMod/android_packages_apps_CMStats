package com.cyanogenmod.stats;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class ReportingService extends Service {
    private static final String PREF_NAME = "CMStats";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void onCreate() {
        if (isFirstBoot()) {
            Log.d("CMStats", "First Boot, prompting user.");
        } else if (canReport() == true) {
            report();
        }
    }
    
    private boolean isFirstBoot() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        boolean firstboot = settings.getBoolean("firstboot", true);
        return firstboot;
    }
    
    private boolean canReport() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        boolean optin = settings.getBoolean("optin", false);
        return optin;
    }
    
    private void report() {
        String deviceId = Utilities.getUniqueID(getApplicationContext());
        String deviceName = Utilities.getDevice();
        String deviceVersion = Utilities.getModVersion();
        
        Log.d("CMStats", "Device ID: " + deviceId);
        Log.d("CMStats", "Device Name: " + deviceName);
        Log.d("CMStats", "Device Version: " + deviceVersion);
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://cyanogenmodstats.appspot.com/submit");
        try {
            List<NameValuePair> kv = new ArrayList<NameValuePair>(3);
            kv.add(new BasicNameValuePair("id", deviceId));
            kv.add(new BasicNameValuePair("type", deviceName));
            kv.add(new BasicNameValuePair("version", deviceVersion));
            httppost.setEntity(new UrlEncodedFormEntity(kv));
            httpclient.execute(httppost);
        } catch (Exception e) {
            Log.e("CMStats", "Got Exception", e);
        }
    }
}

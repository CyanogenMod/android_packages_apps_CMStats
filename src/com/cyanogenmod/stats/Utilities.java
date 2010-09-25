package com.cyanogenmod.stats;

import java.math.BigInteger;
import java.security.MessageDigest;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;

public class Utilities {
    public static String getUniqueID(Context ctx){
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        
        String device_id = digest(tm.getDeviceId());
        
        return device_id;
    }
    
    public static String getDevice() {
        return SystemProperties.get("ro.product.device");
    }
    
    public static String getModVersion() {
        return SystemProperties.get("ro.modversion");
    }
    
    public static String digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new BigInteger(1, md.digest(input.getBytes())).toString(16).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }
}

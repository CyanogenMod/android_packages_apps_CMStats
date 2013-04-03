package com.cyanogenmod.stats;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class Utilities {
    public static String getUniqueID(Context ctx){
        return digest(ctx.getPackageName() + Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID));
    }
    
    public static String getCarrier(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String carrier = tm.getNetworkOperatorName();
        if ("".equals(carrier)) {
            carrier = "Unknown";
        }
        return carrier;
    }

    public static String getCarrierId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierId = tm.getNetworkOperator();
        if ("".equals(carrierId)) {
            carrierId = "0";
        }
        return carrierId;
    }
    
    public static String getCountryCode(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso();
        if (countryCode.equals("")) {
            countryCode = "Unknown";
        }
        return countryCode;
    }

    public static String getDevice() {
        return SystemProperties.get("ro.cm.device");
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

package com.cyanogenmod.stats;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;

public class Utilities {
    public static String getUniqueID(Context ctx){
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        String device_id = digest(tm.getDeviceId());
        if (device_id == null) {
            try {
            WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            String wifiMac = new String (getWifiNetworkInterface(wm).getHardwareAddress());
            device_id = digest(wifiMac);
            } catch (SocketException e) {
                return null;
            }
        }

        return device_id;
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
    public static NetworkInterface getWifiNetworkInterface(WifiManager manager) {

        Enumeration<NetworkInterface> interfaces = null;
        try {
            //the WiFi network interface will be one of these.
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }

        //We'll use the WiFiManager's ConnectionInfo IP address and compare it with
        //the ips of the enumerated NetworkInterfaces to find the WiFi NetworkInterface.

        //Wifi manager gets a ConnectionInfo object that has the ipAdress as an int
        //It's endianness could be different as the one on java.net.InetAddress
        //maybe this varies from device to device, the android API has no documentation on this method.
        int wifiIP = manager.getConnectionInfo().getIpAddress();

        //so I keep the same IP number with the reverse endianness
        int reverseWifiIP = Integer.reverseBytes(wifiIP);

        while (interfaces.hasMoreElements()) {

            NetworkInterface iface = interfaces.nextElement();

            //since each interface could have many InetAddresses...
            Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress nextElement = inetAddresses.nextElement();
                int byteArrayToInt = byteArrayToInt(nextElement.getAddress(),0);

                //grab that IP in byte[] form and convert it to int, then compare it
                //to the IP given by the WifiManager's ConnectionInfo. We compare
                //in both endianness to make sure we get it.
                if (byteArrayToInt == wifiIP || byteArrayToInt == reverseWifiIP) {
                    return iface;
                }
            }
        }

        return null;
    }

    public static final int byteArrayToInt(byte[] arr, int offset) {
        if (arr == null || arr.length - offset < 4)
            return -1;

        int r0 = (arr[offset] & 0xFF) << 24;
        int r1 = (arr[offset + 1] & 0xFF) << 16;
        int r2 = (arr[offset + 2] & 0xFF) << 8;
        int r3 = arr[offset + 3] & 0xFF;
        return r0 + r1 + r2 + r3;
    }
}

package com.example.broadlinksdkdemo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtil {

    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;

    public NetworkUtil(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public void startScan() {
        mWifiManager.startScan();
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public String getWiFiSSID() {
        String CurInfoStr = mWifiInfo.toString() + "";
        String CurSsidStr = mWifiInfo.getSSID() + "";
        if (CurInfoStr.contains(CurSsidStr)) {
            return CurSsidStr;
        } else {
            return CurSsidStr.replaceAll("\"", "") + "";
        }
    }
}

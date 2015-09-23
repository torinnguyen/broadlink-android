package com.example.broadlinksdkdemo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Locale;

public class MyApplication extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();

        setLocaleEn(this);

        //Init Broadlink API
        initBroadlink();

        //Extract list of device from Broadlink app
        ArrayList<DeviceInfo> deviceInfoArrayList = BroadlinkAPI.getInstance().getBroadlinkAppDeviceInfoArray();
        if (deviceInfoArrayList != null && deviceInfoArrayList.size() > 0) {
            DeviceInfo device = deviceInfoArrayList.get(0);
            BroadlinkAPI.getInstance().addDevice(device);
        }
    }

    // Init Network Lib
    public void initBroadlink() {
        //Initialize Broadlink SDK
        JsonObject initJson = BroadlinkAPI.getInstance().broadlinkInitNetwork();

        JsonObject versionJson = BroadlinkAPI.getInstance().broadlinkVersion();
        if (versionJson.has("version") && versionJson.get("version") != null)
            Log.e(this.getClass().getSimpleName(), "BROADLINK VERSION: " + versionJson.get("version"));
        if (versionJson.has("update") && versionJson.get("update") != null)
            Log.e(this.getClass().getSimpleName(), "BROADLINK CHANGE LOG: " + versionJson.get("update"));
    }


    //------------------------------------------------------------------------------------------------
    // Helpers
    //------------------------------------------------------------------------------------------------

    public static Context getAppContext() {
        return mAppContext;
    }

    //Force using English language
    private void setLocaleEn(Context context) {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }


    //------------------------------------------------------------------------------------------------



}

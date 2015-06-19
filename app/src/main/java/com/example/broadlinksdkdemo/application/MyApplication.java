package com.example.broadlinksdkdemo.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.broadlink.blcloudac.BLCloudAC;
import com.example.broadlinksdkdemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;

import cn.com.broadlink.blnetwork.BLNetwork;

public class MyApplication extends Application {

    public static BLNetwork mBlNetwork;
    public static BLCloudAC blCloudAC;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mBlNetwork = BLNetwork.getInstanceBLNetwork(this);
            blCloudAC = BLCloudAC.getInstance();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "" + e);
        }
        setLocaleEn(this);
        initBroadlink();
    }

    // Init Network Lib
    public void initBroadlink() {
        //Initialize Broadlink SDK
        JsonObject initJson = broadlinkInitNetwork();

        JsonObject versionJson = broadlinkVersion();
        if (versionJson.has("version") && versionJson.get("version") != null)
            Log.e(this.getClass().getSimpleName(), "BROADLINK VERSION: " + versionJson.get("version"));
        if (versionJson.has("update") && versionJson.get("update") != null)
            Log.e(this.getClass().getSimpleName(), "BROADLINK CHANGE LOG: " + versionJson.get("update"));
    }


    //------------------------------------------------------------------------------------------------
    // Broadlink lowest level functions
    //------------------------------------------------------------------------------------------------

    private String broadlinkLicense() {
        return getString(R.string.broadlink_key);
    }

    private JsonObject broadlinkStandardParams(int api_id, String command){
        JsonObject initJsonObjectParams = new JsonObject();
        initJsonObjectParams.addProperty(BroadlinkConstants.API_ID, api_id);
        initJsonObjectParams.addProperty(BroadlinkConstants.COMMAND, command);
        initJsonObjectParams.addProperty(BroadlinkConstants.LICENSE, broadlinkLicense());
        return initJsonObjectParams;
    }

    private JsonObject broadlinkExecuteCommand(int api_id, String command) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(api_id, command);
        String responseString = mBlNetwork.requestDispatch(initJsonObjectParams.toString());
        JsonObject responseJsonObject = new JsonParser().parse(responseString).getAsJsonObject();
        Log.d(this.getClass().getSimpleName(), responseString);
        return responseJsonObject;
    }

    //------------------------------------------------------------------------------------------------
    // Broadlink highest level functions
    //------------------------------------------------------------------------------------------------

    //Initialize Broadlink SDK
    private JsonObject broadlinkInitNetwork() {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_NETWORK_INIT_ID, BroadlinkConstants.CMD_NETWORK_INIT);
    }

    //Show SDK version
    private JsonObject broadlinkVersion() {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_SDK_VERSION_ID, BroadlinkConstants.CMD_SDK_VERSION);
    }


    //------------------------------------------------------------------------------------------------
    // Helpers
    //------------------------------------------------------------------------------------------------

    //Force using English language
    private void setLocaleEn(Context context) {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

}

package com.example.broadlinksdkdemo;

import android.content.Context;
import android.util.Log;

import com.broadlink.blcloudac.BLCloudAC;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.com.broadlink.blnetwork.BLNetwork;

/**
 * Created by Torin on 19/6/15.
 */
public class BroadlinkAPI {

    private static BLNetwork mBlNetwork;
    private static BLCloudAC blCloudAC;

    //------------------------------------------------------------------------------------------------
    // Singleton
    //------------------------------------------------------------------------------------------------

    private static BroadlinkAPI instance = null;
    private static final String mLicenseString = "IDqOTOuVhMNQz8XWEc2wqmrjuYeTDGtBlMkm6AT1mmKKNLTrl45x4KzHGywehG/TzmSMIDnemvSlaNMSyYceBTJnNVQ10LKQ9sNzVIBX21r87yx+quE=";

    protected BroadlinkAPI() {
        Context context = MyApplication.getAppContext();
        try {
            mBlNetwork = BLNetwork.getInstanceBLNetwork(context);
            blCloudAC = BLCloudAC.getInstance();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "" + e);
        }
    }

    public static BroadlinkAPI getInstance() {
        if (instance == null) {
            instance = new BroadlinkAPI();
        }
        return instance;
    }

    //------------------------------------------------------------------------------------------------
    // To be removed
    //------------------------------------------------------------------------------------------------

    public BLNetwork getBlNetwork() {
        return mBlNetwork;
    }

    public BLCloudAC getBlCloudAC() {
        return blCloudAC;
    }


    //------------------------------------------------------------------------------------------------
    // Broadlink lowest level functions
    //------------------------------------------------------------------------------------------------

    private String broadlinkLicense() {
        return mLicenseString;
    }

    public JsonObject broadlinkStandardParams(int api_id, String command){
        JsonObject initJsonObjectParams = new JsonObject();
        initJsonObjectParams.addProperty(BroadlinkConstants.API_ID, api_id);
        initJsonObjectParams.addProperty(BroadlinkConstants.COMMAND, command);
        initJsonObjectParams.addProperty(BroadlinkConstants.LICENSE, broadlinkLicense());
        return initJsonObjectParams;
    }

    public JsonObject broadlinkExecuteCommand(JsonObject params) {
        String responseString = mBlNetwork.requestDispatch(params.toString());
        JsonObject responseJsonObject = new JsonParser().parse(responseString).getAsJsonObject();
        Log.d(this.getClass().getSimpleName(), responseString);
        return responseJsonObject;
    }

    public JsonObject broadlinkExecuteCommand(int api_id, String command) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(api_id, command);
        return broadlinkExecuteCommand(initJsonObjectParams);
    }


    //------------------------------------------------------------------------------------------------
    // Broadlink highest level functions
    //------------------------------------------------------------------------------------------------

    //Initialize Broadlink SDK
    public JsonObject broadlinkInitNetwork() {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_NETWORK_INIT_ID, BroadlinkConstants.CMD_NETWORK_INIT);
    }

    //Show SDK version
    public JsonObject broadlinkVersion() {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_SDK_VERSION_ID, BroadlinkConstants.CMD_SDK_VERSION);
    }

}

package com.example.broadlinksdkdemo;

import android.content.Context;
import android.util.Log;

import com.broadlink.blcloudac.BLCloudAC;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public JsonObject broadlinkStandardParams(int api_id, String command) {
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

    //Magic config
    public boolean easyConfig(String ssid, String password, boolean isVersion2) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_EASY_CONFIG_ID, BroadlinkConstants.CMD_EASY_CONFIG);
        initJsonObjectParams.addProperty("ssid", ssid);
        initJsonObjectParams.addProperty("password", password);
        initJsonObjectParams.addProperty("broadlinkv2", isVersion2 ? 1 : 0);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Retrieve a list of devices within same WiFi
    public ArrayList<DeviceInfo> getProbeList() {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_PROBE_LIST_ID, BroadlinkConstants.CMD_PROBE_LIST);
        JsonArray listJsonArray = out.get("list").getAsJsonArray();

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<DeviceInfo>>() {
        }.getType();
        ArrayList<DeviceInfo> deviceArrayList = (ArrayList<DeviceInfo>) gson.fromJson(listJsonArray, listType);
        if (deviceArrayList.size() <= 0)
            return null;

        return deviceArrayList;
    }

    //Add a device to Broadlink SDK before it can be controlled
    public JsonObject addDevice(DeviceInfo selectedDevice) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_DEVICE_ADD_ID, BroadlinkConstants.CMD_DEVICE_ADD);
        initJsonObjectParams.addProperty("mac", selectedDevice.getMac());
        initJsonObjectParams.addProperty("type", selectedDevice.getType());
        initJsonObjectParams.addProperty("name", selectedDevice.getName());
        initJsonObjectParams.addProperty("lock", selectedDevice.getLock());
        initJsonObjectParams.addProperty("password", selectedDevice.getPassword());
        initJsonObjectParams.addProperty("id", selectedDevice.getId());
        initJsonObjectParams.addProperty("subdevice", selectedDevice.getSubdevice());
        initJsonObjectParams.addProperty("key", selectedDevice.getKey());
        return broadlinkExecuteCommand(initJsonObjectParams);
    }

    //Turn on SP2 device
    //Note: device must be added to SDK first
    public boolean SP2On(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_SP2_CONTROL_ID, BroadlinkConstants.CMD_SP2_CONTROL);
        initJsonObjectParams.addProperty("status", 1);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Turn on SP2 device
    //Note: device must be added to SDK first
    public boolean SP2Off(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_SP2_CONTROL_ID, BroadlinkConstants.CMD_SP2_CONTROL);
        initJsonObjectParams.addProperty("status", 0);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Enable study mode on RM1
    public boolean RM1Study(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM1_STUDY_ID, BroadlinkConstants.CMD_RM1_STUDY);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Authenticate a RM1 device?
    public boolean RM1Auth(String mac, int password) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM1_AUTH_ID, BroadlinkConstants.CMD_RM1_AUTH);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("password", password);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Retrieve the studied code from RM1
    public String RM1Code(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM1_CODE_ID, BroadlinkConstants.CMD_RM1_CODE);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        if (0 != code)
            return null;

        String data = out.get("data").getAsString();
        Log.e("RM1StudyCode", data );
        return data;
    }

    //Send a code string from RM1
    public boolean RM1Send(String mac, String sendData) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM1_SEND_ID, BroadlinkConstants.CMD_RM1_SEND);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("data", sendData);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Enable study mode on RM2
    public boolean RM2Study(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM2_STUDY_ID, BroadlinkConstants.CMD_RM2_STUDY);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    //Retrieve the studied code from RM2
    public String RM2Code(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM2_CODE_ID, BroadlinkConstants.CMD_RM2_CODE);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        if (0 != code)
            return null;

        String data = out.get("data").getAsString();
        Log.e("RM2StudyCode", data );
        return data;
    }

    //Send a code string from RM2
    public boolean RM2Send(String mac, String sendData) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM2_SEND_ID, BroadlinkConstants.CMD_RM2_SEND);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("data", sendData);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

}

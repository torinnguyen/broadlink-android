package com.example.broadlinksdkdemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.broadlink.blcloudac.BLCloudAC;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            Log.e(this.getClass().getSimpleName(), "Check app permissions");
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

    /**
     * Construct a standard JsonObject with standard parameters for all Broadlink API
     */
    private JsonObject broadlinkStandardParams(int api_id, String command) {
        JsonObject initJsonObjectParams = new JsonObject();
        initJsonObjectParams.addProperty(BroadlinkConstants.API_ID, api_id);
        initJsonObjectParams.addProperty(BroadlinkConstants.COMMAND, command);
        initJsonObjectParams.addProperty(BroadlinkConstants.LICENSE, broadlinkLicense());
        return initJsonObjectParams;
    }

    /**
     * Execute a Broadlink API with the given parameters
     */
    private JsonObject broadlinkExecuteCommand(JsonObject params) {
        if (mBlNetwork == null) {
            Log.e(this.getClass().getSimpleName(), "mBlNetwork is uninitialized, check app permissions");
            return null;
        }
        String responseString = mBlNetwork.requestDispatch(params.toString());
        JsonObject responseJsonObject = new JsonParser().parse(responseString).getAsJsonObject();
        Log.d(this.getClass().getSimpleName(), responseString);
        return responseJsonObject;
    }

    /**
     * Execute a Broadlink API with additional MAC parameters
     */
    private JsonObject broadlinkExecuteCommand(int api_id, String command, String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(api_id, command);
        initJsonObjectParams.addProperty("mac", mac);
        return broadlinkExecuteCommand(initJsonObjectParams);
    }

    /**
     * Execute a Broadlink API with no extra parameters
     */
    private JsonObject broadlinkExecuteCommand(int api_id, String command) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(api_id, command);
        return broadlinkExecuteCommand(initJsonObjectParams);
    }


    //------------------------------------------------------------------------------------------------
    // Broadlink highest level functions
    //------------------------------------------------------------------------------------------------

    /**
     * Initialize Broadlink SDK
     */
    public JsonObject broadlinkInitNetwork() {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_NETWORK_INIT_ID, BroadlinkConstants.CMD_NETWORK_INIT);
    }

    /**
     * Show SDK version
     */
    public JsonObject broadlinkVersion() {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_SDK_VERSION_ID, BroadlinkConstants.CMD_SDK_VERSION);
    }

    /**
     * Magic config
     */
    public boolean easyConfig(String ssid, String password, boolean isVersion2) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_EASY_CONFIG_ID, BroadlinkConstants.CMD_EASY_CONFIG);
        initJsonObjectParams.addProperty("ssid", ssid);
        initJsonObjectParams.addProperty("password", password);
        initJsonObjectParams.addProperty("broadlinkv2", isVersion2 ? 1 : 0);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Retrieve a list of devices within same WiFi
     */
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

    /**
     * Add a device to Broadlink SDK before it can be controlled
     */
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

    /**
     * Update a device's name after being added to Broadlink SDK
     */
    public boolean updateDevice(String mac, String name, boolean isLocked) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_DEVICE_UPDATE_ID, BroadlinkConstants.CMD_DEVICE_UPDATE);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("name", name);
        initJsonObjectParams.addProperty("lock", isLocked ? 1 : 0);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Remove a device being added earlier to Broadlink SDK
     */
    public boolean deleteDevice(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_DEVICE_DELETE_ID, BroadlinkConstants.CMD_DEVICE_DELETE, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Retrieve a device's LAN IP.
     * This command can be used remotely (?)
     */
    public String getDeviceLanIp(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_DEVICE_LAN_IP_ID, BroadlinkConstants.CMD_DEVICE_LAN_IP, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        String msg = out.get(BroadlinkConstants.MSG).getAsString();
        String lanIp = out.get("lan_ip").getAsString();
        if (code == 0)
            return lanIp;
        return msg;
    }

    /**
     * Retrieve device's network status
     * NOT_INIT: uninitialized
     * LOCAL: LAN REMOTE:
     * Remote OFFLINE: Offline
     */
    public String getDeviceState(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_DEVICE_STATE_ID, BroadlinkConstants.CMD_DEVICE_STATE, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        String msg = out.get(BroadlinkConstants.MSG).getAsString();
        String status = out.get(BroadlinkConstants.STATUS).getAsString();
        if (code == 0)
            return status;
        return msg;
    }


    /**
     * Refresh SP2 info, returning its lock state & scheduled tasks
     */
    public JsonObject SP2Refresh(String mac) {
        return broadlinkExecuteCommand(BroadlinkConstants.CMD_SP2_REFRESH_ID, BroadlinkConstants.CMD_SP2_REFRESH, mac);
    }

    /**
     * Turn on SP2 device
     */
    public boolean SP2On(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_SP2_CONTROL_ID, BroadlinkConstants.CMD_SP2_CONTROL);
        initJsonObjectParams.addProperty("status", 1);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Turn on SP2 device
     */
    public boolean SP2Off(String mac) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_SP2_CONTROL_ID, BroadlinkConstants.CMD_SP2_CONTROL);
        initJsonObjectParams.addProperty("status", 0);
        initJsonObjectParams.addProperty("mac", mac);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Enable study mode on RM1
     */
    public boolean RM1StudyMode(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_RM1_STUDY_ID, BroadlinkConstants.CMD_RM1_STUDY, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Authenticate a RM1 device & retrieve current temperature
     */
    public float RM1Auth(String mac, int password) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM1_AUTH_ID, BroadlinkConstants.CMD_RM1_AUTH);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("password", password);
        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        if (code != 0)
            return BroadlinkConstants.INVALID_TEMPERATURE;
        return out.get(BroadlinkConstants.TEMPERATURE).getAsFloat();
    }

    /**
     * Retrieve the studied code from RM1
     */
    public String RM1Code(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_RM1_CODE_ID, BroadlinkConstants.CMD_RM1_CODE, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        if (0 != code)
            return null;

        String data = out.get("data").getAsString();
        Log.e("RM1StudyCode", data);
        return data;
    }

    /**
     * Send a code string from RM1
     */
    public boolean RM1Send(String mac, String sendData) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM1_SEND_ID, BroadlinkConstants.CMD_RM1_SEND);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("data", sendData);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Refresh RM2 info, mainly for current temperature reading
     */
    public float RM2Refresh(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_RM2_REFRESH_ID, BroadlinkConstants.CMD_RM2_REFRESH, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        if (code != 0)
            return BroadlinkConstants.INVALID_TEMPERATURE;
        return out.get(BroadlinkConstants.TEMPERATURE).getAsFloat();
    }

    /**
     * Enable study mode on RM2
     */
    public boolean RM2StudyMode(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_RM2_STUDY_ID, BroadlinkConstants.CMD_RM2_STUDY, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }

    /**
     * Retrieve the studied code from RM2
     */
    public String RM2Code(String mac) {
        JsonObject out = broadlinkExecuteCommand(BroadlinkConstants.CMD_RM2_CODE_ID, BroadlinkConstants.CMD_RM2_CODE, mac);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        if (0 != code)
            return null;

        String data = out.get("data").getAsString();
        Log.e("RM2StudyCode", data);
        return data;
    }

    /**
     * Send a code string from RM2
     */
    public boolean RM2Send(String mac, String sendData) {
        JsonObject initJsonObjectParams = broadlinkStandardParams(BroadlinkConstants.CMD_RM2_SEND_ID, BroadlinkConstants.CMD_RM2_SEND);
        initJsonObjectParams.addProperty("mac", mac);
        initJsonObjectParams.addProperty("data", sendData);

        JsonObject out = broadlinkExecuteCommand(initJsonObjectParams);
        int code = out.get(BroadlinkConstants.CODE).getAsInt();
        return code == 0;
    }


    //------------------------------------------------------------------------------------------------
    // Stealing data from Broadlink Android App
    //------------------------------------------------------------------------------------------------

    public ArrayList<DeviceInfo> getBroadlinkAppDeviceInfoArray() {
        ArrayList<JsonDevice> jsonDevicesArray = getBroadlinkAppJsonDevices();
        if (jsonDevicesArray == null || jsonDevicesArray.size() <= 0)
            return null;

        ArrayList<DeviceInfo> deviceInfoArrayList = new ArrayList<>();
        for (JsonDevice jsonDevice : jsonDevicesArray) {
            DeviceInfo deviceInfo = new DeviceInfo(jsonDevice);
            if (deviceInfo != null)
                deviceInfoArrayList.add(deviceInfo);
        }

        return deviceInfoArrayList;
    }

    public ArrayList<JsonDevice> getBroadlinkAppJsonDevices() {
        String broadlinkDevicesFile = getBroadlinkJsonDeviceFilePath();
        if (broadlinkDevicesFile == null)
            return null;

        String jsonDevices = readFromFile(broadlinkDevicesFile);
        if (jsonDevices == null)
            return null;
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<JsonDevice>>() {
        }.getType();
        ArrayList<JsonDevice> jsonDevicesArray = (ArrayList<JsonDevice>) gson.fromJson(jsonDevices, listType);
        return jsonDevicesArray;
    }

    private String getBroadlinkJsonDeviceFilePath() {
        String filePath = "broadlink";
        File directory = Environment.getExternalStorageDirectory();
        File folder = new File(directory + "/" + filePath);
        if (!folder.exists()) {
            return null;
        }

        //Check empty folder
        if (folder.list().length <= 0)
            return null;

        //Go inside first folder found (newremote)
        File newremoteDir = null;
        for (String fName : folder.list()) {
            newremoteDir = new File(directory + "/" + filePath + "/" + fName);
            if (!newremoteDir.isDirectory())
                return null;
            break;
        }
        if (newremoteDir == null)
            return null;

        //Go inside 'SharedData' folder
        File jsonDeviceFile = new File(newremoteDir + "/" + "SharedData" + "/" + "jsonDevice");
        if (!jsonDeviceFile.exists())
            return null;

        return jsonDeviceFile.getAbsolutePath();
    }

    private boolean checkBroadlinkFolderExists() {
        String filePath = getBroadlinkJsonDeviceFilePath();
        return filePath != null;
    }

    private String readFromFile(String absoluteFilePath) {

        String ret = "";

        try {
            File theFile = new File(absoluteFilePath);
            InputStream inputStream = new FileInputStream(theFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Can not read file: " + e.toString());
        }

        return ret;
    }

}

package com.example.broadlinksdkdemo.application;

import android.app.Application;
import android.util.Log;

import cn.com.broadlink.blnetwork.BLNetwork;

import com.broadlink.blcloudac.BLCloudAC;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MyApplication extends Application {
	public static BLNetwork mBlNetwork;
	

	public static String api_id = "api_id";
	public static String command = "command";
	public static String CODE = "code";
	public static String licenseValue = "IDqOTOuVhMNQz8XWEc2wqmrjuYeTDGtBlMkm6AT1mmKKNLTrl45x4KzHGywehG/TzmSMIDnemvSlaNMSyYceBTJnNVQ10LKQ9sNzVIBX21r87yx+quE=";
	public static BLCloudAC blCloudAC;
	@Override
	public void onCreate() {
		super.onCreate();
        try {
            mBlNetwork = BLNetwork.getInstanceBLNetwork(this);
            blCloudAC = BLCloudAC.getInstance();
        }
        catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "" + e);
        }
		init();

	}

	// Init Network Lib
	public void init() {
		JsonObject initJsonObjectIn = new JsonObject();
		JsonObject initJsonObjectOut = new JsonObject();
		String initOut;
		initJsonObjectIn.addProperty(api_id, 1);
		initJsonObjectIn.addProperty(command, "network_init");
		initJsonObjectIn.addProperty("license", licenseValue);
		String string = initJsonObjectIn.toString();
		initOut = mBlNetwork.requestDispatch(string);
		initJsonObjectOut = new JsonParser().parse(initOut).getAsJsonObject();

	}

}

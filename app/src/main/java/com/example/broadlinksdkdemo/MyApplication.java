package com.example.broadlinksdkdemo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        //Steal device data from Broadlink app
        String broadlinkDevicesFile = getBroadlinkJsonDeviceFilePath();
        if (broadlinkDevicesFile != null) {
            String jsonDevices = readFromFile(broadlinkDevicesFile);
            if (jsonDevices != null) {

            }
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

    private boolean checkBroadLinkFolderExists() {
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

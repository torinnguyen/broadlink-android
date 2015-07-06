package com.example.broadlinksdkdemo;

/**
 * Created by Torin on 19/6/15.
 */
public class BroadlinkConstants {

    //Broadlink standard input parameters
    public static final String API_ID = "api_id";
    public static final String COMMAND = "command";
    public static final String LICENSE = "license";

    //Broadlink standard output parameters
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String STATUS = "status";
    public static final String TEMPERATURE = "temperature";

    //Broadlink devices
    public static final String A1 = "A1";
    public static final String RM1 = "RM1";
    public static final String RM2 = "RM2";
    public static final String SP1 = "SP1";
    public static final String SP2 = "SP2";
    public static final String TC1 = "TC1";

    //Broadlink command strings
    public static final String CMD_NETWORK_INIT = "network_init";
    public static final String CMD_SDK_VERSION = "SDK_version";
    public static final String CMD_PROBE_LIST = "probe_list";
    public static final String CMD_DEVICE_ADD = "device_add";
    public static final String CMD_DEVICE_UPDATE = "device_update";
    public static final String CMD_DEVICE_DELETE = "device_delete";
    public static final String CMD_DEVICE_LAN_IP = "device_lan_ip";
    public static final String CMD_DEVICE_STATE = "device_state";
    public static final String CMD_SP2_REFRESH = "sp2_refresh";
    public static final String CMD_SP2_CONTROL = "sp2_control";
    public static final String CMD_SP2_CURRENT_POWER = "sp2_current_power";
    public static final String CMD_RM1_AUTH = "rm1_auth";
    public static final String CMD_RM1_STUDY = "rm1_study";
    public static final String CMD_RM1_CODE = "rm1_code";
    public static final String CMD_RM1_SEND = "rm1_send";
    public static final String CMD_RM2_REFRESH = "rm2_refresh";
    public static final String CMD_RM2_STUDY = "rm2_study";
    public static final String CMD_RM2_CODE = "rm2_code";
    public static final String CMD_RM2_SEND = "rm2_send";
    public static final String CMD_EASY_CONFIG = "easyconfig";

    //Broadlink command api_id
    public static final int CMD_NETWORK_INIT_ID = 1;
    public static final int CMD_SDK_VERSION_ID = 2;
    public static final int CMD_PROBE_LIST_ID = 11;
    public static final int CMD_DEVICE_ADD_ID = 12;
    public static final int CMD_DEVICE_UPDATE_ID = 13;      //Update device name, after being added
    public static final int CMD_DEVICE_DELETE_ID = 14;
    public static final int CMD_DEVICE_LAN_IP_ID = 15;
    public static final int CMD_DEVICE_STATE_ID = 16;       //wifi or cloud state
    public static final int CMD_SP2_REFRESH_ID = 71;
    public static final int CMD_SP2_CONTROL_ID = 72;
    public static final int CMD_SP2_CURRENT_POWER_ID = 74;
    public static final int CMD_RM1_AUTH_ID = 101;
    public static final int CMD_RM1_STUDY_ID = 102;
    public static final int CMD_RM1_CODE_ID = 103;
    public static final int CMD_RM1_SEND_ID = 104;
    public static final int CMD_RM2_REFRESH_ID = 131;
    public static final int CMD_RM2_STUDY_ID = 132;
    public static final int CMD_RM2_CODE_ID = 133;
    public static final int CMD_RM2_SEND_ID = 134;
    public static final int CMD_EASY_CONFIG_ID = 10000;
}

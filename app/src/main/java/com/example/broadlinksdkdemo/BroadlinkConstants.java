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
    public static final float INVALID_TEMPERATURE = -999;

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

    //Broadlink error codes
    public static String getErrorMessage(int code) {
        if (code == -1)
            return "设备所在网络已改变或网络密码已经修改";
        if (code == -2)
            return "设备已在其他地方登录,如需继续控制,请重新登录(针对rm1/sp1)";
        if (code == -3)
            return "设备不在线";
        if (code == -4)
            return "不支持的操作";
        if (code == -5)
            return "空间满";
        if (code == -6)
            return "数据结构异常";
        if (code == -7)
            return "设备已经复位,需进入局域网重新认证。(针对sp1/rm1以外的设备)";
        if (code == -100)
            return "超时";
        if (code == -101)
            return "网络线程找不到该设备";
        if (code == -102)
            return "内存不足";
        if (code == -103)
            return "设备未初始化";
        if (code == -104)
            return "网络线程已暂停";
        if (code == -105)
            return "返回消息类型错误";
        if (code == -106)
            return "操作过于频繁";
        if (code == -107)
            return "服务器已拒绝该license操作,请联系客服处理";
        if (code == -108)
            return "设备不在局域网中";
        if (code == -10000)
            return "未知错误";

        return "Unknown error code: " + code;
    }
}

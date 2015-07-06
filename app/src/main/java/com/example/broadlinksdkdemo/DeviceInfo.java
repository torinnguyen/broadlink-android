package com.example.broadlinksdkdemo;

import android.text.TextUtils;

class DeviceInfo {

	/* From Broadlink SDK
    {
		code = 0;
		list =     (
					{
				id = 1;
				key = d6ccb6520d402d4243b3a3317a261a21;
				lock = 0;
				mac = "b4:43:0d:10:56:3a";
				name = "\U667a\U80fd\U9065\U63a7";
				password = 347606638;
				subdevice = 0;
				type = RM2;
			}
		);
		msg = "Execute success";
	}
	*/

	/* From Broadlink App
	  {
		"id": 1,
		"terminalId": 1,
		"mac": "b4430d10563a",
		"subDevice": 0,
		"longitude": 0,
		"latitude": 0,
		"type": 10002,
		"new": 0,
		"password": 1658915694,
		"lock": 0,
		"publicKey": [
		  -76,
		  42,
		  -109,
		  36,
		  -21,
		  -99,
		  9,
		  20,
		  33,
		  17,
		  -128,
		  3,
		  88,
		  -124,
		  -10,
		  114
		],
		"name": "Thai An"
	  }
	 */

    public String mac;
    public String type;
    public String name;
    public String key;
    public int lock;
    public int password;
    public int id;
    public int subdevice;

    //Use this constructor only for restoring from Broadlink App json
    DeviceInfo(JsonDevice jsonDevice) {
        this.id = jsonDevice.id;
        this.password = jsonDevice.password;
        this.lock = jsonDevice.lock;
        this.subdevice = jsonDevice.subDevice;
        this.name = jsonDevice.name;

        //I'm just guessing
        if (jsonDevice.type == 10001)
            this.type = BroadlinkConstants.RM1;
        if (jsonDevice.type == 10002)
            this.type = BroadlinkConstants.RM2;

        //Public key
        String key = "";
        for (Integer integer : jsonDevice.publicKey) {
            String hexString = Integer.toHexString(integer);
            if (hexString.length() == 1)
                hexString = "0" + hexString;
            else if (hexString.length() > 2)
                hexString = hexString.substring(hexString.length() - 2);
            key += hexString;
        }
        this.key = key;

        //MAC
        String[] macParts = splitByNumber(jsonDevice.mac, 2);
        this.mac = TextUtils.join(":", macParts);
    }

    public String getMac() {
        return mac;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getLock() {
        return lock;
    }

    public int getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public int getSubdevice() {
        return subdevice;
    }

    public String getKey() {
        return key;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubdevice(int subdevice) {
        this.subdevice = subdevice;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //-----------------------

    private String[] splitByNumber(String s, int size) {
        if (s == null || size <= 0)
            return null;
        int chunks = s.length() / size + ((s.length() % size > 0) ? 1 : 0);
        String[] arr = new String[chunks];
        for (int i = 0, j = 0, l = s.length(); i < l; i += size, j++)
            arr[j] = s.substring(i, Math.min(l, i + size));
        return arr;
    }

}
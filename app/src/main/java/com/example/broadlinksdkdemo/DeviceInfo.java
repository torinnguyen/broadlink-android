package com.example.broadlinksdkdemo;

class DeviceInfo {

	/*
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

	public String mac;
	public String type;
	public String name;
	public String key;
	public int lock;
	public int password;
	public int id;
	public int subdevice;

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

}
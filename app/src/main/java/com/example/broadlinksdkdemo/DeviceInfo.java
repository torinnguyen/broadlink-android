package com.example.broadlinksdkdemo;

class DeviceInfo {
	public DeviceInfo() {

	}

	String mac;
	String type;
	String name;
	int lock;
	int password;
	int id;
	int subdevice;
	String key;

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
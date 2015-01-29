package com.example.broadlinksdkdemo;

public class A1Task {
	public A1Task() {

	}

	private String mac;
	private int time_enable;
	private int task_enable;
	private int index;
	private String start_time;
	private String end_time;
	private int repeat;
	private int sensor_type;
	private int sensor_trigger;
	private float sensor_value;
	private String task_name;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getTime_enable() {
		return time_enable;
	}

	public void setTime_enable(int time_enable) {
		this.time_enable = time_enable;
	}

	public int getTask_enable() {
		return task_enable;
	}

	public void setTask_enable(int task_enable) {
		this.task_enable = task_enable;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getSensor_type() {
		return sensor_type;
	}

	public void setSensor_type(int sensor_type) {
		this.sensor_type = sensor_type;
	}

	public int getSensor_trigger() {
		return sensor_trigger;
	}

	public void setSensor_trigger(int sensor_trigger) {
		this.sensor_trigger = sensor_trigger;
	}

	public float getSensor_value() {
		return sensor_value;
	}

	public void setSensor_value(float sensor_value) {
		this.sensor_value = sensor_value;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

}

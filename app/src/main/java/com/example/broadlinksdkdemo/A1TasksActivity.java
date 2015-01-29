package com.example.broadlinksdkdemo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.broadlinksdkdemo.application.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class A1TasksActivity extends Activity {

	private ListView mLvA1TaskListView;
	private Button mBtnAddTask;
	private Button mBtnDeleteTask;
	private String mMac;
	private List<A1Task> mA1Tasks;

	private List<A1Task> mResultA1Tasks;
	private A1TaskAdapter mA1TaskAdapter;

	private int mDeleteTaskID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a1_tasks);
		mMac = getIntent().getStringExtra("A1_Mac");
		findView();
		initView();
		setListener();

		mA1Tasks = new ArrayList<A1Task>();
		mResultA1Tasks = new ArrayList<A1Task>();

	}

	private void findView() {
		mLvA1TaskListView = (ListView) findViewById(R.id.lv_a1_task_list);
		mBtnAddTask = (Button) findViewById(R.id.btn_add_task);
		mBtnDeleteTask = (Button) findViewById(R.id.btn_del_task);
	}

	private void initView() {
		A1TaskList();
	}

	private void setListener() {
		mBtnAddTask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				A1AddTask();
			}
		});
		mBtnDeleteTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				A1TaskDelete();
			}
		});
	}

	private void A1TaskList() {

		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(MyApplication.api_id, 162);
		in.addProperty(MyApplication.command, "a1_task_list");
		in.addProperty("mac", mMac);
		Log.e("mac", mMac);
		String inString = in.toString();
		outString = MyApplication.mBlNetwork.requestDispatch(inString);
		Log.e("A1Tasks", "A1Tasks\n" + outString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(MyApplication.CODE).getAsInt();

		if (0 == code) {
			Toast.makeText(A1TasksActivity.this, R.string.toast_a1_task_list_success, Toast.LENGTH_SHORT).show();
			Gson gson = new Gson();
			JsonArray listJsonArray = new JsonArray();
			listJsonArray = out.get("list").getAsJsonArray();
			Type listType = new TypeToken<ArrayList<A1Task>>() {
			}.getType();
			mA1Tasks = (ArrayList<A1Task>) gson.fromJson(listJsonArray, listType);
			mA1TaskAdapter = new A1TaskAdapter(mA1Tasks, A1TasksActivity.this);
			mLvA1TaskListView.setAdapter(mA1TaskAdapter);
			mA1TaskAdapter.notifyDataSetChanged();

		} else {
			Toast.makeText(A1TasksActivity.this, R.string.toast_a1_task_list_fail, Toast.LENGTH_SHORT).show();
		}

	}

	private void A1AddTask() {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(MyApplication.api_id, 163);
		in.addProperty(MyApplication.command, "a1_add_task");
		in.addProperty("mac", mMac);
		in.addProperty("task_name", "关闭空调");
		in.addProperty("time_enable", 1);
		in.addProperty("task_enable", 1);
		in.addProperty("start_time", "2014-06-06 10:30:00");
		in.addProperty("end_time", "2014-06-06 18:30:00");
		in.addProperty("repeat", 7);

		in.addProperty("sensor_type", 0);
		in.addProperty("sensor_trigger", 1);
		in.addProperty("sensor_value", 20.0);
		in.addProperty("device_mac", "aa:bb:cc:dd:ee:ff");
		in.addProperty("device_id", 25);

		in.addProperty("device_key", "097628343fe99e23765c1513accf8b02");
		in.addProperty("device_type", "RM2");
		in.addProperty("task_data", "2600e6006f390e2a0e290d100d100e0f0e290e0f0e0f0e2a0e290d100e2a0e0e0f0e0e2a0d2a0e0f0e2a0e");
		in.addProperty("status", 0);

		Log.e("mac", mMac);
		String inString = in.toString();
		outString = MyApplication.mBlNetwork.requestDispatch(inString);
		Log.e("A1Tasks", "A1Tasks\n" + outString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(MyApplication.CODE).getAsInt();

		if (0 == code) {
			Toast.makeText(A1TasksActivity.this, R.string.toast_a1_task_add_success, Toast.LENGTH_SHORT).show();
			Gson gson = new Gson();
			JsonArray listJsonArray = new JsonArray();
			listJsonArray = out.get("list").getAsJsonArray();
			Type listType = new TypeToken<ArrayList<A1Task>>() {
			}.getType();
			mResultA1Tasks = (ArrayList<A1Task>) gson.fromJson(listJsonArray, listType);
			for (int i = 0; i < mResultA1Tasks.size(); i++) {
				if (mResultA1Tasks.get(i).getMac().equals(mMac)) {
					mDeleteTaskID = mResultA1Tasks.get(i).getIndex();
				}
			}
			A1TaskList();
		} else {
			Toast.makeText(A1TasksActivity.this, R.string.toast_a1_task_add_fail, Toast.LENGTH_SHORT).show();
		}

	}

	private void A1TaskDelete() {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(MyApplication.api_id, 164);
		in.addProperty(MyApplication.command, "a1_del_task");
		in.addProperty("mac", mMac);
		in.addProperty("index", mDeleteTaskID);

		String inString = in.toString();
		outString = MyApplication.mBlNetwork.requestDispatch(inString);

		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(MyApplication.CODE).getAsInt();

		if (0 == code) {
			Toast.makeText(A1TasksActivity.this, R.string.toast_a1_task_del_success, Toast.LENGTH_SHORT).show();
			A1TaskList();
		} else {
			Toast.makeText(A1TasksActivity.this, R.string.toast_a1_task_del_fail, Toast.LENGTH_SHORT).show();
		}
	}
}

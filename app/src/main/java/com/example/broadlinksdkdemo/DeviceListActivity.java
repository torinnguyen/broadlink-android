package com.example.broadlinksdkdemo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.broadlinksdkdemo.application.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class DeviceListActivity extends Activity {
	private ListView mLvDeviceList;

	private Button mBtnInitButton, mBtnProbeListButton, mBtnSwitchOn, mBtnSwitchOff, mBtnEasyConfigV2, mBtnEasyConfigV1;
	private EditText mEtWifiSSIDEditText, mEtWifiPasswordEditText;
	private String CODE = "code";
	private String MSG = "msg";

	private List<DeviceInfo> mDeviceList;
	private DeviceListAdapter mDeviceListAdapter;
	private Context context = DeviceListActivity.this;

	private Button mBtnProbe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		findView();
		setListener();

		probeList();

	}

	private void findView() {
		mLvDeviceList = (ListView) findViewById(R.id.lv_device_list);
		mBtnProbe = (Button) findViewById(R.id.btn_probe);
	}

	private void setListener() {
		mLvDeviceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mDeviceList.get(position).type.equals(DeviceType.A1)) {
					Intent intent = new Intent(DeviceListActivity.this, A1InfoActivity.class);
					intent.putExtra("A1_Mac", mDeviceList.get(position).mac);
					addDevice(position);
					startActivity(intent);
				} else {
					Toast.makeText(context, R.string.toast_demo_is_under_vevision, Toast.LENGTH_SHORT).show();
				}
			}
		});
		mBtnProbe.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						probeList();

					}
				}).start();

			}
		});
	}

	// Probe List
	public void probeList() {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		JsonArray listJsonArray = new JsonArray();
		String probeOut;
		in.addProperty(MyApplication.api_id, 11);
		in.addProperty(MyApplication.command, "probe_list");
		String string = in.toString();
		probeOut = MyApplication.mBlNetwork.requestDispatch(string);

		out = new JsonParser().parse(probeOut).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		String msg = out.get(MSG).getAsString();
		Log.e("result", "probeOut " + probeOut + "code  " + code + "msg  " + msg);
		listJsonArray = out.get("list").getAsJsonArray();

		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<DeviceInfo>>() {
		}.getType();
		mDeviceList = (ArrayList<DeviceInfo>) gson.fromJson(listJsonArray, listType);

		mDeviceListAdapter = new DeviceListAdapter(mDeviceList, DeviceListActivity.this);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mLvDeviceList.setAdapter(mDeviceListAdapter);
				mDeviceListAdapter.notifyDataSetChanged();
			}
		});

	}

	public void addDevice(int position) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		in.addProperty(MyApplication.api_id, 12);
		in.addProperty(MyApplication.command, "device_add");
		in.addProperty("mac", mDeviceList.get(position).getMac());
		in.addProperty("type", mDeviceList.get(position).getType());
		in.addProperty("name", mDeviceList.get(position).getName());
		in.addProperty("lock", mDeviceList.get(position).getLock());
		in.addProperty("password", mDeviceList.get(position).getPassword());
		in.addProperty("id", mDeviceList.get(position).getId());
		in.addProperty("subdevice", mDeviceList.get(position).getSubdevice());
		in.addProperty("key", mDeviceList.get(position).getKey());
		String string = in.toString();
		String outString;
		outString = MyApplication.mBlNetwork.requestDispatch(string);
		out = new JsonParser().parse(outString).getAsJsonObject();

	}
}

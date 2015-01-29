package com.example.broadlinksdkdemo;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.broadlink.blnetwork.BLNetwork;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {

	private BLNetwork mBlNetwork;

	private String api_id = "api_id";
	private String command = "command";
	private String licenseValue = "IDqOTOuVhMNQz8XWEc2wqmrjuYeTDGtBlMkm6AT1mmKKNLTrl45x4KzHGywehG/TzmSMIDnemvSlaNMSyYceBTJnNVQ10LKQ9sNzVIBX21r87yx+quE=";
	private Button mBtnProbeListButton, mBtnSwitchOn, mBtnSwitchOff, mBtnEasyConfigV2, mBtnEasyConfigV1;
	private EditText mEtWifiSSIDEditText, mEtWifiPasswordEditText;
	private String CODE = "code";
	private String MSG = "msg";
	private Context context = MainActivity.this;
	private String selectDeviceMac;
	private DeviceInfo selectedDevice;
	private ArrayList<DeviceInfo> deviceArrayList;
	// RM2 Control
	private Button mBtnRM2Study, mBtnRM2Code, mBtnRM2Send;
	private TextView mTvRM2CodeResult;
	private String mRM2SendData;

	// RM1 Control
	private Button mBtnRM1Auth, mBtnRM1Study, mBtnRM1Code, mBtnRM1Send;
	private TextView mTvRM1CodeResult;
	private String mRM1SendData;
	private int mCurrentRM1Password;
	// A1 Control
	private Button mBtnA1Control;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBlNetwork = BLNetwork.getInstanceBLNetwork(MainActivity.this);
		findView();

		setListener();

	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();
	}

	public void findView() {

		mBtnProbeListButton = (Button) findViewById(R.id.btn_probe_list);
		mBtnSwitchOn = (Button) findViewById(R.id.btn_switch_on);
		mBtnSwitchOff = (Button) findViewById(R.id.btn_switch_off);
		mEtWifiSSIDEditText = (EditText) findViewById(R.id.et_wifi_ssid);
		mEtWifiPasswordEditText = (EditText) findViewById(R.id.et_wifi_password);
		mBtnEasyConfigV2 = (Button) findViewById(R.id.btn_smartConfig_v2);
		mBtnEasyConfigV1 = (Button) findViewById(R.id.btn_smartConfig_v1);

		mBtnRM2Study = (Button) findViewById(R.id.btn_rm2_study);
		mBtnRM2Code = (Button) findViewById(R.id.btn_rm2_code);
		mBtnRM2Send = (Button) findViewById(R.id.btn_rm2_send);
		mTvRM2CodeResult = (TextView) findViewById(R.id.tv_rm2_code_result);

		mBtnRM1Auth = (Button) findViewById(R.id.btn_rm1_auth);
		mBtnRM1Study = (Button) findViewById(R.id.btn_rm1_study);
		mBtnRM1Code = (Button) findViewById(R.id.btn_rm1_code);
		mBtnRM1Send = (Button) findViewById(R.id.btn_rm1_send);
		mTvRM1CodeResult = (TextView) findViewById(R.id.tv_rm1_code_result);

		mBtnA1Control = (Button) findViewById(R.id.btn_a1_control);

	}

	// Probe List
	public void probeList() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int selectedPosition = ((AlertDialog) arg0).getListView().getCheckedItemPosition();
				selectDeviceMac = deviceArrayList.get(selectedPosition).getMac();
				selectedDevice = deviceArrayList.get(selectedPosition);
				Log.e("selectDeviceMac", selectDeviceMac);
				addDevice();
				deviceArrayList.clear();

			}
		};
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		JsonArray listJsonArray = new JsonArray();
		String probeOut;
		in.addProperty(api_id, 11);
		in.addProperty(command, "probe_list");
		String string = in.toString();
		probeOut = mBlNetwork.requestDispatch(string);
		out = new JsonParser().parse(probeOut).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		String msg = out.get(MSG).getAsString();
		listJsonArray = out.get("list").getAsJsonArray();

		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<DeviceInfo>>() {
		}.getType();
		deviceArrayList = (ArrayList<DeviceInfo>) gson.fromJson(listJsonArray, listType);
		DeviceInfo device;
		String[] deviceNamesAndMac = new String[deviceArrayList.size()];
		if (deviceArrayList.size() > 0) {
			for (int i = 0; i < deviceArrayList.size(); i++) {
				device = deviceArrayList.get(i);
				deviceNamesAndMac[i] = device.getName() + "\n:" + device.getMac();
			}
			new AlertDialog.Builder(context).setSingleChoiceItems(deviceNamesAndMac, 0, null)
					.setPositiveButton(R.string.alert_button_confirm, listener).show();
		} else {
			Toast.makeText(MainActivity.this, R.string.toast_probe_no_device, Toast.LENGTH_SHORT).show();
		}

	}

	public void addDevice() {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		in.addProperty(api_id, 12);
		in.addProperty(command, "device_add");
		in.addProperty("mac", selectedDevice.getMac());
		in.addProperty("type", selectedDevice.getType());
		in.addProperty("name", selectedDevice.getName());
		in.addProperty("lock", selectedDevice.getLock());
		in.addProperty("password", selectedDevice.getPassword());
		mCurrentRM1Password = selectedDevice.getPassword();
		in.addProperty("id", selectedDevice.getId());
		in.addProperty("subdevice", selectedDevice.getSubdevice());
		in.addProperty("key", selectedDevice.getKey());
		String string = in.toString();
		String outString;
		outString = mBlNetwork.requestDispatch(string);
		out = new JsonParser().parse(outString).getAsJsonObject();

	}

	public void SP2On(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String sp2outString;
		in.addProperty(api_id, 72);
		in.addProperty(command, "sp2_control");
		in.addProperty("mac", mac);
		in.addProperty("status", 1);
		String string = in.toString();
		sp2outString = mBlNetwork.requestDispatch(string);
		out = new JsonParser().parse(sp2outString).getAsJsonObject();

	}

	public void SP2Off(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String sp2outString;
		in.addProperty(api_id, 72);
		in.addProperty(command, "sp2_control");
		in.addProperty("mac", mac);
		in.addProperty("status", 0);
		String string = in.toString();
		sp2outString = mBlNetwork.requestDispatch(string);
		out = new JsonParser().parse(sp2outString).getAsJsonObject();
	}

	private void RM2Study(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 132);
		in.addProperty(command, "rm2_study");
		in.addProperty("mac", mac);
		String inString = in.toString();
		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		if (0 == code) {
			Toast.makeText(context, R.string.toast_rm2_study_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, R.string.toast_rm2_study_fail, Toast.LENGTH_SHORT).show();
		}

	}

	private void RM2Code(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 133);
		in.addProperty(command, "rm2_code");
		in.addProperty("mac", mac);
		String inString = in.toString();

		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		if (0 == code) {
			mRM2SendData = out.get("data").getAsString();
			Log.e("RM2StudyCode", mRM2SendData);
			mTvRM2CodeResult.setText(mRM2SendData);
			Toast.makeText(context, R.string.toast_rm2_code_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, R.string.toast_rm2_code_fail, Toast.LENGTH_SHORT).show();
		}
	}

	private void RM2Send(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 134);
		in.addProperty(command, "rm2_send");
		in.addProperty("mac", mac);
		in.addProperty("data", mRM2SendData);
		String inString = in.toString();

		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();

	}

	private void RM1Auth(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 101);
		in.addProperty(command, "rm1_auth");
		in.addProperty("mac", mac);
		in.addProperty("password", mCurrentRM1Password);
		String inString = in.toString();
		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		if (0 == code) {
			Toast.makeText(context, R.string.toast_rm1_auth_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, R.string.toast_rm1_auth_fail, Toast.LENGTH_SHORT).show();
		}

	}

	private void RM1Study(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 102);
		in.addProperty(command, "rm1_study");
		in.addProperty("mac", mac);
		String inString = in.toString();
		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		if (0 == code) {
			Toast.makeText(context, R.string.toast_rm1_study_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, R.string.toast_rm1_study_fail, Toast.LENGTH_SHORT).show();
		}

	}

	private void RM1Code(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 103);
		in.addProperty(command, "rm1_code");
		in.addProperty("mac", mac);
		String inString = in.toString();

		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		if (0 == code) {
			mRM1SendData = out.get("data").getAsString();
			mTvRM1CodeResult.setText(mRM1SendData);
			Toast.makeText(context, R.string.toast_rm1_code_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, R.string.toast_rm1_code_fail, Toast.LENGTH_SHORT).show();
		}
	}

	private void RM1Send(String mac) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 104);
		in.addProperty(command, "rm1_send");
		in.addProperty("mac", mac);
		in.addProperty("data", mRM1SendData);
		String inString = in.toString();

		outString = mBlNetwork.requestDispatch(inString);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();

	}

	public void easyConfig(String ssid, String password, int easyConfigVersion) {
		JsonObject in = new JsonObject();
		JsonObject out = new JsonObject();
		String outString;
		in.addProperty(api_id, 10000);
		in.addProperty(command, "easyconfig");
		in.addProperty("ssid", ssid);
		in.addProperty("password", password);
		in.addProperty("broadlinkv2", easyConfigVersion);
		String string = in.toString();
		outString = mBlNetwork.requestDispatch(string);
		out = new JsonParser().parse(outString).getAsJsonObject();
		int code = out.get(CODE).getAsInt();
		String msg = out.get(MSG).getAsString();
		if (0 == code) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, R.string.toast_probe_to_show_new_device, Toast.LENGTH_SHORT).show();
				}
			});

		} else {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
				}
			});

		}

	}

	public void initView() {
		NetworkUtil networkUtil = new NetworkUtil(MainActivity.this);
		networkUtil.startScan();
		String ssid = networkUtil.getWiFiSSID();
		mEtWifiSSIDEditText.setText(ssid);

	}

	public void setListener() {
		mBtnProbeListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				probeList();
			}
		});
		mBtnSwitchOn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("SP2ON_Mac", selectDeviceMac);
				SP2On(selectDeviceMac);

			}
		});
		mBtnSwitchOff.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("SP2Off_Mac", selectDeviceMac);
				SP2Off(selectDeviceMac);
			}
		});
		mBtnEasyConfigV2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						String ssid = mEtWifiSSIDEditText.getText().toString();
						String password = mEtWifiPasswordEditText.getText().toString();
						easyConfig(ssid, password, 1);
					}
				}).start();

			}
		});
		mBtnEasyConfigV1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						String ssid = mEtWifiSSIDEditText.getText().toString();
						String password = mEtWifiPasswordEditText.getText().toString();
						easyConfig(ssid, password, 0);
					}
				}).start();

			}
		});

		mBtnRM2Study.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RM2Study(selectDeviceMac);
			}
		});
		mBtnRM2Code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTvRM2CodeResult.setText("");
				RM2Code(selectDeviceMac);
			}
		});
		mBtnRM2Send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RM2Send(selectDeviceMac);
			}
		});

		mBtnRM1Auth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RM1Auth(selectDeviceMac);
			}
		});
		mBtnRM1Study.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RM1Study(selectDeviceMac);
			}
		});
		mBtnRM1Code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTvRM1CodeResult.setText("");
				RM1Code(selectDeviceMac);
			}
		});
		mBtnRM1Send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RM1Send(selectDeviceMac);
			}
		});

		mBtnA1Control.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, DeviceListActivity.class);
				startActivity(intent);
			}
		});

	}

}

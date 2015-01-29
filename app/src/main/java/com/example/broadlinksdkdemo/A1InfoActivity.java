package com.example.broadlinksdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.broadlinksdkdemo.application.MyApplication;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class A1InfoActivity extends Activity {

	private TextView mTvTemperature, mTvHumidity;
	private String mMac;
	private Button mBtnShowTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a1_info);
		findView();
		setListener();
		mMac = getIntent().getStringExtra("A1_Mac");

	}

	@Override
	protected void onResume() {
		super.onResume();
		A1Refresh();
	}

	private void findView() {
		mTvTemperature = (TextView) findViewById(R.id.tv_a1_temperature_value);
		mTvHumidity = (TextView) findViewById(R.id.tv_a1_humidity_value);
		mBtnShowTask = (Button) findViewById(R.id.btn_show_task);

	}

	private void initView() {

	}

	private void setListener() {
		mBtnShowTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(A1InfoActivity.this, A1TasksActivity.class);
				intent.putExtra("A1_Mac", mMac);
				startActivity(intent);
			}
		});
	}

	private void A1Refresh() {
		JsonObject initJsonObjectIn = new JsonObject();
		JsonObject initJsonObjectOut = new JsonObject();
		String initOut;
		initJsonObjectIn.addProperty(MyApplication.api_id, 161);
		initJsonObjectIn.addProperty(MyApplication.command, "a1_refresh");
		initJsonObjectIn.addProperty("mac", mMac);

		String string = initJsonObjectIn.toString();
		initOut = MyApplication.mBlNetwork.requestDispatch(string);

		initJsonObjectOut = new JsonParser().parse(initOut).getAsJsonObject();

		int code = initJsonObjectOut.get(MyApplication.CODE).getAsInt();
		if (0 == code) {
			Toast.makeText(this, getString(R.string.toast_a1_refresh_success), Toast.LENGTH_SHORT).show();
			mTvTemperature.setText(initJsonObjectOut.get("temperature").getAsFloat() + "");
			mTvHumidity.setText(initJsonObjectOut.get("humidity").getAsFloat() + "");
		} else {
			Toast.makeText(this, getString(R.string.toast_a1_refresh_fail), Toast.LENGTH_SHORT).show();
		}

	}

}

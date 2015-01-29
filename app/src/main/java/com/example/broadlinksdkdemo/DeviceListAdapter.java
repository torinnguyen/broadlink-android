package com.example.broadlinksdkdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceListAdapter extends BaseAdapter {

	private List<DeviceInfo> mDeviceList;
	private LayoutInflater mInflater;

	public DeviceListAdapter(List<DeviceInfo> deviceList, Context context) {
		mDeviceList = deviceList;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDeviceList.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.device_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.mac = (TextView) convertView.findViewById(R.id.tv_mac);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(mDeviceList.get(position).name);
		viewHolder.type.setText(mDeviceList.get(position).type);
		viewHolder.mac.setText(mDeviceList.get(position).mac);

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView mac;
		TextView type;
	}
}

package com.example.broadlinksdkdemo;

import java.util.List;

import com.example.broadlinksdkdemo.DeviceListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class A1TaskAdapter extends BaseAdapter {

	private List<A1Task> mA1Tasks;
	private LayoutInflater mInflater;

	public A1TaskAdapter(List<A1Task> mA1Tasks, Context context) {
		this.mA1Tasks = mA1Tasks;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mA1Tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.a1_task_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.startTime = (TextView) convertView.findViewById(R.id.tv_start_time);
			viewHolder.endTime = (TextView) convertView.findViewById(R.id.tv_end_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(mA1Tasks.get(position).getTask_name());
		viewHolder.startTime.setText(mA1Tasks.get(position).getStart_time());
		viewHolder.endTime.setText(mA1Tasks.get(position).getEnd_time());
		return convertView;

	}

	class ViewHolder {
		TextView startTime;
		TextView endTime;
		TextView name;
	}

}

package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.LogisticData;

public class LogisticAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<LogisticData> dataSet;
	private LayoutInflater mInflater;

	public LogisticAdapter(Context mContext, ArrayList<LogisticData> dataSet) {
		this.mContext = mContext;
		this.dataSet = dataSet;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return dataSet.size();
	}

	public void setData(ArrayList<LogisticData> mData) {
		this.dataSet = mData;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int arg0) {
		return dataSet.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_logistic_info, null);
			holder.ivProgressing = (ImageView) convertView
					.findViewById(R.id.iv_progressing);
			holder.ivProgressStart = (ImageView) convertView
					.findViewById(R.id.iv_progress_start);
			holder.tvLogisticDes = (TextView) convertView
					.findViewById(R.id.tv_logistic_des);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_logistic_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (pos == 0) {
			holder.ivProgressing.setVisibility(View.GONE);
			holder.ivProgressStart.setVisibility(View.VISIBLE);
		} else {
			holder.ivProgressing.setVisibility(View.VISIBLE);
			holder.ivProgressStart.setVisibility(View.GONE);
		}
		holder.tvLogisticDes.setText(dataSet.get(pos).getDeliverInfo());
		holder.tvTime.setText(dataSet.get(pos).getDeliverTime());

		return convertView;
	}

	class ViewHolder {
		public ImageView ivProgressing;
		public ImageView ivProgressStart;
		public TextView tvLogisticDes;
		public TextView tvTime;
	}

}

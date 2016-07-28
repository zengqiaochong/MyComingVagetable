package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.order.OrderData;
 
public class OrderAllAdapter extends BaseAdapter {

	private ArrayList<OrderData> orderList;
	private LayoutInflater mInflater;
	private OnClickListener mListener;

	public OrderAllAdapter(Context mContext, ArrayList<OrderData> orderList,
			OnClickListener mListener) {
		this.orderList = orderList;
		this.mListener = mListener;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return orderList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.parseLong(orderList.get(arg0).getOrder_id());
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_order_all, null);
			holder.panleOrderInfo = (LinearLayout) convertView.findViewById(R.id.ll_order_info);
			holder.userName = (TextView) convertView.findViewById(R.id.tv_user_name);
			holder.orderNo = (TextView) convertView.findViewById(R.id.tv_order_no);
			holder.orderTime = (TextView) convertView.findViewById(R.id.tv_order_time);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.userName.setText(orderList.get(index).getAddress().getUsername());
		holder.orderNo.setText(orderList.get(index).getOrderNO());
		holder.orderTime.setText(orderList.get(index).getBuyTime());
		holder.tvStatus.setText(orderList.get(index).getStatus());
		return convertView;
	}

	class ViewHolder {
		public LinearLayout panleOrderInfo;
		public TextView userName;
		public TextView orderNo;
		public TextView orderTime;
		public TextView tvStatus;
	}
}

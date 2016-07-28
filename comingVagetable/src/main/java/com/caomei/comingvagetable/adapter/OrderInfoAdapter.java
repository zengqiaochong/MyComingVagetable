package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

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
import com.caomei.comingvagetable.bean.order.OrderVegeInfo;

public class OrderInfoAdapter extends BaseAdapter {

	private ArrayList<OrderVegeInfo> vegeList;
	private LayoutInflater mInflater;

	public OrderInfoAdapter(Context mContext, ArrayList<OrderVegeInfo> orderList) {
		this.vegeList = orderList;
		mInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vegeList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return vegeList.get(arg0);
	}

 

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_order_info, null);
			holder.vegeName = (TextView) convertView
					.findViewById(R.id.tv_vege_name);
			holder.vegeAmount = (TextView) convertView
					.findViewById(R.id.tv_vege_amount);
			holder.vegePrice = (TextView) convertView
					.findViewById(R.id.tv_order_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String str = "";
		if(vegeList.get(index).grade == 0){
			str = "(有机)";
		}else if(vegeList.get(index).grade == 1){
			str = "(优选)";
		}else if(vegeList.get(index).grade == 2){
			str = "(一级)";
		}
		holder.vegeName.setText(vegeList.get(index).getVegename() + str);
		holder.vegeAmount.setText(String.valueOf(vegeList.get(index)
				.getBuyAmount()));
		holder.vegePrice.setText(String.valueOf(vegeList.get(index)
				.getSubTotal()));
		return convertView;
	}

	class ViewHolder {
		public TextView vegeName;
		public TextView vegeAmount;
		public TextView vegePrice;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

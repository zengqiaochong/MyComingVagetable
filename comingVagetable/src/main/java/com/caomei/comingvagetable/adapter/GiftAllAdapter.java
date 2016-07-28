package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.gift.GiftOrderData;
import com.caomei.comingvagetable.bean.order.OrderData;
 
public class GiftAllAdapter extends BaseAdapter {

	private ArrayList<GiftOrderData> giftOrderList;
	private LayoutInflater mInflater; 

	public GiftAllAdapter(Context mContext, ArrayList<GiftOrderData> orderList) {
		this.giftOrderList = orderList; 
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return giftOrderList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return giftOrderList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.parseLong(giftOrderList.get(arg0).getId()+"");
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_gift_all, null);
			holder.panleOrderInfo = (LinearLayout) convertView
					.findViewById(R.id.ll_order_info);
			holder.tvStatus=(TextView)convertView.findViewById(R.id.tv_status);
			holder.userName = (TextView) convertView
					.findViewById(R.id.tv_user_name);
			holder.giftName = (TextView) convertView
					.findViewById(R.id.tv_gift_name);
			holder.orderTime = (TextView) convertView
					.findViewById(R.id.tv_order_time);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.userName
				.setText(giftOrderList.get(index).getGuestName());
		holder.giftName.setText(giftOrderList.get(index).getGiftName());
		holder.orderTime.setText(giftOrderList.get(index).getCreateTime());
		holder.tvStatus.setText(giftOrderList.get(index).getStatus());
		return convertView;

	}

	class ViewHolder {
		public LinearLayout panleOrderInfo;
		public TextView userName;
		public TextView giftName;
		public TextView orderTime;
		public TextView tvStatus;
	}
}

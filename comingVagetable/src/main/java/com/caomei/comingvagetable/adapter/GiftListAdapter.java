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
import com.caomei.comingvagetable.bean.gift.GiftOrderData;

public class GiftListAdapter extends BaseAdapter{

	private ArrayList<GiftOrderData> orderList;
	private LayoutInflater mInflater;
	/**
	 * flag 标示那个listview的设配器
	 * 0 所有订单 （未启用）
	 * 1 待付款
	 * 2 待发货
	 * 3 待签收
	 * 4 待评价
	 * 
	 */
	private int flag;
	private OnClickListener mListener;
	public GiftListAdapter(Context mContext,ArrayList<GiftOrderData> orderList,OnClickListener mListener,int flag){
		this.orderList=orderList;
		this.mListener=mListener;
		this.flag=flag;
		mInflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
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
		return Long.parseLong(orderList.get(arg0).getId()+"");
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_gift_order, null);
			holder.panleOrderInfo=(LinearLayout)convertView.findViewById(R.id.ll_order_info);
			holder.userName=(TextView)convertView.findViewById(R.id.tv_user_name);
			holder.orderNo=(TextView)convertView.findViewById(R.id.tv_order_no);
			holder.giftName=(TextView)convertView.findViewById(R.id.tv_gift_name);
			holder.orderTime=(TextView)convertView.findViewById(R.id.tv_order_time);
			holder.btPay=(Button)convertView.findViewById(R.id.bt_pay);
			holder.btNotify=(Button)convertView.findViewById(R.id.bt_notify);
			holder.panelSign=(LinearLayout)convertView.findViewById(R.id.ll_to_sign);
			holder.btLogistic=(Button)convertView.findViewById(R.id.bt_logistic);
			holder.btSign=(Button)convertView.findViewById(R.id.bt_sign); 
			holder.btEvaluate=(Button)convertView.findViewById(R.id.bt_evaluate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		 
		holder.userName.setText(orderList.get(index).getGuestName());
		holder.orderNo.setText(orderList.get(index).getGiftOrderNO());
//		holder.orderNo.setText(""+orderList.get(index).getId());
		holder.orderTime.setText(orderList.get(index).getCreateTime());
		holder.giftName.setText(orderList.get(index).getGiftName());
		holder.btPay.setTag(index);
		holder.panleOrderInfo.setTag(index);
		holder.btEvaluate.setTag(index);
		holder.panleOrderInfo.setOnClickListener(mListener);
		holder.btEvaluate.setOnClickListener(mListener);
		holder.btPay.setOnClickListener(mListener);
		holder.btNotify.setOnClickListener(mListener);
		holder.btLogistic.setTag(index);
		holder.btLogistic.setOnClickListener(mListener);
		holder.btSign.setTag(orderList.get(index).getId());
		holder.btSign.setOnClickListener(mListener); 
		setViews(holder);
		
		return convertView;
	}
	private void setViews(ViewHolder holder) {
		switch (flag) {
		case 0:
//			holder.btNotify.setVisibility(View.GONE);
//			holder.btPay.setVisibility(View.VISIBLE);
			break;
		case 1: 
			resetAllViews(holder);
			holder.btPay.setVisibility(View.VISIBLE); 
			break;
		case 2:
			resetAllViews(holder);
			holder.btNotify.setVisibility(View.VISIBLE); 			
			break;
		case 3:
			resetAllViews(holder);
			holder.panelSign.setVisibility(View.VISIBLE); 
			
			break;
		case 4:
			resetAllViews(holder);
			holder.btEvaluate.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private void resetAllViews(ViewHolder holder) {
		holder.btPay.setVisibility(View.GONE);
		holder.btNotify.setVisibility(View.GONE);
		holder.panelSign.setVisibility(View.GONE);
		holder.btEvaluate.setVisibility(View.GONE);
	}

	class ViewHolder{
		public LinearLayout panleOrderInfo;
		public TextView giftName;
		public TextView userName;
		public TextView orderNo;
		public TextView orderTime;
		public Button btPay;
		public Button btNotify;
		
		public LinearLayout panelSign;
		public Button btLogistic;
		public Button btSign;
		 
		public Button btEvaluate;
	}
}

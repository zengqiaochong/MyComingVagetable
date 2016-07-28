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
import com.caomei.comingvagetable.fragment.FragmentToSign.CommonListener;

public class SignListAdapter extends BaseAdapter{

		private ArrayList<OrderData> signList;
		private LayoutInflater mInflater;
		private Context mContext;
		private OnClickListener mListener;
		
		public SignListAdapter(Context mContext,ArrayList<OrderData> signList,OnClickListener mListener){
			this.signList=signList;
			this.mContext=mContext;
			this.mListener=mListener;
			mInflater=LayoutInflater.from(mContext);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return signList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return signList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return Long.parseLong(signList.get(arg0).getOrder_id());
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_sign, null);
				holder.userName=(TextView)convertView.findViewById(R.id.tv_user_name);
				holder.orderNo=(TextView)convertView.findViewById(R.id.tv_order_no);
				holder.orderTime=(TextView)convertView.findViewById(R.id.tv_order_time);
				holder.btCheck=(Button)convertView.findViewById(R.id.bt_sign);
				holder.btLogi=(Button)convertView.findViewById(R.id.bt_logi);
				holder.panelOrderInfo=(LinearLayout)convertView.findViewById(R.id.ll_order_info);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}		 
			holder.userName.setText(signList.get(index).getAddress().getUsername());
			holder.orderNo.setText(signList.get(index).getOrderNO());
			holder.orderTime.setText(signList.get(index).getBuyTime());
			
			holder.btCheck.setTag(signList.get(index).getOrder_id());
			holder.btLogi.setTag(index);
			holder.btCheck.setOnClickListener(mListener);
			holder.btLogi.setOnClickListener(mListener);
			holder.panelOrderInfo.setTag(index);
			holder.panelOrderInfo.setOnClickListener(mListener);
			return convertView;
		}

		class ViewHolder{
			public LinearLayout panelOrderInfo;
			public TextView userName;
			public TextView orderNo;
			public TextView orderTime;
			public Button btCheck;
			public Button btLogi;
		}
	}

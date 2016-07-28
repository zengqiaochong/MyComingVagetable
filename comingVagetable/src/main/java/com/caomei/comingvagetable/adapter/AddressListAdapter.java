package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.AddressManageActivity.CommonListener;
import com.caomei.comingvagetable.adapter.OrderConfirmAdapter.ViewHolder;
import com.caomei.comingvagetable.bean.AddressBean;
import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.caomei.comingvagetable.util.ImageUtil;
import com.caomei.comingvagetable.util.MethodUtil;

public class AddressListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<AddressData> data;
	private LayoutInflater mInflater = null;
	private CommonListener mListener;

	public AddressListAdapter(Context mContext, ArrayList<AddressData> data,
			CommonListener listener) {
		this.mListener = listener;
		this.mContext = mContext;
		this.data = data;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		Long res;
		try {
			res = Long.parseLong(data.get(arg0).getAddress_id());
		} catch (Exception ex) {
			res = 0L;
		}
		return res;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_my_address, null);
			holder.tvPhoneNo = (TextView) convertView
					.findViewById(R.id.tv_phone_number);
			holder.tvAddress = (TextView) convertView
					.findViewById(R.id.tv_detail_address);
			holder.cbDefault = (CheckBox) convertView
					.findViewById(R.id.cb_default_addr);
			holder.ivEdit = (ImageView) convertView
					.findViewById(R.id.iv_edit_addr);
			holder.ivDel = (ImageView) convertView
					.findViewById(R.id.iv_del_addr);
			holder.panel = (LinearLayout) convertView
					.findViewById(R.id.ll_addr_panel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvPhoneNo.setText(data.get(pos).getPhoneNo());
		holder.tvAddress.setText(data.get(pos).getBigAddress()
				+ data.get(pos).getSmallAddress());
		if (data.get(pos).getIsDefault() == 1) {
			Log.e("cb", "is defalut " + pos);
			holder.cbDefault.setChecked(true);
		} else {
			Log.e("cb", "is not defalut " + pos);
			holder.cbDefault.setChecked(false);
		}

		holder.cbDefault.setTag(getItemId(pos));
		holder.cbDefault.setOnClickListener(mListener);
		holder.ivDel.setOnClickListener(mListener);
		holder.ivDel.setTag(getItemId(pos));
		holder.ivEdit.setOnClickListener(mListener);
		holder.ivEdit.setTag(pos);
		holder.ivEdit.setTag(R.id.tag_first, data.get(pos));
		holder.panel.setOnClickListener(mListener);
		holder.panel.setTag(data.get(pos));
		return convertView;
	}

	class ViewHolder {
		private ImageView ivEdit;
		private ImageView ivDel;
		private CheckBox cbDefault;
		private TextView tvPhoneNo;
		private TextView tvAddress;
		private LinearLayout panel;
	}
}

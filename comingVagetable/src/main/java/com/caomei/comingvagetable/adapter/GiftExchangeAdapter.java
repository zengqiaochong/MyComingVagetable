package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.adapter.GiftAllAdapter.ViewHolder;
import com.caomei.comingvagetable.bean.gift.GiftData;
import com.caomei.comingvagetable.util.ImageUtil;

public class GiftExchangeAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<GiftData> dataSet;
	private LayoutInflater mInflater = null;
	public GiftExchangeAdapter(Context mContext,ArrayList<GiftData> dataSet){
		this.mContext=mContext;
		this.dataSet=dataSet;
		mInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataSet.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
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
			convertView = mInflater.inflate(R.layout.item_funs_grid_view, null);
			holder.itemPic = (ImageView) convertView
					.findViewById(R.id.iv_item_product_pic);
			holder.itemDes = (TextView) convertView
					.findViewById(R.id.tv_item_product_des);
			 holder.itemRemain=(TextView)convertView.findViewById(R.id.tv_remain);
			holder.itemMadun = (TextView) convertView
					.findViewById(R.id.tv_product_madun);
			holder.itemPrice = (TextView) convertView
					.findViewById(R.id.tv_product_price);
			holder.itemLevel = (TextView) convertView
					.findViewById(R.id.tv_level_limit);
 
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageUtil.showImageFromUrl(dataSet.get(pos).getImgUrl(),
				holder.itemPic);
		holder.itemDes.setText(dataSet.get(pos).getGiftName());
		holder.itemRemain.setText("库存："+dataSet.get(pos).getTotal()+dataSet.get(pos).getUnit());
		holder.itemPrice.setText("原价：¥" + dataSet.get(pos).getPrice());
		holder.itemMadun.setText(dataSet.get(pos).getNeedCredit() + "马盾"+	" /"+dataSet.get(pos).getUnit());
		holder.itemLevel.setText(dataSet.get(pos).getMinLevelName());
		return convertView;
	}

	class ViewHolder {
		public ImageView itemPic; 
		public TextView itemLevel;
		public TextView itemDes; 
		public TextView itemPrice;
		public TextView itemMadun;
		public TextView itemRemain;
	}

}

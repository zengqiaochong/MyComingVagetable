package com.caomei.comingvagetable.adapter;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.adapter.VegeCartAdapter.ViewHolder;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.caomei.comingvagetable.util.ImageUtil;
import com.caomei.comingvagetable.util.MethodUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderConfirmAdapter extends BaseAdapter{
	private Context mContext;
	private VegeCartBean data;
	private LayoutInflater mInflater = null;

	public OrderConfirmAdapter(Context mContext,VegeCartBean bean){
		this.mContext=mContext;
		this.data=bean;
		mInflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		if(data==null)
			return 0;
		return data.getData().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
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
			convertView = mInflater.inflate(R.layout.item_confirm_order, null);
			holder.itemPic = (ImageView) convertView
					.findViewById(R.id.iv_vege);
			holder.itemName = (TextView) convertView
					.findViewById(R.id.tv_vege_name);
			holder.itemUnit=(TextView)convertView.findViewById(R.id.tv_unit);
			holder.itemAmount = (TextView) convertView
					.findViewById(R.id.tv_product_count);
			holder.panelPrice=(LinearLayout)convertView.findViewById(R.id.ll_panel_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		} 
		
		holder.itemName.setText(data.getData().get(pos).getVegeInfo().getVegeName());
		ImageUtil.showImageFromUrl(data.getData().get(pos).getFormatedImgUrl(),holder.itemPic,ImageUtil.imageOptions);
		holder.itemUnit.setText(data.getData().get(pos).getVegeInfo().getUnit());
		holder.itemAmount.setText("x"+data.getData().get(pos).getBuyAmount()); 
		MethodUtil.SetPanelPrice(Float.valueOf(data.getData().get(pos).getVegeInfo().getPrice()),holder.panelPrice); 
		return convertView;
	}

	class ViewHolder {
		private ImageView itemPic;
		private TextView itemName;
		private TextView itemUnit;
		private LinearLayout panelPrice; 
		public TextView itemAmount;
	}
	
}


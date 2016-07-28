package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.CartActivity.CartCommonListener;
import com.caomei.comingvagetable.bean.vegedata.VegeCartData;
import com.caomei.comingvagetable.util.ImageUtil;
import com.caomei.comingvagetable.util.MethodUtil;

public class VegeCartAdapter extends BaseAdapter {

	private ArrayList<VegeCartData> mData;
	private Context mContext;
	private LayoutInflater mInflater = null;
	private CartCommonListener mListener;
	private boolean selectedAble;

	public VegeCartAdapter(Context mContext, ArrayList<VegeCartData> data,
			CartCommonListener listener) {
		this.mContext = mContext;
		this.mData = data;
		this.mListener = listener;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int pos) {
		return Long.parseLong(mData.get(pos).getScarid());
	}

	public void setSelectedAble(boolean selected) {
		this.selectedAble = selected;
	}

	/**
	 * 是否显示单选按钮
	 * 
	 * @return
	 */
	public boolean getSelectedAble() {
		return this.selectedAble;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_vege_cart, null);
			holder.llPanel = (LinearLayout) convertView
					.findViewById(R.id.ll_product_pic);
			holder.itemDel=(ImageView)convertView.findViewById(R.id.iv_del_item);
			holder.ivPlus = (ImageView) convertView
					.findViewById(R.id.iv_count_plus);
			holder.tvCountBuy=(TextView)convertView.findViewById(R.id.tv_count_buy);
			holder.ivMinus = (ImageView) convertView
					.findViewById(R.id.iv_count_minus); 
			holder.cbSel = (CheckBox) convertView
					.findViewById(R.id.cb_item_selected);
			holder.itemPic = (ImageView) convertView.findViewById(R.id.iv_vege);
			holder.itemName = (TextView) convertView
					.findViewById(R.id.tv_vege_name);
			holder.itemUnit = (TextView) convertView
					.findViewById(R.id.tv_vege_unit);
			holder.itemAmount = (TextView) convertView
					.findViewById(R.id.tv_vege_sel_count); 
			holder.panelPrice = (LinearLayout) convertView
					.findViewById(R.id.ll_price_panel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String str = "";
		if(mData.get(pos).grade == 0){
			str = "(有机)";
		}else if(mData.get(pos).grade == 1){
			str = "(优选)";
		}else if(mData.get(pos).grade == 2){
			str = "(一级)";
		}
		holder.itemName.setText(mData.get(pos).getVegeInfo().getVegeName() + str);
		ImageUtil.showImageFromUrl(mData.get(pos).getFormatedImgUrl(),
				holder.itemPic, ImageUtil.imageOptions);

		holder.itemUnit.setText("/"+mData.get(pos).getVegeInfo().getUnit());
		holder.itemAmount
				.setText("已选"+String.valueOf(mData.get(pos).getBuyAmount())+"件商品");
		holder.tvCountBuy.setText(String.valueOf(mData.get(pos).getBuyAmount()));
		holder.ivPlus.setOnClickListener(mListener);
		holder.ivPlus.setTag(getItemId(pos));
		holder.ivMinus.setOnClickListener(mListener);
		holder.ivMinus.setTag(getItemId(pos)); 
		holder.itemDel.setTag(pos);
		holder.itemDel.setOnClickListener(mListener);
		holder.cbSel.setTag(pos);
		Log.e("pos", "pos " + pos + " " + mData.get(pos).isSelected());
		holder.cbSel.setChecked(mData.get(pos).isSelected());

		holder.cbSel.setOnCheckedChangeListener(mListener);
		if (selectedAble) {
			holder.cbSel.setVisibility(View.VISIBLE);
		} else {
			holder.cbSel.setVisibility(View.GONE);
		}
		MethodUtil.SetPanelPrice(
				Float.valueOf(mData.get(pos).getVegeInfo().getPrice()),
				holder.panelPrice);
		return convertView;
	}

	class ViewHolder {
		
		private CheckBox cbSel;
		private LinearLayout llPanel;
		private ImageView ivPlus;
		private ImageView ivMinus; 
		private ImageView itemPic;
		private TextView itemName;
		private TextView itemUnit;
		private LinearLayout panelPrice;
		public TextView itemAmount;
		public TextView tvCountBuy;
		private ImageView itemDel;
	}

}

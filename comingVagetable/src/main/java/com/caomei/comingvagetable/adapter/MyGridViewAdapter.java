package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.vegedata.VegeAllBeanData;
import com.caomei.comingvagetable.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MyGridViewAdapter extends BaseAdapter {
  
	private ArrayList<VegeAllBeanData> itemData;
	private LayoutInflater mInflater = null;
	private Context mContext;
	
	public MyGridViewAdapter(Context mContext, ArrayList<VegeAllBeanData> itemData) {
		// this.slideData=slideData;
		this.itemData = itemData; 
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return itemData.size();
	}
	public void setData(ArrayList<VegeAllBeanData> mData){
		this.itemData=mData;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int arg0) {
		return itemData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_grid_view, null);
			holder.itemPic = (ImageView) convertView
					.findViewById(R.id.iv_item_product_pic);
			holder.itemDes = (TextView) convertView
					.findViewById(R.id.tv_item_product_des);
			holder.itemProvider=(TextView)convertView.findViewById(R.id.tv_product_unit);
			holder.itemOnsellTime=(TextView)convertView.findViewById(R.id.tv_onsell_time);
			holder.itemPrice=(TextView)convertView.findViewById(R.id.tv_product_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		} 
		
		holder.itemDes.setText(itemData.get(pos).getVegeName());
		if(itemData.get(pos).getFormatImgUrls()!=null&&itemData.get(pos).getFormatImgUrls().size()>0){
			ImageUtil.showImageFromUrl(itemData.get(pos).getFormatImgUrls().get(0),holder.itemPic,ImageUtil.imageVegeOptions);
		}
		holder.itemOnsellTime.setText("市场参考价格："+itemData.get(pos).marketPrice + "元/kg");
		holder.itemProvider.setText("重量：>" + itemData.get(pos).perUnitWeight + "kg/" + itemData.get(pos).getUnit());
		holder.itemPrice.setText("今日最低价：" + itemData.get(pos).getPrice()+"元/"+itemData.get(pos).getUnit());
		return convertView;
	}

	class ViewHolder {
		private ImageView itemPic;
		private TextView itemOnsellTime;
		private TextView itemDes;
		private TextView itemProvider;
		private TextView itemPrice;  
	}

}

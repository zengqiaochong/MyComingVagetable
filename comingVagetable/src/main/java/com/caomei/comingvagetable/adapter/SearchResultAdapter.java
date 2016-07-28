package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.vegedata.VegeAllBeanData;
import com.caomei.comingvagetable.util.ImageUtil;

public class SearchResultAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<VegeAllBeanData> dataSet;
	private LayoutInflater mInflater;
	public SearchResultAdapter(Context mContext,ArrayList<VegeAllBeanData> dataSet){
		this.mContext=mContext;
		this.dataSet=dataSet;
		mInflater= LayoutInflater.from(mContext);
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
			convertView = mInflater.inflate(R.layout.item_search_result, null);
			holder.itemPic = (ImageView) convertView.findViewById(R.id.iv_vege);
			holder.itemName = (TextView) convertView
					.findViewById(R.id.tv_vege_name);
			holder.itemPrice = (TextView) convertView
					.findViewById(R.id.tv_price);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.itemName.setText("菜品："
				+ dataSet.get(pos).getVegeName());
		try {
			holder.itemPrice.setText(dataSet.get(pos).getPrice()
					+ "/" + dataSet.get(pos).getUnit());
			ImageUtil.showImageFromUrl(dataSet.get(pos)
					.getFormatImgUrls().get(0), holder.itemPic,
					ImageUtil.imageOptions);
		} catch (Exception ex) {
			Log.e("data","view error "+pos);
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView itemPic;
		private TextView itemName;
		public TextView itemPrice;
	}

}

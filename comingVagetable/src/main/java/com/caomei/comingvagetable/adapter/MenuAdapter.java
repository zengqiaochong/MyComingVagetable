package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.vegedata.VegeCategoryData;

/**
 * ��Ʒ��߻��˵�����������
 * @author Wang Xiaojian
 *
 */
public class MenuAdapter extends BaseAdapter{

	private ArrayList<VegeCategoryData> mData;
	private LayoutInflater mInflater = null;
	
	
	public MenuAdapter(Context mContext,ArrayList<VegeCategoryData> mDataSet){
		this.mData=mDataSet; 
		this.mInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		 
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	} 

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_menu_view, null);
			holder.tv=(TextView)convertView.findViewById(R.id.tv_menu_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(mData.get(pos).getVegetype());
		return convertView;
	}
	
	class ViewHolder {
		TextView tv;
	}

}

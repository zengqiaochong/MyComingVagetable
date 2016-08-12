package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.adapter.ProvinceAdapter.ViewHolder;
import com.caomei.comingvagetable.sortlistview.CharacterParser;

public class CityAdapter extends BaseAdapter {

	private int selecteIndex = 0;
	private Context mContext;
	private ArrayList<String> data;
	private LayoutInflater mInflater;

	public CityAdapter(Context mContext, ArrayList<String> data) {
		this.mContext = mContext;
		this.data = data;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_community, null);
			holder.tvVillage = (TextView) convertView
					.findViewById(R.id.tv_community);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvVillage.setText(data.get(pos));
		return convertView;
	} 

	class ViewHolder {
		public TextView tvVillage;
	}

}
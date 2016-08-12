package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.CommunityData;

public class CommunitAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CommunityData> data;
	private LayoutInflater mInflater;

	public CommunitAdapter(Context mContext, ArrayList<CommunityData> items) {
		this.mContext = mContext;
		this.data = items;
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
		try {
			return Long.parseLong(data.get(arg0).getCommunity_id());
		} catch (Exception ex) {
			return 0L;
		}
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_community, null);
			holder.tvCommunity = (TextView) convertView
					.findViewById(R.id.tv_community);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvCommunity.setText(data.get(arg0).getCommunityName());
		return convertView;
	}

	class ViewHolder {
		public TextView tvCommunity;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的char ascii值
	 */
	public int getSectionForPosition(int position) {
		return data.get(position).getCommunityName().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = data.get(i).sortLetters;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
}
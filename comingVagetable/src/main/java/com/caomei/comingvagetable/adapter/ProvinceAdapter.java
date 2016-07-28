package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caomei.comingvagetable.R; 

public class ProvinceAdapter extends BaseAdapter{

	private int selecteIndex=0;
	private Context mContext;
	private ArrayList<String> data;
	private LayoutInflater mInflater;
	
	public ProvinceAdapter(Context mContext,ArrayList<String> data){
		this.mContext =mContext;
		this.data=data;
		mInflater=LayoutInflater.from(mContext);
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
			convertView = mInflater.inflate(R.layout.item_village, null);
			holder.tvVillage = (TextView) convertView
					.findViewById(R.id.tv_village);
			holder.vBoard=convertView.findViewById(R.id.v_board);
			holder.vIndicator=convertView.findViewById(R.id.v_indicator);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		} 
		holder.tvVillage.setText(data.get(pos));
		if(selecteIndex==pos){
			holder.vBoard.setVisibility(View.GONE);
			holder.vIndicator.setVisibility(View.VISIBLE);
			holder.tvVillage.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		}else{
			holder.vBoard.setVisibility(View.VISIBLE);
			holder.vIndicator.setVisibility(View.GONE);
			holder.tvVillage.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bg));
		} 
		return convertView;
	}
	
	public void setIndex(int i){
		this.selecteIndex=i;
	}

	class ViewHolder{
		public TextView tvVillage; 
		private View vIndicator;
		private View vBoard;
	}
}

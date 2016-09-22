package com.caomei.comingvagetable.takeaway.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.takeaway.bean.CommType;

import java.util.List;

/*
* 快餐外卖、类别、列表适配器
* */
public class CaiTypeListrAdapter extends BaseAdapter {
	private List<CommType.TYPE> items;
	private Context context;
	ViewHolder holder = null;

	public CaiTypeListrAdapter(Context context, List<CommType.TYPE> items) {
		this.context = context;
		this.items = items;
	}

	public void notiDataChanged(List<CommType.TYPE> items){
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_cai_type_list, null);
			/*菜品类型名字*/
			holder.tv_name = (TextView) convertView.findViewById(R.id.cai_type_name);
			/*背景*/
			holder.cai_type_layout = (RelativeLayout) convertView.findViewById(R.id.cai_type_layout);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_name.setText(items.get(position).typeName);
		if(items.get(position).isclick){
			holder.cai_type_layout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
		}
		else {
			holder.cai_type_layout.setBackgroundColor(ContextCompat.getColor(context,R.color.white_slide));
		}

		return convertView;
	}

	private class ViewHolder {
		public TextView tv_name;
		public RelativeLayout cai_type_layout;
	}



}

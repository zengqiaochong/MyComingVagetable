package com.caomei.comingvagetable.housekeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.housekeeper.bean.HouseKeeperBean;
import com.caomei.comingvagetable.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class HouseKeeperAdapter extends BaseAdapter {
	private List<HouseKeeperBean.HouseKeeperVege> items;
	private Context context;

	public HouseKeeperAdapter(Context context, List<HouseKeeperBean.HouseKeeperVege> items) {
		this.context = context;
		this.items = items;
	}

	public void notiDataChanged(List<HouseKeeperBean.HouseKeeperVege> items){
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.imageView = (ImageView) convertView.findViewById(R.id.housekeeper_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			if(items.get(position).imgs != null && items.get(position).imgs.size() > 0){
				System.out.println("url======="+items.get(position).BASEURL + items.get(position).imgs.get(0).picUrl);
				ImageLoader.getInstance().displayImage(items.get(position).BASEURL + items.get(position).imgs.get(0).picUrl,
						holder.imageView, ImageUtil.imageVegeOptions);
			}else{
				holder.imageView.setImageResource(R.drawable.none);
			}
		}catch (Exception e){
			holder.imageView.setImageResource(R.drawable.none);
		}
		holder.tv_name.setText(items.get(position).goodsName);

		return convertView;
	}

	private class ViewHolder {
		public TextView tv_name, tv_amount, tv_price;
		public ImageView imageView;
	}
}

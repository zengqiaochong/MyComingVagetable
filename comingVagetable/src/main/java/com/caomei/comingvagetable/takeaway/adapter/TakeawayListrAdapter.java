package com.caomei.comingvagetable.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.takeaway.bean.TakeawaycommodityBean;
import com.caomei.comingvagetable.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
/*
* 快餐外卖列表适配器
* */
public class TakeawayListrAdapter extends BaseAdapter {
	private List<TakeawaycommodityBean.HouseKeeperVege> items;
	private Context context;
	private OnClickincreaseListener onClickincrease;


	public TakeawayListrAdapter(Context context, List<TakeawaycommodityBean.HouseKeeperVege> items,
								final OnClickincreaseListener onClickincrease ) {
		this.context = context;
		this.items = items;
		this.onClickincrease = onClickincrease;

	}

	public void notiDataChanged(List<TakeawaycommodityBean.HouseKeeperVege> items){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.take_away_list_view, null);
			/*菜品名字*/
			holder.tv_name = (TextView) convertView.findViewById(R.id.store_commodity_name_text);
			/*菜品数量*/
			holder.tv_amount = (TextView) convertView.findViewById(R.id.store_commodity_Price);
			/*菜品价格*/
			holder.tv_price = (TextView) convertView.findViewById(R.id.inventory_quantity);
			/*图片*/
			holder.imageView = (ImageView) convertView.findViewById(R.id.commodity_img);

			/*加入购物车*/
			holder.cai_increase = (Button) convertView.findViewById(R.id.cai_increase);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			if(items.get(position).imgs != null && items.get(position).imgs.size() > 0){
				System.out.println("url======="+items.get(position).BASEURL + items.get(position).imgs.get(0).picUrl);
				ImageLoader.getInstance().displayImage(items.get(position).BASEURL + items.get(position).imgs.get(0).picUrl, holder.imageView, ImageUtil.imageVegeOptions);
			}else{
				holder.imageView.setImageResource(R.drawable.none);
			}
		}catch (Exception e){
			holder.imageView.setImageResource(R.drawable.none);
		}
		holder.tv_name.setText(items.get(position).goodsName);
		holder.tv_amount.setText(items.get(position).Amount+"");
		holder.tv_price.setText(items.get(position).price);

		/*增加数量*/
		holder.cai_increase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onClickincrease!=null){
					onClickincrease.onClickincrease(view,position);
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {
		public TextView tv_name, tv_amount, tv_price;
		public ImageView imageView;
		Button cai_increase;
	}
	public interface OnClickincreaseListener {
		void onClickincrease(View view,int position);
	}
}

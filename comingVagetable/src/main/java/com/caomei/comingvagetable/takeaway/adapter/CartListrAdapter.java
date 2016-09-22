package com.caomei.comingvagetable.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.takeaway.bean.ShoppingCartType;

import java.util.List;

/*
* 快餐外卖、购物车、列表适配器
* */
public class CartListrAdapter extends BaseAdapter {
	private List<ShoppingCartType.CART> items;
	private Context context;
	ViewHolder holder = null;
	private OnClickincreaseListener onClickincrease;
	private OnClickreduceListener onClickreduce;
	public CartListrAdapter(Context context, List<ShoppingCartType.CART> items,
							final OnClickincreaseListener onClickincrease,final OnClickreduceListener onClickreduce ) {
		this.context = context;
		this.items = items;
		this.onClickincrease = onClickincrease;
		this.onClickreduce = onClickreduce;
	}

	public void notiDataChanged(List<ShoppingCartType.CART> items){
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_cai_cart_list, null);
			/*菜品名字*/
			holder.tv_name = (TextView) convertView.findViewById(R.id.cai_type_name);
			/*价格*/
			holder.price = (TextView) convertView.findViewById(R.id.price);
			//显示数量
			holder.cai_size = (TextView) convertView.findViewById(R.id.cai_size);
			/*减少*/
			holder.cai_reduce = (Button) convertView.findViewById(R.id.cai_reduce);
			/*增加*/
			holder.cai_increase = (Button) convertView.findViewById(R.id.cai_increase);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_name.setText(items.get(position).goodsInfo.vegeName+"");
		holder.price.setText("￥"+items.get(position).subTotal+"");
		holder.cai_size.setText(items.get(position).buyAmount+"");

		/*增加数量*/
		holder.cai_increase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onClickincrease!=null){
					onClickincrease.onClickincrease(view,position);
				}
			}
		});
		/*减少数量*/
		holder.cai_reduce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onClickreduce!=null) {
					onClickreduce.onClickreduce(position);
				}
			}
		});
		return convertView;
	}

	private class ViewHolder {
		public TextView tv_name,price,cai_size;
		public  Button cai_reduce,cai_increase;
	}
	public interface OnClickincreaseListener {
		void onClickincrease(View view,int position);
	}
	public interface OnClickreduceListener {
		void onClickreduce(int position);
	}
}

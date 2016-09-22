package com.caomei.comingvagetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.MyDiscuntBean;
import com.caomei.comingvagetable.util.ImageUtil;

import java.util.List;

public class MyDiscountAdapter extends BaseAdapter {
	private List<MyDiscuntBean.DiscountBean> data;
	private LayoutInflater mInflater = null;

	public MyDiscountAdapter(Context mContext, List<MyDiscuntBean.DiscountBean> data) {
		this.data = data;
		mInflater = LayoutInflater.from(mContext);
	}

	public void changedData(List<MyDiscuntBean.DiscountBean> lists){
		this.data = lists;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		Long res;
		try {
			res = Long.parseLong(data.get(arg0).discount_id);
		} catch (Exception ex) {
			res = 0L;
		}
		return res;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.my_discount_item, null);
			holder.discount_img = (ImageView) convertView.findViewById(R.id.discount_img);
			holder.discount_img_two = (ImageView) convertView.findViewById(R.id.discount_img_two);
			holder.discount_name = (TextView) convertView.findViewById(R.id.discount_name);
			holder.discount_time = (TextView) convertView.findViewById(R.id.discount_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(data.get(pos).isUsed.equals("0")){
			if(data.get(pos).usable.equals("0")){
				ImageUtil.showImageFromUrl(data.get(pos).getOneFormatedImgUrl(), holder.discount_img, ImageUtil.imageOptions);
				holder.discount_img_two.setVisibility(View.INVISIBLE);
			}else if(data.get(pos).usable.equals("2")){
				ImageUtil.showImageFromUrl(data.get(pos).getOneFormatedImgUrl(), holder.discount_img, ImageUtil.imageOptions);
				holder.discount_img_two.setVisibility(View.VISIBLE);
				holder.discount_img_two.setImageResource(R.drawable.about_to_expire_img);
			}else{
				if(data.get(pos).pic_id.split(";").length > 1){
					ImageUtil.showImageFromUrl(data.get(pos).getSecFormatedImgUrl(), holder.discount_img, ImageUtil.imageOptions);
					holder.discount_img_two.setVisibility(View.VISIBLE);
					holder.discount_img_two.setImageResource(R.drawable.has_failed_img);
				}else{
					holder.discount_img.setVisibility(View.INVISIBLE);
					holder.discount_img_two.setVisibility(View.INVISIBLE);
				}
			}
		}else{
			if(data.get(pos).pic_id.split(";").length > 1){
				ImageUtil.showImageFromUrl(data.get(pos).getSecFormatedImgUrl(), holder.discount_img, ImageUtil.imageOptions);
				holder.discount_img_two.setImageResource(R.drawable.already_used_img);
			}else{
				holder.discount_img.setVisibility(View.INVISIBLE);
				holder.discount_img_two.setVisibility(View.INVISIBLE);
			}
		}
		holder.discount_name.setText(data.get(pos).name);
		holder.discount_time.setText("使用期限 " + data.get(pos).startTime.split(" ")[0] + " - " + data.get(pos).endTime.split(" ")[0]);
		return convertView;
	}

	class ViewHolder {
		private ImageView discount_img;
		private TextView discount_name;
		private TextView discount_time;
		private ImageView discount_img_two;
	}
}

package com.caomei.comingvagetable.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.takeaway.bean.MerchantList;
import com.caomei.comingvagetable.util.ShareUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/*
* 快餐外卖、商家（小区管家）、列表适配器
* */
public class MerchantListrAdapter extends BaseAdapter {
	private List<MerchantList.MERC> items;
	private Context context;
	ViewHolder holder = null;

	public MerchantListrAdapter(Context context, List<MerchantList.MERC> items) {
		this.context = context;
		this.items = items;
	}

	public void notiDataChanged(List<MerchantList.MERC> items){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_cai_merchant_list, null);
			/*商家名字*/
			holder.cai_merchant_name = (TextView) convertView.findViewById(R.id.cai_merchant_name);
			holder.cai_merchant_img = (ImageView) convertView.findViewById(R.id.cai_merchant_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cai_merchant_name.setText(items.get(position).name+"");
		String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_IMG, items.get(position).imgid,"PictureOfUser", ShareUtil.getInstance(AppData.mContext).getUserId());

		ImageLoader.getInstance().displayImage(url,
				holder.cai_merchant_img, options);
		return convertView;
	}

	private class ViewHolder {
		public TextView cai_merchant_name;
		public ImageView cai_merchant_img;

	}
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.main_spring_festival_img) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.main_spring_festival_img) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.main_spring_festival_img) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(false) // 设置下载的图片是否缓存在SD卡中
			/*.displayer(new RoundedBitmapDisplayer(100)) // 设置成圆角图片*/
			.build();
}

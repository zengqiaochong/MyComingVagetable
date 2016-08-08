package com.caomei.comingvagetable.activity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.R.color;
import com.caomei.comingvagetable.adapter.GiftFragmentAdapter;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.fragment.BaseFragment;
import com.caomei.comingvagetable.fragment.FragmentGiftAll;
import com.caomei.comingvagetable.fragment.FragmentGiftToEvaluate;
import com.caomei.comingvagetable.fragment.FragmentGiftToSend;
import com.caomei.comingvagetable.fragment.FragmentGiftToSign;
import com.caomei.comingvagetable.util.AppUtil;

import de.greenrobot.event.EventBus;

public class GiftOrderActivity extends BaseActivity {

	private ViewPager vpGift;

	private TextView tvOrderAll;
	private TextView tvToSign;
	private TextView tvToSend;
	private TextView tvOrderDone;
	private ArrayList<BaseFragment> mFragmentList;
	private GiftFragmentAdapter giftFragmentAdapter;
	private CommonListener mListener;
	private Context mContext;
	private ImageView ivCursor;

	private int currentIndex = 0;
	private int screenWidth;
	private ImageView ivOrderAll;
	private ImageView ivToSign;
	private ImageView ivOrderDone;
	private ImageView ivOrderToSend;
	private ImageView ivBack;
	private LinearLayout panelOrderAll;
	private LinearLayout panelToSign;
	private LinearLayout panelToSend;
	private LinearLayout panelOrderDone;

	private PopupWindow pWindow;
	/**
	 * 对订单进行时间过滤的标示位：0　全部订单 1　今天 2　三天内订单 3　一周内订单 4　一月内订单 
	 */
	public static int orderTimeType = 0;
	
	private LinearLayout panelOrderTime;
	private TextView tvOrderTime;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_gift_order);
		initView();
		initData();
	}

	private void initData() {
		mListener = new CommonListener();
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new FragmentGiftAll());
		mFragmentList.add(new FragmentGiftToSend());
		mFragmentList.add(new FragmentGiftToSign());
		mFragmentList.add(new FragmentGiftToEvaluate());

		giftFragmentAdapter = new GiftFragmentAdapter(getSupportFragment(),
				mFragmentList);
		vpGift.setAdapter(giftFragmentAdapter);
		vpGift.addOnPageChangeListener(mListener);
		vpGift.setOffscreenPageLimit(3);
		panelOrderAll.setOnClickListener(mListener);
		panelOrderDone.setOnClickListener(mListener);
		panelToSend.setOnClickListener(mListener);
		panelToSign.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
		panelOrderTime.setOnClickListener(mListener);
		initTabLineWidth();
	}

	private void initView() {

		vpGift = (ViewPager) findViewById(R.id.vp_gift);
		 
		tvOrderAll=(TextView)findViewById(R.id.tv_order_all); 
		tvToSend=(TextView)findViewById(R.id.tv_to_send);
		tvToSign=(TextView)findViewById(R.id.tv_to_sign);
		tvOrderDone=(TextView)findViewById(R.id.tv_order_done);
		
		ivOrderAll=(ImageView)findViewById(R.id.iv_order_all); 
		Drawable icon = getResources().getDrawable(R.drawable.iv_order_all);
		Drawable tintIcon = DrawableCompat.wrap(icon);
		DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.money_yellow));
		ivOrderAll.setImageDrawable(tintIcon);
		
		ivOrderToSend=(ImageView)findViewById(R.id.iv_to_send);
		ivToSign=(ImageView)findViewById(R.id.iv_to_sign);
		ivOrderDone=(ImageView)findViewById(R.id.iv_order_done);
			
		ivBack=(ImageView)findViewById(R.id.iv_back);
		panelOrderAll=(LinearLayout)findViewById(R.id.ll_order_all);
		 
		panelToSend=(LinearLayout)findViewById(R.id.ll_to_send);
		panelToSign=(LinearLayout)findViewById(R.id.ll_to_sign);
		panelOrderDone=(LinearLayout)findViewById(R.id.ll_order_done);
		panelOrderTime=(LinearLayout)findViewById(R.id.ll_panel_order_time);
		tvOrderTime=(TextView)findViewById(R.id.tv_order_time);
		ivCursor = (ImageView) findViewById(R.id.iv_cursor);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		((GiftOrderActivity) mContext).getWindow().getWindowManager()
				.getDefaultDisplay().getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivCursor
				.getLayoutParams();
		lp.width = screenWidth / 4;
		ivCursor.setLayoutParams(lp);
	}

	private int time_id;
	class CommonListener implements ViewPager.OnPageChangeListener, OnClickListener {
		@Override
		public void onClick(View v) {
			int time=0;
			switch (v.getId()) {
			case R.id.ll_order_all:
				vpGift.setCurrentItem(0);
				break;
			case R.id.ll_to_send:
				vpGift.setCurrentItem(1);
				break;
			case R.id.ll_to_sign:
				vpGift.setCurrentItem(2);
				break;
			case R.id.ll_order_done:
				vpGift.setCurrentItem(3);
				break;
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.ll_panel_order_time:
				if (pWindow != null && pWindow.isShowing()) {
					pWindow.dismiss();
				} else if(pWindow != null && !pWindow.isShowing()) {
					pWindow.showAsDropDown(
							findViewById(R.id.ll_panel_order_time), 0, 50);
				}else{
					IntialPopupWindow();
					pWindow.showAsDropDown(
							findViewById(R.id.ll_panel_order_time), 0, 50);
				}
				break;
			case R.id.panel_30day:
				time++;
			case R.id.panel_7day:
				time++;
			case R.id.panel_3day:
				time++;
			case R.id.panel_today:
				time++;
			case R.id.panel_allday:
				if (pWindow != null && pWindow.isShowing()) {
					pWindow.dismiss();
				}
				orderTimeType = time;
				if(time == 1){
					time_id = R.id.tv_time_str;
				}else if(time == 2){
					time_id = R.id.tv_time_str2;
				}else if(time == 3){
					time_id = R.id.tv_time_str3;
				}else if(time == 4){
					time_id = R.id.tv_time_str4;
				}else{
					time_id = R.id.tv_time_str5;
				}
				tvOrderTime.setText(((TextView)menu.findViewById(time_id)).getText());
				EventBus.getDefault().post(
						new EventMsg(OpCodes.GET_GIFT_ORDER_BY_TIME, orderTimeType));
				 
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int position, float offset, int offsetPixels) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivCursor
					.getLayoutParams();
			/**
			 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来 设置mTabLineIv的左边距
			 * 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1; 1->0
			 */

			if (currentIndex == 0 && position == 0)// 0->1
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
						* (screenWidth / 4));

			} else if (currentIndex == 1 && position == 0) // 1->0
			{
				lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 4) + currentIndex
						* (screenWidth / 4));

			} else if (currentIndex == 1 && position == 1) // 1->2
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
						* (screenWidth / 4));
			} else if (currentIndex == 2 && position == 1) // 2->1
			{
				lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 4) + currentIndex
						* (screenWidth / 4));
			} else if (currentIndex == 2 && position == 2) // 2->3
			{
				lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
						* (screenWidth / 4));
			} else if (currentIndex == 3 && position == 2) // 3->2
			{
				lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / 4) + currentIndex
						* (screenWidth / 4));
			}
			ivCursor.setLayoutParams(lp);
		}

		@Override
		public void onPageSelected(int position) {
			resetView();
			switch (position) {

			case 0:
				tvOrderAll.setTextColor(getResources().getColor(
						color.money_yellow));
				Drawable icon1 = getResources().getDrawable(
						R.drawable.iv_order_all);
				Drawable tintIcon1 = DrawableCompat.wrap(icon1);
				DrawableCompat.setTintList(tintIcon1, getResources()
						.getColorStateList(R.color.money_yellow));
				ivOrderAll.setImageDrawable(tintIcon1);
				break;
			case 1:
				tvToSend.setTextColor(getResources().getColor(
						color.money_yellow));
				Drawable icon2 = getResources().getDrawable(
						R.drawable.iv_deliving_white);
				Drawable tintIcon2 = DrawableCompat.wrap(icon2);
				DrawableCompat.setTintList(tintIcon2, getResources()
						.getColorStateList(R.color.money_yellow));
				ivOrderToSend.setImageDrawable(tintIcon2);
				break;
			case 2:
				tvToSign.setTextColor(getResources().getColor(
						color.money_yellow));
				Drawable icon3 = getResources().getDrawable(
						R.drawable.iv_to_check);
				Drawable tintIcon3 = DrawableCompat.wrap(icon3);
				DrawableCompat.setTintList(tintIcon3, getResources()
						.getColorStateList(R.color.money_yellow));
				ivToSign.setImageDrawable(tintIcon3);
				break;
			case 3:
				tvOrderDone.setTextColor(getResources().getColor(
						color.money_yellow));
				Drawable icon4 = getResources().getDrawable(
						R.drawable.iv_checked_white);
				Drawable tintIcon4 = DrawableCompat.wrap(icon4);
				DrawableCompat.setTintList(tintIcon4, getResources()
						.getColorStateList(R.color.money_yellow));
				ivOrderDone.setImageDrawable(tintIcon4);
				break;
			}
			currentIndex = position;
		}

		
		private void resetView() {

			tvOrderAll.setTextColor(getResources().getColor(color.white));
			tvToSign.setTextColor(getResources().getColor(color.white));
			tvOrderDone.setTextColor(getResources().getColor(color.white));
			tvToSend.setTextColor(getResources().getColor(color.white));

			ivOrderAll.setImageResource(R.drawable.iv_order_all);
			ivOrderToSend.setImageResource(R.drawable.iv_deliving_white);
			ivToSign.setImageResource(R.drawable.iv_to_check);
			ivOrderDone.setImageResource(R.drawable.iv_checked_white);
		}

	}

	private View menu;
	private void IntialPopupWindow() {
		menu = ((GiftOrderActivity) mContext).getLayoutInflater().inflate(
				R.layout.gift_order_time_list, null, false);
		pWindow = new PopupWindow(menu, (int) AppUtil.dpToPixel(mContext, 110),
				(int) AppUtil.dpToPixel(mContext, 185));
		pWindow.setAnimationStyle(R.style.AnimTop);
		pWindow.setFocusable(true);
		pWindow.setOutsideTouchable(false);
		menu.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (pWindow != null && pWindow.isShowing()) {
					pWindow.dismiss();
					//pWindow = null;
				}
				return false;
			}
		});
		menu.findViewById(R.id.panel_today).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_3day).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_7day).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_30day).setOnClickListener(mListener); 
		menu.findViewById(R.id.panel_allday).setOnClickListener(mListener);

	}

	@Override
	public void onBackPressed() {
		tvOrderTime.setFocusable(true);
		tvOrderTime.setFocusableInTouchMode(true);
		if (pWindow != null && pWindow.isShowing()) {
			pWindow.dismiss();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		tvOrderTime.setFocusable(true);
		tvOrderTime.setFocusableInTouchMode(true);
		if (pWindow != null && pWindow.isShowing()) {
			pWindow.dismiss();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	} 
}

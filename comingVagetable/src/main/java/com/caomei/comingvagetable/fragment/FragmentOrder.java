package com.caomei.comingvagetable.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.R.color;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.adapter.FragmentAdapter;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.AppUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


/*订单页面*/
@SuppressLint("ValidFragment")
public class FragmentOrder extends BaseFragment {

	private View view;
	private ViewPager vpOrder;

	private TextView tvOrderAll; 
	private TextView tvToSign;
	private TextView tvToSend;
	private TextView tvOrderDone;
	private ArrayList<BaseFragment> mFragmentList;
	private FragmentAdapter orderFragmentAdapter;
	private CommonListener mListener;
	private Context mContext;
	private ImageView ivCursor;

	private int currentIndex = 0;
	private int screenWidth;
	private ImageView ivOrderAll; 
	private ImageView ivToSign;
	private ImageView ivOrderDone;
	private ImageView ivOrderToSend;

	private LinearLayout panelOrderAll; 
	private LinearLayout panelToSign;
	private LinearLayout panelToSend;
	private LinearLayout panelOrderDone;

	public PopupWindow pWindow;
	/**
	 * 对订单进行时间过滤的标示位：0　全部订单 1　一小时内订单 2　一天内订单 3　一周内订单 4　一月内订单 5　三月内订单
	 */
	public static int orderTimeType = 0;
	private LinearLayout panelOrderTime;
	private TextView tvOrderTime;

	public static ArrayList<String> OrderType = new ArrayList<String>();
	static {
		OrderType.add("ORDER_ALL");
		OrderType.add("ORDER_HOUR");
		OrderType.add("ORDER_DAY");
		OrderType.add("ORDER_WEEK");
		OrderType.add("ORDER_MONTH");
		OrderType.add("ORDER_3_MONTH");
	}

	@SuppressLint("ValidFragment")
	public FragmentOrder(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListener = new CommonListener();
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new FragmentOrdertAll(0)); 
		mFragmentList.add(new FragmentToSend());
		mFragmentList.add(new FragmentToSign());
		mFragmentList.add(new FragmentToEvaluate());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_order, container, false);
		vpOrder = (ViewPager) view.findViewById(R.id.vp_order);

		tvOrderAll = (TextView) view.findViewById(R.id.tv_order_all); 
		tvToSend = (TextView) view.findViewById(R.id.tv_to_send);
		tvToSign = (TextView) view.findViewById(R.id.tv_to_sign);
		tvOrderDone = (TextView) view.findViewById(R.id.tv_order_done);

		ivOrderAll = (ImageView) view.findViewById(R.id.iv_order_all); 
		ivOrderToSend = (ImageView) view.findViewById(R.id.iv_to_send);
		ivToSign = (ImageView) view.findViewById(R.id.iv_to_sign);
		ivOrderDone = (ImageView) view.findViewById(R.id.iv_order_done);

		panelOrderAll = (LinearLayout) view.findViewById(R.id.ll_order_all); 
		panelToSend = (LinearLayout) view.findViewById(R.id.ll_to_send);
		panelToSign = (LinearLayout) view.findViewById(R.id.ll_to_sign);
		panelOrderDone = (LinearLayout) view.findViewById(R.id.ll_order_done);

		panelOrderTime = (LinearLayout) view
				.findViewById(R.id.ll_panel_order_time);
		tvOrderTime=(TextView)view.findViewById(R.id.tv_order_time);
		ivCursor = (ImageView) view.findViewById(R.id.iv_cursor);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		orderFragmentAdapter = new FragmentAdapter(
				getChildFragmentManager(), mFragmentList);
		vpOrder.setAdapter(orderFragmentAdapter);
		vpOrder.addOnPageChangeListener(mListener);
		vpOrder.setOffscreenPageLimit(3);
		panelOrderAll.setOnClickListener(mListener);
		panelOrderDone.setOnClickListener(mListener);
		panelToSend.setOnClickListener(mListener); 
		panelToSign.setOnClickListener(mListener);
		panelOrderTime.setOnClickListener(mListener);
		initTabLineWidth();
	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		((MainActivity) mContext).getWindow().getWindowManager()
				.getDefaultDisplay().getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivCursor
				.getLayoutParams();
		lp.width = screenWidth / 4;
		ivCursor.setLayoutParams(lp);
	}

	private int time_id;
	class CommonListener implements OnPageChangeListener, OnClickListener {

		@Override
		public void onClick(View v) {
			int time = 0;
			switch (v.getId()) {
			case R.id.ll_order_all:
				vpOrder.setCurrentItem(0);
				break; 
			case R.id.ll_to_send:
				vpOrder.setCurrentItem(1);
				break;
			case R.id.ll_to_sign:
				vpOrder.setCurrentItem(2);
				break;
			case R.id.ll_order_done:
				vpOrder.setCurrentItem(3);
				break;
			case R.id.ll_panel_order_time:
				if (pWindow != null && pWindow.isShowing()) {
					pWindow.dismiss();
				} else if(pWindow != null && !pWindow.isShowing()) {
					pWindow.showAsDropDown(
							view.findViewById(R.id.ll_panel_order_time), 0, 50);
				}else{
					IntialPopupWindow();
					pWindow.showAsDropDown(
							view.findViewById(R.id.ll_panel_order_time), 0, 50);
				}
				break;

			case R.id.panel_90day:
				time++;
			case R.id.panel_30day:
				time++;
			case R.id.panel_7day:
				time++;
			case R.id.panel_1day:
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
				}else if(time == 5){
					time_id = R.id.tv_time_str5;
				}else{
					time_id = R.id.tv_time_str6;
				}
				tvOrderTime.setText(((TextView)menu.findViewById(time_id)).getText());
				EventBus.getDefault().post(new EventMsg(OpCodes.GET_ORDER_BY_TIME, orderTimeType));
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int position, float offset, int offsetPixels) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivCursor
					.getLayoutParams();

			Log.e("offset:", offset + "");
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
						color.button_blue));
				ivOrderAll.setImageResource(R.drawable.iv_order_all_green);
				break;
			case 1:
				tvToSend.setTextColor(getResources()
						.getColor(color.button_blue));
				ivOrderToSend.setImageResource(R.drawable.iv_deliving_green);
				break;
			case 2:
				tvToSign.setTextColor(getResources()
						.getColor(color.button_blue));
				ivToSign.setImageResource(R.drawable.iv_order_active);
				break;
			case 3:
				tvOrderDone.setTextColor(getResources().getColor(
						color.button_blue));
				ivOrderDone.setImageResource(R.drawable.iv_checked_green);
				break;
			}
			currentIndex = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	}

	private View menu;
	private void IntialPopupWindow() {
		menu = ((MainActivity) mContext).getLayoutInflater().inflate(
				R.layout.order_time_list, null, false);
		pWindow = new PopupWindow(menu, (int) AppUtil.dpToPixel(mContext, 110),
				(int) AppUtil.dpToPixel(mContext, 220));
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
		menu.findViewById(R.id.panel_1day).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_7day).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_30day).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_90day).setOnClickListener(mListener);
		menu.findViewById(R.id.panel_allday).setOnClickListener(mListener);

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

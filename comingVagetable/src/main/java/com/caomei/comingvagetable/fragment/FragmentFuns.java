package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.activity.GiftOrderActivity;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.adapter.FragmentAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.UserInfoBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class FragmentFuns extends BaseFragment {

	private Context mContext; 
	private TextView tvAllMadun;

	private LinearLayout panelExDetail;
	private TextView tvMadun;
	private TextView tvMoney;
	private CommonListener mListener;

	private int totalMoney;
	private int totalMadun;

	private UserInfoBean ucInfoBbean;
	private LinearLayout panelRules; 
	
	private ArrayList<BaseFragment> mFragmentList;
	private ViewPager viewPage;
	private FragmentAdapter mAdapter;
	private LinearLayout indPanel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		mListener = new CommonListener();
		mContext = getActivity(); 
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new FragmentGiftExchange()); 
		mFragmentList.add(new FragmentActivity());
		mFragmentList.add(new FragmentOffline());
		mFragmentList.add(new FragmentOnline());
		mAdapter=new FragmentAdapter(getChildFragmentManager(), mFragmentList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_funs, container, false);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tvAllMadun = (TextView) mView.findViewById(R.id.tv_madun);  
	 
		panelRules=(LinearLayout)mView.findViewById(R.id.ll_rules);
		panelExDetail = (LinearLayout) mView
				.findViewById(R.id.ll_exchange_order);
		indPanel=(LinearLayout)mView.findViewById(R.id.vp_indicator);
		panelExDetail.setOnClickListener(mListener);  
		for(int i=0;i<4;i++){
			indPanel.getChildAt(i).setOnClickListener(mListener);
		}
		viewPage=(ViewPager)mView.findViewById(R.id.view_pager);
		viewPage.setAdapter(mAdapter);
		viewPage.setOffscreenPageLimit(3);
		viewPage.setOnPageChangeListener(mListener);
		requestMadunData();
	}

	private void requestMadunData() {

		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_USER_INFO, ShareUtil
						.getInstance(mContext).getUserId());
		Log.e("url", "获取用户信息接口： " + url);
		new Thread(new Runnable() {

			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						ucInfoBbean = new Gson().fromJson(bean.getResult(),
								UserInfoBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_USER_INFO_DONE,
										ucInfoBbean));
					} catch (Exception e) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_USER_INFO_ERROR, bean
										.getState()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_USER_INFO_ERROR,
									"请求用户信息出错"));
				}
			}
		}).start();

	}


	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
	
		case OpCodes.GET_GIF_DATA_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.GET_USER_INFO_DONE:
			tvAllMadun.setText(ucInfoBbean.getCredits());
			break;
		case OpCodes.GET_USER_INFO_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	public class CommonListener implements OnClickListener,OnPageChangeListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_exchange_order: 
				((MainActivity) mContext).startNewActivity(
						GiftOrderActivity.class, R.anim.activity_slide_right_in,R.anim.activity_slide_left_out, false, null);
				break;
			case R.id.ll_rules:
				Bundle mBundle=new Bundle();
				((MainActivity) mContext).startNewActivity(
						GiftOrderActivity.class, R.anim.activity_slide_right_in,R.anim.activity_slide_left_out, false, mBundle);
				break;
			case R.id.tv_exchange:
				viewPage.setCurrentItem(0);
				break;
			case R.id.tv_activity:

				viewPage.setCurrentItem(1);
				break;
			case R.id.tv_offline:
				viewPage.setCurrentItem(2);
				
				break;
			case R.id.tv_online:
				viewPage.setCurrentItem(3);
				
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
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			setIndicator(arg0);
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public void setIndicator(int i) {
		resetIndicator();
		((TextView)indPanel.getChildAt(i)).setTextColor(getResources().getColor(R.color.white));
		indPanel.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.money_yellow));
	}

	private void resetIndicator() {
	
		for(int i=0;i<indPanel.getChildCount();i++){
			((TextView)indPanel.getChildAt(i)).setTextColor(getResources().getColor(R.color.money_yellow));
			indPanel.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.gray_bg));
		}
		
	}

}

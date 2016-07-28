package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.activity.GiftInfoActivity;
import com.caomei.comingvagetable.activity.GiftOrderActivity;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.OrderInfoActivity;
import com.caomei.comingvagetable.adapter.GiftAllAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.gift.GiftOrderBean;
import com.caomei.comingvagetable.bean.gift.GiftOrderData;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

@SuppressLint("ValidFragment")
public class FragmentGiftAll extends BaseFragment {
	private View view;
	private ListView lvOrderAll;
	private Context mContext;
	private RelativeLayout panelLoading;
	private GiftOrderBean oBean;
	private ArrayList<GiftOrderData> giftOrderList;
	private GiftAllAdapter orderAdapter;
	private CommonListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		EventBus.getDefault().register(this);
		giftOrderList = new ArrayList<GiftOrderData>();
		mListener = new CommonListener();
		orderAdapter = new GiftAllAdapter(mContext, giftOrderList);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_order_all, container, false);
		panelLoading = (RelativeLayout) view.findViewById(R.id.rl_loading);
		lvOrderAll = (ListView) view.findViewById(R.id.lv_order_all);
		lvOrderAll.setAdapter(orderAdapter);
		lvOrderAll.setOnItemClickListener(mListener);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		requestAllOrderData(GiftOrderActivity.orderTimeType);
	}

	private void requestAllOrderData(int orderTime) {
		panelLoading.setVisibility(View.VISIBLE);
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_GIFT_ORDER_INFO, "0",
						"100000", "0", ShareUtil.getInstance(mContext)
								.getUserId(), MethodUtil.getTimeString(orderTime), "", "", "-1");
		Log.e("url", "礼品所有订单的查询接口：" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						giftOrderList.clear();
						oBean = new Gson().fromJson(bean.getResult(),
								GiftOrderBean.class);
						giftOrderList.addAll(oBean.getData());
						EventBus.getDefault()
								.post(new EventMsg(OpCodes.GET_ALL_ORDER_DONE,
										oBean));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_ALL_ORDER_ERROR,
										"数据格式错误"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_ALL_ORDER_ERROR, bean
									.getState()));
				}

			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_ALL_ORDER_DONE:
			panelLoading.setVisibility(View.GONE);
			orderAdapter.notifyDataSetChanged();
			break;
		case OpCodes.GET_ALL_ORDER_ERROR:
			panelLoading.setVisibility(View.GONE);
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.CANCEL_ORDER_DONE:
		case OpCodes.SUBMIT_SIGN_DONE:
		case OpCodes.EVALUATE_ORDER_DONE:
		case OpCodes.GET_GIFT_ORDER_BY_TIME:
			requestAllOrderData(GiftOrderActivity.orderTimeType);
			break;
		default:
			break;
		}
	}

	public class CommonListener implements OnClickListener, OnItemClickListener {

		@Override
		public void onClick(View v) {

		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			Bundle bundle = new Bundle(); 
			bundle.putString(
					"data",
					String.valueOf(giftOrderList.get(index).getGift_id()
							+ giftOrderList.get(index).getAgent2_id()));
			bundle.putInt("flag", 0);
			((GiftOrderActivity) mContext).startNewActivity(
					GiftInfoActivity.class, R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, false, bundle);
		}

	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

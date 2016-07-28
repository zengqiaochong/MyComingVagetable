package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

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
import com.caomei.comingvagetable.adapter.GiftListAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.gift.GiftOrderBean;
import com.caomei.comingvagetable.bean.gift.GiftOrderData;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class FragmentGiftToSend extends BaseFragment {

	private View view;
	private ListView lvOrder;
	private Context mContext;
	private RelativeLayout panelLoading;

	private GiftOrderBean oBean;
	private ArrayList<GiftOrderData> orderList;
	private GiftListAdapter orderAdapter;
	private CommonListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mListener = new CommonListener();
		orderList = new ArrayList<GiftOrderData>();
		orderAdapter = new GiftListAdapter(mContext, orderList, mListener, 2);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_to_pay, null);
		lvOrder = (ListView) view.findViewById(R.id.lv_to_pay);
		lvOrder.setAdapter(orderAdapter);
		lvOrder.setOnItemClickListener(mListener);
		panelLoading = (RelativeLayout) view.findViewById(R.id.rl_loading);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		requestOrderData(GiftOrderActivity.orderTimeType);
	}

	private void requestOrderData(int index) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_GIFT_ORDER_INFO, "0",
						"100000", "0", ShareUtil.getInstance(mContext)
								.getUserId(),MethodUtil.getTimeString(index),"","", "0");
		Log.e("url", "获取待发货礼品的接口：" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						oBean = new Gson().fromJson(bean.getResult(),
								GiftOrderBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_ORDER_TO_SEND_DONE,
										null));
					} catch (Exception ex) {
						ex.printStackTrace();
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_ORDER_TO_SEND_ERROR,
										bean.getState()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_ORDER_TO_SEND_ERROR,
									"请求出错"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_ORDER_TO_SEND_DONE:
			panelLoading.setVisibility(View.GONE);
			fillData();
			break;
		case OpCodes.GET_ORDER_TO_SEND_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.PAY_ORDER_DONE:
		case OpCodes.GET_GIFT_ORDER_BY_TIME:
			requestOrderData(GiftOrderActivity.orderTimeType);
			break;
		default:
			break;
		}
	}

	private void fillData() {
		orderList.clear();
		orderList.addAll(oBean.getData());
		orderAdapter.notifyDataSetChanged();

	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public class CommonListener implements OnItemClickListener, OnClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long id) {
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("data", orderList.get(index));
			// bundle.putInt("order_id", orderId);
			// bundle.putFloat("price", price);
			// startNewActivity(PaymentActivity.class,
			// R.anim.activity_slide_right_in,
			// R.anim.activity_slide_left_out, false, bundle);
		}

		@Override
		public void onClick(View v) {
			int index;
			switch (v.getId()) {
			case R.id.ll_order_info:
				index = Integer.parseInt(v.getTag().toString());
				Bundle bundle = new Bundle();
				bundle.putInt("flag", 1);
				bundle.putString("data", String.valueOf(orderList.get(index).getGift_id()+orderList.get(index).getAgent2_id()));
				((GiftOrderActivity) mContext).startNewActivity(
						GiftInfoActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, bundle);
				break;
			case R.id.bt_notify:
				Toast.makeText(mContext, "已提醒卖家发货", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		}
	}
}

package com.caomei.comingvagetable.fragment;

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

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.OrderInfoActivity;
import com.caomei.comingvagetable.adapter.OrderAllAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.order.OrderData;
import com.caomei.comingvagetable.bean.order.OrderToPayBean;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
/*
* 全部订单
* */
@SuppressLint("ValidFragment")
public class FragmentOrdertAll extends BaseFragment {
	private View view;
	private ListView lvOrderAll;
	private Context mContext;
	private RelativeLayout panelLoading;
	private OrderToPayBean oBean;
	private ArrayList<OrderData> orderList;
	private OrderAllAdapter orderAdapter;
	private CommonListener mListener;
	private int flag = 0;

	public FragmentOrdertAll(int Flag) {
		/**
		 * flag的取值及含义： 0 标示所有菜品订单 1 标示所有礼品订单
		 */
		this.flag = Flag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		EventBus.getDefault().register(this);
		orderList = new ArrayList<OrderData>();
		mListener = new CommonListener();
		orderAdapter = new OrderAllAdapter(mContext, orderList, mListener);

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
		requestAllOrderData(FragmentOrder.orderTimeType);
	}

	private void requestAllOrderData(int timeType) {
		panelLoading.setVisibility(View.VISIBLE);
		String temUrl = null;
		String caiUrl = null;
		if (flag == 0) {
			temUrl = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_ORDER_INFO, "0", "100000", "0",FragmentOrder.OrderType.get(timeType),
					ShareUtil.getInstance(mContext).getUserId(), "-1");
			Log.e("url","菜品所有订单的查询接口："+temUrl);

			caiUrl = CommonAPI.BASE_URL + String.format(CommonAPI.URL_ORDER_LIST,
					ShareUtil.getInstance(mContext).getUserId(),0,FragmentOrder.OrderType.get(timeType),-1,0,10000);
			Log.e("url","外卖所有订单的查询接口："+caiUrl);


		}
		if (flag == 1) {
			temUrl = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_GIFT_ORDER_INFO, "0",
							"100000", "0", ShareUtil.getInstance(mContext)
									.getUserId(),"2000-01-01","","","-1");
			Log.e("url","礼品所有订单的查询接口："+temUrl);
		} 
		final String url=temUrl;
		final String urlwai=caiUrl;

		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						Log.e("url","查询用户菜来了 订单接口："+bean.getResult());
						orderList.clear();
						oBean = new Gson().fromJson(bean.getResult(), OrderToPayBean.class);
						orderList.addAll(oBean.getData());
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_ALL_ORDER_DONE, oBean));
					} catch (Exception ex) {
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_ALL_ORDER_ERROR, "数据格式错误"));
					}
				} else {
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_ALL_ORDER_ERROR, bean.getState()));
				}

			}
		}).start();
  /*查询用户外卖，商城 订单接口*/
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext).getDataFromNetByGet(urlwai);
				if (bean.getState() == AccessNetState.Success) {
					try {
					Log.e("url","查询用户外卖，商城 订单接口："+bean.getResult());
					OrderToPayBean 	oBean = new Gson().fromJson(bean.getResult(), OrderToPayBean.class);
				   	orderList.addAll(oBean.getData());
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_ALL_ORDER_DONE,oBean));
					} catch (Exception ex) {
						ex.getStackTrace();
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_ALL_ORDER_ERROR, "数据格式错误"));
					}
				} else {
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_ALL_ORDER_ERROR, bean.getState()));
				}

			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
			/*获取订单成功*/
		case OpCodes.GET_ALL_ORDER_DONE:
			panelLoading.setVisibility(View.GONE);
			orderAdapter.notifyDataSetChanged();
			break;
		case OpCodes.GET_ALL_ORDER_ERROR:
			panelLoading.setVisibility(View.GONE);
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.PAY_ORDER_DONE:
		case OpCodes.CANCEL_ORDER_DONE: 
		case OpCodes.SUBMIT_SIGN_DONE:
		case OpCodes.EVALUATE_ORDER_DONE:
		case OpCodes.PURSE_CHARGE_DONE:
		case OpCodes.GET_ORDER_BY_TIME:
		case OpCodes.SUBMIT_ORDER_DONE:
			
			requestAllOrderData(FragmentOrder.orderTimeType);
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

			bundle.putSerializable("data", orderList.get(index));
			if(orderList.get(index).getStatus().equals("已下单_未付款")){
				bundle.putInt("flag", 1);
			}else if(orderList.get(index).getStatus().equals("已付款_未发货")){
				bundle.putInt("flag", 2);
			}else if(orderList.get(index).getStatus().equals("已到达_未签收")){
				bundle.putInt("flag", 3);
			}else if(orderList.get(index).getStatus().equals("已签收_未评价")){
				bundle.putInt("flag", 4);
			}else{
				bundle.putInt("flag", 0);
			}
			((MainActivity) mContext).startNewActivity(OrderInfoActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, false, bundle);
		}

	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.OrderInfoActivity;
import com.caomei.comingvagetable.activity.PaymentActivity;
import com.caomei.comingvagetable.adapter.OrderListAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.order.OrderData;
import com.caomei.comingvagetable.bean.order.OrderToPayBean;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.wxapi.WXPayEntryActivity;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class FragmentToPay extends BaseFragment {

	private View view;
	private ListView lvOrder;
	private Context mContext;
	private RelativeLayout panelLoading;

	private OrderToPayBean oBean;
	private ArrayList<OrderData> orderList;
	private OrderListAdapter orderAdapter;
	private CommonListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mListener = new CommonListener();
		orderList = new ArrayList<OrderData>();
		orderAdapter = new OrderListAdapter(mContext, orderList, mListener, 1);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_to_pay, null);
		lvOrder = (ListView) view.findViewById(R.id.lv_to_pay);
		lvOrder.setAdapter(orderAdapter);

		panelLoading = (RelativeLayout) view.findViewById(R.id.rl_loading);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		requestOrderData(FragmentOrder.orderTimeType);
	}

	private void requestOrderData(int orderType) {
		panelLoading.setVisibility(View.VISIBLE);
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_ORDER_INFO, "0", "100000",
						"0", FragmentOrder.OrderType.get(orderType), ShareUtil
								.getInstance(mContext).getUserId(), "0");
		Log.e("url", "获取待付款订单的接口：" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						oBean = new Gson().fromJson(bean.getResult(),
								OrderToPayBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_ORDER_TO_PAY_DONE,
										null));
					} catch (Exception ex) {
						ex.printStackTrace();
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_ORDER_TO_PAY_ERROR,
										bean.getState()));
					}
				} else {
					EventBus.getDefault()
							.post(new EventMsg(OpCodes.GET_ORDER_TO_PAY_ERROR,
									"请求出错"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_ORDER_TO_PAY_DONE:
			panelLoading.setVisibility(View.GONE);
			fillData();
			break;
		case OpCodes.GET_ORDER_TO_PAY_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.CANCEL_ORDER_DONE:
		case OpCodes.PAY_ORDER_DONE:
		case OpCodes.PURSE_CHARGE_DONE:
		case OpCodes.GET_ORDER_BY_TIME:
		case OpCodes.SUBMIT_ORDER_DONE:

			requestOrderData(FragmentOrder.orderTimeType);
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

	public class CommonListener implements OnClickListener {

		@Override
		public void onClick(final View v) {
			int index;
			switch (v.getId()) {
			case R.id.ll_order_info:
				index = Integer.parseInt(v.getTag().toString());
				Bundle bundle = new Bundle();
				bundle.putInt("flag", 1);
				bundle.putSerializable("data", orderList.get(index));
				((MainActivity) mContext).startNewActivity(
						OrderInfoActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, bundle);
				break;
			case R.id.bt_pay:
				DialogUtil.DefaultDialog(mContext, "提示", "确认付款吗？", "付款", "取消", new IDialogOperation() {
					
					@Override
					public void onPositive() {
						int index = Integer.parseInt(v.getTag().toString());

						Bundle mBundle = new Bundle();
						mBundle.putString("order_id", orderList.get(index)
								.getOrder_id());
						mBundle.putFloat("price", orderList.get(index).getTotalMoney());

						((MainActivity) mContext).startNewActivity(
								PaymentActivity.class, R.anim.activity_slide_right_in,
								R.anim.activity_slide_left_out, false, mBundle);
					}
					
					@Override
					public void onNegative() {
						// TODO Auto-generated method stub
						
					}
				});
			
				break;

			default:
				break;
			}

		}
	}

	public void requestOrderInfo(String id) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_ORDER_INFO_BY_ID, id,
						ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "获取指定order_id的订单信息：" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {

				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_ORDER_INFO_BY_ID_ERROR,
									bean.getState()));
				}

			}
		}).start();

	}
}

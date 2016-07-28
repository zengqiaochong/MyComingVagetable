package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

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
import com.caomei.comingvagetable.activity.LogisticActivity;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.OrderInfoActivity;
import com.caomei.comingvagetable.adapter.SignListAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.order.OrderData;
import com.caomei.comingvagetable.bean.order.OrderToPayBean;
import com.caomei.comingvagetable.bean.order.SignData;
import com.caomei.comingvagetable.bean.order.SignList;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class FragmentToSign extends BaseFragment {

	private View view;
	private ArrayList<OrderData> signList;
	private ListView lvSign;
	private SignListAdapter mAdapter;
	private RelativeLayout rlLoading;
	private CommonListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		super.onCreate(savedInstanceState);
		signList = new ArrayList<OrderData>();
		mListener = new CommonListener();
		mAdapter = new SignListAdapter(mContext, signList, mListener);
		mAdapter.notifyDataSetChanged();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_to_sign, null);
		lvSign = (ListView) view.findViewById(R.id.lv_sign);
		rlLoading = (RelativeLayout) view.findViewById(R.id.rl_loading);
		lvSign.setAdapter(mAdapter);
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestSignData(FragmentOrder.orderTimeType);
		super.onViewCreated(view, savedInstanceState);
	}

	private void requestSignData(int orderType) {
		final String urlArrival = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_ORDER_INFO, 0, 100000, 0,
						FragmentOrder.OrderType.get(orderType), ShareUtil
								.getInstance(mContext).getUserId(), "3");
		final String urlSend = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_ORDER_INFO, 0, 1000, 0,
						FragmentOrder.OrderType.get(orderType), ShareUtil
								.getInstance(mContext).getUserId(), "2");
		Log.e("url", "获取已发货未到达列表：" + urlSend);
		Log.e("url", "获取已到达未签收列表：" + urlArrival);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean sendBean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(urlSend);
				AccessNetResultBean arrivalBean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(urlArrival);

				if (sendBean.getState() == AccessNetState.Success
						&& arrivalBean.getState() == AccessNetState.Success) {
					try {
						signList.clear();
						signList.addAll((new Gson().fromJson(
								sendBean.getResult(), OrderToPayBean.class))
								.getData());
						signList.addAll((new Gson().fromJson(
								arrivalBean.getResult(), OrderToPayBean.class))
								.getData());

						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_SIGN_LIST_DONE,
										signList));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_SIGN_LIST_ERROR,
										"解析数据出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_SIGN_LIST_ERROR, sendBean
									.getState()));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_SIGN_LIST_DONE:
			rlLoading.setVisibility(View.GONE);
			mAdapter.notifyDataSetChanged();
			break;
		case OpCodes.GET_SIGN_LIST_ERROR:
			rlLoading.setVisibility(View.GONE);
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.SUBMIT_SIGN_DONE:
		case OpCodes.EVALUATE_ORDER_DONE:
		case OpCodes.GET_ORDER_BY_TIME:
			requestSignData(FragmentOrder.orderTimeType);
			break;
		case OpCodes.SUBMIT_SIGN_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	public class CommonListener implements OnClickListener {

		@Override
		public void onClick(final View v) {

			int index;
			switch (v.getId()) {
			case R.id.bt_sign:
				DialogUtil.DefaultDialog(mContext, "提示", "确认签收？", "签收", "取消",
						new IDialogOperation() {

							@Override
							public void onPositive() {
								requestSign(v.getTag().toString());
							}

							@Override
							public void onNegative() {
								// TODO Auto-generated method stub

							}
						});

				break;
			case R.id.bt_logi:
				Bundle mBundle = new Bundle();
				index = Integer.parseInt(v.getTag().toString());
				mBundle.putSerializable("data", signList.get(index));
				((MainActivity) mContext).startNewActivity(
						LogisticActivity.class, R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, mBundle);
				break;
			case R.id.ll_order_info:
				index = Integer.parseInt(v.getTag().toString());
				Bundle bundle = new Bundle();
				bundle.putInt("flag", 3);
				bundle.putSerializable("data", signList.get(index));
				((MainActivity) mContext).startNewActivity(
						OrderInfoActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, bundle);
				break;
			default:
				break;
			}
		}
	}

	public void requestSign(String id) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_UPDATE_ORDER_STATUS, id, "4",
						ShareUtil.getInstance(mContext).getUserId());
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						TypeMsgBean tBean = new Gson().fromJson(
								bean.getResult(), TypeMsgBean.class);
						if (tBean.getRESULT_TYPE() == 1) {
							EventBus.getDefault().post(
									new EventMsg(OpCodes.SUBMIT_SIGN_DONE,
											"签收成功"));
						} else {
							EventBus.getDefault().post(
									new EventMsg(OpCodes.SUBMIT_SIGN_ERROR,
											tBean.getRESULT_MSG()));
						}
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.SUBMIT_SIGN_ERROR,
										"系统错误：返回结果格式有误"));
					}

				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.SUBMIT_SIGN_DONE, bean
									.getState()));
				}
			}
		}).start();

	}

	public void requestLogistic(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

package com.caomei.comingvagetable.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.adapter.LogisticAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.LogisticBean;
import com.caomei.comingvagetable.bean.LogisticData;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.gift.GiftInfoBean;
import com.caomei.comingvagetable.bean.gift.GiftOrderData;
import com.caomei.comingvagetable.bean.order.OrderData;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class LogisticGiftActivity extends BaseActivity {

	private GiftOrderData orderData;
	private ListView lvLogistic;
	private LogisticAdapter mAdapter;
	private LogisticBean logisticBean;
	private ArrayList<LogisticData> dataSet;
	private ImageView ivBack;
	private String orderId;
	private TextView tvOrderNo;
	private TextView tvOrderStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistic);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		ivBack = (ImageView) findViewById(R.id.iv_back);
		lvLogistic = (ListView) findViewById(R.id.lv_logistic);
		tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
		tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);

	}

	private void initData() {
		dataSet = new ArrayList<LogisticData>();
		orderData = (GiftOrderData)getIntent().getSerializableExtra("data");
		mAdapter = new LogisticAdapter(mContext, dataSet);
		orderId =String.valueOf(orderData.getId());
		lvLogistic.setAdapter(mAdapter);
		requestOrderLogistic();
	}

	private void requestOrderLogistic() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.GET_DELIVER_INFO_BY_ORDER_ID, "0",
						"100000000", "0", orderId,
						ShareUtil.getInstance(mContext).getUserId(), "1", "",
						"");
		Log.e("url", "获取礼品订单物流信息的接口  " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					Log.e("data", "获取订单物流信息 " + bean.getResult());
					try {
						logisticBean = new Gson().fromJson(bean.getResult(),
								LogisticBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_LOGISTIC_INFO_DONE,
										null));
					} catch (Exception e) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_LOGISTIC_INFO_ERROR,
										"物流信息解析出错"));

					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_LOGISTIC_INFO_ERROR, bean
									.getResult()));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_LOGISTIC_INFO_DONE:
			dataSet.clear();
			// dataSet.addAll(logisticBean.getData());
			for (int i = logisticBean.getData().size() - 1; i >= 0; i--) {
				dataSet.add(logisticBean.getData().get(i));
			}
			mAdapter.notifyDataSetChanged();
			tvOrderNo.setText(orderData.getGiftOrderNO());
			tvOrderStatus.setText(orderData.getStatus());
			break;

		case OpCodes.GET_LOGISTIC_INFO_ERROR:

			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

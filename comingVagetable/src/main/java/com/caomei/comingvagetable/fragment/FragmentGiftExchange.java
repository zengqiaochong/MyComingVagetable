package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.activity.GiftInfoActivity;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.adapter.GiftExchangeAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.gift.GiftBean;
import com.caomei.comingvagetable.bean.gift.GiftData;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class FragmentGiftExchange extends BaseFragment {

	private View view;
	private GridView gridView;
	private RelativeLayout llLoading;
	private GiftBean giftBean;
	private ArrayList<GiftData> dataSets;
	private GiftExchangeAdapter mAdapter;
	private CommonListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		mListener=new CommonListener();
		dataSets = new ArrayList<GiftData>();
		mAdapter = new GiftExchangeAdapter(mContext, dataSets);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_gift_exchange, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		gridView = (GridView) view.findViewById(R.id.gv_gift);
		llLoading = (RelativeLayout) view.findViewById(R.id.rl_loading);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(mListener);
		requestGiftData();
	}

	private void requestGiftData() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_GIFT, "0", "1000000", "0",
						ShareUtil.getInstance(mContext).getUserId(), "-1",
						"2000-01-01", "", "", ShareUtil.getInstance(mContext)
								.getHomeCommunityID());
		Log.e("url", "获取礼品的接口  " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						giftBean = new Gson().fromJson(bean.getResult(),
								GiftBean.class);

						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_GIF_DATA_DONE,
										giftBean));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_GIF_DATA_ERROR,
										"数据格式出错" + ex.getLocalizedMessage()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_GIF_DATA_ERROR, bean
									.getState()));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_GIF_DATA_DONE:
			Log.e("data", "收到完成数据的消息");
			dataSets.clear();
			dataSets.addAll(giftBean.getData());
			mAdapter.notifyDataSetChanged();
			llLoading.setVisibility(View.GONE);
			break;
		case OpCodes.UPDATE_GIFT_ORDER_DONE:
			requestGiftData();
			break;
		}
	}

	class CommonListener implements OnItemClickListener{
		  @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int index,
	                long arg3) {
	            Bundle b = new Bundle();
	            b.putString("data", dataSets.get(index).getId());
	            ((MainActivity) mContext).startNewActivity(GiftInfoActivity.class,
	                    R.anim.activity_slide_right_in,
	                    R.anim.activity_slide_left_out, false, b);
	        }
	}
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}

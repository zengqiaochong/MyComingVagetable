package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.OrderInfoActivity;
import com.caomei.comingvagetable.adapter.OrderListAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.order.OrderData;
import com.caomei.comingvagetable.bean.order.OrderToPayBean; 
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentToEvaluate extends BaseFragment{

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
		mContext=getActivity();
		mListener=new CommonListener();
		orderList=new ArrayList<OrderData>();
		orderAdapter=new OrderListAdapter(mContext, orderList,mListener,4);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view=inflater.inflate(R.layout.fragment_to_pay, null);
		lvOrder=(ListView)view.findViewById(R.id.lv_to_pay);
		lvOrder.setAdapter(orderAdapter);
		lvOrder.setOnItemClickListener(mListener);
		panelLoading=(RelativeLayout)view.findViewById(R.id.rl_loading);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		requestOrderData(FragmentOrder.orderTimeType);
	}
	
	private void requestOrderData(int orderType) {
		final String url=CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_ORDER_INFO, "0","100000","0",FragmentOrder.OrderType.get(orderType),ShareUtil.getInstance(mContext).getUserId(),"4");
		Log.e("url", "获取待评价的接口："+url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean=NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if(bean.getState()==AccessNetState.Success){
					try{
						oBean=new Gson().fromJson(bean.getResult(), OrderToPayBean.class);
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_TO_EVALUATE_LIST_DONE, null));
					}catch(Exception ex){
						ex.printStackTrace();
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_TO_EVALUATE_LIST_ERROR, bean.getState()));
					}
				}else{
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_TO_EVALUATE_LIST_ERROR, "请求出错"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg){
		switch (msg.getFlag()) {
		case OpCodes.GET_TO_EVALUATE_LIST_DONE: 
			panelLoading.setVisibility(View.GONE);
			fillData();
			break;
		case OpCodes.GET_TO_EVALUATE_LIST_ERROR:
			Toast.makeText(mContext, msg.getData().toString(), Toast.LENGTH_SHORT).show();
			break;
			//刷新数据
		case OpCodes.SUBMIT_SIGN_DONE:
		case OpCodes.EVALUATE_ORDER_DONE:
		case OpCodes.GET_ORDER_BY_TIME:
			requestOrderData(FragmentOrder.orderTimeType);			
			break;
		default:
			break;
		}
	}
	
	private void fillData(){
		orderList.clear();
		orderList.addAll(oBean.getData());
		orderAdapter.notifyDataSetChanged();
	}
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public class CommonListener implements OnItemClickListener,OnClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View vv, int index,
				long id) {			
		}

		@Override
		public void onClick(View v) {
			
			Bundle bundle = new Bundle();
			bundle.putInt("flag",4);
			bundle.putSerializable("data", orderList.get(Integer.parseInt(v.getTag().toString())));
			((MainActivity)mContext).startNewActivity(OrderInfoActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, false, bundle);
		} 
	}
}

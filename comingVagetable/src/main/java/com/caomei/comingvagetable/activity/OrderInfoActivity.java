package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.adapter.OrderInfoAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.order.OrderData;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.caomei.comingvagetable.wxapi.WXPayEntryActivity;
import com.google.gson.Gson;

import dalvik.bytecode.Opcodes;
import de.greenrobot.event.EventBus;

public class OrderInfoActivity extends BaseActivity{
	private ListView lvOrderInfo;
	private ImageView ivBack;
	private OrderInfoAdapter mAdapter;
	private CommonListener mListener;
	private OrderData orderInfo;
	private TextView tvUserName;
	private TextView tvAddress;
	private TextView tvWay;
	private Button btAction1;
	/**
	 * 不同的flag值标示 来来自不同的fragment
	 */
	private int flag;
	private Button btAction2;
	private RatingBar rbMaijia;
	private RatingBar rbFuwu;
	private RatingBar rbCaipin;
	private LinearLayout llEva;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_order_info);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		tvWay = (TextView) findViewById(R.id.tv_way);
		tvUserName=(TextView)findViewById(R.id.tv_user_name);
		tvAddress=(TextView)findViewById(R.id.tv_address);
		lvOrderInfo=(ListView)findViewById(R.id.lv_info);
		ivBack=(ImageView)findViewById(R.id.iv_back);
		btAction1=(Button)findViewById(R.id.bt_action1);
		btAction2=(Button)findViewById(R.id.bt_action2);
		rbMaijia=(RatingBar)findViewById(R.id.rb_eval_maijia);
		rbFuwu=(RatingBar)findViewById(R.id.rb_eval_fuwu);
		rbCaipin=(RatingBar)findViewById(R.id.rb_eval_caiping);
		llEva=(LinearLayout)findViewById(R.id.ll_eva);
	}

	private void initData() {
		mContext =this;
		orderInfo=(OrderData)getIntent().getSerializableExtra("data");
		flag=getIntent().getIntExtra("flag", -1);
		mListener=new CommonListener();
		ivBack.setOnClickListener(mListener); 
		mAdapter=new OrderInfoAdapter(mContext, orderInfo.getVegeInfo());
		lvOrderInfo.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		
		tvUserName.setText("收件人：" + (orderInfo.getAddress().getUsername() == null ? orderInfo.getAddress().getGuestname() : orderInfo.getAddress().getUsername()) + "  联系电话：" + orderInfo.getAddress().getPhone());
		tvAddress.setText("配送地址：" + orderInfo.getAddress().getSmallAddress());
		String way = "";
		if(orderInfo.deliverType == 0){
			way = "配送方式：门口取件(另需：8元配送费)";
		}else if(orderInfo.deliverType == 1){
			way = "配送方式：送货上门(另需：7元配送费)";
		}else if(orderInfo.deliverType == 2){
			way = "配送方式：小区内取货(另需：5元配送费)";
		}
		if("".equals(way)){
			tvWay.setVisibility(View.GONE);
		}else{
			tvWay.setText(way);
		}
		initButtonByFlag();
	}
	
	public class CommonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_back:
				onBackPressed();
				break; 
			default:
				break;
			}
		}
	}

	public void initButtonByFlag() {
		switch (flag) {
		case -1:
			Toast.makeText(getApplicationContext(), "位置错误", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case 0:
			btAction1.setVisibility(View.GONE);
			btAction2.setVisibility(View.GONE);
			break;
		case 1:
			btAction1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					 cancelOrderRequest();
				}
			});
			btAction2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Bundle mBundle = new Bundle();
					mBundle.putString("order_id", orderInfo
							.getOrder_id());
					mBundle.putFloat("price",
							orderInfo.getTotalMoney());
					((OrderInfoActivity) mContext).startNewActivity(
							PaymentActivity.class, R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, mBundle);
				}
			}); 
			break;
		case 2:
			btAction1.setText("提醒发货");
			btAction2.setVisibility(View.GONE); 
			btAction1.setOnClickListener(new OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext,"已提醒卖家发货",Toast.LENGTH_SHORT).show();
				}
			});
			break;
			
		case 3:
			btAction1.setText("物流信息");
			btAction2.setText("签收");
			btAction1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle mBundle=new Bundle();
					mBundle.putSerializable("data",orderInfo);
					((OrderInfoActivity) mContext).startNewActivity(LogisticActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, mBundle); 
				}
			});
			btAction2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					requestSign(orderInfo.getOrder_id());
				}
			});
			break;
		case 4:
			btAction1.setText("评价");
			llEva.setVisibility(View.VISIBLE);
			btAction2.setVisibility(View.GONE);
			btAction1.setOnClickListener(new OnClickListener() { 

				@Override
				public void onClick(View arg0) { 
					requestEvaluate(orderInfo.getOrder_id(),rbMaijia.getRating(),rbFuwu.getRating(),rbCaipin.getRating());
				}
			});
			break;
		default:
			break;
		} 
	}

	protected void requestEvaluate(String id,float rating, float rating2, float rating3) {
		 final String url=CommonAPI.BASE_URL+String.format(CommonAPI.URL_EVEL_ORDER,id, ""+(int)rating+";"+(int)rating2+";"+(int)rating3,"a;a;a",ShareUtil.getInstance(mContext).getUserId()) ;
		 Log.e("url", "评价订单的url "+ url);
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				AccessNetResultBean bean=NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if(bean.getState()==AccessNetState.Success){
					try{
						TypeMsgBean tBean=new Gson().fromJson(bean.getResult(), TypeMsgBean.class);
						if(tBean.getRESULT_TYPE()==1){
							EventBus.getDefault().post(new EventMsg(OpCodes.EVALUATE_ORDER_DONE, tBean.getRESULT_MSG()));
						}else{
							EventBus.getDefault().post(new EventMsg(OpCodes.EVALUATE_ORDER_ERROR, tBean.getRESULT_MSG()));
						}
					}catch(Exception ex){
						EventBus.getDefault().post(new EventMsg(OpCodes.EVALUATE_ORDER_ERROR, "数据解析出错"));
					}
				}else{
					EventBus.getDefault().post(new EventMsg(OpCodes.EVALUATE_ORDER_ERROR, bean.getResult()));
				}
			}
		}).start();
		
	}

	public void requestSign(String id) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_UPDATE_ORDER_STATUS, id, "4",
						ShareUtil.getInstance(mContext).getUserId());
		Log.e("url","菜品签收接口Info："+url);
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

	private void cancelOrderRequest() {
		final String url=CommonAPI.BASE_URL+String.format(CommonAPI.URL_CANCEL_ORDER, orderInfo.getOrder_id(),"无",ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "取消订单的接口："+url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean=NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if(bean.getState()==AccessNetState.Success){
					try{
						TypeMsgBean tBean=new Gson().fromJson(bean.getResult(), TypeMsgBean.class);
						if(tBean.getRESULT_TYPE()==1){
							EventBus.getDefault().post(new EventMsg(OpCodes.CANCEL_ORDER_DONE, tBean.getRESULT_MSG()));
						}else{
							EventBus.getDefault().post(new EventMsg(OpCodes.CANCEL_ORDER_ERROR, tBean.getRESULT_MSG()));
						}
						
					}catch(Exception ex){
						EventBus.getDefault().post(new EventMsg(OpCodes.CANCEL_ORDER_ERROR, "返回结果处理出错"));
					}
				}else{
					EventBus.getDefault().post(new EventMsg(OpCodes.CANCEL_ORDER_ERROR, bean.getState()));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg){
		switch (msg.getFlag()) {
		case OpCodes.CANCEL_ORDER_DONE:
			Toast.makeText(mContext, msg.getData().toString(),Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					onBackPressed();
				}
			},500);
			break;
		case OpCodes.CANCEL_ORDER_ERROR:
			Toast.makeText(mContext, msg.getData().toString(), Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.EVALUATE_ORDER_ERROR: 
		case OpCodes.EVALUATE_ORDER_DONE: 
			Toast.makeText(mContext,msg.getData().toString(),Toast.LENGTH_SHORT).show();
			break;
			
		case OpCodes.SUBMIT_SIGN_DONE:
			ToastUtil.Show(mContext, "签收成功");
			OrderInfoActivity.this.finish();
			//requestEvaluate(orderInfo.getOrder_id(),rbMaijia.getRating(),rbFuwu.getRating(),rbCaipin.getRating());
			
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


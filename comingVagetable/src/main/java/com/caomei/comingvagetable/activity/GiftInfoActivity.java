package com.caomei.comingvagetable.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.gift.GiftInfoBean;
import com.caomei.comingvagetable.bean.gift.GiftOrderData;
import com.caomei.comingvagetable.util.AppUtil;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.ImageUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.view.CircleButton;
import com.google.gson.Gson;
import com.minking.imagecycleview.ImageCycleView;
import com.minking.imagecycleview.PicSlideInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

public class GiftInfoActivity extends BaseActivity {

	private GiftInfoBean giftInfoBean;
	private String giftId;

	private ImageView ivBack;

	private ImageCycleView ivGiftPic;

	private TextView tvgiftName;
	private TextView tvMadun;
	private TextView tvPriceItem;

	private CircleButton ivMinus;
	private CircleButton btPlus;
	private TextView tvBuyCount;

	private TextView tvGiftUnit;
	private TextView tvMinLevel;

	private TextView tvTotalAmount;
	private TextView tvSoldAmount;

	private TextView tvGiftDetails;

	private TextView tvMadunToatl;
	private TextView tvMoneyTotal;
	private Button btExchange;

	private GiftOrderData item;
	private Context mContext;
	private CommonListener mListener;
	private ProgressDialog pDialog;
	private ArrayList<PicSlideInfo> infos;
	private int flag=0;
	private LinearLayout panelAmount;
	private LinearLayout panelJifen;
	private Button btAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_gift_info);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivMinus = (CircleButton) findViewById(R.id.iv_count_minus);
		btPlus = (CircleButton) findViewById(R.id.iv_count_plus);
		tvBuyCount = (TextView) findViewById(R.id.tv_count_buy);
		panelAmount = (LinearLayout)findViewById(R.id.ll_price_count);

		panelJifen = (LinearLayout) findViewById(R.id.ll_jifen_info);
		ivGiftPic = (ImageCycleView) findViewById(R.id.iv_gift_pics);
		initLayoutParam(ivGiftPic);
		tvgiftName = (TextView) findViewById(R.id.tv_gift_name);
		tvGiftUnit = (TextView) findViewById(R.id.tv_gift_unit);
		tvMinLevel = (TextView) findViewById(R.id.tv_onsell_time);
		tvPriceItem = (TextView) findViewById(R.id.tv_price_item);

		tvTotalAmount = (TextView) findViewById(R.id.tv_gift_total_count);
		tvSoldAmount = (TextView) findViewById(R.id.tv_gift_sold_count);
		tvGiftDetails = (TextView) findViewById(R.id.tv_gift_detail);
		tvMadun = (TextView) findViewById(R.id.tv_madun);

		tvMadunToatl = (TextView) findViewById(R.id.tv_madun_total);
		tvMoneyTotal = (TextView) findViewById(R.id.tv_money_total);
		btExchange = (Button) findViewById(R.id.bt_exchange);
		btAction = (Button) findViewById(R.id.bt_action_2);
	}
	private void initLayoutParam(ImageCycleView mCycleViewPics) {
		ViewGroup.LayoutParams params = mCycleViewPics.getLayoutParams();
		Log.e("data", "屏幕宽度： " + params.width);
		params.height=AppUtil.getScreenWidth(this);
		mCycleViewPics.setLayoutParams(params);
	}
	private void initData() {
		infos = new ArrayList<PicSlideInfo>();
		item=(GiftOrderData)getIntent().getSerializableExtra("item");
		giftId = getIntent().getStringExtra("data");
		flag = getIntent().getIntExtra("flag", -1);
		mListener = new CommonListener();
		ivBack.setOnClickListener(mListener);
		ivMinus.setOnClickListener(mListener);
		btPlus.setOnClickListener(mListener);
		btExchange.setOnClickListener(mListener);
		btAction.setOnClickListener(mListener);
		switch (flag) {
		case 0:
			// 从所有订单中进入
			// 加减数量的空间
			panelAmount.setVisibility(View.GONE);
			findViewById(R.id.rl_bottom).setVisibility(View.GONE);
			break;
		case 1:
			// 从待发货礼品中进入
			panelAmount.setVisibility(View.GONE);
			panelJifen.setVisibility(View.GONE);
			btExchange.setText("提醒发货");

			break;
		case 2:
			panelAmount.setVisibility(View.GONE);
			panelJifen.setVisibility(View.GONE);
			btExchange.setText("签收");
			btAction.setText("查看物流");
			btAction.setVisibility(View.VISIBLE);
			break;
		case 3:
			panelAmount.setVisibility(View.GONE);
			panelJifen.setVisibility(View.GONE);
			btExchange.setText("评价");
			break;
		 
		default:
			break;
		}

		requestGiftInfo();
	}

	private void requestGiftInfo() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_GIFT_INFO, giftId, ShareUtil
						.getInstance(mContext).getUserId());
		Log.e("url", "获取礼品信息的url " + url);
		new Thread(new Runnable() {

			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						giftInfoBean = new Gson().fromJson(bean.getResult(),
								GiftInfoBean.class);

						EventBus.getDefault().post(
								new EventMsg(
										OpCodes.PRODUCT_GIFT_INFO_GET_SUCCESS,
										giftInfoBean));
					} catch (Exception e) {
						EventBus.getDefault().post(
								new EventMsg(
										OpCodes.PRODUCT_GIFT_INFO_GET_FAILED,
										"获取礼品数据出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.PRODUCT_GIFT_INFO_GET_FAILED,
									bean.getState()));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.PRODUCT_GIFT_INFO_GET_FAILED:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.PRODUCT_GIFT_INFO_GET_SUCCESS:
			fillViews();

			break;
		case OpCodes.UPDATE_GIFT_ORDER_DONE:
			onBackPressed();
			Toast.makeText(getApplicationContext(), msg.getData().toString(),
					Toast.LENGTH_SHORT).show();

			break;
		case OpCodes.UPDATE_GIFT_ORDER_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.SUBMIT_SIGN_DONE:
			Toast.makeText(mContext,msg.getData().toString(),Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.SUBMIT_SIGN_ERROR:
			Toast.makeText(mContext,msg.getData().toString(),Toast.LENGTH_SHORT).show();			
			break;
		default:
			break;
		}
	}

	private void fillViews() {
		tvgiftName.setText("礼品："+giftInfoBean.getGiftName());
		tvGiftUnit.setText("单位：" + giftInfoBean.getUnit());
		tvMadun.setText(giftInfoBean.getNeedCredit() + "马盾");
		tvMinLevel.setText("最低等级:" + giftInfoBean.getMinLevelName());
		tvPriceItem.setText("¥" + giftInfoBean.getPrice());
		tvPriceItem.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		tvTotalAmount.setText("礼品总量：" + String.valueOf(giftInfoBean.getTotal())
				+ giftInfoBean.getUnit());
		tvSoldAmount.setText("已兑换：" + String.valueOf(giftInfoBean.getSoldNum())
				+ giftInfoBean.getUnit());
		tvMoneyTotal.setText(giftInfoBean.getPrice()
				* Integer.valueOf(tvBuyCount.getText().toString()) + "");
		tvMadunToatl.setText(giftInfoBean.getNeedCredit()
				* Integer.valueOf(tvBuyCount.getText().toString()) + "");

		tvGiftDetails.setText(giftInfoBean.getRemark());
		for (String str : giftInfoBean.getFormatUrl()) {
			PicSlideInfo info = new PicSlideInfo();
			info.setUrl(str);
			info.setContent("test");
			infos.add(info);
			Log.e("data", "url " + str);
		}
		ivGiftPic.setImageResources(infos, mAdCycleViewListener);
	}

	private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

		@Override
		public void onImageClick(PicSlideInfo info, int position, View imageView) {

		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView,
					ImageUtil.imageVegeOptions);
		}
	};

	class CommonListener implements OnClickListener {
		@SuppressLint("ResourceAsColor")
		int count;
		String price;
		DecimalFormat df = new DecimalFormat("#0.00"); 
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.iv_count_minus:
				count = Integer.parseInt(tvBuyCount.getText().toString());
				if (count <= 1) {
					return;
				}
				tvBuyCount.setText(String.valueOf(--count));
				  
				price = String.valueOf(df.format(giftInfoBean.getPrice()
						* Integer.valueOf(tvBuyCount.getText().toString())));
				tvMoneyTotal.setText(price);
				 
				tvMadunToatl
						.setText(giftInfoBean.getNeedCredit()
								* Integer.valueOf(tvBuyCount.getText()
										.toString()) + "");
				break;
			case R.id.iv_count_plus:
				count = Integer.parseInt(tvBuyCount.getText().toString());
				if(count>=giftInfoBean.getTotal()){
					Toast.makeText(mContext, "最多只能兑换"+giftInfoBean.getTotal()+"件礼品。", Toast.LENGTH_SHORT).show();
					return;
				}
				tvBuyCount.setText(String.valueOf(++count));
				 
				price = String.valueOf(df.format(giftInfoBean.getPrice()
						* Integer.valueOf(tvBuyCount.getText().toString())));
				tvMoneyTotal.setText(price);
				tvMadunToatl.setText(giftInfoBean.getNeedCredit()
						* Integer.valueOf(tvBuyCount.getText().toString())
						+ "");
			 
				break;
			case R.id.bt_exchange:
				switch (flag) {
				//从礼品列表中
				case -1: 
					DialogUtil.DefaultDialog(mContext, "提示", "是否使用马盾兑换礼品", "确定", "取消", new IDialogOperation() {
						
						@Override
						public void onPositive() {
							requestExchange();
						}
						
						@Override
						public void onNegative() {
							// TODO Auto-generated method stub
							
						}
					});
					break;
				case 1:
					Toast.makeText(mContext, "提醒发货", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					AlertDialog.Builder builder = new Builder(
							GiftInfoActivity.this);
					builder.setMessage("确认签收吗？");
					builder.setTitle("提示");
					builder.setPositiveButton(R.string.comfirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									requestSign(giftId);
								}
							});
					builder.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
								}
							});
					builder.create().show();
					break;
				case 3:
				 
				break;
				default:
					break;
				}
				break;

			case R.id.bt_action_2:
				switch (flag) {
				case 2:
					Bundle mBundle=new Bundle();
					mBundle.putSerializable("data", item);
					((GiftInfoActivity) mContext).startNewActivity(LogisticGiftActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, mBundle); 
					break;

				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	}

	public void requestSign(String id) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_UPDATE_GIFT_ORDER_STATUS, id, "4",
						ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "签收礼品的url "+url);
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
	
	public void requestExchange() {
		if ("-1".equals(ShareUtil.getInstance(mContext).getDefaultAddressId())) {
			Toast.makeText(mContext, "没设置默认的收货地址", Toast.LENGTH_SHORT).show();
			return;
		}
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_UPDATE_GIFT_ORDER, ShareUtil
						.getInstance(mContext).getUserId(), giftId, Integer
						.valueOf(tvBuyCount.getText().toString()), ShareUtil
						.getInstance(mContext).getDefaultAddressId(), "无");
		Log.e("url", "提交礼品订单的接口" + url);
		new Thread(new Runnable() {

			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						TypeMsgBean tBean = new Gson().fromJson(
								bean.getResult(), TypeMsgBean.class);
						if (tBean.getRESULT_TYPE() == 1) {
							EventBus.getDefault().post(
									new EventMsg(
											OpCodes.UPDATE_GIFT_ORDER_DONE,
											tBean.getRESULT_MSG()));
						} else {
							EventBus.getDefault().post(
									new EventMsg(
											OpCodes.UPDATE_GIFT_ORDER_ERROR,
											tBean.getRESULT_MSG()));
						}

					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.UPDATE_GIFT_ORDER_ERROR,
										"提交订单返回数据出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.UPDATE_GIFT_ORDER_ERROR, bean
									.getState()));
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

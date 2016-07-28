package com.caomei.comingvagetable.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.AlipayUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.caomei.comingvagetable.util.WXPayUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class ChargeMoney extends BaseActivity {

	/**
	 * 0：表示充钱 1：表示充马盾
	 */
	private int flag = 0;
	private TextView tvDes;
	private LinearLayout panelDes;
	private TextView tvTitle;
	private Button btCharge;
	private Button bt10;
	private Button bt20;
	private Button bt50;
	private Button bt100;
	private Button bt200;
	private EditText etOther;

	private CommonListener mListener;

	private String chargeUrl;

	/**
	 * payWay:支付方式 0 支付宝 1 微信
	 */
	private int payWay = 0;
	private int chargeMoney = 10;
	private RadioGroup rgPay;
	private ImageView ivBack;
	private RadioButton rbAli;
	private RadioButton rbWx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_charge);
		setView();
		setData();
	}

	private void setView() {

		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDes = (TextView) findViewById(R.id.tv_description);
		panelDes = (LinearLayout) findViewById(R.id.panel_des);
		btCharge = (Button) findViewById(R.id.bt_charge_now);

		bt10 = (Button) findViewById(R.id.bt_charge_10);
		bt20 = (Button) findViewById(R.id.bt_charge_20);
		bt50 = (Button) findViewById(R.id.bt_charge_50);
		bt100 = (Button) findViewById(R.id.bt_charge_100);
		bt200 = (Button) findViewById(R.id.bt_charge_200);
		etOther = (EditText) findViewById(R.id.et_charge_other);
		rbAli = (RadioButton) findViewById(R.id.rb_alipay);
		rbWx = (RadioButton) findViewById(R.id.rb_wxpay);
		rgPay = (RadioGroup) findViewById(R.id.rg_pay_way);

	}

	private void setData() {
		mListener = new CommonListener();
		bt10.setOnClickListener(mListener);
		bt20.setOnClickListener(mListener);
		bt50.setOnClickListener(mListener);
		bt100.setOnClickListener(mListener);
		bt200.setOnClickListener(mListener);
		etOther.setOnClickListener(mListener);
		flag = getIntent().getIntExtra("data", 0);
		ivBack.setOnClickListener(mListener);

		btCharge.setOnClickListener(mListener);
		initView();
		rgPay.setOnCheckedChangeListener(mListener);
	}

	@SuppressLint("NewApi")
	private void initView() {
		if (flag == 1) {
			tvTitle.setText("充值马盾");
			tvDes.setText("1元=0.8马盾，多充值，购物更优惠哦");
			panelDes.setBackground(getResources().getDrawable(
					R.color.money_yellow));
			btCharge.setBackground(getResources().getDrawable(
					R.color.money_yellow));
			rbWx.setVisibility(View.GONE);
			rbAli.setText("余额");
			bt10.setBackground(getResources().getDrawable(
					R.drawable.charge_madun_selected));
		} else if (flag == 0) {
			bt10.setBackground(getResources().getDrawable(
					R.drawable.charge_money_selected));
		}
	}

	class CommonListener implements OnClickListener, OnCheckedChangeListener {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_charge_10:
				chargeMoney = 10;
				resetAllButton();
				((TextView) v).setTextColor(getResources().getColor(
						R.color.white));
				if (flag == 0) {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_money_selected));
				} else {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_madun_selected));
				}

				break;
			case R.id.bt_charge_20:
				chargeMoney = 20;
				resetAllButton();
				((TextView) v).setTextColor(getResources().getColor(
						R.color.white));
				if (flag == 0) {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_money_selected));
				} else {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_madun_selected));
				}

				break;
			case R.id.bt_charge_50:
				chargeMoney = 50;
				resetAllButton();
				((TextView) v).setTextColor(getResources().getColor(
						R.color.white));
				if (flag == 0) {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_money_selected));
				} else {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_madun_selected));
				}

				break;
			case R.id.bt_charge_100:
				chargeMoney = 100;
				resetAllButton();
				((TextView) v).setTextColor(getResources().getColor(
						R.color.white));
				if (flag == 0) {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_money_selected));
				} else {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_madun_selected));
				}

				break;
			case R.id.bt_charge_200:
				chargeMoney = 200;
				resetAllButton();
				((TextView) v).setTextColor(getResources().getColor(
						R.color.white));
				if (flag == 0) {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_money_selected));
				} else {
					v.setBackground(getResources().getDrawable(
							R.drawable.charge_madun_selected));
				}

				break;
			case R.id.et_charge_other:
				resetAllButton();
				chargeMoney = 0;
				break;

			case R.id.bt_charge_now:
				if (chargeMoney == 0) {
					try {
						chargeMoney = Integer.parseInt(etOther
								.getEditableText().toString());
					} catch (Exception ex) {
						chargeMoney = 0;
					}
					if (chargeMoney <= 0) {
						ToastUtil.Show(mContext, "充值的钱数必须大于零");
						return;
					}
				}
				if (flag == 0) {
					// 充值余额
					if (payWay == 0) {
						// 支付宝支付
						chargeUrl = CommonAPI.BASE_URL
								+ String.format(CommonAPI.URL_CHARGE_BY_ALIPAY,
										ShareUtil.getInstance(mContext)
												.getUserId());
						requestCharge(chargeMoney, chargeUrl);

					} else {
						// 微信支付
						chargeUrl = CommonAPI.BASE_URL
								+ CommonAPI.URL_CHARGE_BY_WXPAY;
						wxCharge(chargeMoney, chargeUrl);
					}
				} else if (flag == 1) {
					// 充值马盾
					chargeUrl = CommonAPI.BASE_URL
							+ String.format(
									CommonAPI.URL_CHARGE_MADUN_BY_MONEY,
									chargeMoney, ShareUtil
											.getInstance(mContext).getUserId());
					new Thread(new Runnable() {
						@Override
						public void run() {
							AccessNetResultBean bean = NetUtil.getInstance(
									mContext).getDataFromNetByGet(chargeUrl);
							if (bean.getState() == AccessNetState.Success) {
								try {
									TypeMsgBean tBean = new Gson().fromJson(
											bean.getResult(), TypeMsgBean.class);
									if (tBean.getRESULT_TYPE() == 1) {
										EventBus.getDefault()
												.post(new EventMsg(
														OpCodes.CHARGE_MADUN_DONE,
														tBean.getRESULT_MSG()));
									} else {
										EventBus.getDefault()
												.post(new EventMsg(
														OpCodes.CHARGE_MADUN_ERROR,
														tBean.getRESULT_MSG()));
									}
								} catch (Exception e) {
									EventBus.getDefault().post(
											new EventMsg(
													OpCodes.CHARGE_MADUN_ERROR,
													"数据出错"));
								}
							} else {
								EventBus.getDefault().post(
										new EventMsg(
												OpCodes.CHARGE_MADUN_ERROR,
												bean.getResult()));
							}
						}
					}).start();
				}
				break;
			case R.id.iv_back:
				onBackPressed();
				break;

			default:
				break;
			}
		}

		private void wxCharge(int money, String url) {
			WXPayUtil util = new WXPayUtil(mContext, money*100, url,0);
			util.WXPay();
		}

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			payWay = (R.id.rb_alipay==arg1)?0:1;
			Log.e("data", "payway " + payWay);
		}
	}

	@SuppressLint("NewApi")
	public void resetAllButton() {
		bt10.setBackground(getResources().getDrawable(R.drawable.gray_borad_bg));
		bt20.setBackground(getResources().getDrawable(R.drawable.gray_borad_bg));
		bt50.setBackground(getResources().getDrawable(R.drawable.gray_borad_bg));
		bt100.setBackground(getResources()
				.getDrawable(R.drawable.gray_borad_bg));
		bt200.setBackground(getResources()
				.getDrawable(R.drawable.gray_borad_bg));

		bt10.setTextColor(getResources().getColor(R.color.des_black));
		bt20.setTextColor(getResources().getColor(R.color.des_black));
		bt50.setTextColor(getResources().getColor(R.color.des_black));
		bt100.setTextColor(getResources().getColor(R.color.des_black));
		bt200.setTextColor(getResources().getColor(R.color.des_black));
	}

	public void requestCharge(float money, final String url) {
		AlipayUtil aliPay = new AlipayUtil(this, money, url, ShareUtil
				.getInstance(mContext).getUserId()
				+ "_"
				+ "ID为:"
				+ ShareUtil.getInstance(mContext).getUserId() + "的用户充值了钱包");
		aliPay.Pay();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.CHARGE_MADUN_DONE:
		case OpCodes.PAY_ORDER_DONE:
			ToastUtil.Show(mContext, msg.getData().toString());
			finish();
			break;
		case OpCodes.CHARGE_MADUN_ERROR:
			ToastUtil.Show(mContext, msg.getData().toString());
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

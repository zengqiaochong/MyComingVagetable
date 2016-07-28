package com.caomei.comingvagetable.activity;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.Constants;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.UserInfoBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.AlipayUtil;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.MD5;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.Util;
import com.caomei.comingvagetable.util.WXPayUtil;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory; 

import de.greenrobot.event.EventBus;

public class PaymentActivity extends BaseActivity implements IWXAPIEventHandler {
	private ImageView ivBack;
	private RelativeLayout pZhiFuBao;
	private RelativeLayout pWeiXin;
	private LinearLayout llPricePanle;
	private CommonListener listener;
	private float price;
	// private VegeCartBean bean;
	private String orderId;
	private RelativeLayout pQianbao;

	

	private IWXAPI api;
	private Context mContext;

	private Map<String,String> resultunifiedorder;
	private StringBuffer sb;
	private PayReq req;
	private static final String TAG = "WXEntryActivity";
	private Button payment_order_next;//下一步
	private UserInfoBean ucInfoBbean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		EventBus.getDefault().register(this);
		initView();
		initData();
		sb=new StringBuffer();
		mContext = this;
	
	}

	private void initView() {
		payment_order_next = (Button) findViewById(R.id.payment_order_next);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		pZhiFuBao = (RelativeLayout) findViewById(R.id.panel_zhifubao);
		pWeiXin = (RelativeLayout) findViewById(R.id.panel_weixin);
		llPricePanle = (LinearLayout) findViewById(R.id.panel_price);
		pQianbao = (RelativeLayout) findViewById(R.id.panel_qianbao);
	}

	private void initData() {
		price = getIntent().getFloatExtra("price", 0f);
		orderId = getIntent().getStringExtra("order_id");
		// bean = (VegeCartBean) getIntent().getSerializableExtra("data");
		if (orderId == null) {
			Toast.makeText(mContext, "订单信息有误", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		listener = new CommonListener();
		ivBack.setOnClickListener(listener);
		pZhiFuBao.setOnClickListener(listener);
		pWeiXin.setOnClickListener(listener);
		pQianbao.setOnClickListener(listener);
		payment_order_next.setOnClickListener(listener);
		MethodUtil.SetPanelPrice(price, llPricePanle);
		requestUserInfo();
	}

	private void requestUserInfo() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_USER_INFO, ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "获取用户信息接口： " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						ucInfoBbean = new Gson().fromJson(bean.getResult(), UserInfoBean.class);
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_USER_INFO_DONE, ucInfoBbean));
					} catch (Exception e) {
					}
				}
			}
		}).start();
	}

	class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.panel_qianbao:
				DialogUtil.DefaultDialog(mContext, "提示", "确定使用钱包支付？", "确定",
						"取消", new IDialogOperation() {
							@Override
							public void onPositive() {
								requestPayBuyPurse();
							}

							@Override
							public void onNegative() {

							}
						});
				break;
			case R.id.panel_zhifubao:
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_PURCHARSE_WITH_ALIPAY,
								ShareUtil.getInstance(mContext).getUserId());
				AlipayUtil aliPay = new AlipayUtil(PaymentActivity.this, price,
						url, ShareUtil.getInstance(mContext).getUserId() + "_"
								+ orderId + "_菜来了商品支付");
				aliPay.Pay();
				break;
			case R.id.panel_weixin: 
				String wxUrl = CommonAPI.BASE_URL+CommonAPI.URL_PURCHARSE_WITH_WXPAY;
				WXPayUtil util = new WXPayUtil(mContext, (int)(price*100), wxUrl,1,orderId);
				util.WXPay();
				break;
			case R.id.iv_back:
				onBackPressed();
				break;
				case R.id.payment_order_next:
					if(money >= price){
						DialogUtil.DefaultDialog(mContext, "提示", "确定使用钱包支付？", "确定",
								"取消", new IDialogOperation() {
									@Override
									public void onPositive() {
										requestPayBuyPurse();
									}

									@Override
									public void onNegative() {

									}
								});
					}else{
						String url2 = CommonAPI.BASE_URL
								+ String.format(CommonAPI.URL_PURCHARSE_WITH_ALIPAY,
								ShareUtil.getInstance(mContext).getUserId());
						AlipayUtil aliPay2 = new AlipayUtil(PaymentActivity.this, price,
								url2, ShareUtil.getInstance(mContext).getUserId() + "_"
								+ orderId + "_菜来了商品支付");
						aliPay2.Pay();
					}
					break;
			}
		}
	}

	// ------------ 微信支付 --------------------- 
	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

		private ProgressDialog dialog;


		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PaymentActivity.this, "提示","处理中");
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			Log.e("sbb", " result "+result.toString());
			resultunifiedorder=result;
			EventBus.getDefault().post(new EventMsg(OpCodes.GO_TO_WXPAY, null));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion",entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String,String> xml=decodeXml(content);

			return xml;
		}
	}
	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n"+req.sign+"\n\n");
 

		Log.e("orion", "asdfa"+signParams.toString());
		api.registerApp(Constants.APP_ID);
		api.sendReq(req);
	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

        this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",appSign);
		return appSign;
	}
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		String notifyUrl=CommonAPI.BASE_URL+String.format(CommonAPI.URL_PURCHARSE_WITH_WXPAY,ShareUtil.getInstance(mContext).getUserId());
		try {
			String	nonceStr = genNonceStr();


			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("attach", ShareUtil.getInstance(mContext).getUserId()+"_"+System.currentTimeMillis()));
			
			packageParams.add(new BasicNameValuePair("body", "OrderNo:"+genOutTradNo()));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
			packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
//			packageParams.add(new BasicNameValuePair("total_fee", "1"));
			packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(price*100))));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
 
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


		   String xmlstring =toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}
		

	}
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion",sb.toString());
		return sb.toString();
	}
	private String genOutTradNo() {
 
		Random random = new Random();
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",Locale.getDefault());
		Date date = new Date();
		String key = ShareUtil.getInstance(mContext).getUserId()+format.format(date)+String.valueOf(random.nextInt(10000));
		return MD5.getMessageDigest(key.getBytes());
		}
	

	/**
	 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);
		

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",packageSign);
		return packageSign;
	}
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	public Map<String,String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}
	// -------------------------------------------------

	private void requestPayBuyPurse() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_PAY_BY_PURSE, ShareUtil
						.getInstance(mContext).getUserId(), orderId, price,
						"钱包支付");
		Log.e("url", "使用钱包支付：" + url);
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
									new EventMsg(OpCodes.PURSE_CHARGE_DONE,
											tBean.getRESULT_MSG()));
						} else {
							EventBus.getDefault().post(
									new EventMsg(OpCodes.PURSE_CHARGE_ERROR,
											tBean.getRESULT_MSG()));
						}
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.PURSE_CHARGE_ERROR,
										"数据解析出错"));
					}
				}
			}
		}).start();
	}

	private float money;
	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
			case OpCodes.GET_USER_INFO_DONE:
				ucInfoBbean = (UserInfoBean) msg.getData();
				money = ucInfoBbean.getFMoney();
				break;
		case OpCodes.PURSE_CHARGE_DONE:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			finish();
			break;
		case OpCodes.PURSE_CHARGE_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			finish();
			break;
		case OpCodes.GO_TO_WXPAY:
			genPayReq();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

}
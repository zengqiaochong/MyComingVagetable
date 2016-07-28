package com.caomei.comingvagetable.util;

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
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.Constants;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.activity.PaymentActivity;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayUtil implements IWXAPIEventHandler{
	
	private Context mContext;
	private StringBuilder sb;
	private Map<String,String> resultunifiedorder;
	private PayReq req;

	private IWXAPI api;
	private String notifyUrl;
	private String money;
	private String orderId;
	/**
	 * 0 充值
	 * 1 付款订单
	 */
	
	private int opType=0;
	 
	public WXPayUtil(Context mContext,int money,String url,int opType){
		this.mContext=mContext;
		this.notifyUrl=url;
		sb=new StringBuilder(); 
		req=new PayReq();
		this.opType=opType;
		this.money=String.valueOf(money);
		api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
		api.registerApp(Constants.APP_ID);
		api.handleIntent(((Activity)mContext).getIntent(), this);
		
	}
	public WXPayUtil(Context mContext,int money,String url,int opType,String orderId){
		 this.orderId=orderId;
			this.mContext=mContext;
			this.notifyUrl=url;
			sb=new StringBuilder(); 
			req=new PayReq();
			this.opType=opType;
			this.money=String.valueOf(money);
			api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
			api.registerApp(Constants.APP_ID);
			api.handleIntent(((Activity)mContext).getIntent(), this);
	}
	
	public void WXPay(){
		GetPrepayIdTask task=new GetPrepayIdTask();
		task.execute();
	}
	
	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "提示","处理中");
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			Log.e("sbb", " result "+result.toString());
			resultunifiedorder=result;
			genPayReq();
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
	 	try {
	 		String attach="";
	 		String tradeNo=genOutTradNo();
	 		if(opType==0){
	 			//充值
	 			attach=ShareUtil.getInstance(mContext).getUserId()+"_"+System.currentTimeMillis();
	 		}else{
	 			//付款
	 			attach=ShareUtil.getInstance(mContext).getUserId()+"_"+orderId+"_"+System.currentTimeMillis();
	 		}
	 		Log.e("sbb", " tradeNo 1"+attach);
			String	nonceStr = genNonceStr();
			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("attach",attach));
			packageParams.add(new BasicNameValuePair("body",  "Coming Vegetable Payment"));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
			Log.e("notifyUrl", "notify url ："+notifyUrl);
			packageParams.add(new BasicNameValuePair("out_trade_no",tradeNo));
	 		Log.e("sbb", " tradeNo 2"+tradeNo);
			packageParams.add(new BasicNameValuePair("total_fee", money));
//			packageParams.add(new BasicNameValuePair("total_fee", "1"));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


		   String xmlstring =toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
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
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		
	}

}

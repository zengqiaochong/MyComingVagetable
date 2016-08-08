package com.caomei.comingvagetable.activity;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.RegistResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.NetUtil;
import com.google.gson.Gson;
import de.greenrobot.event.EventBus;

public class RegistActivity extends BaseActivity {

	private EditText etPhoneNum;
	private Button btGetCode;
	private EditText etCode;
	private Button btRegist;
	private EditText etPwd;
	private EditText etPwdComfirm;
	private ImageView ivBack;
	
	private CommonListener mListener;
	private Timer mTimer = new Timer();
	private myTimerTask myTask;
	private TextView tvContact;
	private CheckBox cbAgree;
	private EditText et_recommend_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		EventBus.getDefault().register(this);
		setView();
		setData();
	}

	private void setView() {
		et_recommend_confirm = (EditText) findViewById(R.id.et_recommend_confirm);
		ivBack=(ImageView)findViewById(R.id.iv_back);
		etPhoneNum = (EditText) findViewById(R.id.et_phone_number);
		etCode = (EditText) findViewById(R.id.et_code_number);
		btGetCode = (Button) findViewById(R.id.bt_get_code);
		btRegist = (Button) findViewById(R.id.bt_regist);
		etPwd = (EditText) findViewById(R.id.et_pwd);
		tvContact=(TextView)findViewById(R.id.tv_contract);
		etPwdComfirm = (EditText) findViewById(R.id.et_pwd_confirm);
		cbAgree=(CheckBox)findViewById(R.id.cb_agree);
	}

	private void setData() {
		mListener = new CommonListener();
		btGetCode.setOnClickListener(mListener);
		btRegist.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
		tvContact.setOnClickListener(mListener);
		cbAgree.setOnCheckedChangeListener(mListener);
	}

	class CommonListener implements OnClickListener,OnCheckedChangeListener{
		String phoneNum;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_get_code:
				phoneNum = etPhoneNum.getEditableText().toString();
				if (phoneNum == null || phoneNum.length() < 11) {
					Toast.makeText(mContext, "电话号码错误", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				RequestCode(phoneNum);
				btGetCode.setClickable(false);
				btGetCode.setBackgroundColor(getResources().getColor(R.color.gray_slice));
				myTask = new myTimerTask(btGetCode, 300);
				mTimer.schedule(myTask, 1000, 1000);
				break;
			case R.id.bt_regist:
				pDialog.show();
				if (CheckAll()) {
					String code = etCode.getEditableText().toString();
					CheckCodeResult(phoneNum, code);
				}else{
					pDialog.dismiss();
				}
				break;
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.tv_contract:
				Bundle mBundle =new Bundle();
				mBundle.putString("title", "客户协议");
				mBundle.putString("data", "http://www.zmbok.com/agreement.html");
				startNewActivity(BroswerActivity.class, R.anim.slide_in_bottom,R.anim.slide_out_top, false,mBundle);
				
				break;
				
			default:
				break;
			}
		}

		@SuppressLint("NewApi") @Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			 
//				btRegist.setClickable(true); 
			btRegist.setEnabled(arg1);
			if(arg1){
				btRegist.setBackground(getResources().getDrawable(R.drawable.button_bg));
			}else{
				btRegist.setBackgroundColor(getResources().getColor(R.color.mark_black));
			}
		}

	}

	private boolean CheckAll() {
		if (etPhoneNum.getEditableText().toString().length() != 11) {
			Toast.makeText(mContext, "电话号码格式错误", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(etCode.getEditableText().toString())) {
			Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(etPwd.getEditableText().toString())
				|| !etPwd.getEditableText().toString()
						.equals(etPwdComfirm.getEditableText().toString())) {
			Toast.makeText(mContext, "两次登录密码不相同", Toast.LENGTH_LONG).show();
			return false;
		}else if (etPwd.getEditableText().toString().length()<6) {
				Toast.makeText(mContext, "登录密码不能少于6位", Toast.LENGTH_LONG).show();
				return false;
			}
		return true;
	}

	/** 
	 * 
	 */
	public void RequestCode(String phoneNum) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_SMS_CODE, phoneNum, -1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url); 
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean smsBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (smsBean.getRESULT_TYPE() == 1) { 
					 
					} else {
					 
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_SMS_CODE_FAILED, smsBean.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_SMS_CODE_FAILED, "短信验证码请求失败"));
				}
			}
		}).start();
	}

	public void CheckCodeResult(String phoneNum, String code) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_CHECK_SMS_CODE, phoneNum, code,
						-1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success){
					TypeMsgBean sBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sBean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(new EventMsg(OpCodes.CHECK_SMS_CODE_SUCCESS, sBean));
					 
					}else{
						EventBus.getDefault().post(new EventMsg(OpCodes.CHECK_SMS_CODE_FAILED, sBean.getRESULT_MSG()));
					}
				}else{
					EventBus.getDefault().post(new EventMsg(OpCodes.CHECK_SMS_CODE_FAILED, "注册失败"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg Msg) {
		switch (Msg.getFlag()) {
		case OpCodes.GET_SMS_CODE_FAILED:
			if (myTask != null) {
				myTask.cancel();
			} 
			btGetCode.setClickable(true);
			btGetCode.setText("获取验证码");
			Toast.makeText(mContext, String.valueOf(Msg.getData()),
					Toast.LENGTH_LONG).show();
			break;
		case OpCodes.CHECK_SMS_CODE_FAILED:
			pDialog.dismiss();
			Toast.makeText(mContext, Msg.getData().toString(),Toast.LENGTH_SHORT).show();
			break;
			
		case OpCodes.CHECK_SMS_CODE_SUCCESS:
		 
			requestForRegist(); 
			break;
		case OpCodes.REGIST_SUCCESS:
			pDialog.dismiss();
			Toast.makeText(mContext,"注册成功",Toast.LENGTH_LONG).show();
			
			onBackPressed();
			break;
		case OpCodes.REGIST_FAILED:
			pDialog.dismiss();
			Toast.makeText(mContext,Msg.getData().toString(),Toast.LENGTH_SHORT).show(); 
			break;  
		}
	}

	private void requestForRegist() {
		String uName = etPhoneNum.getEditableText().toString();
		String pwd = etPwd.getEditableText().toString();
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_REGIST, uName, pwd, et_recommend_confirm.getEditableText().toString());

		System.out.println("url========" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					RegistResultBean rBean = new Gson().fromJson(
							bean.getResult(), RegistResultBean.class);
					if (rBean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(new EventMsg(OpCodes.REGIST_SUCCESS, null));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.REGIST_FAILED, rBean.getRESULT_MSG()));
						
					}
				} else {
					EventBus.getDefault().post(new EventMsg(OpCodes.REGIST_FAILED, "注册失败"));
				}
			}
		}).start();
	}

	class myTimerTask extends TimerTask {
		private int length;
		private Button btn;

		public myTimerTask(Button btn, int len) {
			this.length = len;
			this.btn = btn;
		}

		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					length--;
					btn.setText(String.valueOf(length)+"s后重发");
					if (length < 0) {
						if(myTask!=null){
							myTask.cancel();
						}
						btGetCode.setClickable(true);
						btGetCode.setBackgroundColor(getResources().getColor(R.color.location_bar_background));
						btn.setText("获取验证码");
					}
				}
			});
		}
	}


	@Override 
	public void onBackPressed() {  
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
	}  
	
	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

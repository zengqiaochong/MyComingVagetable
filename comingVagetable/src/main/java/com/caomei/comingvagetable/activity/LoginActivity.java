package com.caomei.comingvagetable.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity {

	private TextView tvRegist;
	private TextView tvForgetPwd;
	private CommonListener mListener;
	private Button btLogin;
	private EditText etPhoneNum;
	private EditText etPassword;
	private ImageView ivBack;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		EventBus.getDefault().register(mContext);
		InitView();
		InitData();
		pDialog = new ProgressDialog(this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("加载中...");
		pDialog.setIndeterminate(true);
		pDialog.setCancelable(true);
	}

	private void InitView() {
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvRegist = (TextView) findViewById(R.id.tv_regist_now);
		tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
		btLogin = (Button) findViewById(R.id.bt_login);
		etPhoneNum = (EditText) findViewById(R.id.et_phone_number);
		etPassword = (EditText) findViewById(R.id.et_code_number);
	}

	private void InitData() {
		mListener = new CommonListener();
		tvRegist.setOnClickListener(mListener);
		tvForgetPwd.setOnClickListener(mListener);
		btLogin.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
		etPhoneNum.setText(ShareUtil.getInstance(LoginActivity.this).getUserName());
	}

	class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_regist_now:
				startNewActivity(RegistActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);
				break;
			case R.id.tv_forget_pwd:
				//Toast.makeText(mContext, "暂无此功能", Toast.LENGTH_SHORT).show();
				startNewActivity(Forgetpassword.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, true, null);
				break;
			case R.id.bt_login:
				pDialog.show();
				if (CheckUserInfo()) {
					LoginRequest();
				} else {
					pDialog.dismiss();
				}
				break;
			case R.id.iv_back:
				onBackPressed();
				break;
			default:
				break;
			}
		}

	}

	private boolean CheckUserInfo() {
		if (MethodUtil.isMobileNO(etPhoneNum.getEditableText().toString())) {
			String pwd = etPassword.getEditableText().toString();
			if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
				Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		} else {
			Toast.makeText(mContext, "电话号码填写错误", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
 
	private void LoginRequest() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_LOGIN, etPhoneNum
						.getEditableText().toString(), etPassword
						.getEditableText().toString());

		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean mBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (mBean.getRESULT_TYPE() == 1) {

						ShareUtil.getInstance(mContext).setUserId(
								mBean.getRESULT_USER_ID());
						EventBus.getDefault().post(
								new EventMsg(OpCodes.LOGIN_SUCCESS, null));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.LOGIN_FAILED, mBean
										.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.LOGIN_FAILED, "登录请求失败"));
				}
			}
		}).start();

	}

	public void onEventMainThread(EventMsg Msg) {
		switch (Msg.getFlag()) {
		case OpCodes.LOGIN_SUCCESS:
			Log.e("userid", "userid "
					+ ShareUtil.getInstance(mContext).getUserId());
			ShareUtil.getInstance(mContext).setIsLogin(true);
			ShareUtil.getInstance(mContext).setUserName(
					etPhoneNum.getEditableText().toString());
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case OpCodes.LOGIN_FAILED:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			Toast.makeText(mContext, Msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			etPassword.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in,
				R.anim.activity_slide_right_out);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

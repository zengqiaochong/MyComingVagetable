package com.caomei.comingvagetable.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;

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

	/*登录请求*/
	private void LoginRequest() {
		final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_LOGIN, etPhoneNum.getEditableText().toString(), etPassword.getEditableText().toString());
		AsyncHttpClient client = new AsyncHttpClient();
		saveCookie(client);//Cookie自动保存在SharedPreferences
		client.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				TypeMsgBean mBean = new Gson().fromJson(new String(bytes), TypeMsgBean.class);
				if (mBean.getRESULT_TYPE() == 1) {
					ShareUtil.getInstance(mContext).setUserId(mBean.getRESULT_USER_ID());
					ShareUtil.getInstance(mContext).setIsLogin(true);
					ShareUtil.getInstance(mContext).setUserName(etPhoneNum.getEditableText().toString());
					try {
						pDialog.dismiss();
					} catch (Exception e) {}
					Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
					Utils.setCookies(getCookie());
					finish();
				} else {
					loginfailed(mBean.getRESULT_MSG());
				}
			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				loginfailed("登录请求失败");
			}
		});

	}

	/*登录失败*/
	private void loginfailed(String msg){
		try {
			pDialog.dismiss();
		} catch (Exception e) {}
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		etPassword.setText("");
	}

	/*保存Cookie到SharedPreferences中*/
	private void saveCookie(AsyncHttpClient client) {
		PersistentCookieStore cookieStore = new PersistentCookieStore(this);
		client.setCookieStore(cookieStore);
	}

	/*获取Cookie*/
	private List<Cookie> getCookie(){
		PersistentCookieStore cookieStore = new PersistentCookieStore(LoginActivity.this);
		List<Cookie> cookies = cookieStore.getCookies();
		return cookies;
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
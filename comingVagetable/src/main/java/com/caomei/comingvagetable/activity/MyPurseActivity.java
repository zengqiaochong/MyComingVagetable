package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.MyDiscuntBean;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class MyPurseActivity extends BaseActivity{
	private RelativeLayout rlChargeMoney;
	private RelativeLayout rlChargeMadun;
	private CommonListener mListener;
	private ImageView ivBack;
	private TextView purse_activity_content;
	private RelativeLayout purse_activity_re;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purse);
		initView();
		initData();
	}

	private void initView() {
		rlChargeMoney=(RelativeLayout)findViewById(R.id.panel_charge_money);
		rlChargeMadun=(RelativeLayout)findViewById(R.id.panel_charge_madun);
		ivBack=(ImageView)findViewById(R.id.iv_back);
		purse_activity_content = (TextView) findViewById(R.id.purse_activity_content);
		purse_activity_re = (RelativeLayout) findViewById(R.id.purse_activity_re);
	}

	private void initData() {
		mListener=new CommonListener();
		rlChargeMoney.setOnClickListener(mListener);
		rlChargeMadun.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
		getMyActivityContent();
	}

	/*活动细则接口*/
	private void getMyActivityContent() {
		final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_COMPLAIN_ACTIVITY, ShareUtil.getInstance(mContext).getUserId());//"10000057");
		Log.e("url", "活动细则接口: " + url);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				if(bytes != null && !new String(bytes).equals("")){
					purse_activity_re.setVisibility(View.VISIBLE);
					purse_activity_content.setText(new String(bytes));
				}

			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				//ToastUtil.Show(MyPurseActivity.this, "获取活动信息失败！");
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public class CommonListener implements OnClickListener{
		Bundle b=new Bundle();
		@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.panel_charge_money:
			b.putInt("data", 0);
			startNewActivity(ChargeMoney.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, true, b);
			break;
		case R.id.panel_charge_madun:
			b.putInt("data", 1);
			startNewActivity(ChargeMoney.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, true, b);
			break;
		case R.id.iv_back:
			onBackPressed();
			break;
			
		default:
			break;
		}
		}
		
	}

}

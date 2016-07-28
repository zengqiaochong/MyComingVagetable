package com.caomei.comingvagetable.activity;

import com.caomei.comingvagetable.R;

import android.widget.Button;
import android.widget.TextView;

public class WeChatPayActivity extends BaseActivity{
	private Button btPay;
	private TextView tvInfo;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		setContentView(R.layout.activity_wechat_pay);
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		btPay=(Button)findViewById(R.id.bt_pay);
		tvInfo=(TextView)findViewById(R.id.tv_info);
	}
	
	private void initData() {
		
	}
}

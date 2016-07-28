package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caomei.comingvagetable.R;

public class MyPurseActivity extends BaseActivity{

	private RelativeLayout rlChargeMoney;
	private RelativeLayout rlChargeMadun;
	private CommonListener mListener;
	private ImageView ivBack;
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
	}

	private void initData() {
		mListener=new CommonListener();
		rlChargeMoney.setOnClickListener(mListener);
		rlChargeMadun.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
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

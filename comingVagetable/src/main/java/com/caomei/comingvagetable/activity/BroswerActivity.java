package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;

public class BroswerActivity extends BaseActivity {

	private WebView webView;
	private ImageView ivBack;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broswer);
		setView();
		setData();
	}

	private void setData() {
		// TODO Auto-generated method stub
		String url = getIntent().getStringExtra("data");
		String title=getIntent().getStringExtra("title");
		tvTitle.setText(title);
		webView.loadUrl(url);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	private void setView() {
		webView = (WebView) findViewById(R.id.wv_view);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle=(TextView)findViewById(R.id.tv_title);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_from_top,
				R.anim.slide_out_to_bottom);
	}

}

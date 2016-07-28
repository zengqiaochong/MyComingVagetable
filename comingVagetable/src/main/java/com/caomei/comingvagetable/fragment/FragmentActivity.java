package com.caomei.comingvagetable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.util.ToastUtil;

public class FragmentActivity extends BaseFragment{

	private View view;
	private Button btActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_activity, null);
		btActivity=(Button)view.findViewById(R.id.bt_activity);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		btActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ToastUtil.Show(mContext, "活动尚未开始，了解活动进展，请加QQ群咨询。");
			}
		});
	}

}

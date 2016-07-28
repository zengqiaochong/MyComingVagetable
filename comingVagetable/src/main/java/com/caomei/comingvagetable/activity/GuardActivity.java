package com.caomei.comingvagetable.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.adapter.GuardViewPagerAdapter;
import com.caomei.comingvagetable.util.ShareUtil;

public class GuardActivity extends BaseActivity {

	private View view1;
	private View view2;
	private View view3;
	private ArrayList<View> viewList;
	private ViewPager mViewpager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guard);
		mViewpager=(ViewPager)findViewById(R.id.vp_app_guard);
		getLayoutInflater();
		LayoutInflater inflater = LayoutInflater.from(this);
		view1 = inflater.inflate(R.layout.item_guard_view1, null);
		view2 = inflater.inflate(R.layout.item_guard_view2, null);
		view3 = inflater.inflate(R.layout.item_guard_view3, null);

		viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		mViewpager.setAdapter(new GuardViewPagerAdapter(mContext,viewList));
	   
  		view3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startNewActivity(MainActivity.class,R.anim.activity_slide_right_in, R.anim.activity_slide_left_out,
						true,null);
				ShareUtil.getInstance(mContext).setShowGuard(false);
			}
		});	   
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
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

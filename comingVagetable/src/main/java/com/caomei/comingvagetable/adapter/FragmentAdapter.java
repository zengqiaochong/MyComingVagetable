package com.caomei.comingvagetable.adapter;

import java.util.ArrayList;
import java.util.List;

import com.caomei.comingvagetable.fragment.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{

	private ArrayList<BaseFragment> mFragmentList;
	
	public FragmentAdapter(FragmentManager fragmentManager,ArrayList<BaseFragment> mFragmentList2) {
		super(fragmentManager);
		this.mFragmentList=mFragmentList2;
	}
	public FragmentAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragmentList.size();
	}


	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

}

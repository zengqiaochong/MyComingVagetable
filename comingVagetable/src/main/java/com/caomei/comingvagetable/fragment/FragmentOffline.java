package com.caomei.comingvagetable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caomei.comingvagetable.R;

public class FragmentOffline extends BaseFragment{

	private View view; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_offline, null);
	 
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		 
	}

}

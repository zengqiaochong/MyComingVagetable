package com.caomei.comingvagetable.bean.eventbus;

import java.util.ArrayList;

import com.caomei.comingvagetable.bean.vegedata.VegeAllBeanData;

public class UpdateMainDataSetBean {
	/**
	 * 标志位
	 * 0 表示下拉刷新
	 * 1表示上拉加载
	 */
	private int flag;
	private ArrayList<VegeAllBeanData> mData;
	public UpdateMainDataSetBean(ArrayList<VegeAllBeanData> data,int flag){
		this.mData=data;
		this.flag=flag;
	}
	public ArrayList<VegeAllBeanData> getmData() {
		return mData;
	}
	public int getFlag() {
		return flag;
	}

}

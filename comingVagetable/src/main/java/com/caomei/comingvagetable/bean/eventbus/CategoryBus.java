package com.caomei.comingvagetable.bean.eventbus;

import java.util.ArrayList;

import com.caomei.comingvagetable.bean.vegedata.VegeCategoryData;

public class CategoryBus {
	private ArrayList<VegeCategoryData> mData;
	private int pos;
	public CategoryBus(ArrayList<VegeCategoryData> mdata,int pos){
		this.mData=mdata;
		this.pos=pos;
	}
	public ArrayList<VegeCategoryData> getmData() {
		return mData;
	}
	public int getPos() {
		return pos;
	}
}

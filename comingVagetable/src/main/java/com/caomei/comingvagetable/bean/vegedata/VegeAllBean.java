package com.caomei.comingvagetable.bean.vegedata;

import java.util.ArrayList;
 
public class VegeAllBean {
	
	private int iTotalRecords;
	private int iTotalDisplayRecords;
	private ArrayList<VegeAllBeanData> data;
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public ArrayList<VegeAllBeanData> getData() {
		return data;
	}
	public void setData(ArrayList<VegeAllBeanData> data) {
		this.data = data;
	}
}

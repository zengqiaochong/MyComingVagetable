package com.caomei.comingvagetable.bean.gift;

import java.util.ArrayList;

public class GiftBean {
	private int sEcho;
	private int iTotalRecords;
	private int iTotalDisplayRecords; 
	private ArrayList<GiftData> data;
	public int getsEcho() {
		return sEcho;
	}
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
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
	public ArrayList<GiftData> getData() {
		return data;
	}
	public void setData(ArrayList<GiftData> data) {
		this.data = data;
	}
	
	
}

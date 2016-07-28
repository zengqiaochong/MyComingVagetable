package com.caomei.comingvagetable.bean;

import java.util.ArrayList;

public class LogisticBean {
	private int sEcho;
	private int iTotalRecords;
	private int iTotalDisplayRecords;
	private ArrayList<LogisticData> data;
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
	public ArrayList<LogisticData> getData() {
		return data;
	}
	public void setData(ArrayList<LogisticData> data) {
		this.data = data;
	}
}

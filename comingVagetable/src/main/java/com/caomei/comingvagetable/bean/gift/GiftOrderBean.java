package com.caomei.comingvagetable.bean.gift;

import java.io.Serializable;
import java.util.ArrayList;

public class GiftOrderBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2280085506326078813L;
	private int sEcho;
	private int iTotalRecords;
	private int iTotalDisplayRecords;
	
	private ArrayList<GiftOrderData> data;

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

	public ArrayList<GiftOrderData> getData() {
		return data;
	}

	public void setData(ArrayList<GiftOrderData> data) {
		this.data = data;
	}
	
}

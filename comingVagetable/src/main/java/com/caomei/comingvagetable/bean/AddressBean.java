package com.caomei.comingvagetable.bean;

import java.util.ArrayList;

//{"sEcho":0,"iTotalRecords":0,"iTotalDisplayRecords":0,"data":[]}
public class AddressBean {
	private String sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private ArrayList<AddressData> data=new ArrayList<AddressData>();
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public String getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public ArrayList<AddressData> getData() {
		return data;
	}
	public void setData(ArrayList<AddressData> data) {
		this.data = data;
	}
}

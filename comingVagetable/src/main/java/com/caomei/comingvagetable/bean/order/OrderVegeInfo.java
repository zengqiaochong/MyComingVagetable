package com.caomei.comingvagetable.bean.order;

import java.io.Serializable;

public class OrderVegeInfo implements Serializable{
	public int grade;
	private String vegeid;
	private String vegename;
	private int buyAmount;
	private float subTotal;
	private String remrk;
	public String getVegeid() {
		return vegeid;
	}
	public void setVegeid(String vege_id) {
		this.vegeid = vege_id;
	}
	public String getVegename() {
		return vegename;
	}
	public void setVegename(String vegename) {
		this.vegename = vegename;
	}
	public int getBuyAmount() {
		return buyAmount;
	}
	public void setBuyAmount(int buyAmount) {
		this.buyAmount = buyAmount;
	}
	public float getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}
	public String getRemrk() {
		return remrk;
	}
	public void setRemrk(String remrk) {
		this.remrk = remrk;
	}
	
	
}

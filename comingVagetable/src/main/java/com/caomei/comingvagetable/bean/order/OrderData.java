package com.caomei.comingvagetable.bean.order;

import android.text.TextUtils;

import com.caomei.comingvagetable.bean.AddressData;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderData implements Serializable{
	private static final long serialVersionUID = -8275789382653707399L;
	private String order_id;
	private String orderNO;
	private String buyTime;
	private float totalMoney;
	private String status;
	private String guestname;
	private String remark;
	private String deliver;
	private String deliver_id;
	public int deliverType;
	public String discountName;
	public String discountMoney;

	private AddressData address;
	private ArrayList<OrderVegeInfo> vegeInfo;

	public ArrayList<OrderDiscountInfo> vegeInfo1;

	public class OrderDiscountInfo implements Serializable{
		public String meal_id;
		public String mealName;
		public String mealPrice;
	}
	 
	public String getOrder_id() {
		if(TextUtils.isEmpty(order_id)){
			return null;
		}
		try{
			String[] mStr=order_id.split("'"); 
			String ids=mStr[3];
			return ids;
		}catch(Exception ex){
			
		}
		return null;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrderNO() {
		return orderNO;
	}
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public float getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(float totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGuestname() {
		return guestname;
	}
	public void setGuestname(String guestname) {
		this.guestname = guestname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeliver() {
		return deliver;
	}
	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}
	public String getDeliver_id() {
		return deliver_id;
	}
	public void setDeliver_id(String deliver_id) {
		this.deliver_id = deliver_id;
	}
	public AddressData getAddress() {
		return address;
	}
	public void setAddress(AddressData address) {
		this.address = address;
	}
	public ArrayList<OrderVegeInfo> getVegeInfo() {
		return vegeInfo;
	}
	public void setVegeInfo(ArrayList<OrderVegeInfo> vegeInfo) {
		this.vegeInfo = vegeInfo;
	}
	
	
}

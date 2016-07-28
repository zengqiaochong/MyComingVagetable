package com.caomei.comingvagetable.bean.order;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.vegedata.VegeInfoBean;

public class SignData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String order_id;
	private String orderNO;
	private String buyTime;
	private String totalMoney;
	private String status;
	private String guestname;
	private String remark;
	private String deliver;
	private AddressData address;
	private ArrayList<VegeInfoBean> vegeInfo;
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
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
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
	public AddressData getAddress() {
		return address;
	}
	public void setAddress(AddressData address) {
		this.address = address;
	}
	public ArrayList<VegeInfoBean> getVegeInfo() {
		return vegeInfo;
	}
	public void setVegeInfo(ArrayList<VegeInfoBean> vegeInfo) {
		this.vegeInfo = vegeInfo;
	}
	
	
	
}

package com.caomei.comingvagetable.bean.vegedata;

import java.io.Serializable;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.util.ShareUtil;

public class VegeCartData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2446137766683297108L;
	private String scarid;
	private String user_id;
	private String orderType; 
	private String imgid;
	private int buyAmount;
	private String buyTime;
	private float subTotal;
	private String remark;
	private boolean isSelected;
	private VegeInfoBean vegeInfo=new VegeInfoBean();
	public int grade;
	
	
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public VegeInfoBean getVegeInfo() {
		return vegeInfo;
	}
	public void setVegeInfo(VegeInfoBean vegeInfo) {
		this.vegeInfo = vegeInfo;
	}
	public String getImgid() {
		return imgid;
	}
	public void setImgid(String imgid) {
		this.imgid = imgid;
	}
	public String getScarid() {
		return scarid;
	}
	public void setScarid(String scarid) {
		this.scarid = scarid;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	 
	public int getBuyAmount() {
		return buyAmount;
	}
	public void setBuyAmount(int buyAmount) {
		this.buyAmount = buyAmount;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public float getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getFormatedImgUrl(){
		return CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_IMG,imgid,"PictureOfVege",ShareUtil.getInstance(AppData.mContext).getUserId());
	}
	
	
}

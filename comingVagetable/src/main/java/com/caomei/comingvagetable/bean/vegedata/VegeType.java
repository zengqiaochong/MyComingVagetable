package com.caomei.comingvagetable.bean.vegedata;

import java.io.Serializable;

import android.text.TextUtils;

public class VegeType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9215332297419455380L;
	private String vegeType_id;
	private String vegeTypeName;
	private String remark;
	
	public String getVegeType_id() {
		return vegeType_id;
	}
	public void setVegeType_id(String vegeType_id) {
		this.vegeType_id = vegeType_id;
	}
	public String getVegeTypeName() {
		return vegeTypeName;
	}
	public void setVegeTypeName(String vegeTypeName) {
		this.vegeTypeName = vegeTypeName;
	}
	public String getRemark() {
		if(TextUtils.isEmpty(remark)){
			return "暂无描述";
		}
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
}

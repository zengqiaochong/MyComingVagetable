package com.caomei.comingvagetable.bean.vegedata;

import android.text.TextUtils;

public class VegeCategoryData {
	/** 
	 */
	private String vegetypeid;
	private String remark;
	private String vegetype;

	public String getVegetypeid() {

		if (TextUtils.isEmpty(vegetypeid)) {
			return "-1";
		}
		try {
			String[] mStr = vegetypeid.split("'");
			String ids = mStr[3];
			return ids;
		} catch (Exception ex) {
			return "-1";
		}
	}

	public void setVegetypeid(String vegetypeid) {
		this.vegetypeid = vegetypeid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVegetype() {
		if(TextUtils.isEmpty(vegetype)){
			return "全部";
		}
		return vegetype;
	}

	public void setVegetype(String vegetype) {
		this.vegetype = vegetype;
	}
}

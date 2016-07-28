package com.caomei.comingvagetable.bean.vegedata;

import android.text.TextUtils;
import android.util.Log;

import com.caomei.comingvagetable.CommonData.CommonAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VegeInfoBean implements Serializable {
	private static final long serialVersionUID = -1337192382783225201L;
	public List<MCommons> commons;//等级集合
	private String vege_id;
	private String vegeName;
	private float price;
	private String createTime;
	private float total;
	private String unit;
	private VegeType vegeType = new VegeType();
	private String imgids;
	private String remark;
	public String getRemark() {
		if(TextUtils.isEmpty(remark)){
			return "暂无描述";
		}
		return remark;
	}

	public class MCommons{
		public int grades;//等级0表示有机， 1表示优选， 2表示一级
		public float prices;//价格
		public int sellAmounts;//已销售
		public int amounts;//库存
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String butAmount;

	public String getButAmount() {
		return butAmount;
	}

	public void setButAmount(String butAmount) {
		this.butAmount = butAmount;
	}

	public String getVege_id() {
		return vege_id;
	}

	public void setVege_id(String vege_id) {
		this.vege_id = vege_id;
	}

	public String getVegeName() {
		return vegeName;
	}

	public void setVegeName(String vegeName) {
		this.vegeName = vegeName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCreateTime() {
		if (TextUtils.isEmpty(createTime)) {
			return "暂无数据";
		} else {
			return "上架时间：" + createTime;
		}
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public VegeType getVegeType() {
		return vegeType;
	}

	public void setVegeType(VegeType vegeType) {
		this.vegeType = vegeType;
	}

	public String getImgids() {
		return imgids;
	}

	public void setImgids(String imgids) {
		this.imgids = imgids;
	}

	public ArrayList<String> getFormatedPics() {
		ArrayList<String> res = new ArrayList<String>();
		String temStr = getImgids();
		Log.e("data", " " + temStr);
		String[] temArr = temStr.split(",");
		for (int index = 0; index < temArr.length; index++) {
			if(TextUtils.isEmpty(temArr[index])){
				continue;
			}
			String mUrl = CommonAPI.BASE_URL
					+ String.format(CommonAPI.URL_GET_IMG,
							temArr[index].trim(), "PictureOfVege", 0);
			res.add(mUrl);
		}
		return res;

	}
}

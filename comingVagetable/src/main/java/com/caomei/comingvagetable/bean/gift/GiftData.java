package com.caomei.comingvagetable.bean.gift;

import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Log;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.util.ShareUtil;

public class GiftData {
	private String status;
	private String id;
	private String agent;
	private String agentType;
	private String agent_id;
	private int soldNum;
	private String giftName;
	private long type_id;
	private int num;
	private String typeName;
	private String remark;
	private String minLevelName;
	private float needCredit;
	private int total;
	private String unit;
	private String createTime;
	private float price;
	private String minLevel_id;
	private boolean isSelected;
	private ArrayList<ImgId> imgids;

	public String getImgUrl() {
		if (imgids.size() > 0) {
			return CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_IMG, imgids.get(0).getImgid(),"PictureOfGift",ShareUtil.getInstance(AppData.mContext).getUserId());
		} else {
			return null;
		}

	}

	public ArrayList<ImgId> getImgids() {
		return imgids;
	}

	public void setImgids(ArrayList<ImgId> imgids) {
		this.imgids = imgids;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getVegeImage() {
		String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_IMG, getId(), "PictureOfGift",ShareUtil
						.getInstance(AppData.mContext).getUserId());
		return url;
	}

	public String getStatus() {
		// <span class='label label-warning radius'>已申请_未付款<\/span>
		String tem = status.split(">")[1];// 已申请_未付款<\/span
		tem = tem.split("<")[0];
		Log.e("data", "status " + tem);
		return tem;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		// <input type='checkbox' value='10000100000002' name=''>
		if (TextUtils.isEmpty(id)) {
			return null;
		}
		try {
			String[] mStr = id.split("'");
			String ids = mStr[3];
			Log.e("data", "id " + ids);
			return ids;
		} catch (Exception ex) {

		}
		return null;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public int getSoldNum() {
		return soldNum;
	}

	public void setSoldNum(int soldNum) {
		this.soldNum = soldNum;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public long getType_id() {
		return type_id;
	}

	public void setType_id(long type_id) {
		this.type_id = type_id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMinLevelName() {
		return minLevelName;
	}

	public void setMinLevelName(String minLevelName) {
		this.minLevelName = minLevelName;
	}

	public float getNeedCredit() {
		return needCredit;
	}

	public void setNeedCredit(float needCredit) {
		this.needCredit = needCredit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getUnit() {
		return  unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getMinLevel_id() {
		return minLevel_id;
	}

	public void setMinLevel_id(String minLevel_id) {
		this.minLevel_id = minLevel_id;
	}

	class ImgId {
		private String imgid;

		public String getImgid() {
			return imgid;
		}

		public void setImgid(String imgid) {
			this.imgid = imgid;
		}

	}
}

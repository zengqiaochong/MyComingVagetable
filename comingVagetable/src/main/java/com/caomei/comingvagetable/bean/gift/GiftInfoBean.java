package com.caomei.comingvagetable.bean.gift;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.bean.gift.GiftData.ImgId;
import com.caomei.comingvagetable.util.ShareUtil;

/**
 * 用于礼品详情页面的giftinfobean 有区别于外面的giftbean
 * 
 * @author Wang Xiaojian
 * 
 */
public class GiftInfoBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7288851283625627544L;
	private String id;
	private String giftName;
	private int total;
	private float price;
	private String unit;
	private float needCredit;
	private String createTime;
	private long type_id;
	private String typeName;
	private String agentType;
	private int soldNum;
	private String agent;
	private String agent_id;
	private String status; 
	private String remark;
	private String minLevelName;
	private String minLevel_id;
	private boolean isSelected;
	private ArrayList<ImgId> imgids;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getNeedCredit() {
		return needCredit;
	}

	public void setNeedCredit(float needCredit) {
		this.needCredit = needCredit;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public long getType_id() {
		return type_id;
	}

	public void setType_id(long type_id) {
		this.type_id = type_id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public int getSoldNum() {
		return soldNum;
	}

	public void setSoldNum(int soldNum) {
		this.soldNum = soldNum;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
 

	public String getRemark() {
		if(TextUtils.isEmpty(remark)){
			return "无内容";
		}
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

	public String getMinLevel_id() {
		return minLevel_id;
	}

	public void setMinLevel_id(String minLevel_id) {
		this.minLevel_id = minLevel_id;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ArrayList<ImgId> getImgids() {
		return imgids;
	}

	public void setImgids(ArrayList<ImgId> imgids) {
		this.imgids = imgids;
	}

	public ArrayList<String> getFormatUrl() {
		ArrayList<String> res = new ArrayList<String>();
		for (ImgId imgId : imgids) {
			res.add(CommonAPI.BASE_URL
					+ String.format(CommonAPI.URL_GET_IMG, imgId.getImgid(),"PictureOfGift",
							ShareUtil.getInstance(AppData.mContext).getUserId()));
		}
		return res;
	}

	class ImgId implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2810704666460833126L;
		private String imgid;

		public String getImgid() {
			return imgid;
		}

		public void setImgid(String imgid) {
			this.imgid = imgid;
		}

	}
}

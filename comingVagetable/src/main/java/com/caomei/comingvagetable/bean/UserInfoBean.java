package com.caomei.comingvagetable.bean;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.util.ShareUtil;

import java.io.Serializable;
import java.util.List;

public class UserInfoBean implements Serializable {
	private static final long serialVersionUID = -4604502498468612649L;
	private String postCode;
	private String bigAddress;
	private String smallAddress;
	private String communityName;
	private String community_id;
	private String credits;
	private String money;
	private String jifen;
	private String user_id;
	private String user_name;
	private String username;
	public List<QQNumbers> qq_numbers;
	
	private String photo_id;
	private String nickName;
	private String myIntroduceCode;
	private String introduceCode;
	private String phone;
	private String userLevel;
	private String levelName;
	private String shopingDiscount;
	private String convertDiscount;

	public class QQNumbers{
		public String qq_number;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJifen() {
		return jifen;
	}

	public void setJifen(String jifen) {
		this.jifen = jifen;
	}

	public String getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}

	public String getIntroduceCode() {
		return introduceCode;
	}

	public void setIntroduceCode(String introduceCode) {
		this.introduceCode = introduceCode;
	}

	public String getPhotoUrl() {
		String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_IMG, photo_id,"PictureOfUser",ShareUtil.getInstance(AppData.mContext).getUserId());
		return url;
	}

	public String getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(String photo_id) {
		this.photo_id = photo_id;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getBigAddress() {
		return bigAddress;
	}

	public void setBigAddress(String bigAddress) {
		this.bigAddress = bigAddress;
	}

	public String getSmallAddress() {
		return smallAddress;
	}

	public void setSmallAddress(String smallAddress) {
		this.smallAddress = smallAddress;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getCredits() {
		return credits;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}

	public String getMoney() {
		return money + "元";
	}

	public float getFMoney() {
		try {
			return Float.parseFloat(money);
		}catch (Exception e){
			return 0;
		}
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMyIntroduceCode() {
		return myIntroduceCode;
	}

	public void setMyIntroduceCode(String myIntroduceCode) {
		this.myIntroduceCode = myIntroduceCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getShopingDiscount() {
		return shopingDiscount;
	}

	public void setShopingDiscount(String shopingDiscount) {
		this.shopingDiscount = shopingDiscount;
	}

	public String getConvertDiscount() {
		return convertDiscount;
	}

	public void setConvertDiscount(String convertDiscount) {
		this.convertDiscount = convertDiscount;
	}
}

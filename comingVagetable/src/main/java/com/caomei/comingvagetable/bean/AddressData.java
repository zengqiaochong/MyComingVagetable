package com.caomei.comingvagetable.bean;

import java.io.Serializable;

import android.text.TextUtils;

public class AddressData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7357214393971927913L;
	private String address_id;
	private String community_id;
	private String communityName;
	private String bigAddress;
	private String guestname;
	private int isDefault;
	private String phone;
	private String phoneNo;
	private String postCode;
	private String smallAddress;
	private String user_id;
	private String username;

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress_id() {
		if (address_id.contains("input")) {
			if (TextUtils.isEmpty(address_id)) {
				return null;
			}
			try {
				String[] mStr = address_id.split("'");
				String ids = mStr[3];
				return ids;
			} catch (Exception ex) {
				return null;
			}
		} else {
			return address_id;
		}
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getGuestname() {
		return guestname;
	}

	public void setGuestname(String guestname) {
		this.guestname = guestname;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

}

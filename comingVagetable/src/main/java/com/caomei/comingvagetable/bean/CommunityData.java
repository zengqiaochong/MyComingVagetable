package com.caomei.comingvagetable.bean;

public class CommunityData {
	private String community_id;
	private String communityName; 
	private String simpleName;
	private String bigAddress;
	private String smallAddress;
	private String postCode;
	private String firstAgent_id;
	private String secondAgent_id;
	public String sortLetters;  //显示数据拼音的首字母
	
	
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getFirstAgent_id() {
		return firstAgent_id;
	}
	public void setFirstAgent_id(String firstAgent_id) {
		this.firstAgent_id = firstAgent_id;
	}
	public String getSecondAgent_id() {
		return secondAgent_id;
	}
	public void setSecondAgent_id(String secondAgent_id) {
		this.secondAgent_id = secondAgent_id;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
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
	public String getCommunity_id() {
		return community_id;
	}
	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}	
}

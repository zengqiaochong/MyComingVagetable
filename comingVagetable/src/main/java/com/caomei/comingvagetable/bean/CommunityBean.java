package com.caomei.comingvagetable.bean;

import java.util.ArrayList;

public class CommunityBean {
	private ArrayList<CommunityData> data;

	public ArrayList<CommunityData> getData() {
		return data;
	}

	public void setData(ArrayList<CommunityData> data) {
		this.data = data;
	}

	/**
	 * 获取省份列表
	 * @param
	 * @return 身份列表 （安徽 重庆）
	 */
	public ArrayList<String> getProvince() {
		ArrayList<String> res = new ArrayList<String>();
		try {
			for (CommunityData item : data) {
				if (!res.contains(item.getBigAddress().split(",")[0])) {
					res.add(item.getBigAddress().split(",")[0]);
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return res;
	}
	/**
	 * 获取市列表
	 * @param
	 * @return 
	 */
	public ArrayList<String> getCity(String province) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			for (CommunityData item : data) {
				if (item.getBigAddress().split(",")[0].equals(province)&&
						!res.contains(item.getBigAddress().split(",")[1])
						) {
					res.add(item.getBigAddress().split(",")[1]);
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return res;
	}

	/**
	 * 获取区县列表
	 * 
	 * @param city
	 *            市 （重庆市）
	 * @return 区县列表 （沙坪坝区，江北区）
	 */
	public ArrayList<String> getCounty(String city) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			for (CommunityData item : data) {
				if (item.getBigAddress().split(",")[1].equals(city)) {
					if (!res.contains(item.getBigAddress().split(",")[2])) {
						res.add(item.getBigAddress().split(",")[2]);
					}
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return res;
	}

	/**
	 * 获取小区列表
	 * 
	 * @param area
	 *            区
	 * @return 小区列表 （金沙港湾、松林坡）
	 */
	public ArrayList<CommunityData> getCommunity(String area) {
		ArrayList<CommunityData> res = new ArrayList<CommunityData>();
		try {
			for (CommunityData item : data) {
				if (item.getBigAddress().split(",")[2].equals(area)) {
					if (!res.contains(item.getCommunityName())) {
						res.add(item);
					}
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return res;
	}

}

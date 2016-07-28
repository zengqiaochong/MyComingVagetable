package com.caomei.comingvagetable.bean;

import com.caomei.comingvagetable.CommonData.CommonAPI;

public class CheckUpdateBean {
	private int RESULT_APP_VERSION;
	private int RESULT_TYPE;
	private String RESULT_APP_URL;
	
	public String getRESULT_APP_URL() {
		String temUrl=CommonAPI.BASE_URL+RESULT_APP_URL;
		return temUrl;
	}
	public void setRESULT_APP_URL(String rESULT_APP_URL) {
		RESULT_APP_URL = rESULT_APP_URL;
	}
	public int getRESULT_APP_VERSION() {
		return RESULT_APP_VERSION;
	}
	public void setRESULT_APP_VERSION(int rESULT_APP_VERSION) {
		RESULT_APP_VERSION = rESULT_APP_VERSION;
	}
	public int getRESULT_TYPE() {
		return RESULT_TYPE;
	}
	public void setRESULT_TYPE(int rESULT_TYPE) {
		RESULT_TYPE = rESULT_TYPE;
	}
}

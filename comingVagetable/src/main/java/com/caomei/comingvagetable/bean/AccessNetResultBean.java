package com.caomei.comingvagetable.bean;

import com.caomei.comingvagetable.Enum.AccessNetState;
 

/**
 * 
 */
public class AccessNetResultBean {

	private String result;
	private AccessNetState state;

	public AccessNetResultBean() {
		result = "";
		state = AccessNetState.Initialize;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public AccessNetState getState() {
		return state;
	}

	public void setState(AccessNetState state) {
		this.state = state;
	}
}
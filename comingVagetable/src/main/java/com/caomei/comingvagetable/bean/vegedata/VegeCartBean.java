package com.caomei.comingvagetable.bean.vegedata;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * 
 * @author Wang Xiaojian
 *
 */
public class VegeCartBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private ArrayList<VegeCartData> data=new ArrayList<VegeCartData>();
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public String getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public ArrayList<VegeCartData> getData() {
		return data;
	}
	public void setData(ArrayList<VegeCartData> data) {
		this.data = data;
	}
	
	public String getCartIds(){
		StringBuilder ids=new StringBuilder();
		 
		for(int index=0;index<data.size();index++){
			if(index!=data.size()-1){
				ids.append(data.get(index).getScarid());
				ids.append(";");
			}else{
				ids.append(data.get(index).getScarid());
			}
		}
		 
		return ids.toString();
	} 
	
	/**
	 * 
	 * @return
	 */
	public int getTotalCount(){
		int countRes=0;
		for(VegeCartData data:getData()){
			countRes+=data.getBuyAmount();
		}
		return countRes;
	}
	/** 
	 * @return
	 */
	public float getTotalMoney(){
		float moneyRes=0;
		for(VegeCartData data:getData()){
			moneyRes+=data.getBuyAmount()*data.getVegeInfo().getPrice();
		}
		return moneyRes;
	}
}

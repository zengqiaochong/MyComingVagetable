package com.caomei.comingvagetable.bean.vegedata;

import java.util.ArrayList;

/**
 * ��Ʒ����������
 * @author Wang Xiaojian
 * 
 */
public class VegeCategoryBean {
	private int sEcho;
	private int iTotalDisplayRecords;
	private int iTotalRecords;
	private ArrayList<VegeCategoryData> data;
		
	public String[] getIds(){
		String[] res=new String[data.size()];
		for(int i=0;i<data.size();i++){
			res[i]=data.get(i).getVegetypeid();
		}
		return res;
	}
	public int getsEcho() {
		return sEcho;
	}
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public ArrayList<VegeCategoryData> getData() {
		return data;
	}
	public void setData(ArrayList<VegeCategoryData> data) {
		this.data = data;
	}
	
	
}

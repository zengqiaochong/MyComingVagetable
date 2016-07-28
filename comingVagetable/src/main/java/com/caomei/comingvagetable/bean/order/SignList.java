package com.caomei.comingvagetable.bean.order;

import java.util.ArrayList;
 

public class SignList { 
		
		private String sEcho ;
		private String iTotalRecords;
		private String iTotalDisplayRecords;
		private ArrayList<SignData> data=new ArrayList<SignData>();
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
		public ArrayList<SignData> getData() {
			return data;
		}
		public void setData(ArrayList<SignData> data) {
			this.data = data;
		}
		
		
}

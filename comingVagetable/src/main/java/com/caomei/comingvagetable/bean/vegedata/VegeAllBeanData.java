package com.caomei.comingvagetable.bean.vegedata;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.text.TextUtils;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.ShareUtil;

public class VegeAllBeanData {
	public String vege_id;//商品id
	public String perUnitWeight;//每份的重量
	public String marketPrice;//市场价格
	private String vegeid;
	private String vegeName;
	private String price;//最低价格
	private String unit;
	private String vegetype; 
	private String createTime;
	private String provider;
	private ArrayList<ImageID> imgids; 

	private String agent;
	private String remark;
	private float total;
	private float credit;
	private float perUnitVolume;
	private String pic_barcode_id;
	private String proChannel;
	private String processWay;
	
	public float getPerUnitVolume() {
		return perUnitVolume;
	}
	public void setPerUnitVolume(float perUnitVolume) {
		this.perUnitVolume = perUnitVolume;
	}
	public String getPic_barcode_id() {
		return pic_barcode_id;
	}
	public void setPic_barcode_id(String pic_barcode_id) {
		this.pic_barcode_id = pic_barcode_id;
	}
	public String getProChannel() {
		return proChannel;
	}
	public void setProChannel(String proChannel) {
		this.proChannel = proChannel;
	}
	public String getProcessWay() {
		return processWay;
	}
	public void setProcessWay(String processWay) {
		this.processWay = processWay;
	}
	public String getCreateShortTime() {
		if(TextUtils.isEmpty(createTime)){
			return "未知";
		}
		return createTime.split(" ")[0];
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getProvider() {
		return "供货渠道："+provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * 處理后的id
	 * @return
	 */
	public String getVegeId(){
		if(TextUtils.isEmpty(vegeid)){
			return null;
		}
		try{
			String[] mStr=vegeid.split("'"); 
			String ids=mStr[3];
			return ids;
		}catch(Exception ex){
			
		}
		return null;
	}
	
	public String getVegeid() {
		return vegeid;
	}

	public void setVegeid(String vegeid) {
		this.vegeid = vegeid;
	}

	public String getVegeName() {
		return vegeName;
	}

	public void setVegeName(String vegeName) {
		this.vegeName = vegeName;
	}
	public String getPrice() {
		DecimalFormat df = new DecimalFormat("#0.00");
		String mPrice=String.valueOf(df.format(Float.parseFloat(price)));
		if(TextUtils.isEmpty(mPrice)){
			return "¥ 0.00";
		}
		return "¥ "+mPrice;
	}

	public ArrayList<String> getFormatImgUrls(){
		ArrayList<String> ids=new ArrayList<String>();
		for(ImageID id:getImgids()){
			if(!TextUtils.isEmpty(id.getImgid())){
				String url=CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_IMG,id.getImgid(),"PictureOfVege",ShareUtil.getInstance(AppData.mContext).getUserId());
				ids.add(url);
			}
		}
		return ids;
	}
	
	public ArrayList<ImageID> getImgids() {
		return imgids;
	}


	public void setImgids(ArrayList<ImageID> imgids) {
		this.imgids = imgids;
	}


	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getVegetype() {
		return vegetype;
	}

	public void setVegetype(String vegetype) {
		this.vegetype = vegetype;
	}
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	} 
	 
//	public ArrayList<String> getFormatedImgUrl(){
//		if(TextUtils.isEmpty(imgids)){
//			return null;
//		}else{
//			ArrayList<String> ids=MethodUtil.getArrayList(imgids,",");
//			ArrayList<String> urls=new ArrayList<String>();
//			String url;
//			for(int i=0;i<ids.size();i++){
//				url=CommonValue.BASE_URL+String.format(CommonValue.URL_GET_IMG, ids.get(i),ShareUtil.getInstance(AppData.mContext).getUserId());
//			}
//			return urls;
//		}
//	}
}

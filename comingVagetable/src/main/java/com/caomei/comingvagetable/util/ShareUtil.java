package com.caomei.comingvagetable.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
 
public class ShareUtil {
	
	private static final String FILE_NAME = "comming_vagetable";
	private Context mContext = null;
	private SharedPreferences shared;
	private SharedPreferences.Editor editor;

	private static ShareUtil mInstance; 
	
	public synchronized static ShareUtil getInstance(Context mContext){ 
		if(mInstance == null) 
			mInstance = new ShareUtil(mContext); 
		return mInstance; 
	}
	public ShareUtil(Context context) {
		this.mContext = context;
		shared = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		editor = shared.edit();
	}

	/////////////////////////////////////////////////////////////////////////////////
	//
	//------------------------ -    对外方法      -------------------------------------
	//
	/////////////////////////////////////////////////////////////////////////////////
	
	public boolean getShowGuard(){
		return shared.getBoolean("show_guard", true);
	}
	public void setShowGuard(boolean show){
		editor.putBoolean("show_guard", show);
		editor.commit();
	}
	
	public boolean getIsLogin(){
		return shared.getBoolean("is_login", false);
	}
	
	public void setIsLogin(boolean login){
		editor.putBoolean("is_login", login);
		editor.commit();
	}
	
	public void setUserName(String userName){
		editor.putString("user_name", userName);
		editor.commit();
	}

	public String getUserName(){
		return shared.getString("user_name", "");
	}
	
	/**
	 * 获取上次更新菜单分类的时间 -- 目的是避免每次都去请求 而是间隔一定的 时间在请求。。
	 * @return
	 */
	
	public long getLastUpdateTime(){
		return shared.getLong("start_time", 0L);
	}
	public void setLastUpdateTime(Long time){
		editor.putLong("start_time", time);
		editor.commit();
	}
	/**
	 * 设置主页面所有分类的json数据
	 * @param mJson
	 * Key:"main_catagory_all_data"
	 */
	public void setMainAllCategoryJson(String mJson){
		editor.putString("main_catagory_all_data", mJson);
		editor.commit();
	}
	public String getMainAllCategoryJson(){
		return shared.getString("main_catagory_all_data",null);
	}
	/**
	 * 设置菜品分类的json数据
	 * @param mJson
	 * Key:"vege_category"
	 */
	public void setVegeCategoryJson(String mJson){
		editor.putString("vege_category", mJson);
		editor.commit();
	}
	public String getVegeCategoryJson(){
		return shared.getString("vege_category", null);
	}
	
	/**
	 * 保存用户的userId
	 * @param id
	 * Key:"user_id" 
	 */
	public void setUserId(String id){
		editor.putString("user_id", id);
		editor.commit();		
	}
	public String getUserId(){
		return shared.getString("user_id","0");
	}
	
	/**保存用户购物车中的商品数量*/
	public void setCartVegeCount(int count){
		editor.putInt("vege_count_in_cart", count);
		editor.commit();
 	}
	public int getCartVegeCount(){
		return shared.getInt("vege_count_in_cart",0);
	}
	/**保存用户购物车中的商品总金额*/
	public void setCartVegePrice(float price){
		editor.putFloat("vege_price_in_cart", price);
		editor.commit();
 	}
	public float getCartVegePrice(){
		return shared.getFloat("vege_price_in_cart",0f);
	}
	public boolean isLocked() {
		KeyguardManager mKey = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
		return mKey.inKeyguardRestrictedInputMode();
	} 
	public void setFirstOpenAppTime(long time) {
		editor.putLong("first_date", time);
		editor.commit();
	}

	public long getFirstOpenAppTime() {
		return shared.getLong("first_date", 0);
	} 
	/**
	 * 地位的区/县
	 * @return
	 */
	public String getLocatedCountry(){
		return shared.getString("located_country", "地位失败");
	}
	public void setLocatedCountry(String name) {
		editor.putString("located_country", name);
		editor.commit();
	}

	/**
	 * 定位的小区
	 * @return
	 */
	public String getLocatedCommunity(){
		return shared.getString("located_village", "点击定位");
	}
	public void setLocatedCommunity(String name) {
		editor.putString("located_village", name);
		editor.commit();
	}
	/**
	 * 定位的收货地址的市
	 * @return
	 */
	public String getMyCity(){
		return shared.getString("located_city", "重庆");
	}
	public void setLocatedCity(String name) {
		editor.putString("located_city", name);
		editor.commit();
	}
	/**
	 * 收获小区的id
	 * @return
	 */
	public String getHomeCommunityID(){
		return shared.getString("selected_community_id", "0");
	}
	public void setHomeCommunityID(String id) {
		editor.putString("selected_community_id", id);
		editor.commit();
	}
	
	/**
	 * 我的收获小区名称
	 * @return
	 */
	public String getHomeCommunity(){
		return shared.getString("my_home_community", "点击定位");
	}
	public void setHomeCommunity(String addr) {
		editor.putString("my_home_community", addr);
		editor.commit();
	}
	/**
	 * 设置默认收货地址的id
	 * @param id
	 */
	public void setDefaultAddressId(String id){
		editor.putString("default_address_id", id);
		editor.commit();
	}
	public String getDefaultAddressId(){
		return shared.getString("default_address_id", "-1");
	}
	/**
	 * 设置配送费
	 * @param fee
	 */
	public void setDeliverFee(String fee){
		editor.putString("deliver_fee", fee);
		editor.commit();
	}
	public String getDeliverFee(){
		return shared.getString("deliver_fee", "7");
	}

	/**
	 * 保存单个菜品的体积
	 * @param vegeId
	 * @param vulume
	 */
	public void setVegeVolume(String vegeId,float vulume){
		editor.putFloat("vege_volume_id_"+vegeId, vulume);
		editor.commit();
	}
	public float getVegeVolume(String vegeId){
		return shared.getFloat("vege_volume_id_"+vegeId, 0f);
	}
	/**
	 * 保存购物车中菜品的体积
	 */
	public void setVegeAllVolume(String volume){
		editor.putString("volome_all",volume);
		editor.commit();
	}
	public String getVegeAllVolume(){
		return shared.getString("volome_all","0");
	}
	
}

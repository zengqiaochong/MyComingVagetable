package com.caomei.comingvagetable.CommonData;

/**
 * @author Wang Xiaojian
 *
 */
public class OpCodes { 
	public static final int ADD_TO_CART_REQUEST_FAILED=2;
	public static final int ADD_TO_CART_REQUEST_SUCCESS=3;
	
	public static final int GET_SMS_CODE_FAILED=4;
	public static final int CHECK_SMS_CODE_SUCCESS=5;
	public static final int CHECK_SMS_CODE_FAILED=6;
	
	public static final int REGIST_SUCCESS=7;
	public static final int REGIST_FAILED=8;

	public static final int PRODUCT_INFO_GET_SUCCESS=9;
	public static final int PRODUCT_INFO_GET_FAILED=10;
	
	public static final int CART_DATA_GET_DONE=11;
	public static final int CART_DATA_GET_ERROR=12;
	
	public static final int LOGIN_SUCCESS=13;
	public static final int LOGIN_FAILED=14;

	public static final int ADDRESS_ADD_SUCCESS=15;
	public static final int ADDRESS_ADD_FAILED=16;
	
	
	//获取所有的收货地址ַ
	public static final int GET_ADDRESS_LIST_NULL=17;
	public static final int GET_ADDRESS_LIST_DONE=18;
	public static final int GET_ADDRESS_LIST_ERROR=19; 

	//购物车处理相关
	public static final int DEL_CART_ITEM_ERROR=20;
	public static final int DEL_CART_ITEM_DONE=21;

	public static final int CHANGE_CART_ITEM_COUNT_DONE=22;
	public static final int CHANGE_CART_ITEM_COUNT_ERROR=23;
	
	public static final int SET_DEFAULT_ADDRESS_DONE=24;
	public static final int SET_DEFAULT_ADDRESS_ERROR=25;

	public static final int GET_DEFAULT_ADDRESS_NULL=26;
	public static final int GET_DEFAULT_ADDRESS_DONE=27;
	public static final int GET_DEFAULT_ADDRESS_ERROR=28;
	/**选择收货地址*/
	public static final int CHANGE_USER_ADDRESS=29;

	public static final int SUBMIT_ORDER_DONE=30;
	public static final int SUBMIT_ORDER_ERROR=31;
	
	public static final int MENU_CATEGORY_CLICK=32;
	
	
	public static final int GET_CATEGORY_DATA_DONE=33;
	public static final int GET_CATEGORY_DATA_ERROR=34;
	
	public static final int DEL_USER_ADDRESS_FAILED=35;
	public static final int DEL_USER_ADDRESS_SUCCESS=36;
	

	public static final int ADDRESS_UPDATE_SUCCESS=37;
	public static final int ADDRESS_UPDATE_FAILED=38;

	public static final int NOTIFY_ADD_ADDRESS_SUCCESS=39;
	public static final int NOTIFY_UPDATE_ADDRESS_SUCCESS=40;
	
	public static final int GET_USER_INFO_DONE=41;
	public static final int GET_USER_INFO_ERROR=42;
	
	public static final int GET_COMMUNITY_DATA_ERROR=43;
	public static final int GET_COMMUNITY_DATA_DONE=44;
	
	public static final int SET_HOME_COMMUNITY_DONE=45;

	public static final int GET_VEGE_ALL_DONE=46;
	public static final int GET_VEGE_ALL_ERROR=47;
	 
	public static final int GET_CATEGORY_DATA_NULL=48; 
	
	public static final int GET_VEGE_DATA_BY_CATEGORY_DONE=49; 
	public static final int GET_VEGE_DATA_BY_CATEGORY_ERROR=50; 

	public static final int UPDATE_USER_INFO_ERROR=51;
	public static final int UPDATE_USER_INFO_DONE=52;
	
	public static final int PAY_ORDER_DONE=53;
	public static final int PAY_ORDER_ERROR=54;
	
	public static final int GET_ORDER_TO_PAY_DONE=55;
	public static final int GET_ORDER_TO_PAY_ERROR=56;

	public static final int GET_SIGN_LIST_DONE=57;
	public static final int GET_SIGN_LIST_ERROR=58;

	public static final int GET_TO_EVALUATE_LIST_DONE=59;
	public static final int GET_TO_EVALUATE_LIST_ERROR=60;
	
	public static final int HAS_UPDATE_VERSION=61;
	public static final int GET_UPDATE_INFO_ERROR=62;
	
	public static final int APK_DOWNLOAD_OK=63;
	public static final int UPDATE_PROGRESS_VALUE=64;

	public static final int SUBMIT_SIGN_DONE=65;
	public static final int SUBMIT_SIGN_ERROR=66;

	public static final int GET_ORDER_TO_SEND_DONE=67;
	public static final int GET_ORDER_TO_SEND_ERROR=68;

	public static final int GET_ORDER_INFO_BY_ID_DONE=69;
	public static final int GET_ORDER_INFO_BY_ID_ERROR=70;
	
	public static final int CANCEL_ORDER_DONE=71;
	public static final int CANCEL_ORDER_ERROR=72;

	public static final int GET_ALL_ORDER_ERROR=73;
	public static final int GET_ALL_ORDER_DONE=74;
	
	public static final int GET_GIF_DATA_ERROR=75;
	public static final int GET_GIF_DATA_DONE=76;
	

	public static final int PRODUCT_GIFT_INFO_GET_SUCCESS=77;
	public static final int PRODUCT_GIFT_INFO_GET_FAILED=78;
	
	public static final int APK_DOWNLOAD_ING=79;

	public static final int UPDATE_GIFT_ORDER_ERROR=80;
	public static final int UPDATE_GIFT_ORDER_DONE=81;
	
	public static final int LOG_OUT=82;
	 

	public static final int GET_LOGISTIC_INFO_DONE=83;
	public static final int GET_LOGISTIC_INFO_ERROR=84;

	public static final int EVALUATE_ORDER_DONE=85;
	public static final int EVALUATE_ORDER_ERROR=86;
	
	public static final int DEVEL_FEE_GET_DONE=87;
	public static final int DEVEL_FEE_GET_ERROR=88;
	
	public static final int PURSE_CHARGE_DONE = 89;
	public static final int PURSE_CHARGE_ERROR = 90;

	public static final int GET_ORDER_BY_TIME=91;
	public static final int GET_GIFT_ORDER_BY_TIME=92;
	
	public static final int REFRESH_DATA=93;

	public static final int GET_SEARCH_RESULT_OK=94;
	public static final int GET_SEARCH_RESULT_ERROR=95;
	
	
	public static final int GET_DELIVER_INFO_OK=96;
	public static final int GET_DELIVER_INFO_ERROR=97;

	public static final int CHARGE_MADUN_DONE=98;
	public static final int CHARGE_MADUN_ERROR=99;
	
	
	public static final int VOLUME_OVER=100;
	
	public static final int REQUEST_SUCCESS=101;
	public static final int REQUEST_FAILED=102;
	
	public static final int GO_TO_WXPAY=103;

	public static final int REQUEST_COMPANY_NAME_SUCCESS = 1293;
	public static final int REQUEST_COMPANY_NAME_NULL = 1294;
	public static final int REQUEST_COMPANY_NAME_ERROR = 1295;
	
}

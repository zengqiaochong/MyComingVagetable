package com.caomei.comingvagetable.CommonData;

import com.google.gson.annotations.Until;

/**
 * 通用的接口地址
 * 
 * @author Wang Xiaojian
 * 
 */
public class CommonAPI {

	/**
	 * 获取数据的基础URL 
	 */	public static String BASE_URL = "http://www.happycopy.cn:8080/zouma_t1/";
//	public static String BASE_URL = "http://www.happycopy.cn:8080/zouma/";
//	public static String BASE_URL = "http://192.168.1.100:8080/zouma/";

	/**
	 * <pre>
	 * 获取分类信息的url
	 * 1 iDisplayStart 
	 * 2 iDisplayLength
	 * 3 sEcho
	 * 4 user_id
	 * </pre>
	 */
	public static String URL_VEGE_CATEGORY = BASE_URL
			+ "Vege_getVegeTypesJsonByPage?iDisplayStart=%d&iDisplayLength=%d&sEcho=%d&user_id=%s";

	/**
	 * <pre>
	 * 获取主页菜品信息的接口
	 * 1 community_id
	 * 2 iDisplayStart  
	 * 3 iDisplayLength  
	 * 4 sEcho   
	 * 5 vegeType_id 
	 * 6 city
	 * 7 user_id
	 * </pre>
	 */
	public static String URL_VEGE_MAIN = BASE_URL
			+ "VegeProvide_getVegeProvidesByPage?community_id=%s&iDisplayStart=%d&iDisplayLength=%d&sEcho=%d&vegeType_id=%s&user_id=%s";

	/**
	 * 获取单个菜品信息的接口 1 vegeProvide_id 2 user_id
	 */
	public static String URL_VEGE_INFO = "VegeProvide_getVegeProvideByID?vege_id=%s&user_id=%s";

	/**
	 * <pre>
	 *  获取图片信息的接口
	 * 1 imgid
	 * 2 user_id
	 * </pre>
	 */
	public static String URL_GET_IMG = "Picture_loadPicture?imgid=%s&clazzName=%s&user_id=%s";

	/**
	 * <pre>
	 * 获取购物车信息接口 
	 * 1 iDisplayStart 
	 * 2 iDisplayLength 
	 * 3 sEcho 
	 * 4 user_id
	 * </pre>
	 */
	public static String URL_GET_CART_VEGE = "ShopCar_getShopCarsJsonByPage?iDisplayStart=%d&iDisplayLength=%d&sEcho=%d&user_id=%s";

	/**
	 * 添加菜品到购物车 1 菜品id 2 购买数量 3 注释 4 userid
	 */
	public static String URL_ADD_TO_CART = "ShopCar_addIntoShopCar?vege_id=%s&buyAmount0=%s&buyAmount1=%s&buyAmount2=%s&remark=%s&user_id=%s";
	/**
	 * 从购物车增加数量 1 购物车项目id 2 更改的数值（正数加，负数减） 3 user_id
	 */
	public static String URL_CHANGE_CART_ITEM_COUNT = "ShopCar_updateBuyAmount?car_id=%s&changeVal=%s&user_id=%s";

	/**
	 * 从购物车删除 1 购物车项目id 2 user_id
	 */
	public static String URL_DEL_FROM_CART = "ShopCar_deleteFromShopCar?scar_ids=%s&user_id=%s";

	/**
	 * 获取短信验证码 1 phoneNo 2 user_id:
	 */
	public static String URL_GET_SMS_CODE = "SMSAuth_sendSMSAuthCode?phoneNO=%s&user_id=%s";

	/**
	 * 检查短信验证码 1 phoneNO 2 smsCode 3 user_id
	 */
	public static String URL_CHECK_SMS_CODE = "SMSAuth_checkSMSAuthCode?phoneNO=%s&smsCode=%s&user_id=%s";

	/**
	 * 用户注册接口 1 username 2 password
	 */
	public static String URL_REGIST = "Reg_createGuest?username=%s&password=%s&user_id=-1&code=%s";

	/**
	 * 用户登录接口 1 username 2 password
	 */
	public static String URL_LOGIN = "Login_login_mb?username=%s&password=%s&user_id=-1";

	/**
	 * 从购物车提交订单的接口 1 用户id 2 收货地址id 3 备注 4 购物车订单id 5 user_id
	 */
	public static String URL_SUBMIT_ORDER_FROM_CART = "Order_submitOrderFromCar?guset_id=%s&address_id=%s&remark=%s&carids=%s&user_id=%s&deliver_id=%s&deliverType=%s";

	/**
	 * 获取默认收货地址 1 user_idַ
	 */
	public static String URL_GET_DEFAULT_ADDRESS = "DeAddress_getDefaultAddress?user_id=%s";

	/**
	 * 获取小区店名 1 user_id 2 小区id
	 */
	public static String URL_GET_COMPANY_NAME = "Order_choseStore?user_id=%s&community_id=%s";

	/**
	 * 添加收货地址 1 社区id 2 详细地址ַ 3 收件人姓名 4 联系电话 5 用户id
	 */
	public static String URL_ADD_ADDRESS = "DeAddress_addDeAddress?community_id=%s&smallAddress=%s&guestName=%s&phoneNo=%s&user_id=%s";

	/**
	 * 获取用户所有的收货地址 1 iDisplayStart 2 iDisplayLength 3 sEcho 4 user_id
	 */
	public static String URL_GET_USER_ADDRESS = "DeAddress_getUserDeAddressJsonByPage?iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&user_id=%s";

	/**
	 * 设置默认收货地址 1 address_id 2 user_id
	 */
	public static String URL_SET_DEFAULT_ADDRESS = "DeAddress_setDefalutAddress?address_id=%s&user_id=%s";

	/**
	 * 删除用户收货地址 1 地址id 2 user_id
	 */
	public static String URL_DEL_ADDRESS = "DeAddress_deleteDeAddress?address_id=%s&user_id=%s";

	/**
	 * 修改收货地址 0 addres id; 1 community_id 2 详细地址ַ 3 收件人姓名 4 联系电话 5 邮编 6 user_id
	 */
	public static String URL_UPDATE_ADDRESS = "DeAddress_updateDeAddress?address_id=%s&community_id=%s&smallAddress=%s&guestName=%s&phoneNO=%s&postCode=%s&user_id=%s";

	/**
	 * 获取用户信息 1 user_id
	 */
	public static String URL_GET_USER_INFO = "User_getMyInfo?user_id=%s";

	/**
	 * <pre>
	 * 根据type获取不同的查询结果
	 * 1 type :	all：查询全国所有已被代理的社区，此时areaName可为空
	 *       	province：按省查询，由areaName确定查询的省份
	 *      	city：按市查询，由areaName确定查询的市
	 *       	county：按县/区查询，由areaName确定查询的县/区
	 * 2 areaName:地区名称
	 * 3 user_id
	 * </pre>
	 */
	public static String URL_GET_COMMUNITYLIST = "Community_getCommunityList?type=%s&areaName=%s&user_id=%s";

	/**
	 * <pre>
	 * user_id：待更新信息的用户ID
	 * nickname：昵称，参数验证要求见用户注册接口
	 * phone：联系电话
	 * communityid：用户所属社区id，若用户已关联了社区，则可以不提交该参数，即使提交，已关联的社区也不会更新；否则，若用户还未关联社区，则该参数必填。
	 */
	public static String URL_UPDATE_USER_INFO = "User_updateUser?user_id=%s&nickname=%s&phone=%s&communityid=%s";
	/**
	 * <pre>
	 * 完善用户信息
	 * user_id：待更新信息的用户ID
	 * nickname：昵称，参数验证要求见用户注册接口
	 * phone：联系电话
	 * introduceCode：
	 * communityid：用户所属社区id，若用户已关联了社区，则可以不提交该参数，即使提交，已关联的社区也不会更新；否则，若用户还未关联社区，则该参数必填。
	 */

	public static String URL_COMPLETE_USER_INFO = "User_completeUserInfo?user_id=%s&nickname=%s&phone=%s&introduceCode=%s&community_id=%s";

	/**
	 * 支付宝支付订单的接口
	 */
	public static String URL_PURCHARSE_WITH_ALIPAY = "Pay_purcharseUseAlipay?user_id=%s";
	
	/**
	 * 微信支付订单的接口
	 */
	public static String URL_PURCHARSE_WITH_WXPAY = "Pay_purcharseUseWeipay";
 
	/**
	 * <pre>
	 * 订单查询的通用接口
	 * iDisplayStart：分页查询开始位置
	 * iDisplayLength：当前页显示的最大记录数
	 * sEcho：操作码
	 * 	user_id：当前在线用户的id
	 * type：查询类型，不同取值有如下含义：
	 * 	ORDER_HOUR：查询一个小时内的订单
	 * 	ORDER_DAY：查询一天以内的订单
	 * 	ORDER_WEEK：查询一周内的订单
	 * 	ORDER_MONTH：查询一月内的订单
	 * 	ORDER_3_MONTH：查询三个月内的订单
	 * 	ORDER_ALL：查询所有订单
	 * 	为空时，也将查询所有订单
	 * status :查询的内容标识：
	 * 		0 已下单，未付款
	 * 	1 已付款，未发货
	 * 	2 已发货，未到达
	 * 	3 已到达，未签收
	 * 	4 已签收，未评价
	 * 	5 已评价
	 * 	6 交易关闭
	 * 用户下单后，在未付款之前取消订单；或下单后，在规定时间内未付款；
	 * 
	 */
	public static String URL_GET_ORDER_INFO = "Order_getOrdersJsonByPage?iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&time=%s&user_id=%s&status=%s";

	/**
	 * <pre>
	 * 更新订单状态接口：
	 * 不同状态需要具有不同权限的用户使用。
	 * 已下单_未付款：用户下单后，该状态自动完成；值为0
	 * 已付款_未发货：用户付款后，该状态自动完成；值为1
	 * 已发货_未到达：菜品发货后，状态由地级代理商完成；值为2
	 * 已到达_未签收：菜品到达后，由配送员完成；后台将完成分润；值为3
	 * 已签收_未评价：由用户签收完成；值为4
	 * 已评价：用户在对订单评价后完成；值为5
	 * 
	 * 1 order_id：待更新状态订单的id
	 * 2 newStatus：订单新状态，枚举类型：3.2订单状态（OrderStatus）中订单状态对应的值。如未付款是0，待发货是1。
	 */
	public static String URL_UPDATE_ORDER_STATUS = "Order_updateOrderStatus?order_id=%s&newStatus=%s&user_id=%s";

	/**
	 * <pre>
	 * 查询指定订单的信息
	 *  1 order_id：待查询订单的id
	 * 2 user_id
	 */
	public static String URL_GET_ORDER_INFO_BY_ID = "Order_getOrderInfoByID?order_id=%s&user_id=%s";

	/**
	 * 检测app更新的接口
	 */
	public static String URL_GET_UPDATE_INFO = "App_checkUpdate";

	/**
	 * <pre>
	 *  获取订到的物流信息
	 * 1 iDisplayStart：分页查询开始位置
	 * 2 iDisplayLength：当前页显示的最大记录数
	 * 3 sEcho：操作码
	 * 4 order_id：需要查询物流信息的订单
	 * 5 user_id：当前在线用户的id
	 */
	public static String GET_DELIVERY_INFO_BY_ORDERID = "getDeliveryInfosByOrderID?iDisplayStart=iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&order_id=%s&user_id=%s&orderType=0";

	/**
	 * 取消订单接口
	 * 
	 * <pre>
	 * 1 订单id 
	 * 2 取消原因
	 * 3 user_id
	 */
	public static String URL_CANCEL_ORDER = "Order_cancelOrder?order_id=%s&reason=%s&user_id=%s";

	/**
	 * 查询可兑换礼品接口
	 * 
	 * <pre>
	 * iDisplayStart： 分页查询开始位置
	 * iDisplayLength：当前页显示的最大记录数 
	 * sEcho：操作码
	 * user_id：当前在线用户的id
	 * type_id：礼品类型id，-1表示全部
	 * startDate：搜索起始日期 
	 * endDate：搜索结束日期
	 * key：查询的关键字
	 * community_id：当客户使用该接口时，该字段是必须的。用于查询所属代理区域可兑换的礼品
	 */
	public static String URL_GET_GIFT = "GiftAgent_getAgentGiftJsonByPage?iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&user_id=%s&type_id=%s&startDate=%s&endDate=%s&key=%s&community_id=%s";

	/**
	 * 根据ID查询代理礼品详情接口 1 id：礼品id 2 user_id
	 */
	public static String URL_GET_GIFT_INFO = "GiftAgent_getGiftAgentInfoByID?id=%s&user_id=%s";

	/**
	 * <pre>
	 * GiftOrder_submitGiftOrder  参数：
	 * 1 user_id：兑换礼品用户的id 
	 * 2 id：代理礼品的id，由5.2.7查询获得可兑换礼品列表
	 * 3 amount：兑换的数量 
	 * 4 address_id：配送地址
	 * 5 remark：备注信息
	 */
	public static String URL_UPDATE_GIFT_ORDER = "GiftOrder_submitGiftOrder?user_id=%s&id=%s&amount=%s&address_id=%s&remark=%s";

	/**
	 * <PRE>
	 * GiftOrder_ getOrdersJsonByPage 参数：
	 * 1 iDisplayStart：分页查询开始位置
	 * 2 iDisplayLength：当前页显示的最大记录数
	 * 3 sEcho：操作码 
	 * 4 user_id：当前在线用户的id 
	 * 5 startDate：搜索起始日期
	 * 6 endDate：搜索结束日期
	 * 7  key：查询的关键字 
	 * 8 status：订单状态，参照3.18订单状态枚举类型，取值从0开始；-1表示查询全部状态
	 * GiftOrder_ getGiftOrderJsonByPage
	 */
	public static String URL_GET_GIFT_ORDER_INFO = "GiftOrder_getGiftOrderJsonByPage?iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&user_id=%s&startDate=%s&endDate=%s&key=%s&status=%s";

	/**
	 * <pre>
	 * 参数：
	 * 1 iDisplayStart：分页查询开始位置 
	 * 2 iDisplayLength：当前页显示的最大记录数 
	 * 3 sEcho：操作码
	 * 4 order_id：需要查询物流信息的订单 
	 * 5 user_id：当前在线用户的id 
	 * 6 orderType：订单类型，参照3.3订单类型，取值从0开始 
	 * 7 startDate：搜索起始日期 
	 * 8 endDate：搜索结束日期
	 */
	public static String GET_DELIVER_INFO_BY_ORDER_ID = "DeliverInfo_getDeliveryInfosByOrderID?iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&order_id=%s&user_id=%s&orderType=%s&startDate=%s&endDate=%s";

	/**
	 * <pre>
	 * order_id：订单id
	 * evalLevels： 评价等级，取值参照3.16，从0开始取值。约定分为三部分，用半角的分号分隔，每部分说明如下：
	 * 	1、对地级代理商服务质量的评价，可以看做是对卖家的评价
	 * 	2、对配送服务质量的评论
	 * 	3、对菜品质量的评价，也是对供应商的评价
	 * 		示例：3;3;3表示对上述三种角色的评价等级都为“满意”
	 * 		注意：每部分的顺序是相关的，请与1、2、3分别对应
	 */
	public static String URL_EVEL_ORDER="Eval_evalOrder?order_id=%s&evalLevels=%s&evalContents=%s&user_id=%s";

	/**
	 * <pre>
	 * 更新订单状态接口：
	 * 不同状态需要具有不同权限的用户使用。
	 * 已下单_未付款：用户下单后，该状态自动完成；值为0
	 * 已付款_未发货：用户付款后，该状态自动完成；值为1
	 * 已发货_未到达：菜品发货后，状态由地级代理商完成；值为2
	 * 已到达_未签收：菜品到达后，由配送员完成；后台将完成分润；值为3
	 * 已签收_未评价：由用户签收完成；值为4
	 * 已评价：用户在对订单评价后完成；值为5
	 * 
	 * 1 order_id：待更新状态订单的id
	 * 2 newStatus：订单新状态，枚举类型：3.2订单状态（OrderStatus）中订单状态对应的值。如未付款是0，待发货是1。
	 */
	public static String URL_UPDATE_GIFT_ORDER_STATUS = "GiftOrder_updateOrderStatus?order_id=%s&newStatus=%s&user_id=%s";

	
	/**
	 * 获取配置项
	 * 1 configName：配置项的名称。
	 * 2 user_id
	 */
	public static String URL_GET_CONFIG="Config_getConfig?configName=%s&user_id=%s";
	
	/**
	 * 通過錢包支付訂單
	 * user_id：发起消费动作的用户id
	 * order_id：消费的订单id
	 * money：消费的金额，双精度类型
	 * remark：备注
	 */
	public static String URL_PAY_BY_PURSE="Pay_purcharse?user_id=%s&order_id=%s&money=%s&remark=%s";

	/**
	 * 使用支付宝充值钱包
	 * 1 user_id
	 */
	public static String URL_CHARGE_BY_ALIPAY="Pay_rechargeUseAlipay?user_id=%s";

	/**
	 * 使用微信充值钱包
	 * 1 user_id
	 */
	public static String URL_CHARGE_BY_WXPAY="Pay_rechargeUseWeipay";

	/**
	 * <pre>
	 * 根据关键字查询菜品列表
	 * 1 community_id
	 * 2 iDisplayStart
	 * 3 iDisplayLength
	 * 4 sEcho
	 * 5 key
	 * 6 user_id
	 */
	public static String URL_GET_VEGE_LIST_BY_KEYWORDS="VegeProvide_getVegeProvideBykey?community_id=%s&iDisplayStart=%s&iDisplayLength=%s&sEcho=%s&vegeType_id=-1&key=%s&user_id=%s";
 
	public static String URL_GET_DELIVER_INFO_COMMUNITY_ID="Community_getCommunityByID?community_id=%s&user_id=%s";
	
	public static String URL_CHARGE_MADUN_BY_MONEY="Pay_rechargeCredit?money=%s&user_id=%s";
	
	/**
	 * <pre>
	 * order_id：需要确认的订单id 
	 * isOK：0或者空表示不增加配送费；1表示同意增加配送费
	 * userid
	 */
	public static String URL_COMFIRM_ORDER="Order_confirmOrder?order_id=%s&isOK=%s&user_id=%s";
	
	
	public static String URL_PICTURE_UPLOAD="Picture_setUserPhoto";

	/*
    *找回密码接口
    *
    * */
	public static String URL_BACK_PASSWORD = "User_retrievePasswordForApp?password=%s&user_id=%s&phone=%s&roleType=6";
}

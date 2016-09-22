package com.caomei.comingvagetable.takeaway.bean;

import java.util.List;

/**
 *购物车bean
 * Created by 123456 on 2016/9/2.
 */
public class ShoppingCartType {
    public List<CART> data;
    public String iTotalRecords;  //总数量

    public class CART{
        public String scarid;         //购物车id
        public String  user_id;     //用户id
        public String orderType;   //
        public String imgid;       // 图片id
        public int buyAmount;   //   //数量
        public String buyTime;     //     加入购物车时间
        public float subTotal;    //    总价
        public String remark;      //   备注
        public String grade;       //等级
        public GOODS goodsInfo;  //快餐外卖商品
       /*  public GOODS   vegeInfo;  //菜来了平台商品*/
       public class  GOODS{
           public String goods_id;     //商品id
           public String price;       //价格
           public String unit;         //单位
           public String vegeName;     //名称
           public String total;        //库存
       }
    }



}

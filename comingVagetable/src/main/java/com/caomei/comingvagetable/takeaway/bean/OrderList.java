package com.caomei.comingvagetable.takeaway.bean;

import java.util.List;

/**
 * 外卖、商城订单bean
 * Created by 123456 on 2016/9/2.
 */
public class OrderList {
    public List<TYPE> data;
    public String sEcho;
    public String iTotalRecords;
    public String iTotalDisplayRecords;

    public class TYPE{
        public String order_id;
        public String orderNO;
        public String orderId;
        public String buyTime;
        public String arriveTime;
        public String totalMoney;
        public String status;
        public String guestname;
        public String remark;
        public String deliverName;

        public String deliver_id;
        public String community;
        public ADDRESS address;


        public String deliverType;
        public String operation;
    }
    public class ADDRESS{
        public String smallAddress;   //姓名
        public String username;
        public String phone;
    }
    public class VEG{
        public String vegeid;
        public String vegename;
        public int buyAmount;
        public int subTotal;
        public String remrk;
    }
}

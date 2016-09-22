package com.caomei.comingvagetable.housekeeper.bean;

import java.util.List;

/**
 * Created by 123456 on 2016/9/9.
 */
public class HouseKeeperBean {
    public String RESULT_MSG;
    public int RESULT_TYPE = 1;
    public List<HouseKeeperVege> data;

    public class HouseKeeperVege{
        public String goodsName;//商品名字
        public String goods_id;//商品id
        public String price;//商品价格
        public String unit;//商品单位
        public String goodsType;//商品类型
        public String goodsType_id;
        public String provider_id;//管家id
        public String createTime;//上架时间
        public String remark;//备注
        public String Amount;
        public String sellAmount;
        public String perUnitWeight;
        public String isSoldOut;
        public String isSpecial;
        public String originalPrice;
        public String BASEURL;
        public List<ImageId> imgs;
    }

    public class ImageId{
        //":[{"pic_id":30,"picUrl":"img1473324815563.jpg","isShow":0,"creatTime":"2016-09-08 17:00:10"},{"
        public String pic_id;
        public String picUrl;
        public String isShow;
        public String createTime;
    }
}

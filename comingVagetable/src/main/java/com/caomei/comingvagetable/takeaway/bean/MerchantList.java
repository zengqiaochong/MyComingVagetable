package com.caomei.comingvagetable.takeaway.bean;

import java.util.List;

/**
 * 商家列表（小区管家）
 * Created by 123456 on 2016/9/21.
 */
public class MerchantList {
     public List<MERC> data;

    public class MERC{
        public String  id;    //id
        public String name;   //商家名字（小区管家）
        public String imgid;   //图片id
    }

}

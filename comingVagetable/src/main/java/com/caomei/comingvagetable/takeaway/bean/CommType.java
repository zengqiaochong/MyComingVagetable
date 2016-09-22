package com.caomei.comingvagetable.takeaway.bean;

import java.util.List;

/**
 * 商品类别bean
 * Created by 123456 on 2016/9/2.
 */
public class CommType {
    public List<TYPE> data;

    public class TYPE{

        public int type_id;  //商品id
        public String typeName;  //类别名称
        public boolean isclick;  //是否点击
    }
}

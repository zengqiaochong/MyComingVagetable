package com.caomei.comingvagetable.bean.order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2016/7/22.
 */
public class OrderCompanyName {
    public List<MCompany> data;


    public class MCompany{
        public int deliver_id;
        public String companyName;
        public String deliverType;
    }
}

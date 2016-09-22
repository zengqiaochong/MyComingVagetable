package com.caomei.comingvagetable.bean;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.util.ShareUtil;

import java.util.List;

/**
 * Created by 123456 on 2016/8/24.
 */
public class MyDiscuntBean {
    public List<DiscountBean> data;

    public class DiscountBean{
        public String discount_id;//优惠券id
        public String type;//类型 0表示配送卷 1表示优惠券
        public float money;//面值
        public String isrepeat;//同一张是否可重复使用
        public String ismulti;//是否可与其他券叠加使用
        public float demand;//满多少可以使用
        public String pic_id;//图片id  二张图片id以分号（;）隔开  第一张是未过期和即将过期，第二张是已过期或已使用
        public String name;//优惠券名字
        public String usable;//是否失效 0表示未失效， 1表示已失效, 2表示即将过期
        public String isUsed;//是否已使用 0表示未使用，1表示已使用
        public String startTime;//开始时间
        public String endTime;//结束时间


        public String getOneFormatedImgUrl(){
            return CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_IMG, pic_id.split(";")[0], "PictureOfCommon",
                    ShareUtil.getInstance(AppData.mContext).getUserId());
        }

        public String getSecFormatedImgUrl(){
            return CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_IMG, pic_id.split(";")[1], "PictureOfCommon",
                    ShareUtil.getInstance(AppData.mContext).getUserId());
        }
    }

}
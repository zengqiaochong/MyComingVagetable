package com.caomei.comingvagetable.bean;

import java.util.List;

/**
 * Created by 123456 on 2016/8/11.
 */
public class GetActivityEntity {
    public List<ActivityEntity> data;

    public class ActivityEntity{
        public int id;//图片id
        public String activity_url;//点击图片的链接地址
        public String title;//活动图片标题
    }
}

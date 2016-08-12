package com.caomei.comingvagetable.sortlistview;

import com.caomei.comingvagetable.bean.CommunityData;

import java.util.Comparator;

/**
 * Created by 123456 on 2016/8/10.
 */
public class PinyinComparator implements Comparator<CommunityData> {

    public int compare(CommunityData o1, CommunityData o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }
}

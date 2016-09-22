package com.caomei.comingvagetable.util;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2016/8/19.
 */
public class Utils {

    private static List<Cookie> cookies;

    public static List<Cookie> getCookies() {
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public static void setCookies(List<Cookie> cookies) {
        Utils.cookies = cookies;
    }


}

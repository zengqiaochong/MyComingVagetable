package com.caomei.comingvagetable.util;

import com.loopj.android.http.AsyncHttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * Created by 123456 on 2016/8/19.
 */
public class FinalAsyncHttpClient {
    private AsyncHttpClient client;

    public FinalAsyncHttpClient() {
        client = new AsyncHttpClient();
        client.setConnectTimeout(5);//5s超时
        if (Utils.getCookies() != null) {//每次请求都要带上cookie
            BasicCookieStore bcs = new BasicCookieStore();
            bcs.addCookies(Utils.getCookies().toArray(new Cookie[Utils.getCookies().size()]));
            client.setCookieStore(bcs);
        }
    }

    public AsyncHttpClient getAsyncHttpClient(){
        return this.client;
    }

}

package com.caomei.comingvagetable.util;

import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by 123456 on 2016/8/19.
 */
public class MyNetUtil {
    private AsyncHttpClient client;

    public void getNet(String httpURL, RequestParams params, AsyncHttpResponseHandler handler){
        httpURL = httpURL.replace("https", "http");
        final AccessNetResultBean bean = new AccessNetResultBean();
        client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(httpURL, params, handler);
    }

    public void postNet(String httpURL, RequestParams params, AsyncHttpResponseHandler handler){
        httpURL = httpURL.replace("https", "http");
        final AccessNetResultBean bean = new AccessNetResultBean();
        client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.post(httpURL, params, handler);
    }
}

package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.util.ShareUtil;

/**
 * Created by 123456 on 2016/8/11.
 */
public class PromotionActivity extends BaseActivity{
    private WebView mWebView;
    private ImageView image_back;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_activity);
        init();
        setData();
    }

    private void setData() {
        if(getIntent() != null){
            mWebView.loadUrl(getIntent().getStringExtra("link_url"));
            title.setText(getIntent().getStringExtra("title"));
        }
    }

    private void init() {
        mWebView = (WebView) findViewById(R.id.promotion_webview);
        image_back = (ImageView) findViewById(R.id.iv_back);
        title = (TextView) findViewById(R.id.promotion_title);
        showWebView();
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showWebView(){     // webView与js交互代码
        try {
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            mWebView.addJavascriptInterface(new GetPromotionInfo(), "jsObj");
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    if(!url.contains("ios"))
                        view.loadUrl(url);
                    return true;
                }
            });
            mWebView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetPromotionInfo{
        @JavascriptInterface //sdk17版本以上加上注解
        public void HtmlcallJava(String vege_id){
            //ToastUtil.Show(mContext, "接收数据" + vege_id);
            if (!ShareUtil.getInstance(mContext).getIsLogin()) {
                startNewActivity(LoginActivity.class,
                        R.anim.activity_slide_right_in,
                        R.anim.activity_slide_left_out, false, null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("id", vege_id);
            PromotionActivity.this.startNewActivity(ProductDetailActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, bundle);
        }

        @JavascriptInterface //sdk17版本以上加上注解
        public void HtmlcallJava(String vege_id, String id2, String price){
            //ToastUtil.Show(mContext, "接收数据" + vege_id + "--" + id2 + "--" + price);
            if (!ShareUtil.getInstance(mContext).getIsLogin()) {
                startNewActivity(LoginActivity.class,
                        R.anim.activity_slide_right_in,
                        R.anim.activity_slide_left_out, false, null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("id", vege_id);
            if(!"undefined".equals(id2))
                bundle.putString("id2", id2);
            bundle.putFloat("price", Float.parseFloat(price));
            PromotionActivity.this.startNewActivity(OrderConfirm.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, bundle);
        }
    }
}
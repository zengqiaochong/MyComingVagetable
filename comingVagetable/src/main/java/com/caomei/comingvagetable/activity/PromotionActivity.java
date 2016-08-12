package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.caomei.comingvagetable.R;

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
                PromotionActivity.this.finish();
            }
        });
    }

    private void showWebView(){     // webView与js交互代码
        try {
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            mWebView.addJavascriptInterface(new GetPromotionInfo(), "jsObj");
            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int progress){
                    if(progress >= 80) {
                    }
                }
            });

            mWebView.setOnKeyListener(new View.OnKeyListener() {// webview can go back
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                    return false;
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
        public void HtmlcallJava(String vege_id, String proChannel, String processWay, float perUnitVolume, String perUnitWeight, String unit){
            Bundle bundle = new Bundle();
            bundle.putString("id", vege_id);
            bundle.putString("pc", proChannel);
            bundle.putString("pw", processWay);
            bundle.putFloat("volume", perUnitVolume);
            bundle.putString("weight", "重量：≈" + perUnitWeight  + "kg/" + unit);
            ((MainActivity) mContext).startNewActivity(
                    ProductDetailActivity.class,
                    R.anim.activity_slide_right_in,
                    R.anim.activity_slide_left_out, false, bundle);
        }

    }

}
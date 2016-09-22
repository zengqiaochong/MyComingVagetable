package com.caomei.comingvagetable.wxapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by zengchong on 2016/9/1.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private LinearLayout ly1, ly2;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_share_title);
        init();
    }

    private void init() {
        // 微信注册初始化
        api = WXAPIFactory.createWXAPI(this, "wx5460ccc17f119e8a", true);
        api.registerApp("wx5460ccc17f119e8a");
        api.handleIntent(getIntent(), this);
        cancel = (Button) findViewById(R.id.share_cancel);
        ly1 = (LinearLayout) findViewById(R.id.ly1);
        ly2 = (LinearLayout) findViewById(R.id.ly2);
        ly1.setOnClickListener(new OnClickListenerImpl());
        ly2.setOnClickListener(new OnClickListenerImpl());
        cancel.setOnClickListener(new OnClickListenerImpl());

    }

    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void wechatShare(int flag) {
        if (!api.isWXAppInstalled()) {
            ToastUtil.Show(WXEntryActivity.this, "您还未安装微信客户端");
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        //webpage.webpageUrl = "http://www.happycopy.cn:8080/zouma_t1/zhaoshang-cstx.jsp";
        webpage.webpageUrl = "http://www.zmbok.com/download.html";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        //msg.title = "菜来了招商";  指尖上的菜市场，您的买菜小助手
        //msg.description = "”菜来了“销配平台 加盟火热进行中！！！";
        msg.title = "买菜原来可以这么任性...";
        msg.description = "指尖上的菜市场，您的买菜小助手！";
        //Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_icon);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 1 ? SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        //SendMessageToWX.Req.WXSceneTimeline;发送到朋友圈
        //SendMessageToWX.Req.WXSceneSession;发送到聊天界面
    }

    @Override
    public void onReq(BaseReq arg0) {}

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = WXEntryActivity.this.getResources().getString(R.string.share_is_ok);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = WXEntryActivity.this.getResources().getString(R.string.share_is_cancle);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = WXEntryActivity.this.getResources().getString(R.string.share_is_refuse);
                break;
            default:
                result = WXEntryActivity.this.getResources().getString(R.string.share_is_failed);
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.share_cancel:
                    break;
                case R.id.ly1:
                    wechatShare(1);
                    break;
                case R.id.ly2:
                    wechatShare(3);
                    break;
            }
            WXEntryActivity.this.finish();
        }
    }

}
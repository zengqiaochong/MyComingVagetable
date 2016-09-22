package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.MyDiscuntBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.util.FinalAsyncHttpClient;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2016/9/8.
 */
public class ComplaintActivity extends BaseActivity{
    private ImageView iv_back;
    private EditText title, content;//投诉标题，内容
    private Button submit;//提交投诉
    private String order_id;//投诉的订单id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_activity);
        init();
        initData();
    }

    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        title = (EditText) findViewById(R.id.complaint_title);
        content = (EditText) findViewById(R.id.complaint_content);
        submit = (Button) findViewById(R.id.bt_submit_complaint);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComplaintActivity.super.onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order_id != null && checkedData()){
                    submitComplaint();
                }
            }
        });
    }

    private boolean checkedData() {
        if("".equals(title.getEditableText().toString().trim())){
            ToastUtil.Show(ComplaintActivity.this, "请输入标题");
            return false;
        }
        if("".equals(content.getEditableText().toString().trim())){
            ToastUtil.Show(ComplaintActivity.this, "请输入标题");
            return false;
        }
        return true;
    }

    /*投诉接口*/
    private void submitComplaint() {
        final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_MY_COMPLAIN, order_id, title.getEditableText().toString().trim(),
                content.getEditableText().toString().trim(),ShareUtil.getInstance(mContext).getUserId());//"10000057");
        Log.e("url", "投诉接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                TypeMsgBean mBean = new Gson().fromJson(new String(bytes), TypeMsgBean.class);
                if (mBean.getRESULT_TYPE() == 1) {
                    ToastUtil.Show(ComplaintActivity.this, "投诉成功！");
                }else{
                    ToastUtil.Show(ComplaintActivity.this, mBean.getRESULT_MSG());
                }
                ComplaintActivity.super.onBackPressed();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(ComplaintActivity.this, "对不起，投诉失败，请重试！");
            }
        });

    }

    private void initData() {
        order_id = getIntent().getStringExtra("order_id");
    }
}

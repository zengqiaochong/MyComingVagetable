package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.adapter.MyDiscountAdapter;
import com.caomei.comingvagetable.bean.MyDiscuntBean;
import com.caomei.comingvagetable.util.FinalAsyncHttpClient;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2016/8/24.
 */
public class MyDiscountActivity extends BaseActivity{
    private ImageView ic_back;
    private ListView lv_discount;
    private MyDiscountAdapter adapter;
    private List<MyDiscuntBean.DiscountBean> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_activity);
        init();
        getMyDiscount();
    }

    private void init() {
        lists = new ArrayList<MyDiscuntBean.DiscountBean>();
        ic_back = (ImageView) findViewById(R.id.iv_back);
        lv_discount = (ListView) findViewById(R.id.discount_lv);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDiscountActivity.super.onBackPressed();
            }
        });
        adapter = new MyDiscountAdapter(MyDiscountActivity.this, lists);
        lv_discount.setAdapter(adapter);
    }

    /*优惠券接口*/
    private void getMyDiscount() {
        final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_MY_DISCOUNT, ShareUtil.getInstance(mContext).getUserId());//"10000057");
        Log.e("url", "优惠券接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                MyDiscuntBean data = new Gson().fromJson(new String(bytes), MyDiscuntBean.class);
                List<MyDiscuntBean.DiscountBean> list1 = new ArrayList<MyDiscuntBean.DiscountBean>();
                List<MyDiscuntBean.DiscountBean> list2 = new ArrayList<MyDiscuntBean.DiscountBean>();
                for(MyDiscuntBean.DiscountBean bean : data.data){
                    if(bean.isUsed.equals("0") && !bean.usable.equals("1")){
                        list1.add(bean);
                    }else{
                        list2.add(bean);
                    }
                }
                lists.addAll(list1);
                lists.addAll(list2);
                adapter.changedData(lists);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(MyDiscountActivity.this, "获取优惠券数据失败！");
            }
        });

    }
}

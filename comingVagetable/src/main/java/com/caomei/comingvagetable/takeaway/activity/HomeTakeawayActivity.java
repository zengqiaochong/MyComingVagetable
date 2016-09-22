package com.caomei.comingvagetable.takeaway.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.BaseActivity;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.takeaway.adapter.MerchantListrAdapter;
import com.caomei.comingvagetable.takeaway.bean.MerchantList;
import com.caomei.comingvagetable.util.FinalAsyncHttpClient;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import de.greenrobot.event.EventBus;

/*快餐外卖首页Activity
* */
public class HomeTakeawayActivity extends BaseActivity {
    private  RelativeLayout return_btn,Takeoutfood_tab_layout,activity1_layout;
    private CommonListener mListener;
    private LinearLayout Package1,Package2,Package3;
    private ListView seller_list;
    /*当前商家列表集合*/
    private List<MerchantList.MERC> merchantlist;
    /*商家列表显示适配器*/
    private MerchantListrAdapter merchantadapter;
    /*当前商家集合大小*/
    int commsize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_home_takeaway);
        setview();
        setdata();
        /*获取商家列表*/
        getlist();
    }
    public void   setview(){
       /*返回*/
        return_btn= (RelativeLayout) findViewById(R.id.return_btn);
        /*套餐1*/
        Package1= (LinearLayout) findViewById(R.id.Package1);
        /*套餐1*/
        Package2= (LinearLayout) findViewById(R.id.Package2);
        Takeoutfood_tab_layout= (RelativeLayout) findViewById(R.id.Takeoutfood_tab_layout);
        activity1_layout= (RelativeLayout) findViewById(R.id.activity1_layout);
        Package3= (LinearLayout) findViewById(R.id.Package3);
        /*商家列表*/
        seller_list= (ListView) findViewById(R.id.seller_list);
    }
    public void   setdata(){
        mListener = new CommonListener();
        return_btn.setOnClickListener(mListener);
        Package1.setOnClickListener(mListener);
        Package2.setOnClickListener(mListener);
        Package3.setOnClickListener(mListener);
        Takeoutfood_tab_layout.setOnClickListener(mListener);
        activity1_layout.setOnClickListener(mListener);


    }

    class CommonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.return_btn:
                    finish();
                    break;
              /*  case R.id.Takeoutfood_tab_layout:
                case R.id.activity1_layout:
                case R.id.Package1:
                case R.id.Package2:
                case R.id.Package3:
                    startNewActivity(TakeawayListActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, null);
                    break;*/
            }
        }
    }

    /*获取商家列表*/
    public void getlist(){
        if(ShareUtil.getInstance(mContext).getUserId().equals("0") ||ShareUtil.getInstance(mContext).getUserId()==null ){
            ToastUtil.Show(mContext, "请先登录！");
            return;
        }
        if(  ShareUtil.getInstance(mContext).getHomeCommunityID().equals("0") ||  ShareUtil.getInstance(mContext).getHomeCommunityID()==null ){
            ToastUtil.Show(mContext, "请先选择小区！");
            return;
        }
        pDialog.show();
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_DELIVER_LIST,
                ShareUtil.getInstance(mContext).getHomeCommunityID(),
                ShareUtil.getInstance(mContext).getUserId());
        Log.e("url", "获取外卖 商家列表、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e("url", "获取服务器返回 商家列表、接口: "+ new String(bytes));
                pDialog.dismiss();
                try {
                    MerchantList bean = new Gson().fromJson(new String(bytes), MerchantList.class);
                    if(bean.data== null || bean.data.size() == 0){
                        ToastUtil.Show(mContext,"无商家数据");
                    }else{
                        if(bean.data != null && bean.data.size() > 0){
                            EventBus.getDefault().post(new EventMsg(OpCodes.GET_WAI__DELIVER_LIST_SUCCESS, bean.data));
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.e("url", "数据解析失败！！！！！！！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                pDialog.dismiss();
                ToastUtil.Show(mContext, "获取商家列表失败！");
            }
        });

    }

    public void onEventMainThread(EventMsg msg) {
        switch (msg.getFlag()) {
            /*获取商家列表成功*/
            case OpCodes.GET_WAI__DELIVER_LIST_SUCCESS:
                merchantlist= (List<MerchantList.MERC>) msg.getData();
                if(commsize!=merchantlist.size()) {
                    commsize = merchantlist.size();
                    ViewGroup.LayoutParams param = seller_list.getLayoutParams();
                    param.height = commsize * seller_list.getHeight();
                    seller_list.setLayoutParams(param);
                }

                if( merchantadapter==null){
                    merchantadapter = new MerchantListrAdapter(mContext, merchantlist);
                    seller_list.setAdapter(merchantadapter);
                    seller_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Bundle bundle=new Bundle();
                            bundle.putString("id",merchantlist.get(i).id);
                            startNewActivity(TakeawayListActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, bundle);
                        }
                    });
                }
                merchantadapter.notiDataChanged(merchantlist);
                break;
        }
    }

}

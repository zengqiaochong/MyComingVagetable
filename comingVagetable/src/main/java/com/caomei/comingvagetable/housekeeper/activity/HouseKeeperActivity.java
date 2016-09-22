package com.caomei.comingvagetable.housekeeper.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.BaseActivity;
import com.caomei.comingvagetable.housekeeper.adapter.HouseKeeperAdapter;
import com.caomei.comingvagetable.housekeeper.bean.HouseKeeperBean;
import com.caomei.comingvagetable.housekeeper.util.PullAndUpLoad.MyListener;
import com.caomei.comingvagetable.housekeeper.util.PullAndUpLoad.PullToRefreshLayout;
import com.caomei.comingvagetable.util.FinalAsyncHttpClient;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class HouseKeeperActivity extends BaseActivity {
    private ImageView iv_back;
    private ListView listView;
    private List<HouseKeeperBean.HouseKeeperVege> list;
    private HouseKeeperAdapter adapter;
    private PullToRefreshLayout refresh_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_keeper);
        init();
        getHouseKeeperVegeData();
    }

    private void init() {
        refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ((PullToRefreshLayout) findViewById(R.id.refresh_view)).setOnRefreshListener(new MyListener());
        listView = (ListView) findViewById(R.id.content_view);
        initListView();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HouseKeeperActivity.super.onBackPressed();
            }
        });
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                n = 0;
                getHouseKeeperVegeData();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getHouseKeeperVegeData();
            }
        });
    }

    /**
     * ListView初始化方法
     */
    private void initListView() {
        list = new ArrayList<HouseKeeperBean.HouseKeeperVege>();
        adapter = new HouseKeeperAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HouseKeeperActivity.this, "LongClick on " + parent.getAdapter().getItemId(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HouseKeeperActivity.this, " Click on " + parent.getAdapter().getItemId(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int n;
    private int size = 20;
    /*获取小区内管家所有商品接口*/
    private void getHouseKeeperVegeData(){
        final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_MY_GOODSPROVIDE, getIntent().getStringExtra("community_id"), "")
                + "&vegeType_id=-1&iDisplayStart=" + n + "&iDisplayLength=" + size + "&user_id=" ;//+ ShareUtil.getInstance(mContext).getUserId();
        Log.e("url", "获取小区内管家所有商品接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
                    refresh_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    HouseKeeperBean bean = new Gson().fromJson(new String(bytes), HouseKeeperBean.class);
                    if(bean.RESULT_TYPE == 0){//获取商品失败
                       ToastUtil.Show(HouseKeeperActivity.this, bean.RESULT_MSG);
                    }else{
                        if(bean.data != null && bean.data.size() > 0){
                            if(n == 0){
                                list.clear();
                            }
                            list.addAll(bean.data);
                            n += size;
                            adapter.notiDataChanged(list);
                        }
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(HouseKeeperActivity.this, "获取数据失败！");
                refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
                refresh_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });

    }
}
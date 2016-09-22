package com.caomei.comingvagetable.takeaway.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.BaseActivity;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.takeaway.adapter.CaiTypeListrAdapter;
import com.caomei.comingvagetable.takeaway.adapter.CartListrAdapter;
import com.caomei.comingvagetable.takeaway.adapter.TakeawayListrAdapter;
import com.caomei.comingvagetable.takeaway.bean.CommType;
import com.caomei.comingvagetable.takeaway.bean.ShoppingCartType;
import com.caomei.comingvagetable.takeaway.bean.TakeawaycommodityBean;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.FinalAsyncHttpClient;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/*
* 外卖菜品列表
* */
public class TakeawayListActivity extends BaseActivity {
    private CommonListener mListener;
    private RelativeLayout return_btn,empty_cart,cart_layout1,cart_top_layout;
    private ImageView shoe_Shopping_cart;
    private TextView total_price,freight_text,cart_size,free_shipping;
    private  Button settlement;
    private ListView listView,typelistview,cart_comm_list;
    /*当前菜品集合*/
    private List<TakeawaycommodityBean.HouseKeeperVege> list;
    /*当前类别集合*/
    private List<CommType.TYPE> typelist;
    /*当前购物车集合*/
    private List<ShoppingCartType.CART> cartlist;
    /*菜品显示适配器*/
    private TakeawayListrAdapter adapter;
    /*类别显示适配器*/
    private CaiTypeListrAdapter typeadapter;
    /*购物车显示适配器*/
    private CartListrAdapter cartadapter;
    /*当前操作对象*/
    private int positionid;
    /*当前商品是否已加入购物车*/
    private  boolean  iscart;
    /*当前购物车总价格*/
    private int price=0;
    /*当前购物车总数量*/
    private int size=0;
    /*当前商家id*/
    private String merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_takeaway_list);
        setview();
        setdata();
        if(!TextUtils.isEmpty(getIntent().getStringExtra("id"))){
            merchant=getIntent().getStringExtra("id");
           /*获取菜品*/
            getcaitype();
        /*获取购物车*/
            shoppingcart();
        }else {
            ToastUtil.Show(mContext,"获取信息失败");
        }

    }
    public void   setview(){
        /*返回*/
        return_btn= (RelativeLayout) findViewById(R.id.return_btn);
        /*查看购物车*/
        shoe_Shopping_cart= (ImageView) findViewById(R.id.shoe_Shopping_cart);
        /*总价格*/
        total_price= (TextView) findViewById(R.id.total_price);
        /*配送费*/
        freight_text= (TextView) findViewById(R.id.freight_text);
        //免邮
        free_shipping= (TextView) findViewById(R.id.free_shipping);
        /*结算*/
        settlement= (Button) findViewById(R.id.settlement);
        /*listview*/
        listView= (ListView) findViewById(R.id.listview);
        /*菜品类别*/
        typelistview= (ListView) findViewById(R.id.typelistview);
        /*购物车列表*/
        cart_comm_list= (ListView) findViewById(R.id.cart_comm_list);
        /*清空购物车*/
        empty_cart= (RelativeLayout) findViewById(R.id.empty_cart);
        /*购物车显示与隐藏*/
        cart_layout1= (RelativeLayout) findViewById(R.id.cart_layout1);
        /*购物车数量*/
        cart_size= (TextView) findViewById(R.id.cart_size);
        cart_top_layout= (RelativeLayout) findViewById(R.id.cart_top_layout);
    }
    public void   setdata(){
        mListener = new CommonListener();
        return_btn.setOnClickListener(mListener);
        shoe_Shopping_cart.setOnClickListener(mListener);
        settlement.setOnClickListener(mListener);
        empty_cart.setOnClickListener(mListener);
        cart_top_layout.setOnClickListener(mListener);
        freight_text.setText("另需配送费1元");
        free_shipping.setText("满10元起送");
        typelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                upcolor();
                typelist.get(position).isclick=true;
                typeadapter.notiDataChanged(typelist);
                getcai(typelist.get(position).type_id,merchant);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void upcolor(){
     for (int i=0;i<typelist.size();i++){
         typelist.get(i).isclick=false;
     }
    }
    /*获取菜品类别列表*/
    public void getcaitype(){
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_COMM_TYPE, 1,ShareUtil.getInstance(mContext).getUserId());
        Log.e("url", "获取快餐外卖菜品、类型、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    Log.e("url", "获取服务器返回数据: "+ new String(bytes));
                    CommType bean = new Gson().fromJson(new String(bytes), CommType.class);
                    if(bean.data== null || bean.data.size() == 0){
                        ToastUtil.Show(mContext,"无菜品类型数据");
                    }else{
                        if(bean.data != null && bean.data.size() > 0){
                            typelist=bean.data;
                            if(adapter==null){
                                typelist.get(0).isclick=true;
                                typeadapter = new CaiTypeListrAdapter(mContext, typelist);
                                typelistview.setAdapter(typeadapter);
                            }
                            typeadapter.notiDataChanged(typelist);
                        }
                        getcai(typelist.get(0).type_id,merchant);
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(mContext, "获取类别数据失败！");
            }
        });
    }
    /*加入购物车*/
    public void  addcart(String id,int size){
        if(ShareUtil.getInstance(mContext).getUserId().equals("0") ||ShareUtil.getInstance(mContext).getUserId()==null ){
            ToastUtil.Show(mContext, "请先登录！");
            return;
        }
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_ADD_SHOW_CART, id,size,ShareUtil.getInstance(mContext).getUserId());
        Log.e("url", "加入快餐外卖菜品、购物车、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    Log.e("url", "获取服务器返回数据: "+ new String(bytes));
                    TakeawaycommodityBean bean = new Gson().fromJson(new String(bytes), TakeawaycommodityBean.class);
                    if(bean.RESULT_TYPE==0){
                        ToastUtil.Show(mContext,bean.RESULT_MSG);
                    }else{
                        ToastUtil.Show(mContext,"加入购物车成功");
                        shoppingcart();
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(mContext, "加入购物车数据失败！");
            }
        });
    }
    /*删除购物车*/
    public void  deletecart(String id){
        if(ShareUtil.getInstance(mContext).getUserId().equals("0") ||ShareUtil.getInstance(mContext).getUserId()==null ){
            ToastUtil.Show(mContext, "请先登录！");
            return;
        }
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_DEL_FROM_CART, id, ShareUtil.getInstance(mContext).getUserId());
        Log.e("url", "删除快餐外卖菜品、购物车、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    Log.e("url", "获取服务器返回数据: "+ new String(bytes));
                    TakeawaycommodityBean bean = new Gson().fromJson(new String(bytes), TakeawaycommodityBean.class);
                    if(bean.RESULT_TYPE==0){
                        ToastUtil.Show(mContext,"删除商品数据失败");
                    }else{
                        ToastUtil.Show(mContext,"删除商品成功");
                        shoppingcart();
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(mContext, "删除商品数据失败！");
            }
        });
    }
/*查看购物车*/
    public void shoppingcart(){
        if(ShareUtil.getInstance(mContext).getUserId().equals("0") ||ShareUtil.getInstance(mContext).getUserId()==null ){
            ToastUtil.Show(mContext, "请先登录！");
            return;
        }
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_SHOW_CART, 0,10000,1,ShareUtil.getInstance(mContext).getUserId());
        Log.e("url", "获取快餐外卖菜品、购物车、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Log.e("url", "获取服务器返回数据外卖菜品、购物车、接口: "+ new String(bytes));
                try {
                    ShoppingCartType bean = new Gson().fromJson(new String(bytes), ShoppingCartType.class);
                if(bean.data== null || bean.data.size() == 0){
                    ToastUtil.Show(mContext,"无购物车数据,请先选择菜品");
                    }else{
                        if(bean.data != null && bean.data.size() > 0){
                            cartlist=new ArrayList<>();
                            for(int s=0;s<bean.data.size();s++){
                                if(bean.data.get(s).goodsInfo!=null){
                                    cartlist.add(bean.data.get(s));
                                }
                            }
                                if(cartlist==null || cartlist.size() == 0){
                                    cartlist.clear();
                                    showcart();
                                   return;
                            }
                            EventBus.getDefault().post(new EventMsg(OpCodes.GET_WAI_CART_SUCCESS, cartlist));

                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.e("url", "数据解析失败！！！！！！！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(mContext, "获取购物车数据失败！");
            }
        });

    }


    //从购物车增加数量 1 购物车项目id 2 更改的数值（正数加，负数减） 3 user_id
    public void changeCartItemCount(final String scarid, final double val) {
        pDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_CHANGE_CART_ITEM_COUNT, scarid, val, ShareUtil.getInstance(mContext).getUserId());
                AccessNetResultBean bean = NetUtil.getInstance(mContext).getDataFromNetByGet(url);
                Log.e("url", "从购物车增加数量 1 购物车项目id 2 更改的数值（正数加，负数减） 3 user_id、接口: "+ bean.getResult());
                pDialog.dismiss();
                if (bean.getState() == AccessNetState.Success) {
                    TypeMsgBean sBean = new Gson().fromJson(bean.getResult(), TypeMsgBean.class);
                    if (sBean.getRESULT_TYPE() == 1) {
                        EventBus.getDefault().post(new EventMsg(OpCodes.CHANGE_CART_ITEM_COUNT_DONE,
                                sBean.getRESULT_CAR_VOLUME()));
                    } else {
                        EventBus.getDefault().post(new EventMsg(OpCodes.CHANGE_CART_ITEM_COUNT_ERROR, sBean.getRESULT_MSG()));
                    }
                } else {
                    ToastUtil.Show(mContext, "操作失败！");
                }
            }
        }).start();
    }
    /*获取菜品列表*/
    public void getcai(int id,String merchant){
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_TASE_OUT_FOOD_COMM_,
                merchant,
                ShareUtil.getInstance(mContext).getHomeCommunityID(),
                id,
                0,
                10000
        );
        Log.e("url", "获取快餐外卖菜品接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                TakeawaycommodityBean bean = null;
                try {
                    bean = new Gson().fromJson(new String(bytes), TakeawaycommodityBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.e("url", "数据解析失败!!!");
                }
                if(bean.data== null || bean.data.size() == 0){
                        //获取商品失败
                        if(list!=null){
                            list.clear();
                            if(adapter!=null){
                                adapter.notiDataChanged(list);
                            }
                        }
                        ToastUtil.Show(mContext,"无菜品数据");
                    }else{
                        if(bean.data != null && bean.data.size() > 0){
                            list=bean.data;
                            if(adapter==null){
                                adapter = new TakeawayListrAdapter(mContext, list, new TakeawayListrAdapter.OnClickincreaseListener() {
                                    @Override
                                    public void onClickincrease(View view, int position) {
                                        if(cartlist==null || cartlist.size()==0){
                                            addcart(list.get(position).goods_id,1);
                                        }else {
                                            for(int i=0;i<cartlist.size();i++){
                                                if(list.get(position).goods_id.equals(cartlist.get(i).goodsInfo.goods_id)){
                                                    iscart=true;
                                                    break;
                                                }
                                                iscart=false;
                                            }
                                            if(iscart){
                                                ToastUtil.Show(mContext,"购物车已有当前菜品，请到购物查看购买!");
                                            }else {
                                                addcart(list.get(position).goods_id,1);
                                            }
                                        }
                                    }
                                });
                                listView.setAdapter(adapter);
                            }
                            adapter.notiDataChanged(list);
                        }
                    }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(mContext, "获取数据失败！");
            }
        });
    }


    public void onEventMainThread(EventMsg msg) {
        switch (msg.getFlag()) {
            /*获取购物车成功*/
            case OpCodes.GET_WAI_CART_SUCCESS:
                showcart();
             break;
        }
    }

    /*显示购物车*/
    public void showcart(){
        Calculatedprice();
        if(cartadapter==null){
            cartadapter = new CartListrAdapter(mContext, cartlist, new CartListrAdapter.OnClickincreaseListener() {
                @Override
                public void onClickincrease(View view, int position) {
                                        /*增加数量*/
                    addcartsize(position);

                }
            }, new CartListrAdapter.OnClickreduceListener() {
                @Override
                public void onClickreduce(int position) {
                                        /*减少数量*/
                    deletedcartsize(position);

                }
            });
            cart_comm_list.setAdapter(cartadapter);
        }
        cartadapter.notiDataChanged(cartlist);

    }
    /*添加购物车商品购物车*/

    public void addcartsize(int position){
   /*增加*/
        if(cartlist.get(position).buyAmount ==  Integer.parseInt(cartlist.get(position).goodsInfo.total)) {
            ToastUtil.Show(mContext, "达到最大库存数");
        }else {
            changeCartItemCount(cartlist.get(position).scarid,1);
            shoppingcart();
        }
    }
  /*计算减购物车*/
    public void deletedcartsize(int position){
        /*减少*/
        if(cartlist.get(position).buyAmount==0){
        }
        if(cartlist.get(position).buyAmount==1){
            deletecart(String.valueOf(cartlist.get(position).scarid));
        }else {
            changeCartItemCount(cartlist.get(position).scarid,-1);
        }
        shoppingcart();
    }
    /*计算价格和数量*/
public void  Calculatedprice(){
    price=0;
    size=0;
    for(int i=0;i<cartlist.size();i++){
      price+=cartlist.get(i).subTotal;
      size+=cartlist.get(i).buyAmount;
    }
    total_price.setText("￥"+price);
    cart_size.setText(""+size);
}
    class CommonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.return_btn:
                    finish();
                    break;
                /*查看购物车*/
                case R.id.shoe_Shopping_cart:
                   /*查看购物车列表*/
                    if(cart_layout1.getVisibility()==View.VISIBLE){
                        cart_layout1.setVisibility(View.GONE);
                    }else {
                        cart_layout1.setVisibility(View.VISIBLE);
                    }
                    shoppingcart();
                    break;
                /*结算*/
                case R.id.settlement:
                    if( cartlist==null ||cartlist.size()==0){
                        ToastUtil.Show(mContext, "购物车为空,请先选择菜品！");
                        return;
                    }
                    if( price<10){
                        ToastUtil.Show(mContext, "满10元才能起送呦!");
                        return;
                    }
                    if( cartlist.size()>4){
                        DialogUtil.DefaultDialog(mContext, "提示", "一个订单饭盒暂时只支持4个菜品，确定仍要购买？", "继续购买", "重新选择", new IDialogOperation() {
                            @Override
                            public void onPositive() {
                                Bundle bundle1=new Bundle();
                                bundle1.putString("merchantid",merchant);
                                startNewActivity(SubmitOrderActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, bundle1);
                            }
                            @Override
                            public void onNegative() {
                            }
                        });
                    }else {
                        Bundle bundle1=new Bundle();
                        bundle1.putString("merchantid",merchant);
                        startNewActivity(SubmitOrderActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, bundle1);
                    }

                    break;
                /*清空购物车*/
                case R.id.empty_cart:
                    if( cartlist==null ||cartlist.size()==0){
                        ToastUtil.Show(mContext, "购物车为空,不需要清空！");
                        return;
                    }
                    DialogUtil.DefaultDialog(mContext, "提示", "是否确认清空购物车？", "清空购物车", "取消", new IDialogOperation() {

                        @Override
                        public void onPositive() {
                            String orderIds = "";
                            for (int i = 0; i < cartlist.size(); i++) {
                                orderIds +=cartlist.get(i).scarid+ ";";
                            }
                            final String idList = orderIds;
                            deletecart(idList);
                        }
                        @Override
                        public void onNegative() {
                        }
                    });
                    break;
                /*隐藏购物车*/
                case R.id.cart_top_layout:
                    cart_layout1.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(cart_layout1.getVisibility()==View.VISIBLE){
            cart_layout1.setVisibility(View.GONE);
        }else {
            finish();
        }
    }
}

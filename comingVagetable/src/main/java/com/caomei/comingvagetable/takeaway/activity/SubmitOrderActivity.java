package com.caomei.comingvagetable.takeaway.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.AddressManageActivity;
import com.caomei.comingvagetable.activity.BaseActivity;
import com.caomei.comingvagetable.activity.PaymentActivity;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.takeaway.adapter.CartListrAdapter;
import com.caomei.comingvagetable.takeaway.bean.ShoppingCartType;
import com.caomei.comingvagetable.takeaway.bean.TakeawaycommodityBean;
import com.caomei.comingvagetable.takeaway.bean.Timelist;
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
* 提交订单界面
* */
public class SubmitOrderActivity extends BaseActivity {
    private RelativeLayout return_btn,yes_adress,list_layout;
    private LinearLayout select_address,select_time,select_number;
    private  ListView comm_list_view;
    private TextView freight_no,total_price,buttom_price,no_adress,address_name,adress_tel,detailed_address,number_meals_text,time_text;
    private EditText order_remarks;
    private  Button btn_sub;
    private CommonListener mListener;
    // 收货地址
    private AddressData addr;
    /*当前购买商品集合*/
    private List<ShoppingCartType.CART> cartlist;
    /*当前购买商品显示适配器*/
    private CartListrAdapter cartadapter;
   /*当前菜品大小*/
   private int commsize=0;
    /*当前购物车总价格*/
    private int price=0;
    /*当前购物车总数量*/
    private int size=0;
    /*提交订单服务器返回数据*/
    TypeMsgBean bean;
    /*当前送餐集合*/
    List<String> timelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_submit_order);
        setview();
        setdata();
         /*获取当前购买商品*/
        shoppingcart();
    }
    public void setview(){
        //返回
        return_btn= (RelativeLayout) findViewById(R.id.return_btn);
        //选择收货地址
        select_address= (LinearLayout) findViewById(R.id.select_address);
        /*收货地址显示*/
        yes_adress= (RelativeLayout) findViewById(R.id.yes_adress);
        /*无收获地址显示*/
        no_adress= (TextView) findViewById(R.id.no_adress);
        /*收货人姓名*/
        address_name= (TextView) findViewById(R.id.address_name);
        /*电话*/
        adress_tel= (TextView) findViewById(R.id.adress_tel);
        /*具体地址*/
        detailed_address= (TextView) findViewById(R.id.detailed_address);
        /*选择送达时间*/
        select_time= (LinearLayout) findViewById(R.id.select_time);
        /*送达时间显示*/
        time_text= (TextView) findViewById(R.id.time_text);
        /*商品显示高度*/
        list_layout= (RelativeLayout) findViewById(R.id.list_layout);
        /*商品显示*/
        comm_list_view= (ListView) findViewById(R.id.comm_list_view);
        /*配送费显示*/
        freight_no= (TextView) findViewById(R.id.freight_no);
        /*总计*/
        total_price= (TextView) findViewById(R.id.total_price);
      /*  *//*选择用餐人数*//*
        select_number= (LinearLayout) findViewById(R.id.select_number);
        *//*显示用餐人数*//*
        number_meals_text= (TextView) findViewById(R.id.number_meals_text);*/
        /*订单备注*/
        order_remarks= (EditText) findViewById(R.id.order_remarks);
        /*结算总价格*/
        buttom_price= (TextView) findViewById(R.id.buttom_price);
        /*提交订单*/
        btn_sub= (Button) findViewById(R.id.btn_sub);


    }
    public void setdata(){
        mListener=new CommonListener();
        return_btn.setOnClickListener(mListener);
        select_address.setOnClickListener(mListener);
        select_time.setOnClickListener(mListener);
    /*    select_number.setOnClickListener(mListener);*/
        btn_sub.setOnClickListener(mListener);
        freight_no.setText("￥1.00");

        yes_adress.setVisibility(View.GONE);
        no_adress.setVisibility(View.VISIBLE);
    }

    /*获取购买商品*/
    public void shoppingcart(){
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_SHOW_CART, 0,10000,1,ShareUtil.getInstance(mContext).getUserId());
        Log.e("url", "获取快餐外卖结算页面、菜品、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e("url", "获取服务器返回数据外卖菜品、接口: "+ new String(bytes));
                try {
                    ShoppingCartType bean = new Gson().fromJson(new String(bytes), ShoppingCartType.class);
                    if(bean.data== null || bean.data.size() == 0){
                        ToastUtil.Show(mContext,"无菜品数据,请先选择菜品");
                    }else{
                        if(bean.data != null && bean.data.size() > 0){
                            cartlist=new ArrayList<>();
                            for(int s=0;s<bean.data.size();s++){
                                if(bean.data.get(s).goodsInfo!=null){
                                    cartlist.add(bean.data.get(s));
                                }
                            }
                            EventBus.getDefault().post(new EventMsg(OpCodes.GET_WAI_COMM_LIST_SUCCESS, cartlist));

                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.e("url", "数据解析失败！！！！！！！");
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(mContext, "获取菜品数据失败！");
            }
        });

    }

    public void onEventMainThread(EventMsg flag) {
        switch (flag.getFlag()) {
            /*地址设置成功*/
            case OpCodes.CHANGE_USER_ADDRESS:
                addr = (AddressData) flag.getData();
                setAddressPanel(addr);
                break;
                /*获取所购买商品成功*/
            case OpCodes.GET_WAI_COMM_LIST_SUCCESS:
                 Calculatedprice();
                if(commsize!=cartlist.size()) {
                    commsize = cartlist.size();
                    ViewGroup.LayoutParams param = list_layout.getLayoutParams();
                    param.height = commsize * list_layout.getHeight();
                    list_layout.setLayoutParams(param);
                }
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
                     comm_list_view.setAdapter(cartadapter);
                }
                cartadapter.notiDataChanged(cartlist);
                break;
              /*获取送餐时间成功*/
            case OpCodes.GET_WAI_TIME_SUCCESS:
                timelist= (List<String>) flag.getData();
                final String[] itemtime=new String[timelist.size()];
                     for(int i=0;i<timelist.size();i++){
                         itemtime[i]= timelist.get(i);
                     }
                    new AlertDialog.Builder(mContext).setTitle("选择用餐时间")
                            .setItems(itemtime, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ToastUtil.Show(mContext,"选择用餐时间为：" + itemtime[i]);
                                    time_text.setText(itemtime[i]);
                                }
                            })
                            .show();
                break;
        }
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
    /*删除购物车商品*/
    public void  deletecart(String id){
        pDialog.show();
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
                pDialog.dismiss();
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
                pDialog.dismiss();
                ToastUtil.Show(mContext, "删除商品数据失败！");
            }
        });
    }

    /*计算价格和数量*/
    public void  Calculatedprice(){
        price=0;
        size=0;
        for(int i=0;i<cartlist.size();i++){
            price+=cartlist.get(i).subTotal;
            size+=cartlist.get(i).buyAmount;
        }
        total_price.setText("待支付 ￥"+price);
        buttom_price.setText(" ￥"+price);
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
   private void setAddressPanel(AddressData data) {
       /*显示地址*/
        yes_adress.setVisibility(View.VISIBLE);
        no_adress.setVisibility(View.GONE);
        //姓名
       address_name.setText((data.getUsername() == null ? data.getGuestname() : data.getUsername())+"");
       //电话
       adress_tel.setText( data.getPhoneNo()+"");
       /*具体配送地址*/
       detailed_address.setText("" + data.getSmallAddress());

    }

    class CommonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                /*返回*/
                case R.id.return_btn:
                  finish();
                     break;
                /*选择收货地址*/
                case R.id.select_address:
                    startNewActivity(AddressManageActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, null);
                    break;
                /*选择时间*/
                case R.id.select_time:
                    pDialog.show();
                    gettime();
                    break;
/*                 *//*选择用餐人数*//*
                case R.id.select_number:
               final String[] item=new String[]{"1人", "2人", "3人", "4人", "5人", "6人", "7人", "8人", "9人", "10人", "10人以上"};
                new AlertDialog.Builder(mContext).setTitle("选择用餐人数")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                number_meals_text.setText(item[i]);
                            }
                        })
                        .show();
                    break;*/
             /*提交订单*/
                case R.id.btn_sub:
                       if(isok()){
                           String orderIds = "";
                           for (int i = 0; i < cartlist.size(); i++) {
                               orderIds +=cartlist.get(i).scarid+ ";";
                           }
                           final String idList = orderIds;
                           pDialog.setMessage("提交订单...");
                           pDialog.show();
                           submitsorder(idList);
                       }
                    break;

            }
        }
    }
/*获取送餐时间*/
    public void  gettime(){
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_TIME_LIST,
                ShareUtil.getInstance(mContext).getUserId()
        );
        Log.e("url", "获取送餐时间、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    pDialog.dismiss();
                    Log.e("url", "获取送餐时间获取服务器返回数据: "+ new String(bytes));
                    Timelist bean = new Gson().fromJson(new String(bytes), Timelist.class);

                    if(bean.data==null || bean.data.size()==0){
                        ToastUtil.Show(mContext,"暂无送餐时间，请明天再来下单!");
                    }else{
                        EventBus.getDefault().post(new EventMsg(OpCodes.GET_WAI_TIME_SUCCESS, bean.data));
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                pDialog.dismiss();
                ToastUtil.Show(mContext, "获取送餐时间失败！");
            }
        });

    }
    /*提交订单*/
    public void submitsorder(String id){
         /*
   *   user_id 用户id
   *  address_id 地址id
   *  remark     备注
   *  carids    购物车id
   *  guset_id    用户id
   *  deliver_id   小区id
   *  deliverType  订单类型 1送货上门
   *
   * */
        String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_SUBMIT_WAI_ORDER,
                ShareUtil.getInstance(mContext).getUserId(),
                addr.getAddress_id()
                ,order_remarks.getEditableText().toString(),
                id,
                ShareUtil.getInstance(mContext).getUserId()
                ,getIntent().getStringExtra("merchantid"),
                1
        );
        Log.e("url", "提交快餐外卖订单、接口: " + url);
        AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    pDialog.dismiss();
                    Log.e("url", "获取服务器返回数据: "+ new String(bytes));
                     bean = new Gson().fromJson(new String(bytes), TypeMsgBean.class);
                    if(bean.getRESULT_TYPE()==0){
                        ToastUtil.Show(mContext,"提交订单失败");
                    }else{
                        ToastUtil.Show(mContext,"提交订单成功");
                        String orderId = bean.getRESULT_ORDER_ID();
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", orderId);
                        bundle.putFloat("price", bean.getRESULT_ORDER_RMB());
                        startNewActivity(PaymentActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, true, bundle);
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                pDialog.dismiss();
                ToastUtil.Show(mContext, "提交订单失败！");
            }
        });

    }

    /*判断数据合法*/
    public boolean isok(){
        if(TextUtils.isEmpty(address_name.getText().toString()) || no_adress.getVisibility()==View.VISIBLE){
            ToastUtil.Show(mContext,"请先选择地址!");
            return false;
        }
        if(TextUtils.isEmpty(time_text.getText().toString())|| time_text.getText().toString().equals("请选择送餐时间")){
            ToastUtil.Show(mContext,"请选择送餐时间!");
            return false;
        }
        if(TextUtils.isEmpty( getIntent().getStringExtra("merchantid"))){
            ToastUtil.Show(mContext,"获取商家信息失败，请返回购物车重新结算!");
            return false;
        }

     /*   if(TextUtils.isEmpty(number_meals_text.getText().toString()) || number_meals_text.getText().toString().equals("便于商家带够餐具")){
            ToastUtil.Show(mContext,"请选择用餐人数!");
            return false;
        }*/
        return true;
    }
}

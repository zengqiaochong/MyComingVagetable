package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.MyDiscuntBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.order.OrderCompanyName;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.SelectPicPopupWindow;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
/*
* 提交订单
* */
public class OrderConfirm extends BaseActivity {
	private RadioGroup rg_order, rg_order1, rg_order2;//小区送货门口的group
	private VegeCartBean bean;
	//private ListView listView;
	//private OrderConfirmAdapter mAdapter;
	private boolean noDefaultAddress;
	private ImageView ivBack;
	private CommonListener mListener;
	// 收货地址
	private AddressData addr;
	private LinearLayout llPrice;
	private Button btDone;
	private TextView tvDeliverFee;
	private RelativeLayout llLoc;
	private TypeMsgBean tmb;
	private List<OrderCompanyName.MCompany> listName;
	private RadioButton order_take_of_door, order_provide_home, order_take_of_quarters;//对应的门口取件，送货上门，小区提货
	private RadioGroup order_groups;//配送方式的group
	private TextView tv_note;//温馨提示
	private LinearLayout ly_order_mendian;//选择小区提货时显示
	private String tom_date;//明天的日期
	private String way = "送货上门";//配送方式
	private TextView tv_pay_way;//配送方式的tv
	private String int_way = "1";//选中的配送方式，0表示门口取件，1表示送货上门，2表示小区提货
	private TextView tv_address;//收货地址
	private TextView order_note;//没有门店（小区管家）的情况下给出提示
	private TextView order_remarks;//备注
	private String[] deliverFree;//配送费
	private LinearLayout ly_order_discount;//选择优惠券
	private TextView order_confirm_discount_tv;//显示优惠卷名字
	private int posion;//配送费的下标
	private float total_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		order_confirm_discount_tv = (TextView) findViewById(R.id.order_confirm_discount_tv);
		ly_order_discount = (LinearLayout) findViewById(R.id.ly_order_discount);
		order_remarks = (TextView) findViewById(R.id.order_remarks);
		order_note = (TextView) findViewById(R.id.order_note);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_note = (TextView) findViewById(R.id.tv_note);
		order_groups = (RadioGroup) findViewById(R.id.order_groups);
		ly_order_mendian = (LinearLayout) findViewById(R.id.ly_order_mendian);
		order_take_of_door = (RadioButton) findViewById(R.id.order_take_of_door);
		order_provide_home = (RadioButton) findViewById(R.id.order_provide_home);
		order_take_of_quarters = (RadioButton) findViewById(R.id.order_take_of_quarters);
		rg_order = (RadioGroup) findViewById(R.id.rg_order);
		rg_order1 = (RadioGroup) findViewById(R.id.rg_order1);
		rg_order2 = (RadioGroup) findViewById(R.id.rg_order2);
		//listView = (ListView) findViewById(R.id.lv_order);
		llPrice = (LinearLayout) findViewById(R.id.ll_panel_total_price);
		btDone = (Button) findViewById(R.id.bt_choose_done);
		tvDeliverFee = (TextView) findViewById(R.id.tv_deliver_fees);
		tvDeliverFee.setText(ShareUtil.getInstance(mContext).getDeliverFee());
		llLoc = (RelativeLayout) findViewById(R.id.ll_panel_address);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		order_groups.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
	}

	private void initData() {
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		tvDeliverFee.setText("7");
		posion = 1;
		/*ShareUtil.getInstance(mContext).setDeliverFee("8");*/
		try {
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowDate = sf.format(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sf.parse(nowDate));
			cal.add(Calendar.DATE, +1);
			cal.add(Calendar.HOUR_OF_DAY, +2);
			SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
			tom_date = sf2.format(cal.getTime());
			tv_note.setText("温馨提示：" + tom_date + "  时间：10:30-20:30送货上门；请您确保家中有人取货；谢谢您的惠顾！");
		}catch (Exception e){}
		mListener = new CommonListener();
		bean = (VegeCartBean) getIntent().getSerializableExtra("data");
		if (bean != null) {
			total_price = bean.getTotalMoney();
		}else{
			total_price = getIntent().getFloatExtra("price", 0.0f);
		}
		if(getIntent().getStringExtra("id") != null){
			meal_ids = getIntent().getStringExtra("id");
		}
		if(getIntent().getStringExtra("id2") != null){
			meal_ids = meal_ids + ";" + getIntent().getStringExtra("id2");
		}
		MethodUtil.SetPanelPrice(total_price, llPrice);
		llLoc.setOnClickListener(mListener);
		//mAdapter = new OrderConfirmAdapter(mContext, bean);
		//listView.setAdapter(mAdapter);
		btDone.setOnClickListener(mListener);
		requestDefaultAddress();
		getMyDiscount();
		ly_order_discount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//显示窗口
				menuWindow.showAtLocation(OrderConfirm.this.findViewById(R.id.order_confirm), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				//设置layout在PopupWindow中显示的位置
			}
		});
	}

	private List<MyDiscuntBean.DiscountBean> lists;
	 //自定义的弹出框类
	 private SelectPicPopupWindow menuWindow;
	/*优惠券接口*/
	private void getMyDiscount() {
		lists = new ArrayList<MyDiscuntBean.DiscountBean>();
		final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_MY_DISCOUNT, ShareUtil.getInstance(mContext).getUserId());//"10000057");
		Log.e("url", "优惠券接口: " + url);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				MyDiscuntBean data = new Gson().fromJson(new String(bytes), MyDiscuntBean.class);
				for(MyDiscuntBean.DiscountBean bean : data.data){
					if(bean.isUsed.equals("0") && !bean.usable.equals("1")){
						lists.add(bean);
					}
				}
				if(lists.size() > 0){
					boolean flag = true;
					int index = 0;
					while(flag){
						if(index >= lists.size()){
							flag = false;
						}else if(lists.get(index).demand <= total_price + Float.parseFloat(tvDeliverFee.getText().toString())){
							setDiscountData(index);
							ly_order_discount.setVisibility(View.VISIBLE);
							//实例化SelectPicPopupWindow
							menuWindow = new SelectPicPopupWindow(OrderConfirm.this, myOnitemClickListener, lists);
							flag = false;
						}
						index++;
					}
				}
			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {}
		});
	}

	private AdapterView.OnItemClickListener myOnitemClickListener = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			setDiscountData(i);
		}
	};

	private void changedType(){
		if(lists.size() > 0){
			boolean flag = true;
			int index = 0;
			ly_order_discount.setVisibility(View.GONE);
			discount_ids = "";
			while(flag){
				if(index >= lists.size()){
					flag = false;
				}else if(lists.get(index).demand <= total_price + Float.parseFloat(tvDeliverFee.getText().toString())){
					setDiscountData(index);
					ly_order_discount.setVisibility(View.VISIBLE);
					flag = false;
				}
				index++;
			}
		}
	}

	private void setDiscountData(int i){
		if(lists.get(i).type.equals("0")){
			order_confirm_discount_tv.setText(lists.get(i).money + "元" + lists.get(i).name);
			discount_ids = lists.get(i).discount_id;
			if (deliverFree != null)
				tvDeliverFee.setText((Float.parseFloat(deliverFree[posion]) - lists.get(i).money > 0 ? Float.parseFloat(deliverFree[posion]) - lists.get(i).money : 0) + "");
			MethodUtil.SetPanelPrice(total_price, llPrice);
			if(menuWindow != null && menuWindow.isShowing())
				menuWindow.dismiss();
		}else{
			if(lists.get(i).demand > total_price + Float.parseFloat(tvDeliverFee.getText().toString())){
				ToastUtil.Show(OrderConfirm.this, "对不起，购买" + lists.get(i).demand + "才能使用！");
			}else{
				order_confirm_discount_tv.setText(lists.get(i).money + "元" + lists.get(i).name);
				discount_ids = lists.get(i).discount_id;
				MethodUtil.SetPanelPrice(total_price - lists.get(i).money > 0 ? total_price - lists.get(i).money : 0.0f, llPrice);
				if(deliverFree != null)
					tvDeliverFee.setText(deliverFree[posion]);
				if(menuWindow != null && menuWindow.isShowing())
					menuWindow.dismiss();
			}
		}
	}

	private class OnCheckedChangeListenerImpl implements RadioGroup.OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int i) {
			switch (radioGroup.getCheckedRadioButtonId()){
				case R.id.order_take_of_quarters:
					posion = 0;
					way = "小区内取货";
					int_way = "2";
					String str = "";
					RadioButton button = (RadioButton) rg_order2.findViewById(rg_order2.getCheckedRadioButtonId());
					if(button != null){
						str = "在" + button.getText().toString();
					}
					tv_note.setText("温馨提示：请您与" + tom_date + "  时间：10:30-20:30" + str + "取货；超时取货可能造成冷藏失效，导致您的生鲜产品变质；谢谢您的惠顾！");
					break;
				case R.id.order_take_of_door:
					posion = 2;
					way = "门口取件";
					int_way = "0";
					tv_note.setText("温馨提示：请您与" + tom_date + "  时间：10:30-20:30在家门口取货；10点之后业务员将回收冷藏箱；谢谢您的惠顾！");
					break;
				case R.id.order_provide_home:
					posion = 1;
					way = "送货上门";
					int_way = "1";
					tv_note.setText("温馨提示：" + tom_date + "  时间：10:30-20:30送货上门；请您确保家中有人取货；谢谢您的惠顾！");
					break;
			}
			if(deliverFree != null){
				tvDeliverFee.setText(deliverFree[posion]);
				ShareUtil.getInstance(mContext).setDeliverFee(deliverFree[posion]);
			}
			setMendianRadiogroup(int_way);
			if(tv_pay_way != null){
				tv_pay_way.setText("配送方式：" + way);
			}
			changedType();
		}
	}

	private void requestDefaultAddress() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_DEFAULT_ADDRESS, ShareUtil
						.getInstance(mContext).getUserId());
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				Log.e("url", "default address url: " + url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						AddressData data = new Gson().fromJson(
								bean.getResult(), AddressData.class);
						if (data == null
								|| TextUtils.isEmpty(data.getSmallAddress())) {
							EventBus.getDefault().post(
									new EventMsg(
											OpCodes.GET_DEFAULT_ADDRESS_NULL,
											"没有默认地址"));
						} else {
							EventBus.getDefault().post(
									new EventMsg(
											OpCodes.GET_DEFAULT_ADDRESS_DONE,
											data));
						}
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_DEFAULT_ADDRESS_ERROR,
										"默认地址解析出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_DEFAULT_ADDRESS_ERROR,
									"请求默认地址出错"));
				}
			}
		}).start();
	}

	private void requestCompanyName(String com_id) {
		final String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_COMPANY_NAME, ShareUtil.getInstance(mContext).getUserId(),
				com_id);
		System.out.println("url===========" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				Log.e("url", "default address url: " + url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						OrderCompanyName company = new Gson().fromJson(
								bean.getResult(), OrderCompanyName.class);
						if(company != null && company.data.size() > 0){
							if(company.deliverFree != null)
								deliverFree = company.deliverFree.split(",");
							listName = company.data;
							EventBus.getDefault().post(new EventMsg(OpCodes.REQUEST_COMPANY_NAME_SUCCESS, listName));
						}else{
							if(listName != null){
								listName.clear();
							}
							EventBus.getDefault().post(
									new EventMsg(OpCodes.REQUEST_COMPANY_NAME_NULL, "对不起，该小区暂时不支持送货，请更换小区地址"));
						}
					} catch (Exception ex) {
						if(listName != null){
							listName.clear();
						}
						EventBus.getDefault().post(
								new EventMsg(OpCodes.REQUEST_COMPANY_NAME_ERROR, "门店地址解析出错"));
					}
				} else {
					if(listName != null){
						listName.clear();
					}
					EventBus.getDefault().post(
							new EventMsg(OpCodes.REQUEST_COMPANY_NAME_ERROR, "请求门店出错"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg flag) {
		switch (flag.getFlag()) {
		case OpCodes.GET_DEFAULT_ADDRESS_NULL:
			noDefaultAddress = true;
			Toast.makeText(mContext, "还没有添加收货地址哦~", Toast.LENGTH_SHORT).show();
			order_groups.setVisibility(View.GONE);
			order_note.setText("温馨提示：请先添加收货地址");
			break;
		case OpCodes.GET_DEFAULT_ADDRESS_DONE:
			addr = (AddressData) flag.getData();
			setAddressPanel(addr);
			order_groups.setVisibility(View.VISIBLE);
			requestCompanyName(addr.getCommunity_id());//获取门店名
			break;
		case OpCodes.GET_DEFAULT_ADDRESS_ERROR:
			order_groups.setVisibility(View.GONE);
			order_note.setText("温馨提示：获取收货地址失败，请重试！");
			break;
		case OpCodes.CHANGE_USER_ADDRESS:
			noDefaultAddress = false;
			addr = (AddressData) flag.getData();
			setAddressPanel(addr);
			order_groups.setVisibility(View.VISIBLE);
			requestCompanyName(addr.getCommunity_id());//获取门店名
			break;
		case OpCodes.SUBMIT_ORDER_ERROR:
			try {
				pDialog.dismiss();
			} catch (Exception e) {

			}
			Toast.makeText(mContext, "系统错误：" + flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;

		case OpCodes.VOLUME_OVER:
			 tmb=(TypeMsgBean)flag.getData();
			DialogUtil.DefaultDialog(mContext, "提示",tmb.getRESULT_MSG()+"\n是否同意增加运费？","同意","重新选菜", new IDialogOperation() {

				@Override
				public void onPositive() {
					 requestConfirm(tmb.getRESULT_ORDER_ID(),"1");
				}

				@Override
				public void onNegative() {
					// TODO Auto-generated method stub
					 requestConfirm(tmb.getRESULT_ORDER_ID(),"0");
				}
			});
			break;
		case OpCodes.SUBMIT_ORDER_DONE:
			ShareUtil.getInstance(mContext).setVegeAllVolume("0");
			try {
				pDialog.dismiss();
			} catch (Exception e) {
			}
			TypeMsgBean tBean = (TypeMsgBean) flag.getData();
			ToastUtil.Show(mContext, tBean.getRESULT_MSG());

			String orderId = tBean.getRESULT_ORDER_ID();
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", bean);
			bundle.putString("order_id", orderId);
			bundle.putFloat("price", tBean.getRESULT_ORDER_RMB());
			startNewActivity(PaymentActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, true, bundle);
			break;

		case OpCodes.REQUEST_SUCCESS:
			ToastUtil.Show(mContext, flag.getData().toString());

			ToastUtil.Show(mContext, tmb.getRESULT_MSG());

			String orderId1 = tmb.getRESULT_ORDER_ID();
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("data", bean);
			bundle1.putString("order_id", orderId1);
			bundle1.putFloat("price", tmb.getRESULT_ORDER_RMB());
			startNewActivity(PaymentActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, true, bundle1);
			ShareUtil.getInstance(mContext).setVegeAllVolume("0");
			break;
		case OpCodes.REGIST_FAILED:
			ToastUtil.Show(mContext, flag.getData().toString());
			ShareUtil.getInstance(mContext).setVegeAllVolume("0");
			break;
			case OpCodes.REQUEST_COMPANY_NAME_SUCCESS:
				/*for(OrderCompanyName.MCompany name:listName){
					RadioButton rb = new RadioButton(OrderConfirm.this);
					rb.setText(name.companyName);
					rb.setId(name.deliver_id);
					rg_order.addView(rb);
				}
				if(listName != null && listName.size() > 0){
					((RadioButton)(rg_order.getChildAt(0))).setChecked(true);
				}*/
				order_provide_home.setVisibility(View.VISIBLE);
				setMendianData();
				setMendianRadiogroup(int_way);
				break;
			case OpCodes.REQUEST_COMPANY_NAME_NULL:
				order_groups.setVisibility(View.GONE);
				order_note.setText("温馨提示：对不起，该小区暂时不支持送货，请更换小区地址！");
				ToastUtil.Show(mContext, flag.getData().toString());
				setMendianData();
				break;
			case OpCodes.REQUEST_COMPANY_NAME_ERROR:
				ToastUtil.Show(mContext, flag.getData().toString());
				break;
		default:
			break;
		}
	}

	private void setMendianData() {
		rg_order.clearCheck ();
		rg_order.removeAllViewsInLayout();
		rg_order1.clearCheck ();
		rg_order1.removeAllViewsInLayout();
		rg_order2.clearCheck ();
		rg_order2.removeAllViewsInLayout();
		if(listName != null && listName.size() > 0){
			for(OrderCompanyName.MCompany name:listName){
				String str[] = name.deliverType.split(",");
				for(int i = 0; i < str.length; i++){
					if(str[i].equals("0")){
						RadioButton rb = new RadioButton(OrderConfirm.this);
						rb.setText(name.companyName);
						rb.setId(name.deliver_id);
						rg_order.addView(rb);
					}else if(str[i].equals("1")){
						RadioButton rb = new RadioButton(OrderConfirm.this);
						rb.setText(name.companyName);
						rb.setId(name.deliver_id);
						rg_order1.addView(rb);
					}else if(str[i].equals("2")){
						RadioButton rb = new RadioButton(OrderConfirm.this);
						rb.setText(name.companyName);
						rb.setId(name.deliver_id);
						rg_order2.addView(rb);
					}
				}
			}
			if( rg_order.getChildCount() > 0) {
				((RadioButton) (rg_order.getChildAt(0))).setChecked(true);
			}else{
				order_take_of_door.setVisibility(View.GONE);
			}
			if( rg_order1.getChildCount() > 0) {
				((RadioButton) (rg_order1.getChildAt(0))).setChecked(true);
			}else{
				order_provide_home.setChecked(false);
				if(rg_order2.getChildCount() > 0){
					order_take_of_quarters.setChecked(true);
					int_way = "2";
				}else{
					int_way = "0";
				}
				order_provide_home.setVisibility(View.GONE);
			}
			if( rg_order2.getChildCount() > 0) {
				((RadioButton) (rg_order2.getChildAt(0))).setChecked(true);
			}else{
				order_take_of_quarters.setVisibility(View.GONE);
			}
		}else{
			ly_order_mendian.setVisibility(View.GONE);
		}
	}

	private void setMendianRadiogroup(String type){
		if("0".equals(type)){
			rg_order.setVisibility(View.VISIBLE);
			rg_order1.setVisibility(View.GONE);
			rg_order2.setVisibility(View.GONE);
			if( rg_order.getChildCount() > 0){
				ly_order_mendian.setVisibility(View.VISIBLE);
			}else{
				ly_order_mendian.setVisibility(View.GONE);
			}
		}else if("1".equals(type)){
			rg_order.setVisibility(View.GONE);
			rg_order1.setVisibility(View.VISIBLE);
			rg_order2.setVisibility(View.GONE);
			if( rg_order1.getChildCount() > 0){
				ly_order_mendian.setVisibility(View.VISIBLE);
			}else{
				ly_order_mendian.setVisibility(View.GONE);
			}
		}else if("2".equals(type)){
			rg_order.setVisibility(View.GONE);
			rg_order1.setVisibility(View.GONE);
			rg_order2.setVisibility(View.VISIBLE);
			if( rg_order2.getChildCount() > 0){
				ly_order_mendian.setVisibility(View.VISIBLE);
			}else{
				ly_order_mendian.setVisibility(View.GONE);
			}
		}
	}

	protected void requestConfirm(String orderId,String isOk) {
		final String url=CommonAPI.BASE_URL+String.format(CommonAPI.URL_COMFIRM_ORDER, orderId,isOk,ShareUtil.getInstance(mContext).getUserId());
		new Thread(new Runnable() {

			@Override
			public void run() {
				AccessNetResultBean bean=NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if(bean.getState()==AccessNetState.Success){
					try{
						TypeMsgBean tb=new Gson().fromJson(bean.getResult(), TypeMsgBean.class);
						if(tb.getRESULT_TYPE()==1){
							EventBus.getDefault().post(new EventMsg(OpCodes.REQUEST_SUCCESS, bean.getResult()));
						}else{
							EventBus.getDefault().post(new EventMsg(OpCodes.REQUEST_FAILED, bean.getResult()));
						}
					}catch(Exception ex){
						EventBus.getDefault().post(new EventMsg(OpCodes.REQUEST_FAILED,"处理失败"));
					}
				}else{
					EventBus.getDefault().post(new EventMsg(OpCodes.REQUEST_FAILED, bean.getResult()));
				}
			}
		}).start();
	}

	private void setAddressPanel(AddressData data) {
		LinearLayout panel = (LinearLayout) findViewById(R.id.ly_order_detail);
		panel.setVisibility(View.VISIBLE);
		TextView tvUserName = (TextView) panel.findViewById(R.id.tv_user_name);
		tv_pay_way = (TextView) panel.findViewById(R.id.tv_pay_way);
		TextView tvAddr = (TextView) panel.findViewById(R.id.tv_pay_adress);
		tvUserName.setText("收件人：" + (data.getUsername() == null ? data.getGuestname() : data.getUsername()) + "  联系电话：" + data.getPhoneNo());
		tv_pay_way.setText("配送方式：" + way);
		tvAddr.setText("配送地址：" + data.getSmallAddress());
		tv_address.setText(data.getBigAddress());
	}

	class CommonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_choose_done:
				if (noDefaultAddress) {
					DialogUtil.DefaultDialog(mContext, "提示", "请添加收获地址。", "确认",
							"取消", new IDialogOperation() {
								@Override
								public void onPositive() {
									startNewActivity(AddressManageActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, null);
								}
								@Override
								public void onNegative() {

								}
							});
					return;
				}
				if (CheckBeforeSubmitOrder()) {
					pDialog.setMessage("提交订单...");
					pDialog.show();
					submitOrder();
				}
				break;
			case R.id.ll_panel_address:
				startNewActivity(AddressManageActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);

				break;
			default:
				break;
			}
		}
	}

	public boolean CheckBeforeSubmitOrder() {
		if("0".equals(int_way)){
			delive_id = rg_order.getCheckedRadioButtonId();
		}else if("1".equals(int_way)){
			delive_id = rg_order1.getCheckedRadioButtonId();
		}else if("2".equals(int_way)){
			delive_id = rg_order2.getCheckedRadioButtonId();
		}else{
			delive_id = 0;
		}
		if(delive_id <= 0 || listName == null || listName.size() == 0){
			ToastUtil.Show(OrderConfirm.this, "对不起，请选择或添加新的地址，该地址暂不支持送货！");
			return false;
		}
		return true;
	}

	private String meal_ids = "";//套餐id
	private String discount_ids = "";
	private int delive_id;
	public void submitOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String url = CommonAPI.BASE_URL
							+ String.format(
									CommonAPI.URL_SUBMIT_ORDER_FROM_CART,
									ShareUtil.getInstance(mContext).getUserId(),
									addr.getAddress_id(), order_remarks.getEditableText().toString(),
									bean != null ? bean.getCartIds():"",
									ShareUtil.getInstance(mContext).getUserId(),
							delive_id,
							int_way, discount_ids, meal_ids);
					Log.e("url", "提交订单的url " + url);
					AccessNetResultBean bean = NetUtil.getInstance(mContext)
							.getDataFromNetByGet(url);
					Log.e("data","体积超出后台 "+bean.getResult());
					if (bean.getState() == AccessNetState.Success) {
						try {
							TypeMsgBean sBean = new Gson().fromJson(bean.getResult(), TypeMsgBean.class);
							if (sBean.getRESULT_TYPE() == 1) {
								EventBus.getDefault().post(new EventMsg(OpCodes.SUBMIT_ORDER_DONE, sBean));
							} else if(sBean.getRESULT_TYPE()==-1){
								EventBus.getDefault().post(new EventMsg(OpCodes.VOLUME_OVER, sBean));
							}else{
								EventBus.getDefault().post(new EventMsg(OpCodes.SUBMIT_ORDER_ERROR, sBean.getRESULT_MSG()));
							}
						} catch (Exception ex) {
							EventBus.getDefault().post(new EventMsg(OpCodes.SUBMIT_ORDER_ERROR, "提交订单请求出错"));
						}
					} else {
						EventBus.getDefault().post(new EventMsg(OpCodes.SUBMIT_ORDER_ERROR, "提交订单请求出错"));
					}
				} catch (Exception ex) {
					EventBus.getDefault().post(new EventMsg(OpCodes.SUBMIT_ORDER_ERROR, "提交订单请求出错"));
					Log.e("error", "ex " + ex.getMessage());
				}
			}
		}).start();
	}

	/*** 功能待完善----------------------------------- */
	protected String getUserAddress() {
		StringBuilder mAddr = new StringBuilder();

		mAddr.append(addr.getBigAddress());

		return mAddr.toString();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

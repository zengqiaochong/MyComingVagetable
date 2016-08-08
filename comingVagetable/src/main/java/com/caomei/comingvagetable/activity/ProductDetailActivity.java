package com.caomei.comingvagetable.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.caomei.comingvagetable.bean.vegedata.VegeCartData;
import com.caomei.comingvagetable.bean.vegedata.VegeInfoBean;
import com.caomei.comingvagetable.controls.ColorTextView;
import com.caomei.comingvagetable.util.AppUtil;
import com.caomei.comingvagetable.util.ImageUtil;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.minking.imagecycleview.ImageCycleView;
import com.minking.imagecycleview.PicSlideInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author Wang Xiaojian
 */
public class ProductDetailActivity extends BaseActivity {
	private LinearLayout grade0, grade1, grade2;//对应的等级
	private int total0, total1, total2;//对应的库存的分量
	private TextView tv_product_number, tv_product_number1, tv_product_number2;//对应库存的textview
	private LinearLayout item_price, item_price1, item_price2;//对应的价格linearlayout
	private int total_number, total_number1, total_number2;//对应的已销售的数量，用来显示购买度的

	private ImageView ivBack;
	private ImageView ivMinus;//数量--
	private ImageView ivPlus;//数量++
	private ImageView ivMinus1;//数量--
	private ImageView ivPlus1;//数量++
	private ImageView ivMinus2;//数量--
	private ImageView ivPlus2;//数量++

	private TextView tvBuyCount;//数量
	private TextView tvBuyCount1;//数量
	private TextView tvBuyCount2;//数量

	private ImageCycleView ivProductPic;
	private TextView tvProductName;
	//private TextView tvProductUnit;
	//private TextView tvProductDetails;
	private LinearLayout llItemPrice;
	private ColorTextView tvCartGoodsCount;
	private RelativeLayout panelCart;
	private RelativeLayout mCartPanel;
	private Button btDone;
	private LinearLayout llTotalPrice;

	private String vegeId;
	private Context mContext;
	private CommonListener mListener;

	private ProgressDialog pDialog;
	private VegeInfoBean vBean;
	private ArrayList<PicSlideInfo> infos;
	private TextView tvProductOnSellTime;
	private VegeInfoBean bean;
	private TextView tvDeliverFee;
	private String providerChannel;
	private String processWay;
	private TextView tvProcessWay;
	private TextView tvProviderChannel;
	private TextView tvVolume;
	private float volume;
	private TextView tvTotalVolume;
	private int count, count1, count2;
	private VegeCartBean vegeCartBean;
	private TextView product_detail_content, product_detail_explain;//商品详情，商品说明
	private String weight = "";
	int a, b, c;//对应的有机，优选，一级库存

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_product_detail);
		pDialog = new ProgressDialog(this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("数据加载中...");
		pDialog.setIndeterminate(true);
		pDialog.setCancelable(true);
		pDialog.show();
		EventBus.getDefault().register(this);
		Bundle bundle = getIntent().getExtras();
		infos = new ArrayList<PicSlideInfo>();
		vegeId = bundle.getString("id");
		providerChannel=bundle.getString("pc");
		processWay=bundle.getString("pw");
		volume=bundle.getFloat("volume");
		weight = bundle.getString("weight");
		ShareUtil.getInstance(mContext).setVegeVolume(vegeId, volume); 
		
		initData(vegeId);
		initView();
	}

	private void initData(final String id) {
		mListener = new CommonListener();
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_VEGE_INFO, id, ShareUtil
						.getInstance(mContext).getUserId());
		Log.e("url", "获取菜品详情数据：" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						String formatJson = MethodUtil.getFormatJson2(bean.getResult());
						System.out.println("result========" + formatJson);
						vBean = new Gson().fromJson(formatJson,VegeInfoBean.class);
						EventBus.getDefault().post(new EventMsg(OpCodes.PRODUCT_INFO_GET_SUCCESS, vBean));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.PRODUCT_INFO_GET_FAILED,
										"获取菜品信息出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.PRODUCT_INFO_GET_FAILED, bean
									.getResult()));
				}
			}
		}).start();
	}

	private void initView() {
		item_price = (LinearLayout) findViewById(R.id.ll_item_price) ;
		item_price1 = (LinearLayout) findViewById(R.id.ll_item_price1) ;
		item_price2 = (LinearLayout) findViewById(R.id.ll_item_price2) ;
		tv_product_number = (TextView) findViewById(R.id.tv_product_number);
		tv_product_number1 = (TextView) findViewById(R.id.tv_product_number1);
		tv_product_number2 = (TextView) findViewById(R.id.tv_product_number2);
		grade0 = (LinearLayout) findViewById(R.id.product_detail_grade0);
		grade1 = (LinearLayout) findViewById(R.id.product_detail_grade1);
		grade2 = (LinearLayout) findViewById(R.id.product_detail_grade2);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivPlus = (ImageView) findViewById(R.id.iv_count_plus);
		ivMinus = (ImageView) findViewById(R.id.iv_count_minus);
		ivPlus1 = (ImageView) findViewById(R.id.iv_count_plus1);
		ivMinus1 = (ImageView) findViewById(R.id.iv_count_minus1);
		ivPlus2 = (ImageView) findViewById(R.id.iv_count_plus2);
		ivMinus2 = (ImageView) findViewById(R.id.iv_count_minus2);
		tvBuyCount = (TextView) findViewById(R.id.tv_count_buy);
		tvBuyCount1 = (TextView) findViewById(R.id.tv_count_buy1);
		tvBuyCount2 = (TextView) findViewById(R.id.tv_count_buy2);
		mCartPanel = (RelativeLayout) findViewById(R.id.panel_cart);
		tvCartGoodsCount = (ColorTextView) findViewById(R.id.ct_goods_count);
		panelCart = (RelativeLayout) findViewById(R.id.panel_cart);
		btDone = (Button) findViewById(R.id.bt_choose_done);
		tvVolume=(TextView)findViewById(R.id.tv_volume);
		tvTotalVolume=(TextView)findViewById(R.id.tv_total_volume);

		ivProductPic = (ImageCycleView) findViewById(R.id.iv_product_pics);
		initLayoutParam(ivProductPic);
		tvProductName = (TextView) findViewById(R.id.tv_product_name);
		//tvProductUnit = (TextView) findViewById(R.id.tv_product_unit);
		tvProductOnSellTime = (TextView) findViewById(R.id.tv_onsell_time);
		llTotalPrice = (LinearLayout) findViewById(R.id.ll_panel_total_price);
		tvDeliverFee=(TextView)findViewById(R.id.tv_deliver_fees);
		//tvProductDetails = (TextView) findViewById(R.id.tv_product_detail);
		llItemPrice = (LinearLayout) findViewById(R.id.ll_item_price);
		tvProcessWay=(TextView)findViewById(R.id.tv_process_way);
		tvProviderChannel=(TextView)findViewById(R.id.tv_provide_channel);
		 
		tvProcessWay.setText("菜品加工："+processWay);
		tvProviderChannel.setText("供应渠道："+providerChannel);
		tvVolume.setText(weight);
		tvTotalVolume.setText(ShareUtil.getInstance(mContext).getVegeAllVolume()+"L");
		tvDeliverFee.setText(ShareUtil.getInstance(mContext).getDeliverFee());
		ivBack.setOnClickListener(mListener);
		ivMinus.setOnClickListener(mListener);
		ivPlus.setOnClickListener(mListener);
		ivMinus1.setOnClickListener(mListener);
		ivPlus1.setOnClickListener(mListener);
		ivMinus2.setOnClickListener(mListener);
		ivPlus2.setOnClickListener(mListener);
		mCartPanel.setOnClickListener(mListener);
		btDone.setOnClickListener(mListener);

		product_detail_content = (TextView) findViewById(R.id.product_detail_content);
		product_detail_explain = (TextView) findViewById(R.id.product_detail_explain);
		product_detail_explain.setOnClickListener(mListener);
		product_detail_content.setOnClickListener(mListener);
	}

	private void initLayoutParam(ImageCycleView mCycleViewPics) {
		ViewGroup.LayoutParams params = mCycleViewPics.getLayoutParams();
		Log.e("data", "屏幕宽度： " + params.width);
		params.height=AppUtil.getScreenWidth(this);
		mCycleViewPics.setLayoutParams(params);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in,
				R.anim.activity_slide_right_out);
	}

	class CommonListener implements OnClickListener {
		@SuppressLint("ResourceAsColor")
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.iv_count_minus:
				count = Integer.parseInt(tvBuyCount.getText().toString());
				if (count <= 0) {
					return;
				}
				tvBuyCount.setText(String.valueOf(--count));
				break;
				case R.id.iv_count_minus1:
					count1 = Integer.parseInt(tvBuyCount1.getText().toString());
					if (count1 <= 0) {
						return;
					}
					tvBuyCount1.setText(String.valueOf(--count1));
					break;
				case R.id.iv_count_minus2:
					count2 = Integer.parseInt(tvBuyCount2.getText().toString());
					if (count2 <= 0) {
						return;
					}
					tvBuyCount2.setText(String.valueOf(--count2));
					break;
			case R.id.iv_count_plus:
				count = Integer.parseInt(tvBuyCount.getText().toString());
				
				if (count >= total0) {
					ToastUtil.Show(mContext, "库存不足");
					return;
				}
				tvBuyCount.setText(String.valueOf(++count));
				break;
				case R.id.iv_count_plus1:
					count1 = Integer.parseInt(tvBuyCount1.getText().toString());

					if (count1 >= total1) {
						ToastUtil.Show(mContext, "库存不足");
						return;
					}
					tvBuyCount1.setText(String.valueOf(++count1));
					break;
				case R.id.iv_count_plus2:
					count2 = Integer.parseInt(tvBuyCount2.getText().toString());

					if (count2 >= total2) {
						ToastUtil.Show(mContext, "库存不足");
						return;
					}
					tvBuyCount2.setText(String.valueOf(++count2));
					break;
			case R.id.panel_cart:
				if (!CheckLogin()) {
					return;
				}
				startNewActivity(CartActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, true, null);
				break;
			case R.id.bt_choose_done:
				if (ShareUtil.getInstance(mContext).getIsLogin()) {
					for(VegeInfoBean.MCommons common : bean.commons){
						if(common.grades == 0){
							a = common.amounts;
						}else if(common.grades == 1){
							b = common.amounts;
						}else if(common.grades == 2){
							c = common.amounts;
						}
					}
					if(count + count1 + count2 == 0){
						ToastUtil.Show(mContext, "请选择商品数量");
						break;
					}else if(ContainVege(vegeId, 0)+count>a){
						ToastUtil.Show(mContext, "有机商品库存不足");
						break;
					}else if(ContainVege(vegeId, 1)+count1>b){
						ToastUtil.Show(mContext, "优选商品库存不足");
						break;
					}else if(ContainVege(vegeId, 2)+count2>c){
						ToastUtil.Show(mContext, "一级商品库存不足");
						break;
					}
					pDialog.show();
					count = Integer.parseInt(tvBuyCount.getText().toString());
					count1 = Integer.parseInt(tvBuyCount1.getText().toString());
					count2 = Integer.parseInt(tvBuyCount2.getText().toString());
					AddProductToCart(vegeId);
				} else {
					startNewActivity(LoginActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, true, null);
				}
				break;
				case R.id.product_detail_content://跳转商品详情
					Intent intent = new Intent(ProductDetailActivity.this, ProductDetailContentActivity.class);
					if(bean != null && bean.getRemark() != null)
					intent.putExtra("detail", bean.getRemark());
					ProductDetailActivity.this.startActivity(intent);
					ProductDetailActivity.this.overridePendingTransition(R.anim.activity_open,0);
					break;
				case R.id.product_detail_explain://跳转商品说明
					Intent intent2 = new Intent(ProductDetailActivity.this, ProductDetailExplainActivity.class);
					intent2.putExtra("total_number", total_number);
					intent2.putExtra("total_number1", total_number1);
					intent2.putExtra("total_number2", total_number2);
					ProductDetailActivity.this.startActivity(intent2);
					ProductDetailActivity.this.overridePendingTransition(R.anim.activity_open,0);
					break;
			default:
				break;
			}
		}
	}

	public boolean CheckLogin() {
		if (!ShareUtil.getInstance(mContext).getIsLogin()) {
			Bundle b = new Bundle();
			b.putString("src", "ProductDetailActivity");
			startNewActivity(LoginActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, false, b);
			return false;
		}
		return true;
	}

	public int ContainVege(String id, int grade) {
		for(VegeCartData data:vegeCartBean.getData()){
			if(data.getVegeInfo().getVege_id().equals(id)){
				for(VegeInfoBean.MCommons commons:data.getVegeInfo().commons){
					if(commons.grades == grade){
						return commons.amounts;
					}
				}
			}
		}
		return 0;
	}

	private void AddProductToCart(String id) {
		int mAount = Integer.parseInt(tvBuyCount.getText().toString());
		int mAount1 = Integer.parseInt(tvBuyCount1.getText().toString());
		int mAount2 = Integer.parseInt(tvBuyCount2.getText().toString());
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_ADD_TO_CART, id, mAount, mAount1, mAount2, "null",
						ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "add cart:" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean sBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sBean.getRESULT_TYPE() == 1) {
						EventBus.getDefault()
								.post(new EventMsg(
										OpCodes.ADD_TO_CART_REQUEST_SUCCESS, sBean.getRESULT_CAR_VOLUME()));
					} else {
						EventBus.getDefault().post(
								new EventMsg(
										OpCodes.ADD_TO_CART_REQUEST_FAILED,
										sBean.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.ADD_TO_CART_REQUEST_FAILED,
									"添加到购物车操作失败"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg flag) {
		switch (flag.getFlag()) {
		
		case OpCodes.PRODUCT_INFO_GET_FAILED:
			try {
				pDialog.cancel();
			} catch (Exception e) {
				// TODO: handle exception
			}
			ToastUtil.Show(mContext, "获取菜品信息失败");
			finish();
			break;
			
		case OpCodes.PRODUCT_INFO_GET_SUCCESS:
			try {
				pDialog.cancel();
			} catch (Exception e) {
				// TODO: handle exception
			}
			bean = (VegeInfoBean) flag.getData();
			for (String str : bean.getFormatedPics()) {
				PicSlideInfo info = new PicSlideInfo();
				info.setUrl(str);
				infos.add(info);
			}
			initSlideView();

			tvProductName.setText(bean.getVegeName());
			//tvProductUnit.setText("/" + bean.getUnit());
			//tvProductDetails.setText(bean.getRemark());
			if(bean.getCreateTime() != null){
				tvProductOnSellTime.setText(bean.getCreateTime().split(" ")[0]);
			}
			/*tvProductRemain.setText("库存：" + bean.getTotal());*/
//			MethodUtil.SetPanelPrice(bean.getPrice(), llItemPrice);
			List<VegeInfoBean.MCommons> list = bean.commons;
			if(list != null){
				for(int i = 0; i < list.size(); i++){
					if(list.get(i).grades == 0){
						grade0.setVisibility(View.VISIBLE);
						total0 = list.get(i).amounts;
						total_number = list.get(i).sellAmounts;
						tv_product_number.setText(total0 + bean.getUnit() + "(已售:" + total_number + bean.getUnit() + ")");
						MethodUtil.SetPanelPrice(list.get(i).prices, bean.getUnit(), item_price, R.id.tv_money_big, R.id.tv_money_small, R.id.tv_money_unit);
					}else if(list.get(i).grades == 1){
						grade1.setVisibility(View.VISIBLE);
						total1 = list.get(i).amounts;
						total_number1 = list.get(i).sellAmounts;
						tv_product_number1.setText(total1 + bean.getUnit() + "(已售:" + total_number1 + bean.getUnit() + ")");
						MethodUtil.SetPanelPrice(list.get(i).prices, bean.getUnit(), item_price1, R.id.tv_money_big1, R.id.tv_money_small1, R.id.tv_money_unit1);
					}else if(list.get(i).grades == 2){
						grade2.setVisibility(View.VISIBLE);
						total2 = list.get(i).amounts;
						total_number2 = list.get(i).sellAmounts;
						tv_product_number2.setText(total2 + bean.getUnit() + "(已售:" + total_number2 + bean.getUnit() + ")");
						MethodUtil.SetPanelPrice(list.get(i).prices, bean.getUnit(), item_price2, R.id.tv_money_big2, R.id.tv_money_small2, R.id.tv_money_unit2);
					}
				}
			}
			break;
		case OpCodes.CART_DATA_GET_ERROR:
			tvCartGoodsCount.setText("0");
			break;
			
		case OpCodes.CART_DATA_GET_DONE:
			vegeCartBean=(VegeCartBean)flag.getData();
			tvCartGoodsCount.setText(String.valueOf(((VegeCartBean) flag
					.getData()).getTotalCount()));
			MethodUtil.SetPanelPrice(
					((VegeCartBean) flag.getData()).getTotalMoney(),
					llTotalPrice);
			break;
			
		case OpCodes.ADD_TO_CART_REQUEST_FAILED:
			try {
				pDialog.cancel();
			} catch (Exception e) {
				// TODO: handle exception
			}
			ToastUtil.Show(mContext, flag.getData().toString());
			break;

		case OpCodes.ADD_TO_CART_REQUEST_SUCCESS:
			NetUtil.getCartData(mContext);
			try {
				pDialog.cancel();
			} catch (Exception e) {
			}
			ToastUtil.Show(mContext, "添加成功");
			count = 0;
			count1 = 0;
			count2 = 0;
			tvBuyCount.setText("0");
			tvBuyCount1.setText("0");
			tvBuyCount2.setText("0");
			tvBuyCount.setTextColor(getResources().getColor(R.color.money_yellow));
			String vegeVolumeAll=flag.getData().toString();
			vegeVolumeAll=flag.getData().toString(); 
			ShareUtil.getInstance(mContext).setVegeAllVolume(vegeVolumeAll);
			tvTotalVolume.setText(vegeVolumeAll+"L");
			break;
		default:
			break;
		}
	}

	private void initSlideView() {
		ivProductPic.setImageResources(infos, mAdCycleViewListener);
		ArrayList<ImageView> ivs = new ArrayList<ImageView>();
		for (int i = 0; i < infos.size(); i++) {
			ImageView iv = new ImageView(mContext);
			RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 150);
			iv.setLayoutParams(lParam);
			iv.setScaleType(ScaleType.CENTER_INSIDE);
			ImageLoader.getInstance().displayImage(infos.get(i).getUrl(), iv);
			ivs.add(iv);
		}
	}

	private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
		@Override
		public void onImageClick(PicSlideInfo info, int position, View imageView) {

		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView,
					ImageUtil.imageVegeOptions);
		}
	};

	// ViewPager适配器
	class MyPagerAdapter extends PagerAdapter {
		private ArrayList<ImageView> mViewList;

		public MyPagerAdapter(ArrayList<ImageView> mViewList) {
			this.mViewList = mViewList;
		}

		@Override
		public int getCount() {
			return mViewList.size();// 页卡数
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;// 官方推荐写法
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViewList.get(position));// 添加页卡
			return mViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViewList.get(position));// 删除页卡
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (ShareUtil.getInstance(mContext).getIsLogin()) {
			NetUtil.getCartData(mContext);
		} else {
			tvCartGoodsCount.setText("0");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}

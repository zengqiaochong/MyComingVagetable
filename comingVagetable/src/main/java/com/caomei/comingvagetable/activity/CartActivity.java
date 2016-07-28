package com.caomei.comingvagetable.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView; 

import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.adapter.VegeCartAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.caomei.comingvagetable.bean.vegedata.VegeCartData;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class CartActivity extends BaseActivity {

	private Context mContext;
	private VegeCartAdapter mAdapter;
	private ListView mListView;
	private VegeCartBean cBean;
	private LinearLayout llTotalPrice;
	private Button btChooseDone;

	private CartCommonListener mListener;
	private VegeCartBean bean;
	private ArrayList<VegeCartData> mData = new ArrayList<VegeCartData>();
	private TextView tvDeliverFee;
	private ImageView ivDelAll;
	private CheckBox cbSelectAll;
	private RelativeLayout panelOrderInfo;
	private Button btDelOrder;
	private TextView tvVolume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_cart);
		initView();
		initData();
	}

	private void initData() {
		mListener = new CartCommonListener();
		btChooseDone.setOnClickListener(mListener);
		ivDelAll.setOnClickListener(mListener);
		btDelOrder.setOnClickListener(mListener);
		cbSelectAll.setOnCheckedChangeListener(mListener);
		mAdapter = new VegeCartAdapter(mContext, mData, mListener);
		mListView.setAdapter(mAdapter);
		NetUtil.getCartData(mContext);
	}

	private void initView() {
		cbSelectAll = (CheckBox) findViewById(R.id.cb_select_all);
		btDelOrder = (Button) findViewById(R.id.bt_cart_del);
		mListView = (ListView) findViewById(R.id.lv_cart_list);
		llTotalPrice = (LinearLayout) findViewById(R.id.ll_panel_total_price);
		btChooseDone = (Button) findViewById(R.id.bt_cart_choose_done);
		tvDeliverFee = (TextView) findViewById(R.id.tv_deliver_fees);
		tvDeliverFee.setText(ShareUtil.getInstance(mContext).getDeliverFee()
				+ "元");
		tvVolume=(TextView)findViewById(R.id.tv_total_volume);
		tvVolume.setText("体积："+ShareUtil.getInstance(mContext).getVegeAllVolume()+"L");
		ivDelAll = (ImageView) findViewById(R.id.iv_del_all);
		panelOrderInfo = (RelativeLayout) findViewById(R.id.rl_panel_order_info);

		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);

		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.CART_DATA_GET_DONE:
			bean = (VegeCartBean) msg.getData();
			MethodUtil.SetPanelPrice(bean.getTotalMoney(), llTotalPrice);
			mData.clear();
			mData.addAll(bean.getData());
			mAdapter.notifyDataSetChanged();
			break;

		case OpCodes.CART_DATA_GET_ERROR:
			ToastUtil.Show(mContext,
					getResources().getString(R.string.cart_data_get_failed));
			break;
		case OpCodes.DEL_CART_ITEM_DONE:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			NetUtil.getCartData(mContext);
			tvVolume.setText("体积："+msg.getData().toString()+"L");
			ToastUtil.Show(mContext, "删除成功");
			break;
		case OpCodes.DEL_CART_ITEM_ERROR:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			ToastUtil.Show(mContext, msg.getData().toString());
			break;
		case OpCodes.CHANGE_CART_ITEM_COUNT_DONE:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			tvVolume.setText("体积："+msg.getData().toString()+"L");
			NetUtil.getCartData(mContext);
			break;
		case OpCodes.CHANGE_CART_ITEM_COUNT_ERROR:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			ToastUtil.Show(mContext, msg.getData().toString());

			break;

		case OpCodes.SUBMIT_ORDER_DONE:
			finish();
			break;
		}
	}

	public class CartCommonListener implements OnClickListener,
			OnCheckedChangeListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_cart_choose_done:
				if (bean == null || bean.getData() == null
						|| bean.getData().size() == 0) {
					ToastUtil.Show(mContext, "购物车为空哦");

					return;
				}
				Bundle b = new Bundle();
				b.putSerializable("data", bean);
				startNewActivity(OrderConfirm.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, b);
				break;
			case R.id.iv_del_item:
				final int pos = Integer.parseInt(v.getTag().toString());
				DialogUtil.DefaultDialog(mContext, "提示", "是否确定删除订单？", "确定",
						"取消", new IDialogOperation() {

							@Override
							public void onPositive() {
								pDialog.show();
								delCartItem(mData.get(pos).getScarid());
							}

							@Override
							public void onNegative() {

							}
						});

				break;
			case R.id.bt_cart_del:
				String orderIds = "";
				for (int i = 0; i < mData.size(); i++) {
					if (mData.get(i).isSelected()) {
						if (TextUtils.isEmpty(orderIds)) {
							orderIds = mData.get(i).getScarid();
						} else {
							orderIds = orderIds + ";"
									+ mData.get(i).getScarid();
						}
					}
				}
				final String idList = orderIds;
				if (TextUtils.isEmpty(orderIds)) {
					ToastUtil.Show(mContext, "未选择订单");
					return;
				}

				DialogUtil.DefaultDialog(mContext, "提示", "是否确定所选删除订单？", "确定",
						"取消", new IDialogOperation() {

							@Override
							public void onPositive() {
								pDialog.show();
								delCartItem(idList);
							}

							@Override
							public void onNegative() {

							}
						});

				break;
			case R.id.iv_count_plus:
				if (NetUtil.getNetworkState(mContext)) {
					pDialog.show();
					changeCartItemCount(v.getTag().toString(), 1);
				}
				break;
			case R.id.iv_count_minus:
				if (NetUtil.getNetworkState(mContext)) {
					pDialog.show();
					changeCartItemCount(v.getTag().toString(), -1);
				}
				break;
			case R.id.iv_del_all:
				if (mAdapter.getSelectedAble()) {
					mAdapter.setSelectedAble(false);
					mAdapter.notifyDataSetChanged();
					panelOrderInfo.setVisibility(View.GONE);

				} else {
					mAdapter.setSelectedAble(true);
					mAdapter.notifyDataSetChanged();
					panelOrderInfo.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.cb_select_all:
				mAdapter.setSelectedAble(true);
				mAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			switch (arg0.getId()) {
			case R.id.cb_item_selected:
				Log.e("ratio", "单选");
				int index = Integer.parseInt(arg0.getTag().toString());
				mData.get(index).setSelected(arg1);
				mAdapter.notifyDataSetChanged();
				break;

			case R.id.cb_select_all:
				Log.e("ratio", "多选");
				for (int i = 0; i < mData.size(); i++) {
					mData.get(i).setSelected(arg1);
				}
				mAdapter.notifyDataSetChanged();
				break;
			default:
				Log.e("ratio", "未知");
				break;
			}

		}
	}

	public void changeCartItemCount(final String scarid, final double val) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_CHANGE_CART_ITEM_COUNT,
								scarid, val, ShareUtil.getInstance(mContext)
										.getUserId());
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean sBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sBean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(
								new EventMsg(
										OpCodes.CHANGE_CART_ITEM_COUNT_DONE,
										sBean.getRESULT_CAR_VOLUME()));
					} else {
						EventBus.getDefault().post(
								new EventMsg(
										OpCodes.CHANGE_CART_ITEM_COUNT_ERROR,
										sBean.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.DEL_CART_ITEM_ERROR, "操作失败"));
				}
			}
		}).start();
	}

	private void delCartItem(final String scarid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_DEL_FROM_CART, scarid,
								ShareUtil.getInstance(mContext).getUserId());
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean sBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sBean.getRESULT_TYPE() == 1) {
						EventBus.getDefault()
								.post(new EventMsg(OpCodes.DEL_CART_ITEM_DONE,
										sBean.getRESULT_CAR_VOLUME()));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEL_CART_ITEM_ERROR, sBean
										.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.DEL_CART_ITEM_ERROR,
									"删除购物车记录失败"));
				}
			}
		}).start();
	}

	private void postOrderFromCart() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_SUBMIT_ORDER_FROM_CART, ShareUtil
						.getInstance(mContext).getUserId(),
						getString(R.string.my_address), "", bean.getCartIds());
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						TypeMsgBean sBean = new Gson().fromJson(
								bean.getResult(), TypeMsgBean.class);

					} catch (Exception ex) {

					}
				}

			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in,
				R.anim.activity_slide_right_out);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

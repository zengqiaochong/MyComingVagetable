package com.caomei.comingvagetable.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.adapter.AddressListAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.AddressBean;
import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class AddressManageActivity extends BaseActivity {

	private ListView listView;
	private AddressListAdapter mAdapter;
	private CommonListener mListener;
	private ArrayList<AddressData> data;
	private TextView tvAddAddr;
	private ImageView ivBack;
	//是否可以更改小区：当在支付选择地址时不能更改；当进行收获地址管理时可以更改。
	private boolean canChangeCommunity=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addr_manage);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.lv_addrs);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvAddAddr = (TextView) findViewById(R.id.tv_add_address);
	}

	private void initData() {
		canChangeCommunity=getIntent().getBooleanExtra("canChangeCommunity", false);
		mListener = new CommonListener();
		tvAddAddr.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
		data = new ArrayList<AddressData>();
		mAdapter = new AddressListAdapter(mContext, data, mListener);
		listView.setAdapter(mAdapter);
		requestAddressList();
	}

	public void onEventMainThread(EventMsg flag) {
		switch (flag.getFlag()) {
		case OpCodes.GET_ADDRESS_LIST_NULL:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			data.clear();
			mAdapter.notifyDataSetChanged();
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// 获取到的地址列表为空，则新建的组后一个收获地址为默认地址
					Bundle b = new Bundle();
					b.putBoolean("isDefAddr", true);
					startNewActivity(AddAddressActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, b);
				}
			}, 1000);
			break;
		case OpCodes.GET_ADDRESS_LIST_DONE:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			data.clear();
			data.addAll(((AddressBean) flag.getData()).getData());
			mAdapter.notifyDataSetChanged();
			if(data.size()==1&&data.get(0).getIsDefault()==0){
				setDefAddress(data.get(0).getAddress_id());
			}
			break;
		case OpCodes.GET_ADDRESS_LIST_ERROR:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			Toast.makeText(mContext, R.string.get_address_list_error,
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.SET_DEFAULT_ADDRESS_DONE:
			requestAddressList();
			break;
		case OpCodes.SET_DEFAULT_ADDRESS_ERROR:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.ADDRESS_ADD_SUCCESS:
			requestAddressList();
			break;
		case OpCodes.DEL_USER_ADDRESS_FAILED:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
			
		case OpCodes.DEL_USER_ADDRESS_SUCCESS:
		case OpCodes.NOTIFY_UPDATE_ADDRESS_SUCCESS:
		case OpCodes.NOTIFY_ADD_ADDRESS_SUCCESS:
			requestAddressList();
			break;
		default:
			break;
		}
	}

	private void requestAddressList() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_USER_ADDRESS, 0, 1000, 0,
						ShareUtil.getInstance(mContext).getUserId());

		pDialog.setMessage(getString(R.string.loading));
		pDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				Log.e("url", "地址列表url "+url);
				if (bean.getState() == AccessNetState.Success) {
					AddressBean aBean = new Gson().fromJson(bean.getResult(),
							AddressBean.class);
					if (aBean.getData() == null || aBean.getData().size() == 0) {
						EventBus.getDefault()
								.post(new EventMsg(
										OpCodes.GET_ADDRESS_LIST_NULL,
										getString(R.string.not_add_address_yet)));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_ADDRESS_LIST_DONE,
										aBean));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_ADDRESS_LIST_ERROR,
									R.string.request_address_failed));
				}
			}
		}).start();
	}

	public class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.tv_add_address:
				Bundle b=new Bundle();
				b.putBoolean("canChangeCommunity", canChangeCommunity);
				startNewActivity(AddAddressActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, b);
				break;
			case R.id.cb_default_addr:
				if (((CheckBox) v).isChecked()) {
					pDialog.setMessage("正在设置");
					pDialog.show();
					setDefAddress(v.getTag().toString());
				} else {
					((CheckBox) v).setChecked(true);
				}
				break;
			case R.id.ll_addr_panel:
				EventBus.getDefault().post(new EventMsg(OpCodes.CHANGE_USER_ADDRESS, v.getTag()));
				onBackPressed();
				break;
			case R.id.iv_del_addr:
				ShowDialog(v);

				break;
			case R.id.iv_edit_addr:
				Bundle mBundle = new Bundle();
				mBundle.putString("title", "编辑地址");
				mBundle.putString("id", data.get(Integer.parseInt(v.getTag().toString())).getAddress_id());
				mBundle.putString("community_id",data.get(Integer.parseInt(v.getTag().toString())).getCommunity_id());
				mBundle.putSerializable("data",
						(AddressData) v.getTag(R.id.tag_first));
				mBundle.putBoolean("canChangeCommunity", canChangeCommunity);
				startNewActivity(AddAddressActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, mBundle);
				break;
			default:
				break;
			}
		}

	}

	protected void ShowDialog(final View v) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("真的要删除该地址吗？");
		builder.setTitle("提示");
		builder.setPositiveButton(R.string.dialog_confirm,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						requestDelAddr(v.getTag().toString());
					}
				});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void requestDelAddr(String addrId) {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_DEL_ADDRESS, addrId, ShareUtil
						.getInstance(mContext).getUserId());
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.DEL_USER_ADDRESS_SUCCESS,
									R.string.del_address_request_failed));
					TypeMsgBean sBean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sBean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEL_USER_ADDRESS_SUCCESS,
										sBean.getRESULT_MSG()));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEL_USER_ADDRESS_FAILED,
										sBean.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.DEL_USER_ADDRESS_FAILED, bean
									.getResult()));
				}
			}
		}).start();

	}

	private void setDefAddress(final String addrId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_SET_DEFAULT_ADDRESS,
								addrId, ShareUtil.getInstance(mContext)
										.getUserId());
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean sbean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sbean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.SET_DEFAULT_ADDRESS_DONE,
										"设置成功"));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.SET_DEFAULT_ADDRESS_ERROR,
										sbean.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.SET_DEFAULT_ADDRESS_ERROR,
									"设置请求出错"));
				}
			}
		}).start();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
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

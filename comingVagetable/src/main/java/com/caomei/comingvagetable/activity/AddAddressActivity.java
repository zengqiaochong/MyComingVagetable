package com.caomei.comingvagetable.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.adapter.CityAdapter;
import com.caomei.comingvagetable.adapter.CommunitAdapter;
import com.caomei.comingvagetable.adapter.CountyAdapter;
import com.caomei.comingvagetable.adapter.ProvinceAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.CommunityBean;
import com.caomei.comingvagetable.bean.CommunityData;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.UserInfoBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class AddAddressActivity extends BaseActivity implements OnClickListener {
	private Button btSave;
	private EditText etDetailAddr;
	private EditText etName;
	private EditText etNumber;
	private ImageView ivBack;
	private String title;
	private TextView tvTitle;
	private boolean editMode;
	private String id;
	private AddressData addr;
	private TextView tvCommunity;
	private TextView tvLocCommunity;
	private LinearLayout panelLocCommunity;
	private boolean canChangeCommunity;
	private FrameLayout frameSelectCity;
	private ArrayList<String> Provinces;
	private ArrayList<String> Cities;
	private ArrayList<String> Counties;
	private ArrayList<CommunityData> Communities;
	private ProvinceAdapter provinceAdapter;
	private CityAdapter cityAdapter;
	private CommunitAdapter communityAdapter;
	private CountyAdapter countyAdapter;
	private ListView lvCommunity;
	private ListView lvCounty;
	private ListView lvCity;
	private ListView lvProvince;
	private CommunityBean xBean;

	private String temProvince;
	private String temCity;
	private String temCounty;
	private TextView tvLocaCity;
	private String temCommunityId;
	// 用于指示当前操作的标示：0是正常状态，1标示编辑区县和小区的选择 2标示编辑省市的选择；
	private int flag = 0;
	private LinearLayout panelCommunity;
	private LinearLayout panelChina;
	private TextView tvDeliverName;
	private TextView tvPhone;
	private String communityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		EventBus.getDefault().register(this);

		initViews();
		initData();
	}

	private void initViews() {
		tvCommunity = (TextView) findViewById(R.id.tv_community);
		tvLocCommunity = (TextView) findViewById(R.id.tv_commnity_name);
		panelLocCommunity = (LinearLayout) findViewById(R.id.ll_panel_loc_community);

		btSave = (Button) findViewById(R.id.bt_save);

		etDetailAddr = (EditText) findViewById(R.id.et_building_NO);
		etName = (EditText) findViewById(R.id.et_user_name);
		etNumber = (EditText) findViewById(R.id.et_phone_NO);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		frameSelectCity = (FrameLayout) findViewById(R.id.frame_select_panel);

		lvCounty = (ListView) findViewById(R.id.lv_county);
		lvCommunity = (ListView) findViewById(R.id.lv_community);
		lvCity = (ListView) findViewById(R.id.lv_city);
		lvProvince = (ListView) findViewById(R.id.lv_province);
		panelCommunity = (LinearLayout) findViewById(R.id.ll_panel_community);
		panelChina = (LinearLayout) findViewById(R.id.ll_panel_city);
		
		tvDeliverName=(TextView)findViewById(R.id.tv_server);
		tvPhone=(TextView)findViewById(R.id.tv_phone);
	}

	private void initData() {
		title = getIntent().getStringExtra("title");
		id = getIntent().getStringExtra("id");
		communityId=getIntent().getStringExtra("community_id");
		addr = (AddressData) getIntent().getSerializableExtra("data");
		canChangeCommunity = getIntent().getBooleanExtra("canChangeCommunity",
				false);
		temCommunityId = ShareUtil.getInstance(mContext).getHomeCommunityID();

		if (title != null) {
			tvTitle.setText(title);
			editMode = true;
			etDetailAddr
					.setText(addr.getSmallAddress().subSequence(
							ShareUtil.getInstance(mContext).getHomeCommunity()
									.length(), addr.getSmallAddress().length()));
			etName.setText(addr.getUsername());
			etNumber.setText(addr.getPhoneNo());
		}
		if (addr == null) {
			tvLocCommunity.setText(ShareUtil.getInstance(mContext)
					.getHomeCommunity());
			tvCommunity.setText(ShareUtil.getInstance(mContext)
					.getHomeCommunity());
		} else {
			tvLocCommunity.setText(addr.getCommunityName());
			tvCommunity.setText(addr.getCommunityName());
			temProvince = MethodUtil.getCityName(addr.getBigAddress(), 3);
			temCity = MethodUtil.getCityName(addr.getBigAddress(), 0);
			temCounty = MethodUtil.getCityName(addr.getBigAddress(), 1);
		}

		btSave.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		if (canChangeCommunity) {
			Log.e("can", "canChangeCommunity " + canChangeCommunity);
			panelLocCommunity.setOnClickListener(this);
			initCityData();
		}
		getDeliverInfo(communityId);
	}

	private void initCityData() {
		Provinces = new ArrayList<String>();
		Cities = new ArrayList<String>();
		Counties = new ArrayList<String>();
		Communities = new ArrayList<CommunityData>();

		provinceAdapter = new ProvinceAdapter(mContext, Provinces);
		cityAdapter = new CityAdapter(mContext, Cities);
		communityAdapter = new CommunitAdapter(mContext, Communities);
		countyAdapter = new CountyAdapter(mContext, Counties);
				
		lvCommunity.setAdapter(communityAdapter);
		lvCounty.setAdapter(countyAdapter);
		lvCity.setAdapter(cityAdapter);
		lvProvince.setAdapter(provinceAdapter);

		provinceAdapter.notifyDataSetChanged();
		countyAdapter.notifyDataSetChanged();

		lvProvince.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				temProvince=Provinces.get(arg2);
				provinceAdapter.setIndex(arg2);
				provinceAdapter.notifyDataSetChanged();
				tvLocCommunity.setText(Provinces.get(arg2));
				refreshCityData(Provinces.get(arg2));
			}

		});
		lvCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				flag--;
				temCity = Cities.get(arg2);
				tvLocCommunity.setText(temCity);
				panelCommunity.setVisibility(View.VISIBLE);
				refreshCountyData(temCity); 
				refreshCommunityData(Counties.get(0));
				countyAdapter.setIndex(0); 
				
			}

		});

		lvCounty.setOnItemClickListener(new OnItemClickListener() {
		 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				temCounty=Counties.get(arg2);
				countyAdapter.setIndex(arg2);
				countyAdapter.notifyDataSetChanged();
				tvLocCommunity.setText(Counties.get(arg2));
				refreshCommunityData(Counties.get(arg2));
			}
		});

		ivBack.setOnClickListener(this);
		lvCommunity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				if(arg2==Communities.size()-1){
					ToastUtil.Show(mContext, "更多小区，敬请期待！");
					return;
				}
				flag=0;
				temCommunityId=Communities.get(arg2).getCommunity_id();
				ShareUtil.getInstance(mContext).setHomeCommunityID(Communities.get(arg2).getCommunity_id());
				ShareUtil.getInstance(mContext).setHomeCommunity(Communities.get(arg2).getCommunityName());
				tvLocCommunity.setText(Communities.get(arg2).getCommunityName());
			
				frameSelectCity.setVisibility(View.GONE);
				tvCommunity.setText(Communities.get(arg2).getCommunityName());
				getDeliverInfo(Communities.get(arg2).getCommunity_id());
			}

			
		});
		requestAllCommunity();
		
	}
	private void getDeliverInfo(final String commitId) {
		new Thread(new Runnable() {
			String url=CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_DELIVER_INFO_COMMUNITY_ID,commitId,ShareUtil.getInstance(mContext).getUserId());
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AccessNetResultBean bean=NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				Log.e("url", "获取deliver info "+url);
				if(bean.getState()==AccessNetState.Success){
					try{
						Log.e("data", bean.getResult());
						JSONObject obj=new JSONObject(bean.getResult());
						String deliverId=obj.getString("deliver_id");
						String infoStr=CommonAPI.BASE_URL+String.format(CommonAPI.URL_GET_USER_INFO,deliverId);
						bean=NetUtil.getInstance(mContext).getDataFromNetByGet(infoStr);
						if(bean.getState()==AccessNetState.Success){
							try{
								UserInfoBean uB=new Gson().fromJson(bean.getResult(), UserInfoBean.class);
								Log.e("data", "deliver info "+bean.getResult());
								EventBus.getDefault().post(
										new EventMsg(OpCodes.GET_DELIVER_INFO_OK,uB));
							}catch(Exception ex){
								EventBus.getDefault().post(
										new EventMsg(OpCodes.GET_DELIVER_INFO_ERROR, "配送员信息格式有误"));
							}
						}else{
							EventBus.getDefault().post(
									new EventMsg(OpCodes.GET_DELIVER_INFO_ERROR, bean.getResult()));
						}
					}catch(Exception ex){
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_DELIVER_INFO_ERROR, "配送员id信息格式有误"));
					}
				}else{
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_DELIVER_INFO_ERROR, bean.getResult()));
				}
			}
		}).start();
	}
	private void requestAllCommunity() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_COMMUNITYLIST, "all",
						ShareUtil.getInstance(mContext).getMyCity(), ShareUtil
								.getInstance(mContext).getUserId());
		Log.e("url", "获取全国所有小区的url" + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						xBean = new Gson().fromJson(bean.getResult(),
								CommunityBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_COMMUNITY_DATA_DONE,
										null));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_COMMUNITY_DATA_ERROR,
										"数据格式出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_COMMUNITY_DATA_ERROR,
									"获取小区信息失败"));
				}
			}
		}).start();
	}

	private void refreshCommunityData(String county) {
		if (xBean != null) {
			Communities.clear();
			Communities.addAll(xBean.getCommunity(county));
			CommunityData data = new CommunityData();
			data.setCommunityName("更多小区...");
			Communities.add(data);
			communityAdapter.notifyDataSetChanged();
		}
	}

	private void refreshCountyData(String city) {
		if (xBean != null) {
			Counties.clear();
			Counties.addAll(xBean.getCounty(city));
			countyAdapter.notifyDataSetChanged();
		}
	}

	private void refreshCityData(String string) {
		if (xBean != null) {
			Cities.clear();
			Cities.addAll(xBean.getCity(string));
			cityAdapter.notifyDataSetChanged();
		}
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
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_save:
			if (CheckAddrInfo()) {
				if (editMode) {
					updateAddress();
				} else {
					addAddress();
				}
			} else {
				ToastUtil.Show(mContext,
						getString(R.string.please_complete_your_personal_info));
			}
			break;
		case R.id.iv_back:
			onBackPressed();
			break;
		case R.id.ll_panel_loc_community:
			System.out.println("url=========chong");
			flag++;
			frameSelectCity.setVisibility(View.VISIBLE);
			if (flag == 1) {
				panelCommunity.setVisibility(View.VISIBLE);
				panelChina.setVisibility(View.GONE);
				tvLocCommunity.setText(temCounty);
			} else if (flag == 2) {
				panelCommunity.setVisibility(View.GONE);
				panelChina.setVisibility(View.VISIBLE);
				tvLocCommunity.setText(temProvince);
			}
			break;
		default:
			break;
		}
	}

	private boolean CheckAddrInfo() {
		return !(TextUtils.isEmpty(etDetailAddr.getEditableText().toString())
				|| TextUtils.isEmpty(etName.getEditableText().toString())
				|| !MethodUtil
				.isMobileNO(etNumber.getEditableText().toString()));
	}

	private void updateAddress() {
		pDialog.show();
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_UPDATE_ADDRESS, id, ShareUtil
						.getInstance(mContext).getHomeCommunityID(),
						etDetailAddr.getEditableText().toString(), etName
								.getEditableText().toString(), etNumber
								.getEditableText().toString(), "", ShareUtil
								.getInstance(mContext).getUserId());
		Log.e("url", "更新用户地址url " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean sbean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sbean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.ADDRESS_UPDATE_SUCCESS,
										"修改成功"));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.ADDRESS_UPDATE_FAILED,
										sbean.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.ADDRESS_ADD_FAILED,
									getString(R.string.add_address_failed)));

				}
			}
		}).start();
	}

	private void addAddress() {
		pDialog.show();
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_ADD_ADDRESS, temCommunityId,
						etDetailAddr.getEditableText().toString(), etName
								.getEditableText().toString(), etNumber
								.getEditableText().toString(), ShareUtil
								.getInstance(mContext).getUserId());
		Log.e("url", "add addr " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					TypeMsgBean sbean = new Gson().fromJson(bean.getResult(),
							TypeMsgBean.class);
					if (sbean.getRESULT_TYPE() == 1) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.ADDRESS_ADD_SUCCESS,
										"添加成功"));
					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.ADDRESS_ADD_FAILED, sbean
										.getRESULT_MSG()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.ADDRESS_ADD_FAILED,
									getString(R.string.add_address_failed)));

				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg flag) {
		switch (flag.getFlag()) {
		case OpCodes.ADDRESS_ADD_SUCCESS:
			EventBus.getDefault().post(
					new EventMsg(OpCodes.NOTIFY_ADD_ADDRESS_SUCCESS, null));
			try {
				pDialog.dismiss();
			} catch (Exception e) {

			}
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_LONG).show();

			finish();
			break;
		case OpCodes.ADDRESS_ADD_FAILED:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
			}
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.ADDRESS_UPDATE_SUCCESS:
			EventBus.getDefault().post(
					new EventMsg(OpCodes.NOTIFY_UPDATE_ADDRESS_SUCCESS, null));
			try {
				pDialog.dismiss();
			} catch (Exception e) {

			}
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_LONG).show();
			finish();
			break;
		case OpCodes.ADDRESS_UPDATE_FAILED:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
			}
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case OpCodes.GET_COMMUNITY_DATA_DONE:
			fillData();
			break;
		case OpCodes.GET_COMMUNITY_DATA_ERROR:
			Toast.makeText(mContext, flag.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;

		case OpCodes.GET_DELIVER_INFO_OK:
			Log.e("data", "获取配送员信息成功");
			UserInfoBean bean=(UserInfoBean)flag.getData();
			tvDeliverName.setText(bean.getUsername());
			tvPhone.setText(bean.getPhone());
			break;
		case OpCodes.GET_DELIVER_INFO_ERROR:
			tvDeliverName.setText("暂无");
			tvPhone.setText("暂无");
			break;
		default:
			break;
		}
	}

	private void fillData() {
		if (TextUtils.isEmpty(temProvince)) {
			temProvince = xBean.getProvince().get(0);
		}
		if (TextUtils.isEmpty(temCity)) {
			temCity = xBean.getCity(xBean.getProvince().get(0)).get(0);
		}
		if (TextUtils.isEmpty(temCounty)) {
			temCounty = xBean.getCounty(
					xBean.getCity(xBean.getProvince().get(0)).get(0)).get(0);
		}
		Provinces.clear();
		Provinces.addAll(xBean.getProvince());
		Cities.clear();
		Cities.addAll(xBean.getCity(temProvince));
		if (Provinces.size() == 0) {
			Toast.makeText(mContext, "服务未启动", Toast.LENGTH_SHORT).show();
			return;
		} else {
			provinceAdapter.notifyDataSetChanged();
			provinceAdapter.setIndex(Provinces.indexOf(temProvince));
			refreshCityData(temProvince);
		}
		Counties.clear();
		Counties.addAll(xBean.getCounty(temCity));
		if (Counties.size() == 0) {
			Toast.makeText(mContext, "该城市没有小区覆盖服务", Toast.LENGTH_SHORT).show();
		} else {
			countyAdapter.notifyDataSetChanged();
			countyAdapter.setIndex(Counties.indexOf(temCounty));
			refreshCommunityData(temCounty);
		}
	}
}

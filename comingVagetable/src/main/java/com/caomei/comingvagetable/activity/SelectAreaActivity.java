package com.caomei.comingvagetable.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.sortlistview.CharacterParser;
import com.caomei.comingvagetable.sortlistview.PinyinComparator;
import com.caomei.comingvagetable.sortlistview.SideBar;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class SelectAreaActivity extends BaseActivity {
	private ListView lvCounty;
	private LinearLayout panelLocCity;
	private ListView lvCommunity;
	private TextView tvLocaCity;
	private CommonListener mListener;

	// 省份+区县的bean

	private ArrayList<String> Provinces;
	private ArrayList<String> Cities;
	private ArrayList<String> Counties;
	private CommunityBean xBean;

	private ArrayList<CommunityData> Communities;
	private ProvinceAdapter provinceAdapter;
	private CityAdapter cityAdapter;
	private CountyAdapter countyAdapter;
	private CommunitAdapter communityAdapter;
	private ImageView ivBack;
	private ListView lvCity;
	private ListView lvProvince;
	private LinearLayout panelChina;

	// 区县-小区的选择面板
	private LinearLayout panelCommunity;
	// 省份-市 的选择面板
	private LinearLayout panelProvince;

	// 临时选择的城市
	private String temCounty;
	private String temCity;
	private String temProvince;
	private AddressData addr;
	private SideBar sideBar;
	/**
	 * 显示字母的TextView
	 */
	private TextView dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_county);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		pinyinComparator = new PinyinComparator();
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		lvCounty = (ListView) findViewById(R.id.lv_county);
		lvCommunity = (ListView) findViewById(R.id.lv_community);
		lvCity = (ListView) findViewById(R.id.lv_city);
		lvProvince = (ListView) findViewById(R.id.lv_province);
		panelChina = (LinearLayout) findViewById(R.id.ll_panel_locate_china);
		panelLocCity = (LinearLayout) findViewById(R.id.ll_panel_locate_city);
		panelCommunity = (LinearLayout) findViewById(R.id.ll_panel_community);
		panelProvince = (LinearLayout) findViewById(R.id.ll_panel_city);
		tvLocaCity = (TextView) findViewById(R.id.tv_locate_city);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = -1;
				if(panelCommunity.getVisibility() == View.VISIBLE){
					position = communityAdapter.getPositionForSection(s.charAt(0));
				}
				if(position != -1){
					if(panelCommunity.getVisibility() == View.VISIBLE){
						lvCommunity.setSelection(position);
					}
				}
			}
		});
	}

	private void initData() {
		addr=(AddressData)getIntent().getSerializableExtra("data");
		if(addr!=null){
			temCounty=MethodUtil.getCityName(addr.getBigAddress(),1);
			temCity=MethodUtil.getCityName(addr.getBigAddress(), 0);
			temProvince=MethodUtil.getCityName(addr.getBigAddress(), 3);
		}else{
			panelLocCity.setVisibility(View.GONE);
			panelCommunity.setVisibility(View.GONE);
			panelChina.setVisibility(View.VISIBLE);
		}
		mListener = new CommonListener();
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
				refreshCityData(Provinces.get(arg2));
			}
		});
		lvCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				temCity = Cities.get(arg2);
				tvLocaCity.setText(temCity);
				panelCommunity.setVisibility(View.VISIBLE);

				refreshCountyData(Cities.get(arg2));
				refreshCommunityData(xBean.getCounty(temCity).get(0));
				panelLocCity.setVisibility(View.VISIBLE);
				countyAdapter.setIndex(0);
				countyAdapter.notifyDataSetChanged();
			}
		});

		lvCounty.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				temCounty=Counties.get(arg2);
				countyAdapter.setIndex(arg2);
				countyAdapter.notifyDataSetChanged();
				refreshCommunityData(Counties.get(arg2));
			}
		});

		ivBack.setOnClickListener(mListener);
		lvCommunity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2==Communities.size()-1){
					ToastUtil.Show(mContext, "更多小区，敬请期待");
					return;
				}
				ShareUtil.getInstance(mContext).setHomeCommunityID(Communities.get(arg2).getCommunity_id());
				ShareUtil.getInstance(mContext).setHomeCommunity(Communities.get(arg2).getCommunityName());
				EventBus.getDefault().post(
						new EventMsg(OpCodes.SET_HOME_COMMUNITY_DONE, Communities.get(arg2)));
				onBackPressed();

			}
		});

		tvLocaCity.setText(temCity==null?"中国":temCity);
		panelLocCity.setOnClickListener(mListener);
		panelChina.setOnClickListener(mListener);
		requestAllCommunity();
	}

	private PinyinComparator pinyinComparator;
	private CharacterParser characterParser;
	protected void refreshCommunityData(String county) {
		if (xBean != null) {
			Communities.clear();
			List<CommunityData> lists = xBean.getCommunity(county);
			for(CommunityData sortName : lists){
				sortName.sortLetters = characterParser.getSelling(sortName.getCommunityName());
			}
			// 根据a-z进行排序
			Collections.sort(lists, pinyinComparator);
			Communities.addAll(lists);
			CommunityData data=new CommunityData();
			data.setCommunityName("更多小区...");
			data.sortLetters = "#";
			Communities.add(data);
			communityAdapter.notifyDataSetChanged();
		}
	}
	private void refreshCountyData(String city){
		if(xBean!=null){
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

	/**
	 * 获取所有小区
	 */
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
				Log.e("url", "获取全国所有小区 " + bean.getResult());
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

	class CommonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_panel_locate_city:
				panelLocCity.setVisibility(View.GONE);
				panelCommunity.setVisibility(View.GONE);
				panelChina.setVisibility(View.VISIBLE);
				break;
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.ll_panel_locate_china:
				ToastUtil.Show(mContext, "目前仅支持中国境内");
			 	break;
			default:
				break;
			}
		}
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_COMMUNITY_DATA_DONE:
			fillData();
			break;
		case OpCodes.GET_COMMUNITY_DATA_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void fillData() {
		Provinces.clear();
		Provinces.addAll(xBean.getProvince());
		Cities.clear();
		if(TextUtils.isEmpty(temProvince)){
			temProvince=Provinces.get(0);
		}
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
		if(TextUtils.isEmpty(temCity)){
			temCity=xBean.getCity(temProvince).get(0);
		}
		Counties.addAll(xBean.getCounty(temCity));
		if (Counties.size() == 0) {
			Toast.makeText(mContext, "该城市没有小区覆盖服务", Toast.LENGTH_SHORT).show();
		} else {
			countyAdapter.notifyDataSetChanged();
			countyAdapter.setIndex(Counties.indexOf(temCounty));
			refreshCommunityData(temCounty);
		}
	}

	
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

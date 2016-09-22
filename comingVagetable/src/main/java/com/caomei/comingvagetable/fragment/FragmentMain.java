package com.caomei.comingvagetable.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.MyPurseActivity;
import com.caomei.comingvagetable.activity.ProductDetailActivity;
import com.caomei.comingvagetable.activity.PromotionActivity;
import com.caomei.comingvagetable.activity.SearchActivity;
import com.caomei.comingvagetable.activity.SelectAreaActivity;
import com.caomei.comingvagetable.adapter.MyGridViewAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.AddressData;
import com.caomei.comingvagetable.bean.GetActivityEntity;
import com.caomei.comingvagetable.bean.LocateBean;
import com.caomei.comingvagetable.bean.eventbus.CategoryBus;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.eventbus.UpdateMainDataSetBean;
import com.caomei.comingvagetable.bean.vegedata.VegeAllBean;
import com.caomei.comingvagetable.bean.vegedata.VegeAllBeanData;
import com.caomei.comingvagetable.housekeeper.activity.HouseKeeperActivity;
import com.caomei.comingvagetable.loadmoregridview.GridViewWithHeaderAndFooter;
import com.caomei.comingvagetable.refresh.PtrClassicFrameLayout;
import com.caomei.comingvagetable.refresh.PtrFrameLayout;
import com.caomei.comingvagetable.refresh.PtrHandler;
import com.caomei.comingvagetable.takeaway.activity.HomeTakeawayActivity;
import com.caomei.comingvagetable.util.FinalAsyncHttpClient;
import com.caomei.comingvagetable.util.ImageUtil;
import com.caomei.comingvagetable.util.MethodUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.caomei.comingvagetable.wxapi.WXEntryActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.minking.imagecycleview.ImageCycleView;
import com.minking.imagecycleview.PicSlideInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class FragmentMain extends Fragment {
	private Context mContext;
	private MyGridViewAdapter mAdapter;
	private ArrayList<VegeAllBeanData> gridDataset;
	private ArrayList<PicSlideInfo> slidePics = new ArrayList<PicSlideInfo>();
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private LayoutInflater mInflater;
	private View mView;
	private CommonListener mListener;
	private String curType = "-1";
	private String mURL;
	private String vegeType;
	private int mPage = 1;
	private int mContainer = 3 * 10;

	private VegeAllBean vBean;
	private LocateBean lBean;
	private ProgressDialog pDialog;
	private AlertDialog myDialog;
	private TextView homeAddr;
	private boolean getHomeAddr;
	private AddressData data;
	private GridViewWithHeaderAndFooter gridView;
	private View headView;
	private PtrClassicFrameLayout ptrFrameLayout;

	private boolean isPullToFresh;
	private ImageCycleView mCycleViewPics;
	private ArrayList<String> picUrls;
	private LinearLayout searchPanel;
	private ImageView ivMsg;
	//分别是小吃街、年货预售、摇奖区、管家商城
	private TextView main_snack_street, main_spring_festival_goods,main_draw_lottery, main_housekeeper_mall;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);

		// 初始化页面的数据
		mContext = getActivity();

		pDialog = new ProgressDialog(mContext);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setIndeterminate(true);
		pDialog.setCancelable(true);
		mListener = new CommonListener();
		gridDataset = new ArrayList<VegeAllBeanData>();
		picUrls = new ArrayList<String>();
		//picUrls.add("http://b.picphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=b7903557082442a7aa0ef9a3e173963a/cb8065380cd79123be1c9ba0aa345982b3b78061.jpg");
		//picUrls.add("http://f.picphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=c4abb0f7642762d0843ea0b990dc338b/810a19d8bc3eb1355ebdb86fa11ea8d3fc1f44ef.jpg");
		//picUrls.add("http://g.picphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=64a541100dd162d981ee661a21ef929d/9358d109b3de9c82b7f5f65b6b81800a18d84355.jpg");
		//picUrls.add("http://c.picphotos.baidu.com/album/s%3D1600%3Bq%3D90/sign=7acc410853e736d15c13880eab6074b3/cefc1e178a82b9018852cce7748da9773812ef63.jpg");
		requestActivity();
	}

	//Picture_showPicture
	public GetActivityEntity activityEntity;
	private void requestActivity() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = String.format(CommonAPI.URL_GET_ACTIVITY, ShareUtil.getInstance(mContext).getUserId());
				Log.e("url", "主页获取banner图的url: " + url);
				AccessNetResultBean aBean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (aBean.getState() == AccessNetState.Success) {
					try {
						activityEntity = new Gson().fromJson(aBean.getResult(),
								GetActivityEntity.class);
						Log.e("data", aBean.getResult());
						EventBus.getDefault().post(
								new EventMsg(
										OpCodes.GET_ACTIVYT_DONE,
										null));
					} catch (Exception e) {
						EventBus.getDefault()
								.post(new EventMsg(
										OpCodes.GET_ACTIVYT_ERROR,
										mContext.getResources().getString(
												R.string.get_data_error_format)));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(
									OpCodes.GET_ACTIVYT_ERROR,
									mContext.getResources().getString(
											R.string.request_failed)));
				}
			}
		}).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		mView = inflater.inflate(R.layout.fragment_main, container, false);
		ivMsg = (ImageView) mView.findViewById(R.id.iv_msg);

		gridView = (GridViewWithHeaderAndFooter) mView
				.findViewById(R.id.gv_gift);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mAdapter = new MyGridViewAdapter(mContext, gridDataset);
		headView = mInflater.inflate(R.layout.fragment_main_header, null);
		mCycleViewPics = (ImageCycleView) headView
				.findViewById(R.id.ci_slide_pager);
		searchPanel = (LinearLayout) mView.findViewById(R.id.search_panel);

		gridView.addHeaderView(headView);
		searchPanel.setOnClickListener(mListener);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new VegeItemClickListener());
		ptrFrameLayout = (PtrClassicFrameLayout) mView.findViewById(R.id.refreshView);
		ptrFrameLayout.disableWhenHorizontalMove(true);
		ptrFrameLayout.setLastUpdateTimeRelateObject(this);

		ptrFrameLayout.setPtrHandler(new PtrHandler() {
			public boolean checkCanDoRefresh(final PtrFrameLayout frame,
					final View content, final View header) {
				return gridView.getFirstVisiblePosition() == 0
						&& gridView.getChildAt(0).getTop() == 0;
			}

			public void onRefreshBegin(PtrFrameLayout frame) {
				ptrFrameLayout.postDelayed(new Runnable() {
					public void run() {
						isPullToFresh = true;
						requestDataByCategory(curType);
					}
				}, 10);
			}
		});

		mAdapter.notifyDataSetChanged();

		ImageView ivCategory = (ImageView) mView.findViewById(R.id.iv_category);
		LinearLayout panelLoc = (LinearLayout) headView
				.findViewById(R.id.panel_locate);
		panelLoc.setOnClickListener(mListener);
		ivCategory.setOnClickListener(mListener);
		ivMsg.setOnClickListener(mListener);
		homeAddr = ((TextView) headView.findViewById(R.id.tv_located_village));

		if (!"0".equals(ShareUtil.getInstance(mContext).getHomeCommunityID())) {
			homeAddr.setText(ShareUtil.getInstance(mContext).getHomeCommunity());
			requestDataByCategory(curType);
		}

		main_snack_street = (TextView) headView.findViewById(R.id.main_snack_street);
		main_draw_lottery = (TextView) headView.findViewById(R.id.main_draw_lottery);
		main_housekeeper_mall = (TextView) headView.findViewById(R.id.main_housekeeper_mall);
		main_spring_festival_goods = (TextView) headView.findViewById(R.id.main_spring_festival_goods);
		main_spring_festival_goods.setOnClickListener(mListener);
		main_housekeeper_mall.setOnClickListener(mListener);
		main_draw_lottery.setOnClickListener(mListener);
		main_snack_street.setOnClickListener(mListener);
	}

	private void initLocation() {
		mLocationClient = new LocationClient(
				mContext.getApplicationContext()); //
		mLocationClient.registerLocationListener(myListener); //
		setLocationOption();
		mLocationClient.start();
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.disableCache(true);
		option.setPoiNumber(10);
		option.setPoiDistance(2000);
		option.setPoiExtraInfo(true);
		mLocationClient.setLocOption(option);
	}

	private void resetGridData() {
		gridDataset.clear();
		gridDataset.addAll(vBean.getData());
		if (vBean.getData() == null || vBean.getData().size() == 0) {
			Toast.makeText(mContext, "未获取到相应的菜品信息", Toast.LENGTH_SHORT).show();
		}
		mAdapter.notifyDataSetChanged();
	}

	private void Toggle() {
		((MainActivity) mContext).mDrawerLayout.closeDrawers();
	}

	/*主页获取菜品信息*/
	private void requestDataByCategory(final String id) {
		String url = String.format(CommonAPI.URL_VEGE_MAIN, ShareUtil.getInstance(mContext).getHomeCommunityID(), 0, 10000,
				null, id, ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "主页获取菜品信息的url: " + url);
		AsyncHttpClient client = new FinalAsyncHttpClient().getAsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				try {
					vBean = new Gson().fromJson(new String(bytes), VegeAllBean.class);
					Log.e("data", new String(bytes));
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_VEGE_DATA_BY_CATEGORY_DONE, null));
				} catch (Exception e) {
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_VEGE_DATA_BY_CATEGORY_ERROR, mContext.getResources().getString(
							R.string.get_data_error_format)));
				}
			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				EventBus.getDefault().post(new EventMsg(OpCodes.GET_VEGE_DATA_BY_CATEGORY_ERROR, mContext.getResources().getString(
						R.string.request_failed)));
			}
		});

	}

	/*private void requestDataByCategory(final String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = String.format(CommonAPI.URL_VEGE_MAIN, ShareUtil.getInstance(mContext).getHomeCommunityID(), 0, 100000,
						null, id, ShareUtil.getInstance(mContext).getUserId());
				Log.e("url", "主页获取菜品信息的url: " + url);
				AccessNetResultBean aBean = NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if (aBean.getState() == AccessNetState.Success) {
					try {
						vBean = new Gson().fromJson(aBean.getResult(), VegeAllBean.class);
						Log.e("data", aBean.getResult());
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_VEGE_DATA_BY_CATEGORY_DONE, null));
					} catch (Exception e) {
						EventBus.getDefault().post(new EventMsg(OpCodes.GET_VEGE_DATA_BY_CATEGORY_ERROR, mContext.getResources().getString(
												R.string.get_data_error_format)));
					}
				} else {
					EventBus.getDefault().post(new EventMsg(OpCodes.GET_VEGE_DATA_BY_CATEGORY_ERROR, mContext.getResources().getString(
											R.string.request_failed)));
				}
			}
		}).start();
	}*/

	/**
	 * 显示定位小区信息的对话框
	 */
	private void ShowDialog() {
		myDialog = new AlertDialog.Builder(mContext).create();
		myDialog.show();
		myDialog.setCancelable(false);
		myDialog.getWindow().setContentView(R.layout.location_dialog);
		((TextView) myDialog.getWindow().findViewById(
				R.id.tv_baidu_located_country)).setText(ShareUtil.getInstance(
				mContext).getLocatedCountry());
		myDialog.getWindow().findViewById(R.id.tv_modify_home)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						myDialog.cancel();
						((MainActivity) mContext).startNewActivity(
								SelectAreaActivity.class,
								R.anim.activity_slide_left_in,
								R.anim.activity_slide_right_out, false, null);
					}
				});
		myDialog.getWindow().findViewById(R.id.tv_modify_cancel)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						myDialog.cancel();
					}
				});
	}

	/**
	 * 获取默认地址/设置默认收货地址 以便显示相应小区附近的菜品
	 */
	private void getHomeAddr() {
		if (getHomeAddr) {
			return;
		} else {
			getHomeAddr = true;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_GET_DEFAULT_ADDRESS,
								ShareUtil.getInstance(mContext).getUserId());
				Log.e("url", "默认地址url" + url);
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);

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
										ex.getLocalizedMessage()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_DEFAULT_ADDRESS_ERROR,
									"请求默认地址出错" + bean.getResult()));
				}
			}
		}).start();

	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("定位 : ");
			sb.append(location.getTime());
			sb.append(location.getLocType());
			sb.append(location.getLatitude());
			sb.append(location.getLongitude());
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append(location.getSpeed());
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append(location.getAddrStr());
			}
			Log.e("loc", "定位的详细信息：" + sb.toString());
			String res = location.getAddrStr();
			try {
				mLocationClient.stop();
				ShareUtil.getInstance(mContext).setLocatedCity(
						MethodUtil.getCityName(res, 0));
				ShareUtil.getInstance(mContext).setLocatedCountry(
						MethodUtil.getCityName(res, 1));
				ShareUtil.getInstance(mContext).setLocatedCommunity(
						MethodUtil.getCityName(res, 2));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if ("点击定位".equals(ShareUtil.getInstance(mContext)
						.getHomeCommunity())) {
					getHomeAddr();
				} else {
				}
				getHomeAddr();
			} catch (Exception ex) {
				getHomeAddr();
			}
			mLocationClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr 000: ");
				sb.append(poiLocation.getAddrStr());
				String res = poiLocation.getAddrStr();
				try {
					ShareUtil.getInstance(mContext).setLocatedCity(
							MethodUtil.getCityName(res, 0));
					ShareUtil.getInstance(mContext).setLocatedCountry(
							MethodUtil.getCityName(res, 1));
					ShareUtil.getInstance(mContext).setLocatedCommunity(
							MethodUtil.getCityName(res, 2));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}

			lBean = new Gson().fromJson(poiLocation.getPoi(), LocateBean.class);

			mLocationClient.stop();
		}
	}

	class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.panel_locate:
				Bundle b = new Bundle();
				b.putSerializable("data", data);
				((MainActivity) mContext).startNewActivity(
						SelectAreaActivity.class,
						R.anim.activity_slide_left_in,
						R.anim.activity_slide_right_out, false, b);
				break;
			case R.id.iv_category:
				if (((MainActivity) mContext).mDrawerLayout
						.isDrawerOpen(Gravity.LEFT)) {
					((MainActivity) mContext).mDrawerLayout
							.closeDrawer(Gravity.LEFT);
				} else {
					((MainActivity) mContext).mDrawerLayout
							.openDrawer(Gravity.LEFT);
				}
				break;
			case R.id.search_panel:
				((MainActivity) mContext).startNewActivity(
						SearchActivity.class, R.anim.activity_slide_left_in,
						R.anim.activity_slide_right_out, false, null);
				break;
			case R.id.iv_msg:
				((MainActivity) mContext).startNewActivity(
						WXEntryActivity.class, R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);
			case R.id.main_housekeeper_mall:
				if("0".equals(ShareUtil.getInstance(mContext).getHomeCommunityID())){
					ToastUtil.Show(mContext, "请先选择小区...");
				}else{
					Bundle bundle = new Bundle();
					bundle.putString("community_id", ShareUtil.getInstance(mContext).getHomeCommunityID());
					((MainActivity) mContext).startNewActivity(
							HouseKeeperActivity.class, R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, bundle);
				}
				break;
			case R.id.main_draw_lottery:
			case R.id.main_snack_street:
				ToastUtil.Show(mContext, "正在开发中，敬请期待！");
				break;
			case R.id.main_spring_festival_goods:
				((MainActivity) mContext).startNewActivity(HomeTakeawayActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, false, null);
				break;
			default:
				break;
			}
		}
	}

	class VegeItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle bundle = new Bundle();
			bundle.putString("id", gridDataset.get(arg2).vege_id);
			bundle.putString("pc", gridDataset.get(arg2).getProChannel());
			bundle.putString("pw", gridDataset.get(arg2).getProcessWay());
			bundle.putFloat("volume", gridDataset.get(arg2).getPerUnitVolume());
			bundle.putString("weight", "重量：≈" + gridDataset.get(arg2).perUnitWeight  + "kg/" + gridDataset.get(arg2).getUnit());
			((MainActivity) mContext).startNewActivity(
					ProductDetailActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, false, bundle);
		}
	}

	public void onEventMainThread(CategoryBus mBus) {
		String id = mBus.getmData().get(mBus.getPos()).getVegetypeid();
		vegeType = id;
		((MainActivity) mContext).mDrawerLayout.closeDrawers();

	}

	public void onEventMainThread(UpdateMainDataSetBean bean) {
		if (bean.getmData() == null || bean.getFlag() == -1) {
			Toast.makeText(mContext, R.string.request_data_failed,
					Toast.LENGTH_SHORT).show();
			return;
		} else if (bean.getFlag() == 0) {
			gridDataset.clear();
			gridDataset.addAll(bean.getmData());
			mAdapter.notifyDataSetChanged();
		} else if (bean.getFlag() == 1) {
			gridDataset.addAll(bean.getmData());
			mAdapter.setData(gridDataset);
		}

		mPage++;
	}

	public void onEventMainThread(final EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.MENU_CATEGORY_CLICK:
			Toggle();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					pDialog.show();
					curType = msg.getData().toString();
					requestDataByCategory(msg.getData().toString());
				}
			}, 300);

			break;
		case OpCodes.GET_VEGE_DATA_BY_CATEGORY_DONE:
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			ptrFrameLayout.refreshComplete();
			resetGridData();
			break;
		case OpCodes.GET_VEGE_DATA_BY_CATEGORY_ERROR:
			ptrFrameLayout.refreshComplete();
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;
			case OpCodes.GET_ACTIVYT_DONE:
				addImage();
				break;
			case OpCodes.GET_ACTIVYT_ERROR:
				addImage();
				break;
		case OpCodes.GET_DEFAULT_ADDRESS_NULL:
		case OpCodes.GET_DEFAULT_ADDRESS_ERROR:
			ShowDialog();
			break;
		case OpCodes.GET_DEFAULT_ADDRESS_DONE:
			Log.e("data", "获取到默认地址");
			data = (AddressData) msg.getData();
			ShareUtil.getInstance(mContext).setHomeCommunityID(
					data.getCommunity_id());
			ShareUtil.getInstance(mContext).setDefaultAddressId(
					data.getAddress_id());
			ShareUtil.getInstance(mContext).setHomeCommunity(
					data.getCommunityName());
			homeAddr.setText(data.getCommunityName());
			requestDataByCategory(curType);
			break;
		case OpCodes.SET_HOME_COMMUNITY_DONE:
			homeAddr.setText(ShareUtil.getInstance(mContext).getHomeCommunity());
			requestDataByCategory(curType);
			break;
		case OpCodes.GET_VEGE_ALL_ERROR:
			break;
		case OpCodes.GET_VEGE_ALL_DONE:
			gridDataset = vBean.getData();
			mAdapter.setData(gridDataset);
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void addDefaultImage() {
		picUrls.add("http://www.happycopy.cn:8080/zouma_t1/banner.jpg");
		picUrls.add("http://www.happycopy.cn:8080/zouma_t1/banner1.jpg");
		picUrls.add("http://www.happycopy.cn:8080/zouma_t1/banner2.jpg");
		picUrls.add("http://www.happycopy.cn:8080/zouma_t1/banner3.jpg");
	}

	private void addImage() {
		for(GetActivityEntity.ActivityEntity entity: activityEntity.data){
			picUrls.add(String.format(CommonAPI.URL_GET_ACTIVITY_IMAGE, entity.id, "PictureOfCommon", ShareUtil.getInstance(mContext).getUserId()));
		}
		if(picUrls.size() == 0){
			addDefaultImage();
		}
		slidePics.clear();
		for (int i = 0; i < picUrls.size(); i++) {
			PicSlideInfo info = new PicSlideInfo();
			info.setUrl(picUrls.get(i));
			info.setContent("test");
			slidePics.add(info);
		}
		mCycleViewPics.setImageResources(slidePics, mCycleViewListener);
	}

	private ImageCycleView.ImageCycleViewListener mCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

		@Override
		public void onImageClick(PicSlideInfo info, int position, View imageView) {
			if(activityEntity != null && activityEntity.data != null && activityEntity.data.size() > position){
				if(activityEntity.data.get(position).activity_url != null && !"".equals(activityEntity.data.get(position).activity_url)){
					if("activity".equals(activityEntity.data.get(position).activity_url)){//跳转支付页面
						((MainActivity) mContext).startNewActivity(
								MyPurseActivity.class,
								R.anim.activity_slide_right_in,
								R.anim.activity_slide_left_out, false, null);
					}else{
						Bundle intent = new Bundle();
						intent.putString("link_url", CommonAPI.BASE_URL + activityEntity.data.get(position).activity_url);
						intent.putString("title", activityEntity.data.get(position).title);
						((MainActivity) mContext).startNewActivity(
								PromotionActivity.class,
								R.anim.activity_slide_right_in,
								R.anim.activity_slide_left_out, false, intent);
					}
				}
			}
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView,
					ImageUtil.imageVegeOptions);
		}
	};

	@Override
	public void onResume() {
		initLocation();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}

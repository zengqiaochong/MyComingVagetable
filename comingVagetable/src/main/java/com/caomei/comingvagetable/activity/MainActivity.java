package com.caomei.comingvagetable.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.CheckUpdateBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.caomei.comingvagetable.controls.ColorTextView;
import com.caomei.comingvagetable.fragment.FragmentFuns;
import com.caomei.comingvagetable.fragment.FragmentMain;
import com.caomei.comingvagetable.fragment.FragmentOrder;
import com.caomei.comingvagetable.fragment.FragmentUser;
import com.caomei.comingvagetable.service.DownloadApkService;
import com.caomei.comingvagetable.util.AppUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {
	public DrawerLayout mDrawerLayout;
	private FragmentMain mFragmentMain = null;
	private FragmentOrder mFragmentOrder = null;
	private FragmentUser mFragmentUser = null;
	private FragmentFuns mFragmentFuns = null;
	private LinearLayout ivHome;
	private LinearLayout ivCart;
	private LinearLayout ivSign;
	private LinearLayout ivFans;
	private LinearLayout ivUser;
	private Context mContext;
	private FragmentManager fm;
	private Fragment curFragment;
	private boolean canExit = false;
	private FragmentTransaction ft;
	private ColorTextView tvCartCount;
	private AlertDialog myDialog;
	private String apkUrl;
	private boolean isDestroy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		myDialog = new AlertDialog.Builder(mContext).create();
		EventBus.getDefault().register(this);
		clearFragments();
		initFragments();
		initView();
		initEvents();
		//NetUtil.getDeliverFee(mContext);
		NetUtil.getCartData(mContext);
		requestUpdateInfo();
	}

	private void initFragments() {
		fm = getSupportFragmentManager();
		mFragmentMain = new FragmentMain();
		ft = fm.beginTransaction();
		curFragment = mFragmentMain;
		ft.add(R.id.fragment_content, mFragmentMain);
		ft.show(mFragmentMain);
		ft.commit();
	}

	private void clearFragments() {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if (fragments != null) {
			for (Fragment f : fragments) {
				if (f != null) {
					getSupportFragmentManager().beginTransaction().remove(f)
							.commit();
				}
			}
		}
	}

	private void initView() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
				Gravity.RIGHT);
		ivHome = (LinearLayout) findViewById(R.id.v_home);
		ivCart = (LinearLayout) findViewById(R.id.v_cart);
		ivSign = (LinearLayout) findViewById(R.id.v_order);
		ivUser = (LinearLayout) findViewById(R.id.v_user_center);
		ivFans = (LinearLayout) findViewById(R.id.v_fans);
		CommonListener listener = new CommonListener();
		ivHome.setOnClickListener(listener);
		ivCart.setOnClickListener(listener);
		ivSign.setOnClickListener(listener);
		ivUser.setOnClickListener(listener);
		ivFans.setOnClickListener(listener);
		
		tvCartCount=(ColorTextView)findViewById(R.id.ct_goods_count);
		
	}

	private void initEvents() {
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				Log.e("drawer", " offset :" + slideOffset);
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;

				if (drawerView.getTag().equals("LEFT")) {
					float leftScale = 1 - 0.3f * scale;
					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.8f + 0.2f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}
		});
	}

	class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.v_home:
				Log.e("click", "iv home click");
				mDrawerLayout.setEnabled(true);
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				ft = fm.beginTransaction();
				if (curFragment != null) {
					ft.hide(curFragment);
				}
				if (mFragmentMain == null) {
					mFragmentMain = new FragmentMain();
				}
				curFragment = mFragmentMain;
				ft.show(mFragmentMain);
				ft.commitAllowingStateLoss();
				resetTabView();
				((ImageView) ivHome.findViewById(R.id.iv_home))
						.setImageResource(R.drawable.iv_home_active);
				break;
			case R.id.v_order:
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mDrawerLayout.setEnabled(false);
				if (!ShareUtil.getInstance(mContext).getIsLogin()) {
					Bundle b = new Bundle();
					b.putString("src", "FragmentSign");
					startNewActivity(LoginActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, b);
					return;
				}

				ft = fm.beginTransaction();
				if (curFragment != null) {
					ft.hide(curFragment);
				}
				if (mFragmentOrder == null) {
					mFragmentOrder = new FragmentOrder(mContext);
					ft.add(R.id.fragment_content, mFragmentOrder);
				}
				curFragment = mFragmentOrder;
				ft.show(mFragmentOrder);
				ft.commitAllowingStateLoss();
				resetTabView();
				((ImageView) ivSign.findViewById(R.id.iv_order))
						.setImageResource(R.drawable.iv_sign_active);
				break;
			case R.id.v_user_center:
				if (!ShareUtil.getInstance(mContext).getIsLogin()) {
					startNewActivity(LoginActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, null);
					return;
				}
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mDrawerLayout.setEnabled(false);
				ft = fm.beginTransaction();
				if (curFragment != null) {
					ft.hide(curFragment);
				}
				if (mFragmentUser == null) {
					mFragmentUser = new FragmentUser();
					ft.add(R.id.fragment_content, mFragmentUser);
				}
				curFragment = mFragmentUser;
				ft.show(mFragmentUser);
				ft.commitAllowingStateLoss();
				resetTabView();
				((ImageView) ivUser.findViewById(R.id.iv_user_center))
						.setImageResource(R.drawable.iv_user_active);
				break;

			case R.id.iv_category:
				mDrawerLayout.openDrawer(Gravity.LEFT);
				break;
			case R.id.v_fans:
				if (!ShareUtil.getInstance(mContext).getIsLogin()) {
					startNewActivity(LoginActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, null);
					return;
				}
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mDrawerLayout.setEnabled(false);
				ft = fm.beginTransaction();
				if (curFragment != null) {
					ft.hide(curFragment);
				}
				if (mFragmentFuns == null) {
					mFragmentFuns = new FragmentFuns();
					ft.add(R.id.fragment_content, mFragmentFuns);
				}
				curFragment = mFragmentFuns;
				ft.show(mFragmentFuns);
				ft.commitAllowingStateLoss();
				resetTabView();
				((ImageView) ivFans.findViewById(R.id.iv_fans))
						.setImageResource(R.drawable.iv_fans_active);
				break;
			case R.id.v_cart:
				if (!ShareUtil.getInstance(mContext).getIsLogin()) {
					startNewActivity(LoginActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, false, null);
					return;
				}
				startNewActivity(CartActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);
				break;
			}
		}
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.LOG_OUT:
			mDrawerLayout.setEnabled(true);
			mDrawerLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			ft = fm.beginTransaction();
			if (curFragment != null) {
				ft.hide(curFragment);
			}
			if (mFragmentMain == null) {
				mFragmentMain = new FragmentMain();
			}
			curFragment = mFragmentMain;
			ft.show(mFragmentMain);
			ft.commitAllowingStateLoss();
			resetTabView();
			((ImageView) ivHome.findViewById(R.id.iv_home))
					.setImageResource(R.drawable.iv_home_active);
			break;
		case OpCodes.DEVEL_FEE_GET_DONE:
			ShareUtil.getInstance(mContext).setDeliverFee(
					msg.getData().toString());
			break;
		case OpCodes.CART_DATA_GET_DONE:
			VegeCartBean vbBean = (VegeCartBean) msg.getData();
			tvCartCount.setText(String.valueOf(vbBean.getTotalCount()));
			break;
		case OpCodes.PAY_ORDER_DONE:
		case OpCodes.SUBMIT_ORDER_DONE:
			NetUtil.getCartData(mContext);
			break;
		case OpCodes.HAS_UPDATE_VERSION:
			apkUrl = msg.getData().toString();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					showDialog();
				}
			}, 3000);
			break;
		default:
			break;
		}
	}
	public void showDialog() {
		if(isDestroy)return;
		myDialog = new AlertDialog.Builder(mContext).create();
		myDialog.show();
		myDialog.setCancelable(false);
		myDialog.getWindow().setContentView(R.layout.update_dialog);
		boolean isWifi = NetUtil.isWifiConnected(mContext);
		if (!isWifi) {
			myDialog.findViewById(R.id.tv_notice).setVisibility(View.VISIBLE);
		}
		myDialog.getWindow().findViewById(R.id.tv_update_now)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						myDialog.cancel();
						DownloadLastestApk();
						Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT)
								.show();
					}
				});
		myDialog.getWindow().findViewById(R.id.tv_update_cancel)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						myDialog.cancel();
					}
				});
	}
	protected void DownloadLastestApk() {

		Bundle mBundle = new Bundle();
		mBundle.putString("url", apkUrl);
		mBundle.putString("app_title", "菜来了");
		Intent in = new Intent(mContext, DownloadApkService.class);
		in.putExtras(mBundle);

		mContext.startService(in);
	}
	private void resetTabView() {

		((ImageView) ivFans.findViewById(R.id.iv_fans))
				.setImageResource(R.drawable.iv_fans);
		((ImageView) ivHome.findViewById(R.id.iv_home))
				.setImageResource(R.drawable.iv_home);
		((ImageView) ivSign.findViewById(R.id.iv_order))
				.setImageResource(R.drawable.iv_sign);
		((ImageView) ivUser.findViewById(R.id.iv_user_center))
				.setImageResource(R.drawable.iv_user);
	}

	@Override
	public void onBackPressed() {
		if (canExit) {
			isDestroy=true;
			super.onBackPressed();
		} else {
			canExit = true;
			Toast.makeText(this, "再按一次退出菜來了", Toast.LENGTH_SHORT).show();
			new Timer().schedule(new TimerTask() {
				public void run() {
					canExit = false;
				}
			}, 2000);
		}
	}
	private void requestUpdateInfo() {
		final String url = CommonAPI.BASE_URL + CommonAPI.URL_GET_UPDATE_INFO;
		Log.e("url", "检测更新接口： " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					AccessNetResultBean bean = NetUtil.getInstance(mContext)
							.getDataFromNetByGet(url);
					if (bean.getState() == AccessNetState.Success) {
						CheckUpdateBean cuBean = new Gson().fromJson(
								bean.getResult(), CheckUpdateBean.class);
						if (cuBean.getRESULT_TYPE() == 1) {
							int verOnline = cuBean.getRESULT_APP_VERSION();

							int curVc = Integer.parseInt(AppUtil
									.getAppVersionCode_VersionName(mContext, 1));
							if (verOnline > curVc) {
								EventBus.getDefault().post(
										new EventMsg(
												OpCodes.HAS_UPDATE_VERSION,
												cuBean.getRESULT_APP_URL()));
							}
						}
					}
				} catch (Exception ex) {
					Log.e("error", ex.getLocalizedMessage());
				}
			}
		}).start();

	}
	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}

package com.caomei.comingvagetable.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.IDialogOperation;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.CheckUpdateBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.service.DownloadApkService;
import com.caomei.comingvagetable.util.AppUtil;
import com.caomei.comingvagetable.util.DialogUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class SettingActivity extends BaseActivity {

	private TextView tvVersion;
	private RelativeLayout panelLogout;

	private RelativeLayout panelUpdate;
	private CommonListener mListener;
	private boolean hasNewVersion;
	private AlertDialog myDialog;
	private String apkUrl;
	private DownloadManager downManager;
	private DownLoadCompleteReceiver receiver;
	private RelativeLayout panelAbout;
	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		EventBus.getDefault().register(this);
		initViews();
		initData();
	}

	private void initViews() {
		tvVersion = (TextView) findViewById(R.id.tv_version_cur);
		tvVersion.setText(AppUtil.getAppVersionCode_VersionName(mContext, 2));
		panelLogout = (RelativeLayout) findViewById(R.id.panel_logout);
		panelUpdate = (RelativeLayout) findViewById(R.id.panel_update);
		panelLogout = (RelativeLayout) findViewById(R.id.panel_logout);
		panelAbout = (RelativeLayout) findViewById(R.id.panel_about);
		ivBack = (ImageView) findViewById(R.id.iv_back);
	}

	private void initData() {
		mListener = new CommonListener();
		panelUpdate.setOnClickListener(mListener);
		panelLogout.setOnClickListener(mListener);
		panelAbout.setOnClickListener(mListener);
		ivBack.setOnClickListener(mListener);
		downManager = (DownloadManager) mContext
				.getSystemService(Context.DOWNLOAD_SERVICE);
		receiver = new DownLoadCompleteReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
		mContext.registerReceiver(receiver, filter);
		requestUpdateInfo();
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

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.ADD_TO_CART_REQUEST_FAILED:

			break;
		case OpCodes.HAS_UPDATE_VERSION:
			delOldApk();
			panelUpdate.findViewById(R.id.tv_update_indicator).setVisibility(
					View.VISIBLE);
			hasNewVersion = true;
			// apkUrl="http://7fvh60.com2.z0.glb.qiniucdn.com/calendar-20268_0_1459403244.apk";
			apkUrl = msg.getData().toString();
			Log.e("url", "apk的下载链接：" + apkUrl);
			break;
		case OpCodes.LOG_OUT:
			finish();
			break;
		default:
			break;
		}

	}

	protected void DownloadLastestApk() {

		Bundle mBundle = new Bundle();
		mBundle.putString("url", apkUrl);
		mBundle.putString("app_title", "菜来了");
		Intent in = new Intent(mContext, DownloadApkService.class);
		in.putExtras(mBundle);

		mContext.startService(in);
	}

	public class CommonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.panel_update:
				if (hasNewVersion) {
					showDialog();
				} else {
					Toast.makeText(mContext, "暂无更新", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.panel_logout:
				LogInOrNot();
				break;
			case R.id.panel_about:
				DialogUtil.DefaultDialog(mContext, "关于菜来了", "'菜来了'隶属于湖南阿帕网络科技有限责任公司，是一家农产品移动电子商务平台，省去繁琐的市场采购过程；让您足不出户就能吃上新鲜绿色的健康蔬果。当日22点前下单，次日凌晨即可全程冷链送达至您的家门口。",
						"知道了", new IDialogOperation() {
							
							@Override
							public void onPositive() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onNegative() {
								// TODO Auto-generated method stub
								
							}
						});
				break;
			case R.id.iv_back:
				onBackPressed();
				break;
			
			}
		}
	}

	private void LogInOrNot() {

		Dialog alertDialog = new AlertDialog.Builder(mContext)
				.setTitle("提示！")
				.setMessage("是否退出登录？")
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ShareUtil.getInstance(mContext).setIsLogin(false);
						Toast.makeText(mContext, "退出登录", Toast.LENGTH_SHORT)
								.show();
						EventBus.getDefault().post(
								new EventMsg(OpCodes.LOG_OUT, null));
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).create();
		alertDialog.show();

	}

	public void showDialog() {
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

	protected void delOldApk() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/CVDownload/";
				File dir = new File(path);
				if (dir == null || !dir.exists() || !dir.isDirectory()) {
					dir.mkdir();
					return;
				}
				for (File file : dir.listFiles()) {
					if (file.isFile()) {
						file.delete();
					} else if (file.isDirectory()) {

					}
				}
			}
		}).start();

	}

	private class DownLoadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				// long id = intent.getLongExtra(
				// DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				// Toast.makeText(mContext, "下载完成",
				// Toast.LENGTH_SHORT).show();
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/CVDownload/comingvegetable.apk";
				Log.e("uri", path);
				final File file = new File(path);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						openFile(file);
					}
				}, 2000);

			} else if (intent.getAction().equals(
					DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
			}
		}
	}

	private void openFile(File file) {
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		mContext.unregisterReceiver(receiver);
		super.onDestroy();
	}

}

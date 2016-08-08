package com.caomei.comingvagetable.fragment;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.activity.AddressManageActivity;
import com.caomei.comingvagetable.activity.MyPurseActivity;
import com.caomei.comingvagetable.activity.MainActivity;
import com.caomei.comingvagetable.activity.SettingActivity;
import com.caomei.comingvagetable.activity.UserInfoActivity;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.CheckUpdateBean;
import com.caomei.comingvagetable.bean.UserInfoBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.service.DownloadApkService;
import com.caomei.comingvagetable.util.AppUtil;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import de.greenrobot.event.EventBus;

public class FragmentUser extends Fragment {

	private Context mContext;
	private View mView;
	private RelativeLayout panelAddr;
	private CommonListener mListener;
	private ImageView ivUserHead;
	private RelativeLayout panelUserInfo;
	private RelativeLayout panelMoney;
	private RelativeLayout panelContact;
	private RelativeLayout panelSetting;
	private UserInfoBean ucInfoBbean;
	private TextView tvMoney;
	private TextView tvMadun;
	private TextView tvLevel;
	private TextView tvUserNickName;
	private TextView tvJifen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		EventBus.getDefault().register(this);
		mListener = new CommonListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_user_center, null);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		setView();
		setData();
	}

	private void setView() {
		panelAddr = (RelativeLayout) mView.findViewById(R.id.panel_my_addr);
		ivUserHead = (ImageView) mView.findViewById(R.id.iv_user_header);
		panelUserInfo = (RelativeLayout) mView
				.findViewById(R.id.rl_panel_user_info);
		panelMoney = (RelativeLayout) mView.findViewById(R.id.rl_panel_money);

		panelContact = (RelativeLayout) mView
				.findViewById(R.id.rl_contract__us);
		panelSetting = (RelativeLayout) mView
				.findViewById(R.id.rl_panel_setting);
		tvMoney = (TextView) mView.findViewById(R.id.tv_money);
		tvMadun = (TextView) mView.findViewById(R.id.tv_madun);
		tvLevel = (TextView) mView.findViewById(R.id.tv_level);
		tvJifen = (TextView) mView.findViewById(R.id.tv_jifen);
		tvUserNickName = (TextView) mView.findViewById(R.id.tv_user_nicename);
	}

	private void setData() {
		panelAddr.setOnClickListener(mListener);
		panelUserInfo.setOnClickListener(mListener);
		panelContact.setOnClickListener(mListener);
		panelMoney.setOnClickListener(mListener);
		panelSetting.setOnClickListener(mListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		requestUserInfo();
	}

	private void requestUserInfo() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_USER_INFO, ShareUtil
						.getInstance(mContext).getUserId());
		Log.e("url", "获取用户信息接口： " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						Log.e("data", "用戶信息：  "+bean.getResult());
						ucInfoBbean = new Gson().fromJson(bean.getResult(),
								UserInfoBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_USER_INFO_DONE,
										ucInfoBbean));
					} catch (Exception e) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_USER_INFO_ERROR, bean
										.getState()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_USER_INFO_ERROR,
									"请求用户信息出错"));
				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_USER_INFO_DONE:
			ucInfoBbean = (UserInfoBean) msg.getData();
			InitUCView();
			break;

		case OpCodes.GET_USER_INFO_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;

		case OpCodes.PURSE_CHARGE_DONE:
		case OpCodes.UPDATE_GIFT_ORDER_DONE:
		case OpCodes.CHARGE_MADUN_DONE:
		case OpCodes.PAY_ORDER_DONE:
			requestUserInfo();
			break;

		default:
			break;
		}
	}

	private void InitUCView() {
		tvMoney.setText(ucInfoBbean.getMoney());
		tvMadun.setText(ucInfoBbean.getCredits());
		tvLevel.setText(ucInfoBbean.getLevelName());
		tvJifen.setText("我的积分：" + ucInfoBbean.getJifen());
		ImageLoader.getInstance().displayImage(ucInfoBbean.getPhotoUrl(),
				ivUserHead, options);
		Log.e("usedhead", "usedhead  "+ucInfoBbean.getPhotoUrl());
		tvUserNickName.setText(ucInfoBbean.getNickName());
	}

	class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.panel_my_addr:
				Bundle b = new Bundle();
				b.putBoolean("canChangeCommunity", true);
				((MainActivity) mContext).startNewActivity(
						AddressManageActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, b);
				break;
			case R.id.rl_panel_user_info:
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", ucInfoBbean);
				((MainActivity) mContext).startNewActivity(
						UserInfoActivity.class, R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, bundle);

				break;
			case R.id.rl_contract__us:
				final Dialog dialogContract = new Dialog(mContext, R.style.dialog);
				// 性别选择的dialog，以及其上的控件
				View dialgoview = ((MainActivity) mContext).getLayoutInflater()
						.inflate(R.layout.dialog_contractus, null);
				dialgoview.findViewById(R.id.ll_qq).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dialogContract.dismiss();
								if (!joinQQGroup("jVvA4PqpY9K9ueQZAJLo-Kysr2e175qw")) {
									Toast.makeText(mContext, "请安装手机QQ客户端",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
				dialgoview.findViewById(R.id.ll_phone1).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dialogContract.dismiss();
								Intent intent = new Intent(Intent.ACTION_CALL, Uri
										.parse("tel:" + "073184470808"));
								startActivity(intent);
							}
						});
				
				dialgoview.findViewById(R.id.ll_phone2).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dialogContract.dismiss();
								Intent intent = new Intent(Intent.ACTION_CALL, Uri
										.parse("tel:" + "073184460808"));
								startActivity(intent);
							}
						});
				
				// 设置dialog没有title
				
				dialogContract.setContentView(dialgoview, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				Window window = dialogContract.getWindow();
				// 可以在此设置显示动画
				WindowManager.LayoutParams wl = window.getAttributes();
				wl.x = 0;
				wl.y = ((MainActivity) mContext).getWindowManager()
						.getDefaultDisplay().getHeight();
				// 以下这两句是为了保证按钮可以水平满屏
				wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
				wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
				// 设置显示位置
				dialogContract.onWindowAttributesChanged(wl);
				dialogContract.show();
				break;

			case R.id.rl_panel_money:
				((MainActivity) mContext).startNewActivity(
						MyPurseActivity.class, R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);

				break;
			case R.id.rl_panel_setting:

				((MainActivity) mContext).startNewActivity(
						SettingActivity.class, R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);
				break;
			default:
				Toast.makeText(mContext, "暂无此功能", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}

	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.iv_user_head) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.iv_user_head) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.iv_user_head) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.displayer(new RoundedBitmapDisplayer(100)) // 设置成圆角图片
			.build();

	/****************
	 * 
	 * 发起添加群流程。群号：菜来了会员群(535188422) 的 key 为： jVvA4PqpY9K9ueQZAJLo-Kysr2e175qw 调用
	 * joinQQGroup(jVvA4PqpY9K9ueQZAJLo-Kysr2e175qw) 即可发起手Q客户端申请加群
	 * 菜来了会员群(535188422)
	 * 
	 * @param key
	 *            由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public boolean joinQQGroup(String key) {
		Intent intent = new Intent();
		intent.setData(Uri
				.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D"
						+ key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
		// //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			startActivity(intent);
			return true;
		} catch (Exception e) {
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}

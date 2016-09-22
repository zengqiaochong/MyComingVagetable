package com.caomei.comingvagetable.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class WelcomeActivity extends BaseActivity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		EventBus.getDefault().register(this);
		mContext = this;
		AppData.mContext = this;
		updateAppData();
	}

	private void updateAppData() {
		if (!NetUtil.getNetworkState(mContext)) {
			Toast.makeText(mContext, "无网络", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.e("error", "start err0r1");
					startNewActivity(MainActivity.class,
							R.anim.activity_slide_right_in,
							R.anim.activity_slide_left_out, true, null);
				}
			}, 1500);

		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AccessNetResultBean bean;
					String url = String
							.format(CommonAPI.URL_VEGE_CATEGORY, 0, 1000, 0,
									ShareUtil.getInstance(mContext).getUserId());
					Log.e("url", "main page data url " + url);
					bean = NetUtil.getInstance(mContext).getDataFromNetByGet(
							url);
					if (bean.getState() == AccessNetState.Success) {
						Log.e("data", "菜品分类数据： " + bean.getResult());
						if (TextUtils.isEmpty(bean.getResult())) {
							EventBus.getDefault().post(
									new EventMsg(
											OpCodes.GET_CATEGORY_DATA_NULL,
											null));
							return;
						}
						ShareUtil.getInstance(mContext).setVegeCategoryJson(
								bean.getResult());
						ShareUtil.getInstance(mContext).setLastUpdateTime(
								System.currentTimeMillis());
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_CATEGORY_DATA_DONE,
										null));

					} else {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_CATEGORY_DATA_ERROR,
										bean.getResult()));
					}
				}
			}).start();
		}
	}

	public void onEventMainThread(EventMsg msg) {

		switch (msg.getFlag()) {

		case OpCodes.GET_CATEGORY_DATA_NULL:
			if (ShareUtil.getInstance(mContext).getShowGuard()) {
				Toast.makeText(mContext, "初始化数据出错，重新启动试试吧", Toast.LENGTH_SHORT)
						.show();
				break;
			} else {
				Log.e("error", "start err0r2");
				startNewActivity(MainActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, true, null);
			}
			break;
		case OpCodes.GET_CATEGORY_DATA_DONE:
			if (ShareUtil.getInstance(mContext).getShowGuard()) {
				startNewActivity(GuardActivity.class, R.anim.activity_fade_in,
						R.anim.activity_fade_out, true, null);
			} else {
				Log.e("error", "start err0r3");
				startNewActivity(MainActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, true, null);
			}
			break;

		case OpCodes.GET_CATEGORY_DATA_ERROR:
			Log.e("error", "start err0r4");
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			startNewActivity(MainActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, true, null);
			break;
		}
	}

	@Override
	public void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}

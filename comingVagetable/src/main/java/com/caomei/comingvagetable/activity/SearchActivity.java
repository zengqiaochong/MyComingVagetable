package com.caomei.comingvagetable.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.adapter.SearchResultAdapter;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.vegedata.VegeAllBean;
import com.caomei.comingvagetable.bean.vegedata.VegeAllBeanData;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class SearchActivity extends BaseActivity {

	private ListView lvSearchResult;
	private ImageView ivBack;
	private Button btSearch;
	private CommonListener mListner;
	private ArrayList<VegeAllBeanData> dataSet;
	private SearchResultAdapter mAdapter;
	private EditText etKey;
	private TextView tvQingCai;
	private TextView tvZhuRou;
	private TextView tvYu;
	private TextView tvNullRes;
	private LinearLayout panelSearching;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		EventBus.getDefault().register(this);
		setView();
		setData();
	}

	private void setView() {
		lvSearchResult = (ListView) findViewById(R.id.lv_search_result);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		btSearch = (Button) findViewById(R.id.bt_search);
		etKey = (EditText) findViewById(R.id.et_search_key);
		tvQingCai = (TextView) findViewById(R.id.tv_qc);
		tvZhuRou = (TextView) findViewById(R.id.tv_zr);
		tvYu = (TextView) findViewById(R.id.tv_y);

		tvNullRes = (TextView) findViewById(R.id.tv_null_result);
		tvNullRes.setVisibility(View.GONE);
		panelSearching = (LinearLayout) findViewById(R.id.ll_panel_searching);
		panelSearching.setVisibility(View.GONE);
	}

	private void setData() {
		mListner = new CommonListener();
		dataSet = new ArrayList<VegeAllBeanData>();
		mAdapter = new SearchResultAdapter(mContext, dataSet);
		lvSearchResult.setAdapter(mAdapter);

		ivBack.setOnClickListener(mListner);
		btSearch.setOnClickListener(mListner);

		tvQingCai.setOnClickListener(mListner);
		tvZhuRou.setOnClickListener(mListner);
		tvYu.setOnClickListener(mListner);
		lvSearchResult.setOnItemClickListener(mListner);
	}

	class CommonListener implements OnClickListener, OnItemClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.bt_search:
				tvNullRes.setVisibility(View.GONE);
				panelSearching.setVisibility(View.VISIBLE);
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(SearchActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				requestSearch();
				break;
			case R.id.tv_qc:
				etKey.setText(((TextView) v).getText());
				break;
			case R.id.tv_zr:
				etKey.setText(((TextView) v).getText());
				break;
			case R.id.tv_y:
				etKey.setText(((TextView) v).getText());
				break;
			default:
				break;
			}
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle mBundle = new Bundle();
			mBundle.putString("id", dataSet.get(arg2).getVegeId());
			mBundle.putString("pc", dataSet.get(arg2).getProChannel());
			mBundle.putString("pw", dataSet.get(arg2).getProcessWay());
			mBundle.putFloat("volume", dataSet.get(arg2).getPerUnitVolume());
			startNewActivity(ProductDetailActivity.class,
					R.anim.activity_slide_right_in,
					R.anim.activity_slide_left_out, true, mBundle);
		}

	}

	public void requestSearch() {
		String key = etKey.getEditableText().toString();
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_VEGE_LIST_BY_KEYWORDS,
						ShareUtil.getInstance(mContext).getHomeCommunityID(),
						"0", "1000", "test", key,
						ShareUtil.getInstance(mContext).getUserId());
		Log.e("url", "获取搜索结果的接口 ：" + url);
		if (TextUtils.isEmpty(key)) {
			ToastUtil.Show(mContext, "请输入关键字");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						VegeAllBean vBean = new Gson().fromJson(
								bean.getResult(), VegeAllBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_SEARCH_RESULT_OK,
										vBean));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_SEARCH_RESULT_OK, bean
										.getResult()));
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_SEARCH_RESULT_ERROR,
										"数据出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_SEARCH_RESULT_ERROR, bean
									.getResult()));

				}
			}
		}).start();
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.GET_SEARCH_RESULT_OK:
			dataSet.clear();
			dataSet.addAll(((VegeAllBean) msg.getData()).getData());
			mAdapter.notifyDataSetChanged();

			panelSearching.setVisibility(View.GONE);
			if (dataSet == null || dataSet.size() == 0) {
				tvNullRes.setVisibility(View.VISIBLE);
			}
			break;
		case OpCodes.GET_SEARCH_RESULT_ERROR:
			ToastUtil.Show(mContext, msg.getData().toString());
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_slide_right_in,
				R.anim.activity_slide_left_out);
	}

}

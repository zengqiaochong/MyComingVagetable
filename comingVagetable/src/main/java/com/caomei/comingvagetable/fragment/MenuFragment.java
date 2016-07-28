package com.caomei.comingvagetable.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.adapter.MenuAdapter;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.vegedata.VegeCategoryBean;
import com.caomei.comingvagetable.bean.vegedata.VegeCategoryData;
import com.caomei.comingvagetable.util.ShareUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class MenuFragment extends Fragment {

	private Context mContext;
	private ListView mListView;
	private MenuAdapter mAdapter;

	private View mView;
	private ArrayList<VegeCategoryData> listData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		InitData();
	}

	private void InitData() {
		try {
			listData = new ArrayList<VegeCategoryData>();
			VegeCategoryBean bean = new VegeCategoryBean();

			bean = new Gson().fromJson(ShareUtil.getInstance(mContext)
					.getVegeCategoryJson(), VegeCategoryBean.class);

			VegeCategoryData data = new VegeCategoryData();
			listData.add(data);
			listData.addAll(bean.getData());
		} catch (Exception ex) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_menu, container, false);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);
		mListView = (ListView) mView.findViewById(R.id.lv_menu_vege_category);
		mAdapter = new MenuAdapter(mContext, listData);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				EventBus.getDefault().post(
						new EventMsg(OpCodes.MENU_CATEGORY_CLICK, listData.get(
								arg2).getVegetypeid()));
			}
		});
	}
}

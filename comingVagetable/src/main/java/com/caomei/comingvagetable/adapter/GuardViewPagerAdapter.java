package com.caomei.comingvagetable.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class GuardViewPagerAdapter extends PagerAdapter{

    private List<View> mListView;
    private Context mContext;
    private List<String> mTitleData;

    public GuardViewPagerAdapter(Context mContext,List<View> mList){
        this.mContext=mContext;
        this.mListView=mList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListView.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(mListView.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        container.addView(mListView.get(position), 0);//添加页卡
        return mListView.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleData.get(position);//直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。
    }
}
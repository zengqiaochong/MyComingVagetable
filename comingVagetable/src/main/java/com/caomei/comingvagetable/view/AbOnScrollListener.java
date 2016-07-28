package com.caomei.comingvagetable.view;


/**
 * 名称：AbOnScrollListener.java
 * 描述：滚动监听器
 */
public interface AbOnScrollListener {

	/**
	 * 滚动.
	 * @param arg1 返回参数
	 */
	void onScroll(int arg1);

	/**
	 * 滚动停止.
	 */
	void onScrollStoped();

	/**
	 * 滚到了最左边.
	 */
	void onScrollToLeft();

	/**
	 * 滚到了最右边.
	 */
	void onScrollToRight();

}

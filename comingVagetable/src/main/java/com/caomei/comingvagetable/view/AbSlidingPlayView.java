
package com.caomei.comingvagetable.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.caomei.comingvagetable.R;


/**
 *
 * @author tans
 * @date 2014-09-26
 * @version
 */
@SuppressLint("HandlerLeak")
public class AbSlidingPlayView extends LinearLayout {

	/** The tag. */
	private  String TAG = "AbSlidingPlayView";

	/** The Constant D. */
	@SuppressWarnings("unused")
	private  final boolean D = AbAppData.DEBUG;

	/** The context. */
	private Context context;

	/** The m view pager. */
	private AbInnerViewPager mViewPager;

	/** The page line layout. */
	private LinearLayout pageLineLayout;

	/** The layout params pageLine. */
	public LinearLayout.LayoutParams pageLineLayoutParams = null;

	/** The i. */
	private int count, position;

	/** The hide image. */
	private Bitmap displayImage, hideImage;

	/** The m on item click listener. */
	private AbOnItemClickListener mOnItemClickListener;

	/** The m ab change listener. */
	private AbOnChangeListener mAbChangeListener;

	/** The m ab scrolled listener. */
	private AbOnScrollListener mAbScrolledListener;

	/** The m ab touched listener. */
	private AbOnTouchListener mAbOnTouchListener;

	/** The layout params ff. */
	public LinearLayout.LayoutParams layoutParamsFF = null;

	/** The layout params fw. */
	public LinearLayout.LayoutParams layoutParamsFW = null;

	/** The layout params wf. */
	public LinearLayout.LayoutParams layoutParamsWF = null;

	/** The m list views. */
	private ArrayList<View> mListViews = null;

	/** The m ab view pager adapter. */
	private AbViewPagerAdapter mAbViewPagerAdapter = null;

	/** 锟斤拷锟斤拷锟侥点父View */
	private LinearLayout mPageLineLayoutParent = null;

	/** The page line horizontal gravity. */
	private int pageLineHorizontalGravity = Gravity.RIGHT;

	/** 锟斤拷锟脚的凤拷锟斤拷 */
	private int playingDirection = 0;

	/** 锟斤拷锟脚的匡拷锟斤拷 */
	private boolean play = false;
	/** 锟斤拷锟脚的硷拷锟绞憋拷锟� */
	private int sleepTime = 5000;
	/** 锟斤拷锟脚凤拷锟斤拷式锟斤拷1顺锟津播放猴拷0锟斤拷锟截诧拷锟脚ｏ拷 */
	private int playType = 1;

	/**
	 * 锟斤拷锟斤拷一锟斤拷AbSlidingPlayView
	 *
	 * @param context
	 */
	public AbSlidingPlayView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 锟斤拷xml锟斤拷始锟斤拷锟斤拷AbSlidingPlayView
	 *
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public AbSlidingPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}


	/**
	 *
	 * 锟斤拷锟斤拷锟斤拷锟斤拷始锟斤拷锟斤拷锟絍iew
	 *
	 * @param context
	 * @throws
	 */
	public void initView(Context context) {
		this.context = context;
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		pageLineLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));

		RelativeLayout mRelativeLayout = new RelativeLayout(context);

		mViewPager = new AbInnerViewPager(context);
		// 锟街讹拷锟斤拷锟斤拷锟斤拷ViewPager,锟斤拷锟斤拷锟絝ragment锟斤拷锟斤拷锟斤拷锟絪etId()锟斤拷锟斤拷锟斤拷锟斤拷一锟斤拷id
		int id = 1985;
		mViewPager.setId(id);
		// 锟斤拷锟斤拷锟侥碉拷
		mPageLineLayoutParent = new LinearLayout(context);
		mPageLineLayoutParent.setPadding(0, 5, 0, 5);
		pageLineLayout = new LinearLayout(context);
		pageLineLayout.setPadding(15, 1, 15, 1);
		pageLineLayout.setVisibility(View.INVISIBLE);
		mPageLineLayoutParent.addView(pageLineLayout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		mRelativeLayout.addView(mViewPager, lp1);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mRelativeLayout.addView(mPageLineLayoutParent, lp2);
		addView(mRelativeLayout, layoutParamsFW);

		//锟矫碉拷锟斤拷锟斤拷锟斤拷锟酵计拷锟皆达拷募锟�
		displayImage =getBitmapFormSrc("play_display.png");
		hideImage =getBitmapFormSrc("play_hide.png");

		mListViews = new ArrayList<View>();
		mAbViewPagerAdapter = new AbViewPagerAdapter(context, mListViews);
		mViewPager.setAdapter(mAbViewPagerAdapter);
		mViewPager.setFadingEdgeLength(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				makesurePosition();
				onPageSelectedCallBack(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				onPageScrolledCallBack(position);
			}

		});

	}

	/**
	 * 锟矫碉拷锟斤拷取图片
	 * @param name 图片锟斤拷锟斤拷锟斤拷
	 * @return
	 */
	private Bitmap getBitmapFormSrc(String name){
		Bitmap bitmap=null;

		try {
			InputStream is=getResources().getAssets().open(name);
			bitmap=BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			Log.d(TAG, "锟斤拷取图片锟届常锟斤拷"+e.getMessage());
		}
		return bitmap;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷.
	 */
	public void creatIndex() {
		// 锟斤拷示锟斤拷锟斤拷牡锟�
		pageLineLayout.removeAllViews();
		mPageLineLayoutParent.setHorizontalGravity(pageLineHorizontalGravity);
		pageLineLayout.setGravity(Gravity.CENTER);
		pageLineLayout.setVisibility(View.VISIBLE);
		count = mListViews.size();
		for (int j = 0; j < count; j++) {
			ImageView imageView = new ImageView(context);
			pageLineLayoutParams.setMargins(5, 5, 5, 5);
			imageView.setLayoutParams(pageLineLayoutParams);
			if (j == 0) {
				imageView.setImageBitmap(displayImage);
			} else {
				imageView.setImageBitmap(hideImage);
			}
			pageLineLayout.addView(imageView, j);
		}
	}

	/**
	 * 锟斤拷位锟斤拷锟轿伙拷锟�.
	 */
	public void makesurePosition() {
		position = mViewPager.getCurrentItem();
		for (int j = 0; j < count; j++) {
			if (position == j) {
				((ImageView) pageLineLayout.getChildAt(position)).setImageBitmap(displayImage);
			} else {
				((ImageView) pageLineLayout.getChildAt(j)).setImageBitmap(hideImage);
			}
		}
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷涌刹锟斤拷锟斤拷锟酵�.
	 *
	 * @param view
	 *            the view
	 */
	public void addView(View view) {
		mListViews.add(view);
		if (view instanceof AbsListView) {
		} else {
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onClick(position);
					}
				}
			});
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (mAbOnTouchListener != null) {
						mAbOnTouchListener.onTouch(event);
					}
					return false;
				}
			});
		}

		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷涌刹锟斤拷锟斤拷锟酵硷拷斜锟�.
	 *
	 * @param views
	 *            the views
	 */
	public void addViews(List<View> views) {
		mListViews.addAll(views);
		for (View view : views) {
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						mOnItemClickListener.onClick(position);
					}
				}
			});

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (mAbOnTouchListener != null) {
						mAbOnTouchListener.onTouch(event);
					}
					return false;
				}
			});
		}
		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷删锟斤拷锟缴诧拷锟斤拷锟斤拷图.
	 *
	 */
	@Override
	public void removeAllViews() {
		mListViews.clear();
		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟斤拷锟叫伙拷锟铰硷拷.
	 *
	 * @param position
	 *            the position
	 */
	private void onPageScrolledCallBack(int position) {
		if (mAbScrolledListener != null) {
			mAbScrolledListener.onScroll(position);
		}

	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟斤拷锟叫伙拷锟铰硷拷.
	 *
	 * @param position
	 *            the position
	 */
	private void onPageSelectedCallBack(int position) {
		if (mAbChangeListener != null) {
			mAbChangeListener.onChange(position);
		}

	}

	/** 锟斤拷锟斤拷锟街伙拷锟斤拷 handler. */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				//锟斤拷示锟斤拷锟脚碉拷页锟斤拷
				ShowPlay();
				if (play) {
					handler.postDelayed(runnable, sleepTime);
				}
			}
		}

	};

	/** 锟斤拷锟斤拷锟街诧拷锟斤拷锟竭筹拷. */
	private Runnable runnable = new Runnable() {
		public void run() {
			if (mViewPager != null) {
				handler.sendEmptyMessage(0);
			}
		}
	};

	/**
	 * 锟斤拷锟斤拷锟斤拷锟皆讹拷锟街诧拷. sleepTime 锟斤拷锟脚的硷拷锟绞憋拷锟�
	 */
	public void startPlay() {
		if (handler != null) {
			play = true;
			handler.postDelayed(runnable, sleepTime);
		}
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟皆讹拷锟街诧拷.
	 */
	public void stopPlay() {
		if (handler != null) {
			play = false;
			handler.removeCallbacks(runnable);
		}
	}

	/**
	 * 锟斤拷锟矫碉拷锟斤拷录锟斤拷锟斤拷锟�.
	 *
	 * @param onItemClickListener
	 *            the new on item click listener
	 */
	public void setOnItemClickListener(AbOnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟斤拷锟叫伙拷锟侥硷拷锟斤拷锟斤拷.
	 *
	 * @param abChangeListener
	 *            the new on page change listener
	 */
	public void setOnPageChangeListener(AbOnChangeListener abChangeListener) {
		mAbChangeListener = abChangeListener;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟芥滑锟斤拷锟侥硷拷锟斤拷锟斤拷.
	 *
	 * @param abScrolledListener
	 *            the new on page scrolled listener
	 */
	public void setOnPageScrolledListener(AbOnScrollListener abScrolledListener) {
		mAbScrolledListener = abScrolledListener;
	}

	/**
	 *
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟斤拷Touch锟侥硷拷锟斤拷锟斤拷.
	 *
	 * @param abOnTouchListener
	 * @throws
	 */
	public void setOnTouchListener(AbOnTouchListener abOnTouchListener) {
		mAbOnTouchListener = abOnTouchListener;
	}

	/**
	 * Sets the page line image.
	 *
	 * @param displayImage
	 *            the display image
	 * @param hideImage
	 *            the hide image
	 */
	public void setPageLineImage(Bitmap displayImage, Bitmap hideImage) {
		this.displayImage = displayImage;
		this.hideImage = hideImage;
		creatIndex();

	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟絍iewPager锟斤拷.
	 *
	 * @return the view pager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷取锟斤拷前锟斤拷View锟斤拷锟斤拷锟斤拷.
	 *
	 * @return the count
	 */
	public int getCount() {
		return mListViews.size();
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟斤拷示锟斤拷锟斤拷位锟斤拷,锟斤拷AddView前锟斤拷锟斤拷.
	 *
	 * @param horizontalGravity
	 *            the new page line horizontal gravity
	 */
	public void setPageLineHorizontalGravity(int horizontalGravity) {
		pageLineHorizontalGravity = horizontalGravity;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷ScrollView锟斤拷要锟斤拷锟斤拷.
	 *
	 * @param parentScrollView
	 *            the new parent scroll view
	 */
	public void setParentScrollView(ScrollView parentScrollView) {
		this.mViewPager.setParentScrollView(parentScrollView);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷ListView锟斤拷要锟斤拷锟斤拷.
	 *
	 * @param parentListView
	 *            the new parent list view
	 */
	public void setParentListView(ListView parentListView) {
		this.mViewPager.setParentListView(parentListView);
	}

	/**
	 *
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟矫碉拷锟斤拷锟斤拷谋锟斤拷锟�
	 *
	 * @param resid
	 * @throws
	 */
	public void setPageLineLayoutBackground(int resid) {
		pageLineLayout.setBackgroundResource(resid);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟矫诧拷锟脚的硷拷锟绞憋拷锟�
	 * @param sleepTime  锟斤拷锟绞憋拷涞ノ伙拷呛锟斤拷锟�
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	/**
	 *  锟斤拷锟斤拷锟斤拷锟斤拷锟矫诧拷锟脚凤拷锟斤拷姆锟绞斤拷锟�1顺锟津播放猴拷0锟斤拷锟截诧拷锟脚ｏ拷 playType
	 * @param playType    为0锟斤拷示锟斤拷锟截诧拷锟脚ｏ拷为1锟斤拷示顺锟津播凤拷
	 */
	public void setPlayType(int playType) {
		this.playType = playType;
	}


	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷示锟斤拷锟芥（1顺锟津播放猴拷0锟斤拷锟截诧拷锟脚ｏ拷 playType 为0锟斤拷示锟斤拷锟截诧拷锟脚ｏ拷为1锟斤拷示顺锟津播凤拷
	 */
	public void ShowPlay() {
		//锟斤拷页锟斤拷
		int count = mListViews.size();
		// 锟斤拷前锟斤拷示锟斤拷页锟斤拷
		int i = mViewPager.getCurrentItem();
		switch (playType) {
			case 0:
				// 锟斤拷锟截诧拷锟斤拷
				if (playingDirection == 0) {
					if (i == count - 1) {
						playingDirection = -1;
						i--;
					} else {
						i++;
					}
				} else {
					if (i == 0) {
						playingDirection = 0;
						i++;
					} else {
						i--;
					}
				}
				break;
			case 1:
				// 顺锟津播凤拷
				if (i == count - 1) {
					i = 0;
				} else {
					i++;
				}

				break;

			default:
				break;
		}
		// 锟斤拷锟斤拷锟斤拷示锟节硷拷页
		mViewPager.setCurrentItem(i, true);
	}

}

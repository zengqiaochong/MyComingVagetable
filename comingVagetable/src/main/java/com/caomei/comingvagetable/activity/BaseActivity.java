package com.caomei.comingvagetable.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.WindowManager;

import com.caomei.comingvagetable.R;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity {

	protected ProgressDialog pDialog;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		mContext = this;

		pDialog = new ProgressDialog(this);
		pDialog.setIcon(getResources().getDrawable(R.drawable.icon));
		pDialog.setMessage(getString(R.string.loading));
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setIndeterminate(true);
		pDialog.setCancelable(true);
		SetImageLoaderConfig();
		getSupportFragmentManager();
	}

	public FragmentManager getSupportFragment(){
		return getSupportFragmentManager();
	}

	private void SetImageLoaderConfig() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(),
				"ComingVegetable/cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 2)

				// .memoryCacheExtraOptions(480, 800)
				// .memoryCache(new LruMemoryCache(10 * 1024 * 1024))//new
				// WeakMemoryCache()
				.memoryCacheSizePercentage(20)
				// .denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())

				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new LimitedAgeDiskCache(cacheDir, 60 * 60 * 24))//
				// UnlimitedDiscCache(cacheDir)
				// .discCacheExtraOptions(800, 800, Bitmap.CompressFormat.JPEG,
				// 75, null)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);//
	}

	/**
	 * 
	 * @param target
	 * @param enterAnim
	 * @param exitAnim
	 * @param isFinish
	 * @param mBundle
	 */
	public final void startNewActivity(Class<? extends Activity> target,
			int enterAnim, int exitAnim, boolean isFinish, Bundle mBundle) {
		Intent mIntent = new Intent(this, target);
		if (mBundle != null) {
			mIntent.putExtras(mBundle);
		}
		startActivity(mIntent);
		overridePendingTransition(enterAnim, exitAnim);
		if (isFinish) {
			finish();
		}
	}
	
	/**
     *	打开一个可以回调数据的Activity，并传入必要的参数
     * @param target
     * @param enterAnim
     * @param exitAnim
     * @param requestCode
     * @param mBundle
     */
    protected final void startNewActivityForResult( Class<? extends Activity> target, int enterAnim, int exitAnim, int requestCode, Bundle mBundle ){
        Intent mIntent = new Intent(this, target);
        if( mBundle != null ){
            mIntent.putExtras(mBundle);
        }
        startActivityForResult(mIntent, requestCode);
        overridePendingTransition(enterAnim, exitAnim);
    }
    
	/**
	 *开启一个普通的后台服务
	 * @param target
	 * @param mBundle
	 */
	public final void startService( Class<? extends Service> target, Bundle mBundle ){
		Intent mIntent = new Intent(this, target);
		if( mBundle != null ){
			mIntent.putExtras(mBundle);
		}
		startService(mIntent);
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() { 
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
	}
	
	

}

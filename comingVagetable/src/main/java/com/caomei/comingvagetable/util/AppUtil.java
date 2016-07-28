package com.caomei.comingvagetable.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

public class AppUtil {
	/**
	 * 获取app的版本号 版本名 渠道等数据
	 * 
	 * @param mContext
	 * @param resType
	 *            1 返回版本号 2 返回版本名
	 * @return
	 */
	public static String getAppVersionCode_VersionName(Context mContext,
			int resType) {
		PackageManager packageManager = mContext.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(
					mContext.getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		if (resType == 1) {
			return String.valueOf(packageInfo.versionCode); // packageInfo.versionCode;
		} else if (resType == 2) {
			return String.valueOf(packageInfo.versionName);
		}
		return null;
	}

	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
     * 计算像素大小，从dp转换至pix
     * 
     * @param context
     * @param val
     * @return
     */
    public static float dpToPixel(Context context, float val) {
        float density = context.getResources().getDisplayMetrics().density;
        return val * density;
    }
	
	/**
     * 计算dp大小，从pix转换至dp
     * 
     * @param context
     * @param val
     * @return
     */
    public static float pixelToDP(Context context, float val) {
        float density = context.getResources().getDisplayMetrics().density;
        return val/density;
    }

}

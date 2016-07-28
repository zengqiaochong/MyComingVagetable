package com.caomei.comingvagetable.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caomei.comingvagetable.R;

public class MethodUtil {

	public static String getFormatJson(String src) {
		String res = src.replaceAll("\\[", "").replaceAll("]", "");
		return res;
	}

	public static String getFormatJson2(String src) {
		String res = src.replaceFirst("\\[", "").replaceFirst("]", "");
		return res;
	}

	public static ArrayList<String> getArrayList(String src, String split) {
		String[] temStr1 = src.split(split);
		ArrayList<String> res = new ArrayList<String>();
		for (int index = 0; index < temStr1.length; index++) {
			res.add(temStr1[index]);
		}
		return res;
	}

	public static void SetPanelPrice(float price, LinearLayout panel) {
		TextView tvBig = (TextView) (panel
				.findViewById(R.id.tv_total_money_big));
		TextView tvSmall = (TextView) (panel
				.findViewById(R.id.tv_total_money_small));
		DecimalFormat df = new DecimalFormat("#0.00");
		tvBig.setText(String.valueOf((int) price));
		String small = String.valueOf(df.format(price)).split("\\.")[1];
		tvSmall.setText("." + small);
		panel.invalidate();
	}

	public static void SetPanelPrice(float price, String unit, LinearLayout panel, int big_id, int small_id, int unit_id) {
		TextView tvBig = (TextView) panel.findViewById(big_id);
		TextView tvSmall = (TextView) panel.findViewById(small_id);
		TextView tvUnit = (TextView) panel.findViewById(unit_id);
		DecimalFormat df = new DecimalFormat("#0.00");
		tvBig.setText(String.valueOf((int) price));
		String small = String.valueOf(df.format(price)).split("\\.")[1];
		tvSmall.setText("." + small);
		tvUnit.setText("元/" + unit);
		panel.invalidate();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return mobiles.length() == 11;

		// return m.matches();
	}

	/**
	 * 返回定位的市、区县信息
	 * 
	 * @param addr
	 *            输入定位得到的详细信息
	 * @param type
	 *            想要得到的信息类型： 0 返回市 1 返回 区县 2 返回小区信息 3返回省
	 * @return
	 */
	public static String getCityName(String addr, int type) {
		String res = "暂无";
		boolean directCity = false;
		if (TextUtils.isEmpty(addr)) {
			return res;
		}
		if (addr.indexOf("省") > 0) {
			res = addr.substring(addr.indexOf("省") + 1, addr.length());
		} else {
			directCity = true;
			res = addr;
		}
		if (directCity) {
			res = addr.substring(addr.indexOf("市") + 1, addr.length());
			if (type == 3) {
				return addr.substring(0, addr.indexOf("市") + 1);
			}
		} else {
			if (type == 3) {
				return addr.substring(0, addr.indexOf("省") + 1);
			}
		}
		if (type == 0) {
			if (addr.indexOf("市") > 0) {
				res = res.substring(0, res.indexOf("市") + 1);
				Log.e("loc", "定位的市结果为： " + res);
				return res;
			}
		} else if (type == 1) {
			if (res.indexOf("区") > 0) {
				res = res.substring(res.indexOf("市") + 1, res.indexOf("区") + 1);
			} else if (res.indexOf("县") > 0) {
				res = res.substring(res.indexOf("市") + 1, res.indexOf("县") + 1);
			}
			Log.e("loc", "定位的区县的结果为： " + res);
			return res;
		} else if (type == 2) {
			if (res.indexOf("区") > 0) {
				res = res.substring(res.indexOf("区") + 1, res.length());
			} else if (res.indexOf("县") > 0) {
				res = res.substring(res.indexOf("县") + 1, res.length());
			}
			Log.e("loc", "定位的小区结果为： " + res);
			return res;
		}

		return res;
	}

	public void getHomeCommunityAddrId(String id) {

	}

	public static String getFileNameFromUrl(String url) {
		String[] tem = url.split("=");
		return tem[tem.length - 1];
	}

	public static String getTimeString(int index) {

		Date date = new Date();
		Date as;
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		 
		if (index == 0) {
			return "";
		} else if (index == 1) {
			as = new Date(date.getTime() - 0 * 24 * 60 * 60 * 1000);
			String time = matter1.format(as);
			System.out.println(time);
			return time;
		} else if (index == 2) {
			as = new Date(date.getTime() - 2 * 24 * 60 * 60 * 1000);
			String time = matter1.format(as);
			System.out.println(time);
			return time;
		} else if (index == 3) {
			as = new Date(date.getTime() - 6 * 24 * 60 * 60 * 1000);
			String time = matter1.format(as);
			System.out.println(time);
			return time;
		}else if (index == 4) {
			as = new Date(date.getTime() - 19 * 24 * 60 * 60 * 1000);
			String time = matter1.format(as);
			System.out.println(time);
			return time;
		}else{
			return "";
		}
	}
}

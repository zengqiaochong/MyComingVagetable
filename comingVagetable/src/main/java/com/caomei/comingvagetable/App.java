package com.caomei.comingvagetable;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}
}

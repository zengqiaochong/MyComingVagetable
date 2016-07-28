package com.caomei.comingvagetable.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.MethodUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/4/26.
 */
public class DownloadApkService extends Service {
	public String dirName = Environment.getExternalStorageDirectory()
			+ "/VegeDownload/";
	public String fileName;
	private String newFilename;
	private String adTitle;
	private boolean isDownloaded;
	private boolean downloading;
//	private ApkDownloadNotification apkNotify;

	@Override
	public void onCreate() {
		EventBus.getDefault().register(this);
		Log.e("data","onCreate ");
//		apkNotify = new ApkDownloadNotification(this, 1);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("data","onStartCommand ");
		 
		if(intent==null){
			return 0;
		}
		final String apkUrl = intent.getStringExtra("url");
		 
		
		adTitle = intent.getStringExtra("app_title");
//		apkNotify.showCustomProgressNotify(adTitle);
		if (downloading) {
			Toast.makeText(this, "下载任务正在进行，请稍后", Toast.LENGTH_SHORT).show();
			return flags;
		} else {
			downloading = true;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File file_dir = new File(dirName);
					if (!file_dir.exists()) {
						file_dir.mkdir();
					}
					fileName = MethodUtil.getFileNameFromUrl(apkUrl);
					newFilename = dirName + fileName;
					File file_name = new File(newFilename);
					if (file_name.exists()) {
						if (isDownloaded == true) {
							Log.e("data","1 ");
							EventBus.getDefault().post(
									new EventMsg(OpCodes.APK_DOWNLOAD_OK,
											newFilename));
						} else {
							Log.e("data","2 ");
							DownloadApk(apkUrl, adTitle);
						}
					} else {
						Log.e("data","3 ");
						DownloadApk(apkUrl, adTitle);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
		return flags;
	}

	public void DownloadApk(String apkUrl, String appTitle) {
		try {
			URL url = new URL(apkUrl);
			URLConnection con = url.openConnection();
			con.setRequestProperty("Accept-Encoding", "identity"); 
			InputStream is = con.getInputStream();
			int contentLength = con.getContentLength();
			Log.e("data", "length "+contentLength);
			byte[] bs = new byte[1024];
			int len;
			OutputStream os = new FileOutputStream(newFilename);
			int curLen = 0;
			int progress = 0;
			int tem = progress;
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				curLen += len;
				progress = (int) ((double) curLen / (double) contentLength * 100);// 完成的百分比
				if (progress > tem) {
					tem = progress;
					Bundle bundle = new Bundle();
					bundle.putString("name", appTitle);
					bundle.putInt("progress", progress);
					bundle.putString("file", newFilename);
					EventBus.getDefault()
							.post(new EventMsg(OpCodes.UPDATE_PROGRESS_VALUE,
									bundle));
					Log.e("data", "post progress "+progress);
				}
			}
			os.close();
			is.close();
			isDownloaded = true;
			downloading = false;
			EventBus.getDefault().post(
					new EventMsg(OpCodes.APK_DOWNLOAD_OK, progress));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("download", "error " + e.getMessage());
			isDownloaded = false;
		}
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.UPDATE_PROGRESS_VALUE:

			Log.e("data", "get progress "+((Bundle)msg.getData()).getString("progress"));
			Bundle bundle = (Bundle) msg.getData();
//			apkNotify.setNotify(1, bundle.getInt("progress"), bundle);

			break;

		case OpCodes.APK_DOWNLOAD_ING:
			Toast.makeText(this, msg.getData().toString(), Toast.LENGTH_SHORT)
					.show();
			break;

		case OpCodes.APK_DOWNLOAD_OK:

			Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
			Intent install = new Intent();
			install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			install.setAction(android.content.Intent.ACTION_VIEW);
			install.setDataAndType(Uri.fromFile(new File(newFilename)),
					"application/vnd.android.package-archive");
			startActivity(install);
			break;
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}

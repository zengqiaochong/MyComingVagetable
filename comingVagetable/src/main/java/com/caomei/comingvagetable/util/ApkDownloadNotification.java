package com.caomei.comingvagetable.util;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import com.caomei.comingvagetable.R;

/**
 * Created by Administrator on 2016/4/26.
 */
@SuppressLint("InlinedApi")
public class ApkDownloadNotification {

    public NotificationManager mNotificationManager;
    public NotificationCompat.Builder mBuilder;
    private Context mContext;
    private int progress;
    private int notifyId;
    private RemoteViews mRemoteViews;

    public ApkDownloadNotification(Context mContext, int notifyId) {
        this.mContext = mContext;
        this.notifyId = notifyId;
        initNotify();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setContentIntent(getDefalutIntent(0))
                // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                // requires VIBRATE permission
                .setSmallIcon(R.drawable.icon);
    }

    /**
     * 设置下载进度
     */
    public void setNotify(int notifyId, int progress, Bundle mBundle){
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
        mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, "已完成：" + progress + "%");
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, "正在下载："+mBundle.get("name").toString());
        if (progress == 100) {
            mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, "下载完成点击安装");
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(android.content.Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(mBundle.getString("file").toString())),"application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, install, 0);
            mBuilder.setContentIntent(pendingIntent);
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * 显示自定义的带进度条通知栏
     */
    public void showCustomProgressNotify(String appName) {
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_download_progress);
        mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.drawable.icon);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, appName);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, "下载进度" + progress);
        if (progress >= 100) {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
        } else {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
        }
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(0))
                .setTicker("应用下载");
        Notification nitify = mBuilder.build();
        nitify.contentView = mRemoteViews;
        mNotificationManager.notify(notifyId, nitify);
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, new Intent(), flags);
        return pendingIntent;
    }
}

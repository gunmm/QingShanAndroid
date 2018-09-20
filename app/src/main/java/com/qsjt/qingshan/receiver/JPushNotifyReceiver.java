package com.qsjt.qingshan.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.qsjt.qingshan.ui.activity.MainActivity;
import com.qsjt.qingshan.R;
import com.qsjt.qingshan.application.MyApplication;
import com.qsjt.qingshan.constant.Constants;
import com.qsjt.qingshan.listener.OnJPushReceivedListener;
import com.qsjt.qingshan.model.response.NotifyMessage;
import com.qsjt.qingshan.utils.LogUtils;

import cn.jpush.android.api.JPushInterface;

public class JPushNotifyReceiver extends BroadcastReceiver {

    private static OnJPushReceivedListener onJPushReceivedListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (intent.getAction() == null) {
            return;
        }
        if (bundle == null) {
            return;
        }
        switch (intent.getAction()) {
            //用户注册SDK的intent
            case JPushInterface.ACTION_REGISTRATION_ID:
                break;
            //用户接收SDK消息的intent
            case JPushInterface.ACTION_MESSAGE_RECEIVED:
                processMessages(context, bundle);
                break;
            //用户接收SDK通知栏信息的intent
            case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                break;
            //用户打开自定义通知栏的intent
            case JPushInterface.ACTION_NOTIFICATION_OPENED:
                break;
            //用户点击了通知栏中自定义的按钮的intent
            case JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION:
                break;
            //接收网络变化连接/断开的intent
            case JPushInterface.ACTION_CONNECTION_CHANGE:
                break;
        }
    }

    /**
     * 处理消息
     */
    private void processMessages(Context context, Bundle bundle) {
        String extraMsg = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        LogUtils.i("JPush", "\nextraMsg: " + extraMsg + "\nmsgId: " + msgId + "\nmsg: " + msg);

        NotifyMessage notifyMessage = new Gson().fromJson(extraMsg, NotifyMessage.class);
        if (notifyMessage == null) {
            notifyMessage = new NotifyMessage();
        }
        notifyMessage.setMsgContent(msg);

        if (MyApplication.getMyApplication().isForeground()) {
            showMessageWindow(context, notifyMessage);
        } else {
            showNotification(context, msg);
        }
    }

    private void showMessageWindow(Context context, NotifyMessage notifyMessage) {
        if (onJPushReceivedListener != null) {
            onJPushReceivedListener.onJPReceived(notifyMessage);
        }
    }

    private void showNotification(Context context, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "tt");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(msg);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setColor(context.getResources().getColor(R.color.colorTheme));
        //横幅消息
//        builder.setTicker(msg);
//        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent[] intents = new Intent[]{intent1};
        PendingIntent pdIntent = PendingIntent.getActivities(context, 101,
                intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pdIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //8.0 通知新特性 Channel
            NotificationChannel channel = new NotificationChannel("tt",
                    context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
        if (manager != null) {
            manager.notify(Constants.NOTIFY_ID, notification);
        }
    }

    public static void setOnJPushReceivedListener(OnJPushReceivedListener onJPushReceivedListener) {
        JPushNotifyReceiver.onJPushReceivedListener = onJPushReceivedListener;
    }
}

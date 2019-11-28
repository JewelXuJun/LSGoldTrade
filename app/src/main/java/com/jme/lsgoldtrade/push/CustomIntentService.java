package com.jme.lsgoldtrade.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.receiver.NotificationReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class CustomIntentService extends GTIntentService {

    private static final String CHANNEL_ID = "TJSId";
    private static final String CHANNEL_NAME = "TJS";

    NotificationManager mManager;
    Context mContext;

    public CustomIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {

    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        mContext = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        byte[] payload = msg.getPayload();

        if (null != payload) {
            String data = new String(payload);

            try {
                JSONObject jsonObject = new JSONObject(data);

                showNotification(mContext, jsonObject);
            } catch (JSONException e) {
                showNotification(mContext, data);

                e.printStackTrace();
            }
        }
    }

    private void showNotification(Context context, JSONObject jsonObject) {
        int notificationId = (int) System.currentTimeMillis();
        Notification notification;

        String contentType = jsonObject.optString("contentType");

        if (!TextUtils.isEmpty(contentType) && contentType.equals("4")) {
            String id = jsonObject.optString("id");

            if (TextUtils.isEmpty(id))
                return;

            Intent messageIntent = new Intent(context, NotificationReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            messageIntent.putExtras(bundle);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(mManager);

                notification = new Notification.Builder(mContext)
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setStyle(new Notification.BigTextStyle().bigText("您提交的意见反馈有一条新回复，点击查看详情"))
                        .setContentTitle("意见回复")
                        .setContentText("您提交的意见反馈有一条新回复，点击查看详情")
                        .setContentIntent(pendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("您提交的意见反馈有一条新回复，点击查看详情"))
                        .setContentTitle("意见回复")
                        .setContentText("您提交的意见反馈有一条新回复，点击查看详情")
                        .setContentIntent(pendingIntent).build();
            }
        } else {
            String contractId = jsonObject.optString("contractId");
            String latestPrice = jsonObject.optString("latestPrice");

            if (TextUtils.isEmpty(contractId) || TextUtils.isEmpty(latestPrice))
                return;

            Intent warningIntent = new Intent(context, NotificationReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putString("contractId", contractId);
            warningIntent.putExtras(bundle);

            PendingIntent warningPendingIntent = PendingIntent.getBroadcast(context, notificationId, warningIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(mManager);

                notification = new Notification.Builder(mContext)
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setStyle(new Notification.BigTextStyle().bigText(contractId + "已达到您设置的预警价格，最新价为" + latestPrice))
                        .setContentTitle("行情预警")
                        .setContentText(contractId + "已达到您设置的预警价格，最新价为" + latestPrice)
                        .setContentIntent(warningPendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(contractId + "已达到您设置的预警价格，最新价为" + latestPrice))
                        .setContentTitle("行情预警")
                        .setContentText(contractId + "已达到您设置的预警价格，最新价为" + latestPrice)
                        .setContentIntent(warningPendingIntent).build();
            }
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;

        mManager.notify(notificationId, notification);
    }

    private void showNotification(Context context, String value) {
        int notificationId = (int) System.currentTimeMillis();
        Notification notification;

        String title = value.contains("条件单") ? "条件单" : value.contains("止盈止损单") ? "止盈止损单" : "";

        Intent warningIntent = new Intent(context, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("sheet", title);
        warningIntent.putExtras(bundle);

        PendingIntent warningPendingIntent = PendingIntent.getBroadcast(context, notificationId, warningIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(mManager);

            notification = new Notification.Builder(mContext)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setStyle(new Notification.BigTextStyle().bigText(value))
                    .setContentTitle(title)
                    .setContentText(value)
                    .setContentIntent(warningPendingIntent).build();
        } else {
            notification = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(value))
                    .setContentTitle(title)
                    .setContentText(value)
                    .setContentIntent(warningPendingIntent).build();
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;

        mManager.notify(notificationId, notification);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }

}

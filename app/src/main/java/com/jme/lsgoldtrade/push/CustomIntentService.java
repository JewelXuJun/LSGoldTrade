package com.jme.lsgoldtrade.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.ui.main.MainActivity;
import java.io.Serializable;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class CustomIntentService extends GTIntentService {

    private static final String TAG = "CustomIntentService";
    private static final String CHANNEL_ID = "TJSId";
    private static final String CHANNEL_NAME = "TJS";

    NotificationManager mManager;
    Context mContext;

    public CustomIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.d(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        mContext = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));
        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
            if (TextUtils.isEmpty(data))
                return;

            NotificationMessage message = null;

            try {
                message = new Gson().fromJson(data, NotificationMessage.class);
            } catch (Exception e) {
                e.printStackTrace();
                message = null;
            }

            if (message == null)
                return;

            showNotification(message);
        }
    }

    private void showNotification(NotificationMessage message) {
        int notificationId = (int) System.currentTimeMillis();

        Intent intent = null;
        PendingIntent pendingIntent = null;

//        if (!TextUtils.isEmpty(message.getUrl())) {
//            intent = new Intent(mContext, RfinexWebView.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("title", message.getTitle());
//            intent.putExtra("url", message.getUrl());
//        } else {
            intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }

        pendingIntent = PendingIntent.getActivity(mContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(mManager);
            notification = new Notification.Builder(mContext)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setContentTitle("行情预警")
                    .setContentText(message.getContractId() + "已达到您设置的预警价格，最新价为" + message.getLatestPrice())
                    .setContentIntent(pendingIntent).build();
        } else {
            notification = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setContentTitle("行情预警")
                    .setContentText(message.getContractId() + "已达到您设置的预警价格，最新价为" + message.getLatestPrice())
                    .setContentIntent(pendingIntent).build();
        }

        // 点击notification之后，该notification自动消失
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // notification被notify的时候，触发默认声音和默认震动
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        mManager.notify(notificationId, notification);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + cmdMessage);
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


    private class NotificationMessage implements Serializable {

        @SerializedName("contractId")
        String contractId;
        @SerializedName("latestPrice")
        String latestPrice;
        @SerializedName("priceCeiling")
        String priceCeiling;
        @SerializedName("contentType")
        String contentType;

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public String getLatestPrice() {
            return latestPrice;
        }

        public void setLatestPrice(String latestPrice) {
            this.latestPrice = latestPrice;
        }

        public String getPriceCeiling() {
            return priceCeiling;
        }

        public void setPriceCeiling(String priceCeiling) {
            this.priceCeiling = priceCeiling;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

}

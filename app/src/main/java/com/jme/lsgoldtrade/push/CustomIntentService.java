package com.jme.lsgoldtrade.push;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jme.lsgoldtrade.base.JMEApplication;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class CustomIntentService extends GTIntentService {

    private static final String TAG = "CustomIntentService";

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;

    public CustomIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
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

            try {
                sendMessage(data, 0);//这里对透传消息进行发送 通过App中的方法进行处理
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        JMEApplication.getInstance().sendMessage(data);
    }
}

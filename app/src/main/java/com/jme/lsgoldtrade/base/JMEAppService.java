package com.jme.lsgoldtrade.base;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.jme.common.network.AsynCommon;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.network.OnResultListener;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.SynTimeVo;
import com.jme.lsgoldtrade.service.UserService;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class JMEAppService extends Service implements OnResultListener {

    private SyncTimeHandler mHandler;

    private String mMaxMatchNo = "";

    static final class SyncTimeHandler extends Handler {

        private WeakReference<JMEAppService> mReference;

        public SyncTimeHandler(JMEAppService service) {
            mReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_SYNTIME:
                    if (null == mReference || null == mReference.get())
                        return;

                    removeMessages(Constants.Msg.MSG_SYNTIME);

                    if (!mReference.get().isApplicationBroughtToBackground(mReference.get())) {
                        if (User.getInstance().isLogin())
                            mReference.get().syncTime();
                    }

                    sendEmptyMessageDelayed(Constants.Msg.MSG_SYNTIME, AppConfig.TimeInterval_SYNC);

                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new SyncTimeHandler(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == mHandler)
            mHandler = new SyncTimeHandler(this);

        mHandler.sendEmptyMessage(Constants.Msg.MSG_SYNTIME);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mHandler)
            mHandler.removeMessages(Constants.Msg.MSG_SYNTIME);

        mHandler = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void syncTime() {
        HashMap<String, String> params = new HashMap<>();
        params.put("matchno", mMaxMatchNo);

        AsynCommon.SendRequest(UserService.getInstance().syntime, params, true, false, this, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void OnResult(DTRequest request, Head head, Object response) {
        switch (request.getApi().getName()) {
            case "SynTime":
                if (head.isSuccess()) {
                    SynTimeVo synTimeVo;

                    try {
                        synTimeVo = (SynTimeVo) response;
                    } catch (Exception e) {
                        synTimeVo = null;

                        e.printStackTrace();
                    }

                    if (null == synTimeVo)
                        return;

                    String maxMatchNo = synTimeVo.getMaxMatchNo();

                    if (!TextUtils.isEmpty(maxMatchNo)) {
                        if (TextUtils.isEmpty(mMaxMatchNo) || !TextUtils.equals(maxMatchNo, mMaxMatchNo)) {
                            mMaxMatchNo = maxMatchNo;

                            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_ORDER_SUCCESS, null);
                        }
                    }
                } else {
                    if (head.getCode().equals("-2000")) {
                        User.getInstance().logout();

                        SharedPreUtils.setString(this, SharedPreUtils.Token, "");

                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_SYNTIME, head.getMsg());
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS, null);
                    }
                }

                break;
        }
    }

    public boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);

        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;

            if (!topActivity.getPackageName().equals(context.getPackageName()))
                return true;
        }

        return false;
    }
}

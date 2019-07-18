package com.jme.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 实时监测网络状态
 * Created by zhangzhongqiang on 2015/10/9.
 */
public class NetStateReceiver extends BroadcastReceiver {

    private boolean flag = true;

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            if (flag) {
                Toast.makeText(context, "网络连接断开，请检查网络", Toast.LENGTH_SHORT).show();
                flag = false;
            }
        } else {
            if (!flag) {
                Toast.makeText(context, "网络连接成功", Toast.LENGTH_SHORT).show();
            }
            flag = true;
        }
    }
}
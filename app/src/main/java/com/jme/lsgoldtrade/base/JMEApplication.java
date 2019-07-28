package com.jme.lsgoldtrade.base;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.jme.common.BuildConfig;
import com.jme.common.app.BaseApplication;
import com.jme.common.network.CustomerSSL;
import com.jme.common.network.ImageDownLoader;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.domain.PushVo;
import com.jme.lsgoldtrade.push.CustomIntentService;
import com.jme.lsgoldtrade.push.CustomPushService;
import com.jme.lsgoldtrade.ui.splash.SplashActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import static com.jme.common.network.Connection.trustManagerForCertificates;

/**
 * Created by XuJun on 2018/11/7.
 */
public class JMEApplication extends BaseApplication {

    private static JMEApplication instance;
    private static Context mContext;

    private static OkHttpClient sOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        mContext = this.getApplicationContext();

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
            ButterKnife.setDebug(true);
            Logger.addLogAdapter(new AndroidLogAdapter());
        }

        ARouter.init(this);
        UMConfigure.init(this, "5cdb7eca570df3dfe70011b6", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //友盟相关平台配置。注意友盟官方新文档中没有这项配置，但是如果不配置会吊不起来相关平台的授权界面
        PlatformConfig.setWeixin(AppConfig.WECHATAPPID, AppConfig.WECHATAPPSECRET);//微信APPID和AppSecret
        PlatformConfig.setQQZone(AppConfig.QQAPPID, AppConfig.QQAPPKEY);//QQAPPID和AppSecret
        UMConfigure.setLogEnabled(true);
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), CustomPushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), CustomIntentService.class);
        initPicasso();

        Intent serviceIntent = new Intent(this, JMEAppService.class);
        startService(serviceIntent);

        String account = SharedPreUtils.getString(this, SharedPreUtils.UUID);
        if (TextUtils.isEmpty(account)) {
            AppConfig.UUID = UUID.randomUUID().toString();
            SharedPreUtils.setString(this, SharedPreUtils.UUID, AppConfig.UUID);
        } else {
            AppConfig.UUID = account;
        }
    }

    public static JMEApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    private void initPicasso() {
        //为了兼容okttp3
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(getSOkHttpClient())).loggingEnabled(true)
                .build());
    }

    public static OkHttpClient getSOkHttpClient() {
        if (sOkHttpClient == null) {
            //信任规则全部信任
            sOkHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20000, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(20000, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(20000, TimeUnit.SECONDS)//设置连接超时时间
                    .sslSocketFactory(new CustomerSSL(trustManagerForCertificates), trustManagerForCertificates)    //添加信任所有证书
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
        }
        return sOkHttpClient;
    }

    public synchronized void sendMessage(String msg) {
        PushVo pushVo = new Gson().fromJson(msg, PushVo.class);

        int notifyId = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel("1");
            if (channel == null) {
                channel = new NotificationChannel("1", "channal", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("description");
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setShowBadge(true);
                channel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(this, "1");
        } else {
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        PendingIntent pendingIntent;
        Intent intent = new Intent(this, SplashActivity.class);
        pendingIntent = PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_logo)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo))
                .setContentTitle(getString(R.string.app_name))
                .setContentText("")//透传消息内容
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(getResources().getColor(R.color.colorPrimary));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            builder.setGroupSummary(true);
            builder.setGroup("1");
        }
        Notification notification = builder.build();
        notificationManager.notify(++notifyId, notification);
    }
}

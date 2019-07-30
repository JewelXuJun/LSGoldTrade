package com.jme.lsgoldtrade.base;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.android.arouter.launcher.ARouter;
import com.igexin.sdk.PushManager;
import com.jme.common.BuildConfig;
import com.jme.common.app.BaseApplication;
import com.jme.common.network.CustomerSSL;
import com.jme.common.network.ImageDownLoader;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.push.CustomIntentService;
import com.jme.lsgoldtrade.push.CustomPushService;
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
}

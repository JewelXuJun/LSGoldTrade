package com.jme.lsgoldtrade.base;

import android.content.Context;

import com.jme.common.app.BaseApplication;
import com.jme.common.network.CustomerSSL;
import com.jme.common.network.ImageDownLoader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

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

        initPicasso();
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

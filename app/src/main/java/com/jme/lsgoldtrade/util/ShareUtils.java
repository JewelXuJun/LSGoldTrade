package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.text.TextUtils;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class ShareUtils {

    public static void share(Activity activity, String tradid) {
        UMWeb web = new UMWeb(TextUtils.isEmpty(tradid) ? Constants.HttpConst.URL_TRADINGBOX : Constants.HttpConst.URL_TRADINGBOXINFO + tradid);//连接地址
        web.setTitle("交易匣子");//标题
        web.setDescription("这里发布了一条新的交易匣子，一键掌握非农行情，快来投票吧！");//描述
        web.setThumb(new UMImage(activity, R.drawable.ic_logo_share));  //网络缩略图
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                }).open();
    }

}

package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jme.lsgoldtrade.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class ShareUtils {

    private static PopupWindow window;

    public static void share(Activity activity, String url) {
        UMWeb web = new UMWeb(url);
        web.setTitle("交易匣子");//标题
        web.setDescription("这里发布了一条新的交易匣子，一键掌握非农行情，快来投票吧！");//描述
        web.setThumb(new UMImage(activity, R.mipmap.ic_logo));  //网络缩略图
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
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
                })
                .open();
    }

    public static void popupwidnow(Activity activity, String url, String title, String content, int resId) {
        final View popupwindow = LayoutInflater.from(activity).inflate(R.layout.popupwindow_share, null);
        TextView tv_wechat = popupwindow.findViewById(R.id.tv_wechat);
        TextView tv_wechat_circle = popupwindow.findViewById(R.id.tv_wechat_circle);
        TextView tv_qq = popupwindow.findViewById(R.id.tv_qq);
        TextView tv_cancel_share = popupwindow.findViewById(R.id.tv_cancel_share);

        tv_wechat.setOnClickListener((view) -> {
            shareWeb(activity, url, title, content, SHARE_MEDIA.WEIXIN);

            window.dismiss();
        });

        tv_wechat_circle.setOnClickListener((view) -> {
            shareWeb(activity, url, title, content, SHARE_MEDIA.WEIXIN_CIRCLE);

            window.dismiss();
        });

        tv_qq.setOnClickListener((view) -> {
            shareWeb(activity, url, title, content, SHARE_MEDIA.QQ);

            window.dismiss();
        });

        tv_cancel_share.setOnClickListener((view) -> window.dismiss());

        window = new PopupWindow(popupwindow, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        window.setContentView(popupwindow);
        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        window.showAtLocation(activity.findViewById(resId),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popupwindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = popupwindow.getTop();
                int y = (int) event.getY();

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height)
                        window.dismiss();
                }

                return true;
            }
        });
    }

    public static void shareWeb(Activity activity, String url, String title, String content, SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(content);
        if (platform != SHARE_MEDIA.QQ)
            web.setThumb(new UMImage(activity, R.mipmap.ic_logo));

        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {

                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {

                    }
                })
                .share();
    }
}

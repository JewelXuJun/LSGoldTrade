package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.GLobleConstants;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class ShareUtils {

    private static PopupWindow window;

    public static void share(Activity activity, String tradid) {
        UMWeb web = new UMWeb(TextUtils.isEmpty(tradid) ? GLobleConstants.tradingbox : GLobleConstants.tradingboxinfo + tradid);//连接地址
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

    public static void popupwidnow(Activity activity, String url, String logo, String title, String content, int imageID, int resId) {
        final View popup = LayoutInflater.from(activity).inflate(R.layout.share_popupwindow, null);
        LinearLayout pop_layout = popup.findViewById(R.id.pop_layout);
        TextView iv_wechat = popup.findViewById(R.id.iv_wechat);
        TextView iv_wechat_friend = popup.findViewById(R.id.iv_wechat_friend);
        TextView iv_sina = popup.findViewById(R.id.iv_sina);
        TextView iv_cancel_share = popup.findViewById(R.id.iv_cancel_share);

        iv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                shareWeb(activity, url, logo, title, content, imageID, SHARE_MEDIA.WEIXIN);
            }
        });
        iv_wechat_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                shareWeb(activity, url, logo, title, content, imageID, SHARE_MEDIA.WEIXIN);
            }
        });
        iv_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                shareWeb(activity, url, logo, title, content, imageID, SHARE_MEDIA.SINA);
            }
        });
        iv_cancel_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        window = new PopupWindow(popup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //设置SelectWindow的View
        window.setContentView(popup);
        //设置SelectWindow弹出窗体的宽
        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectWindow弹出窗体的高
        window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        window.showAtLocation(activity.findViewById(resId),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);// 设置layout在popupwindow中显示的位置
        //设置SelectWindow弹窗的可点击
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = pop_layout.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        window.dismiss();
                    }
                }
                return true;
            }
        });
    }

    public static void shareWeb(Activity activity, String url, String logo, String title, String content, int imageID, SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(url);//连接地址
        web.setTitle(title);//标题
        web.setDescription(content);//描述
        web.setThumb(new UMImage(activity, logo));  //网络缩略图
//        if (TextUtils.isEmpty(logo)) {
//            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
//        } else {
//            web.setThumb(new UMImage(activity, logo));  //网络缩略图
//        }
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
//                                    Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
                                } else {
//                                    Toast.makeText(activity, share_media + " 分享成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Logger.e("throw:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(activity, share_media + " 分享失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(activity, share_media + " 分享取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();

        //新浪微博中图文+链接
        /*new ShareAction(activity)
                .setPlatform(platform)
                .withText(description + " " + WebUrl)
                .withMedia(new UMImage(activity,imageID))
                .share();*/
    }
}

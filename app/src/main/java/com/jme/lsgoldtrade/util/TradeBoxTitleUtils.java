package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DialogHelp;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;

public class TradeBoxTitleUtils {

    public static Dialog mDialog;

    public static void popup(Activity context, String type) {
        /* 创建AlertDialog对象并显示 */
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.collect_dialog).create();
        alertDialog.show();
        /* 添加对话框自定义布局 */
        alertDialog.setContentView(R.layout.pup_sort_time);
        /* 获取对话框窗口 */
        Window window = alertDialog.getWindow();
        TextView tv_share = window.findViewById(R.id.tv_share);
        TextView tv_history_box = window.findViewById(R.id.tv_history_box);
        TextView tv_my_order = window.findViewById(R.id.tv_my_order);
        TextView tv_my_gonglve = window.findViewById(R.id.tv_my_gonglve);
        TextView tv_my_wenti = window.findViewById(R.id.tv_my_wenti);

        /* 设置显示窗口的宽高 */
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /* 设置窗口显示位置 */
        window.setGravity(Gravity.RIGHT|Gravity.TOP);
        /* 还可以设置窗口显示动画 */
        // window.setWindowAnimations(R.style.AlertDialog_AppCompat);
        /* 通过window找布局里的控件 */
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                //分享
//                ShareUtils.popupwidnow(context, "https://www.jianshu.com/p/7712d6d00082", "", "", "",  0, resId);

                ShareUtils.share(context, type);
            }
        });
        tv_history_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //历史匣子
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.HISTORYBOX)
                        .navigation();
            }
        });
        tv_my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //我的订单
                if (TextUtils.isEmpty(User.getInstance().getToken())) {
                    showLoginDialog(context);
                } else {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.MYORDER)
                            .navigation();
                }
            }
        });
        tv_my_gonglve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //使用攻略
                String url = "http://www.taijs.com/upload/jyxz/jyxz.html";
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.JMEWEBVIEW)
                        .withString("title", "使用攻略")
                        .withString("url", url)
                        .navigation();
            }
        });
        tv_my_wenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //常见问题
                String url = "http://www.taijs.com/upload/jyxz/cjwt.html";
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.JMEWEBVIEW)
                        .withString("title", "常见问题")
                        .withString("url", url)
                        .navigation();
            }
        });
    }

    public static void showLoginDialog(Context context) {
        if (null == mDialog) {
            mDialog = DialogHelp.getConfirmDialog(context, context.getString(R.string.text_tips), context.getString(R.string.text_message_notlogin),
                    context.getString(R.string.text_login),
                    (dialog, which) -> {
                        dialog.dismiss();
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                                .navigation();
                    },
                    (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        } else {
            if (!mDialog.isShowing())
                mDialog.show();
        }
    }
}

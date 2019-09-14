package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;

public class TradeBoxFunctionUtils {

    public static void show(Activity activity, String url, String title, String content, int resId, boolean showShare) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.function_dialog).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.pupupwindow_function);

        Window window = alertDialog.getWindow();
        LinearLayout layout_share = window.findViewById(R.id.layout_share);
        TextView tv_share = window.findViewById(R.id.tv_share);
        TextView tv_history = window.findViewById(R.id.tv_history);
        TextView tv_order = window.findViewById(R.id.tv_order);
        TextView tv_introduction = window.findViewById(R.id.tv_introduction);
        TextView tv_question = window.findViewById(R.id.tv_question);

        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.RIGHT | Gravity.TOP);

        layout_share.setVisibility(showShare ? View.VISIBLE : View.GONE);

        tv_share.setOnClickListener((view) -> {
            ShareUtils.popupwidnow(activity, url, title, content, resId);

            alertDialog.dismiss();
        });

        tv_history.setOnClickListener((view) -> {
            if (User.getInstance().isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXHISTROY).navigation();
            else
                gotoLogin(activity);

            alertDialog.dismiss();
        });

        tv_order.setOnClickListener((view) -> {
            if (User.getInstance().isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXORDER).navigation();
            else
                gotoLogin(activity);

            alertDialog.dismiss();
        });

        tv_introduction.setOnClickListener((view) -> {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", activity.getResources().getString(R.string.trading_box_function_introduction))
                    .withString("url", Constants.HttpConst.URL_TRADE_BOX_INTRODUCTION)
                    .navigation();

            alertDialog.dismiss();
        });

        tv_question.setOnClickListener((view) -> {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", activity.getResources().getString(R.string.trading_box_function_question))
                    .withString("url", Constants.HttpConst.URL_TRADE_BOX_QUESTION)
                    .navigation();

            alertDialog.dismiss();
        });
    }

    public static void gotoLogin(Context context) {
        String loginType = SharedPreUtils.getString(context, SharedPreUtils.Login_Type);

        if (TextUtils.isEmpty(loginType)) {
            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
        } else {
            if (loginType.equals("Account"))
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else if (loginType.equals("Mobile"))
                ARouter.getInstance().build(Constants.ARouterUriConst.MOBILELOGIN).navigation();
        }
    }

}

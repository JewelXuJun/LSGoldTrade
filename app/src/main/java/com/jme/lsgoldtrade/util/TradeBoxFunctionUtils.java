package com.jme.lsgoldtrade.util;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;

public class TradeBoxFunctionUtils {

    public static void show(Activity activity, String url, String title, String content, int resId) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.function_dialog).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.pupupwindow_function);

        Window window = alertDialog.getWindow();
        TextView tv_share = window.findViewById(R.id.tv_share);
        TextView tv_history = window.findViewById(R.id.tv_history);
        TextView tv_order = window.findViewById(R.id.tv_order);
        TextView tv_introduction = window.findViewById(R.id.tv_introduction);
        TextView tv_question = window.findViewById(R.id.tv_question);

        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.RIGHT | Gravity.TOP);

        tv_share.setOnClickListener((view) -> {
            ShareUtils.popupwidnow(activity, url, title, content, resId);

            alertDialog.dismiss();
        });

        tv_history.setOnClickListener((view) -> {
            if (User.getInstance().isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXHISTROY).navigation();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();

            alertDialog.dismiss();
        });

        tv_order.setOnClickListener((view) -> {
            if (User.getInstance().isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXORDER).navigation();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();

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

}

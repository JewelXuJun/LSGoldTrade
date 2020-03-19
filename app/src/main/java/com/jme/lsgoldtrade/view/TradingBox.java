package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;

public class TradingBox extends RelativeLayout {

    private Boolean bShow = false;

    private static final int TRANSLATE_DURATION_MILLIS = 300;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public TradingBox(Context context) {
        super(context);
    }

    public TradingBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public TradingBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_trading_box, this);
        ImageView tranding = view.findViewById(R.id.tranding);
        ImageView cancel_tranding = view.findViewById(R.id.cancel_tranding);

        show();

        tranding.setOnClickListener((v) -> {
            if (!bShow) {
                show();
            } else {
                if (!User.getInstance().isLogin()) {
                    String loginType = SharedPreUtils.getString(context, SharedPreUtils.Login_Type);

                    if (TextUtils.isEmpty(loginType)) {
                        ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                    } else {
                        if (loginType.equals("Account"))
                            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                        else if (loginType.equals("Mobile"))
                            ARouter.getInstance().build(Constants.ARouterUriConst.MOBILELOGIN).navigation();
                    }
                } else {
                    if (TextUtils.isEmpty(User.getInstance().getAccountID()))
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                    else
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD, null);
//                        ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
                }
            }
        });

        cancel_tranding.setOnClickListener((v) -> hide());
    }

    public void show() {
        if (!bShow) {
            bShow = true;

            toggle();
        }
    }

    public void hide() {
        if (bShow) {
            bShow = false;

            toggle();
        }
    }

    private void toggle() {
        int width = getWidth();
        int translationX = bShow ? 0 : width / 2;

        animate().setInterpolator(mInterpolator).setDuration(TRANSLATE_DURATION_MILLIS).translationX(translationX);
    }
}

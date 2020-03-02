package com.jme.lsgoldtrade.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;

public class StockUserDialog extends Dialog {

    private Context mContext;
    public StockUserDialog(@NonNull Context context) {
        super(context, R.style.dialog);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.popwindow_stock_user, null);
        setContentView(view);
        setCancelable(false);

        LinearLayout mCloseLayout = view.findViewById(R.id.pop_window_stock_user_close);
        mCloseLayout.setOnClickListener((v)->{dismiss();});
        Button mBtnOpen = view.findViewById(R.id.btn_stock_user_pop);
        mBtnOpen.setOnClickListener((v)->{
            ARouter.getInstance().build(Constants.ARouterUriConst.OPENINCREMENT).navigation();
            dismiss();
        });
        TextView mTxClose = view.findViewById(R.id.tv_stock_user_later_pop);
        mTxClose.setOnClickListener((v)->{dismiss();});
    }
}

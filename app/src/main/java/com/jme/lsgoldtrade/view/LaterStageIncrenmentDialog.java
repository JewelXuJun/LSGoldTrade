package com.jme.lsgoldtrade.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;

public class LaterStageIncrenmentDialog extends Dialog {

    private Context mContext;
    public LaterStageIncrenmentDialog(@NonNull Context context) {
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
        View view = inflater.inflate(R.layout.dialog_later_stage_increment, null);
        setContentView(view);
        setCancelable(true);

        Button btnCancel = view.findViewById(R.id.btn_later_stage_cancel);
        Button btnPay = view.findViewById(R.id.btn_later_stage_increment);

        btnCancel.setOnClickListener((v) -> dismiss());

        btnPay.setOnClickListener((v) -> {
            dismiss();
            ARouter.getInstance().build(Constants.ARouterUriConst.CHECKSERVICE).navigation();
        });

    }
}

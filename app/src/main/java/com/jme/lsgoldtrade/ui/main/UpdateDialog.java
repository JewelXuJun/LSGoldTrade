package com.jme.lsgoldtrade.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;

public class UpdateDialog extends Dialog {

    private Context mContext;

    private String mDesc;
    private String mUpdateText;
    private boolean bUpdateFlag;
    private View.OnClickListener mListener;

    private Button mBtn_update;

    public UpdateDialog(@NonNull Context context, String desc, String updateText, boolean updateFlag, View.OnClickListener listener) {
        super(context, R.style.dialog);

        mContext = context;
        mDesc = desc;
        mUpdateText = updateText;
        bUpdateFlag = updateFlag;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_update, null);
        setContentView(view);
        setCancelable(false);

        TextView tv_desc = view.findViewById(R.id.tv_desc);
        ImageView img_cancel = view.findViewById(R.id.img_cancel);
        mBtn_update = view.findViewById(R.id.btn_install);

        tv_desc.setText(mDesc);

        img_cancel.setVisibility(bUpdateFlag ? View.GONE : View.VISIBLE);
        img_cancel.setOnClickListener((v) -> {
            Constants.DownLoadValues.IsDownLoadDialogShow = false;

            dismiss();
        });

        setUpdateButton(mUpdateText, mListener);
    }

    public void setUpdateButton(String updateText, View.OnClickListener listener) {
        if (null == mBtn_update)
            return;

        mBtn_update.setText(updateText);
        mBtn_update.setOnClickListener(listener);
    }

}

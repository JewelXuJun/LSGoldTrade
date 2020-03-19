package com.jme.lsgoldtrade.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jme.lsgoldtrade.R;

public class IncrementAlertDialog extends Dialog {

    private Context mContext;
    private View.OnClickListener mClick;
    private String content;
    private TextView mTxDescribe;

    public IncrementAlertDialog(@NonNull Context context,String tx) {
        super(context, R.style.dialog);

        this.mContext = context;
        this.content = tx;
    }

    public IncrementAlertDialog(@NonNull Context context, String tx, View.OnClickListener click) {
        super(context, R.style.dialog);

        this.mContext = context;
        this.mClick = click;
        this.content = tx;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_increment_alter, null);
        setContentView(view);
        setCancelable(false);

        mTxDescribe = view.findViewById(R.id.dialog_increment_describe);

        TextView mTxSure = view.findViewById(R.id.dialog_increment_sure);

        if(!TextUtils.isEmpty(content)){
            mTxDescribe.setText(content);
        }
        if(mClick!=null){
            mTxSure.setOnClickListener(mClick);
        }else {
            mTxSure.setOnClickListener((v) -> {
                dismiss();
            });
        }
    }

    public void setData(String contentStr){
        if(TextUtils.isEmpty(contentStr))
            return;
        mTxDescribe.setText(contentStr);
    }

}

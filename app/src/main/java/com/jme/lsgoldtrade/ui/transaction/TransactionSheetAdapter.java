package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.InOutTurnOverVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.List;

public class TransactionSheetAdapter extends BaseQuickAdapter<InOutTurnOverVo.TurnOverBean, BaseViewHolder> {

    private Context mContext;

    public TransactionSheetAdapter(Context context, @Nullable List<InOutTurnOverVo.TurnOverBean> data) {
        super(R.layout.item_transaction_sheet, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, InOutTurnOverVo.TurnOverBean item) {
        if (null == item)
            return;

    }

}

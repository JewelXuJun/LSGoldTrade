package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.text.TextUtils;

import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentTradingBoxItemBinding;
import com.jme.lsgoldtrade.domain.TradingBoxVo;

public class TradingBoxFragment extends JMEBaseFragment {

    private FragmentTradingBoxItemBinding mBinding;

    private TradingBoxVo.TradingBoxListVoBean mTradingBoxListVoBean;

    private String mType;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trading_box_item;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentTradingBoxItemBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(TradingBoxVo.TradingBoxListVoBean tradingBoxListVoBean, String type) {
        mTradingBoxListVoBean = tradingBoxListVoBean;
        mType = type;
    }

    private void setValue() {
        if (null == mTradingBoxListVoBean || TextUtils.isEmpty(mType))
            return;

        String direction = mTradingBoxListVoBean.getDirection();
        String pushTime = mTradingBoxListVoBean.getPushTime();

        mBinding.tvAbstract.setText(mTradingBoxListVoBean.getChance());
        mBinding.tvContract.setText(mTradingBoxListVoBean.getVariety());
        mBinding.tvDirection.setText(TextUtils.isEmpty(direction) ? ""
                : direction.equals("0") ? mContext.getResources().getString(R.string.text_more) : mContext.getResources().getString(R.string.text_empty));
        mBinding.btnCheck.setText(mType.equals("TradingBox") ? R.string.trading_box_participate_in : R.string.trading_box_check);
        mBinding.tvPublishTime.setText(TextUtils.isEmpty(pushTime) ? "" : String.format(mContext.getResources().getString(R.string.trading_box_publist_time),
                DateUtil.dataToStringWithData(DateUtil.dateToLong(pushTime))));
    }

    @Override
    public void onResume() {
        super.onResume();

        setValue();
    }

    public class ClickHandlers {

        public void onClickCheck() {
            if (TextUtils.isEmpty(mType))
                return;
        }

    }
}

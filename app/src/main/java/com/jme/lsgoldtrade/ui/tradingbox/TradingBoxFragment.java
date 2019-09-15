package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentTradingBoxBinding;
import com.jme.lsgoldtrade.domain.TradingBoxVo;

import java.util.List;

public class TradingBoxFragment extends JMEBaseFragment {

    private FragmentTradingBoxBinding mBinding;

    private List<TradingBoxVo.TradingBoxListVoBean> mTradingBoxListVoBeanList;
    private TradingBoxVo.TradingBoxListVoBean mTradingBoxListVoBean;

    private int mPosition;
    private String mPeriodName;
    private String mType;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trading_box;
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

        mBinding = (FragmentTradingBoxBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(String periodName, List<TradingBoxVo.TradingBoxListVoBean> tradingBoxListVoBeanList, TradingBoxVo.TradingBoxListVoBean tradingBoxListVoBean, int position, String type) {
        mPeriodName = periodName;
        mTradingBoxListVoBeanList = tradingBoxListVoBeanList;
        mTradingBoxListVoBean = tradingBoxListVoBean;
        mPosition = position;
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

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.TRADINGBOXDETAIL)
                    .withString("PeriodName",  mPeriodName)
                    .withString("Value", new Gson().toJson(mTradingBoxListVoBeanList))
                    .withInt("Position", mPosition)
                    .withString("Type", mType)
                    .navigation();
        }

    }
}

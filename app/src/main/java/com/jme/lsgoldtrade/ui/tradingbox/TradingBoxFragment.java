package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.view.Gravity;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentTradingBoxBinding;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
import com.jme.lsgoldtrade.util.PicassoUtils;
import com.jme.lsgoldtrade.view.TradingBoxPopupwindow;

import java.util.List;

public class TradingBoxFragment extends JMEBaseFragment {

    private FragmentTradingBoxBinding mBinding;

    private int position;

    private List<TradingBoxVo.HistoryListVoListBean> list;

    private TradingBoxPopupwindow mWindow;

    private String direction;

    public String getDirection() {
        return direction;
    }

    private String tradeId;

    public String getTradeId() {
        return tradeId;
    }

    public void setData(List<TradingBoxVo.HistoryListVoListBean> list, int position) {
        this.position = position;
        this.list = list;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trading_box;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (FragmentTradingBoxBinding) mBindingUtil;
        mWindow = new TradingBoxPopupwindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getData();
    }

    private void getData() {
        TradingBoxVo.HistoryListVoListBean historyListVoListBean = list.get(position);
        String chance = historyListVoListBean.getChance();
        String analystOpinion = historyListVoListBean.getAnalystOpinion();
        direction = historyListVoListBean.getDirection();
        String etfUrl = historyListVoListBean.getEtfUrl();
        String moodUrl = historyListVoListBean.getMoodUrl();
        String pushTime = historyListVoListBean.getPushTime();
        tradeId = historyListVoListBean.getTradeId();
        String variety = historyListVoListBean.getVariety();
        int closeTime = historyListVoListBean.getCloseTime();
        if ("0".equals(direction)) {
            mBinding.kLine.setText(variety + "   多");
        } else {
            mBinding.kLine.setText(variety + "   空");
        }

        mBinding.title.setText(chance);
        mBinding.time.setText("发布时间" + pushTime.substring(0, pushTime.lastIndexOf(" ")).replaceAll("-", "/"));
        mBinding.tvAnalyst.setText(analystOpinion);

        PicassoUtils.getInstance().loadImg(mContext, moodUrl, mBinding.tvSpeculativeSentiment);
        PicassoUtils.getInstance().loadImg(mContext, etfUrl, mBinding.glodetf);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickTouji() {
            if (null != mWindow) {
                mWindow.setData(R.mipmap.touji,
                        (View) -> {
                            mWindow.dismiss();
                        });
                mWindow.showAtLocation(mBinding.title, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickBaogao() {
            if (null != mWindow) {
                mWindow.setData(R.mipmap.baogao,
                        (View) -> {
                            mWindow.dismiss();
                        });
                mWindow.showAtLocation(mBinding.title, Gravity.CENTER, 0, 0);
            }
        }
    }
}

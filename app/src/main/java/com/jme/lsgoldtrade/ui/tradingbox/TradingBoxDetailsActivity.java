package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxDetailsBinding;
import com.jme.lsgoldtrade.domain.HistoryBoxVo;
import com.jme.lsgoldtrade.domain.HistoryItemVo;
import com.jme.lsgoldtrade.domain.TradingBoxDetailsVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.TradeBoxTitleUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * 交易匣子详情
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXDETAILS)
public class TradingBoxDetailsActivity extends JMEBaseActivity {

    private ActivityTradingBoxDetailsBinding mBinding;
    private String tradeId, type = "";
    private List<TradingBoxDetailsVo.RelevantInfoListVosBean> relevantInfoListVos;
    private String infoList = "";
    private String direction;
    private String variety;
    private int position = -1;
    private List<HistoryBoxVo.HistoryListVoListBean> historyListVoList;
    private List<HistoryItemVo> historyItemVoList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_details;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityTradingBoxDetailsBinding) mBindingUtil;
        type = getIntent().getStringExtra("type");

        initToolbar(R.string.trade_box, true);
        setRightNavigation();
    }

    private void setRightNavigation() {
        setRightNavigation("", R.mipmap.function, 0, () -> {
            TradeBoxTitleUtils.popup(this, tradeId);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if ("1".equals(type)) {
            mBinding.left.setVisibility(View.GONE);
            mBinding.right.setVisibility(View.GONE);
            tradeId = getIntent().getStringExtra("tradeId");
        } else if ("2".equals(type)) {
            String json = getIntent().getStringExtra("tradeId");
            historyItemVoList = new Gson().fromJson(json, new TypeToken<List<HistoryItemVo>>(){}.getType());
            position = 0;
            tradeId = historyItemVoList.get(position).getTradeId();
            if (historyItemVoList != null && historyItemVoList.size() > 1) {
                mBinding.left.setVisibility(View.VISIBLE);
                mBinding.right.setVisibility(View.VISIBLE);
            } else {
                mBinding.left.setVisibility(View.GONE);
                mBinding.right.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDateFromNet();
    }

    private void getDateFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("tradeId", tradeId);
        sendRequest(ManagementService.getInstance().tradingBoxDetails, params, false);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "TradingBoxDetails":
                if (head.isSuccess()) {
                    TradingBoxDetailsVo value;

                    try {
                        value = (TradingBoxDetailsVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }

                    String analystOpinion = value.getAnalystOpinion();
                    String chance = value.getChance();
                    direction = value.getDirection();
                    String fundamentalAnalysis = value.getFundamentalAnalysis();
                    String id = value.getId();
                    String periodId = value.getPeriodId();
                    String periodName = value.getPeriodName();
                    relevantInfoListVos = value.getRelevantInfoListVos();
                    variety = value.getVariety();
                    int directionUpNum = value.getDirectionUpNum();
                    String directionUpRate = value.getDirectionUpRate();
                    int directionDownNum = value.getDirectionDownNum();
                    String directionDownRate = value.getDirectionDownRate();

                    String openPositionsTimeBegin = value.getOpenPositionsTimeBegin();
                    String openPositionsTimeEnd = value.getOpenPositionsTimeEnd();
                    String closePositionsTimeBegin = value.getClosePositionsTimeBegin();
                    String closePositionsTimeEnd = value.getClosePositionsTimeEnd();

                    time = (long) value.getCloseTime();
                    if (time == 0) {
                        mBinding.daojishi.setVisibility(View.GONE);
                        mBinding.moreChoose.setBackground(getResources().getDrawable(R.drawable.bg_close));
                        mBinding.kongChoose.setBackground(getResources().getDrawable(R.drawable.bg_close));
                        mBinding.moreChoose.setEnabled(false);
                        mBinding.kongChoose.setEnabled(false);
                    } else {
                        mBinding.daojishi.setVisibility(View.VISIBLE);
                        mBinding.moreChoose.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_solid));
                        mBinding.kongChoose.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_solid));
                        mBinding.moreChoose.setEnabled(true);
                        mBinding.kongChoose.setEnabled(true);
                    }

                    mBinding.moreNum.setText(directionUpNum + "票");
                    mBinding.kongNum.setText(directionDownNum + "票");
                    mBinding.moreRate.setText(directionUpRate + "%");
                    mBinding.kongRate.setText(directionDownRate + "%");
                    mBinding.title.setText(chance);

                    mBinding.jibenmian.setDesc(fundamentalAnalysis, TextView.BufferType.NORMAL);
                    mBinding.fenxishi.setDesc(analystOpinion, TextView.BufferType.NORMAL);
                    mBinding.variety.setText(variety);
                    if ("0".equals(direction)) {
                        mBinding.direction.setText("多");
                    } else {
                        mBinding.direction.setText("空");
                    }
                    String[] openBegin = openPositionsTimeBegin.split(" ");
                    String[] openEnd = openPositionsTimeEnd.split(" ");
                    String[] closeBegin = closePositionsTimeBegin.split(" ");
                    String[] closeEnd = closePositionsTimeEnd.split(" ");
                    mBinding.kaicangbeginyear.setText(openBegin[0]);
                    mBinding.kaicangbeginclock.setText(openBegin[1]);
                    mBinding.kaicangcloseyear.setText(openEnd[0]);
                    mBinding.kaicangcloseclock.setText(openEnd[1]);
                    mBinding.pingcangbeginyear.setText(closeBegin[0]);
                    mBinding.pingcangbeginclock.setText(closeBegin[1]);
                    mBinding.pingcangendyear.setText(closeEnd[0]);
                    mBinding.pingcangendclock.setText(closeEnd[1]);

                    if (!TextUtils.isEmpty(directionUpRate)) {
                        if (directionUpRate.contains(".")) {
                            directionUpRate = directionUpRate.substring(0, value.getDirectionUpRate().indexOf("."));
                        }
                        mBinding.progress.setProgress(Integer.parseInt(directionUpRate));
                    }

                    initClock();
                }
                break;
            case "Rate":
                if (head.isSuccess()) {
//                    showShortToast("投票成功");
                }
                break;
        }
    }

    private void initClock() {
        handler.postDelayed(runnable, 1000);
    }

    private long time = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            String formatLongToTimeStr = formatLongToTimeStr(time);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if(i==0){
                    mBinding.tvtime0.setText(split[0]);
                }
                if(i==1){
                    mBinding.tvtime1.setText(split[1]);
                }
                if(i==2){
                    mBinding.tvtime2.setText(split[2]);
                }
                if(i==3){
                    mBinding.tvtime3.setText(split[3]);
                }
            }
            if(time>0){
                handler.postDelayed(this, 1000);
            }
        }
    };

    public  String formatLongToTimeStr(Long l) {
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        String day1 = "";
        String hour1 = "";
        String minute1 = "";
        String second1 = "";
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }


        if (day < 10) {
            day1 = "0" + day;
        } else {
            day1 = day + "";
        }

        if (hour < 10) {
            hour1 = "0" + hour;
        }else {
            hour1 = hour + "";
        }
        if (minute < 10) {
            minute1 = "0" + minute;
        } else {
            minute1 = minute + "";
        }
        if (second < 10) {
            second1 = "0" + second;
        } else {
            second1 = second + "";
        }
        String strtime = day1 + "：" + hour1 + "：" + minute1 + "：" + second1;
        return strtime;

    }

    public class ClickHandlers {

        public void onClickLeft() {
            if (position > 0) {
                position = position - 1;
                tradeId = historyItemVoList.get(position).getTradeId();
                getDateFromNet();
            }
        }

        public void onClickRight() {
            if (position < historyItemVoList.size() - 1) {
                position = position + 1;
                tradeId = historyItemVoList.get(position).getTradeId();

                getDateFromNet();
            }
        }

        public void onClickAboutNews() {
            if (relevantInfoListVos != null && !relevantInfoListVos.isEmpty()) {
                infoList = new Gson().toJson(relevantInfoListVos);
            }

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUTNEWS)
                    .withString("infoList", infoList)
                    .navigation();
        }

        public void onClickMore() {
            String toupiao = "";
            if (TextUtils.isEmpty(User.getInstance().getToken())) {
                showNeedLoginDialog();
            } else {
                rate("0");
                if ("0".equals(direction)) {
                    toupiao = "0";
//                    rate(toupiao);
                } else {
                    toupiao = "1";
//                    rate(toupiao);
                }
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.PLACEORDER)
                        .withString("type", "2")
                        .withString("direction", toupiao)
                        .withString("tradeId", tradeId)
                        .navigation();
            }
        }

        public void onClickKong() {
            String toupiao = "";
            if (TextUtils.isEmpty(User.getInstance().getToken())) {
                showNeedLoginDialog();
            } else {
                rate("1");
                if ("0".equals(direction)) {
                    toupiao = "1";
//                    rate(toupiao);
                } else {
                    toupiao = "0";
//                    rate(toupiao);
                }
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.PLACEORDER)
                        .withString("direction", toupiao)
                        .withString("type", "3")
                        .withString("tradeId", tradeId)
                        .navigation();
            }
        }
    }

    public void rate(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("boxId", tradeId);
        params.put("direction", type);
        params.put("variety", variety);
        sendRequest(ManagementService.getInstance().rate, params, false);
    }
}

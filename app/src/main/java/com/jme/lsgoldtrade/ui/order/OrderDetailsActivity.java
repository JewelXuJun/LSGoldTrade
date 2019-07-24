package com.jme.lsgoldtrade.ui.order;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityOrderDetailBinding;
import com.jme.lsgoldtrade.domain.TradingBoxOrderVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.HashMap;

/**
 * 订单详情
 */
@Route(path = Constants.ARouterUriConst.ORDERDETAILS)
public class OrderDetailsActivity extends JMEBaseActivity {

    private ActivityOrderDetailBinding mBinding;

    private String mID;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_order_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mID = getIntent().getStringExtra("ID");

        if (TextUtils.isEmpty(mID))
            return;

        getDetailInfo();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityOrderDetailBinding) mBindingUtil;
    }

    private void getDetailInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mID);

        sendRequest(ManagementService.getInstance().getDetailInfo, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetDetailInfo":
                if (head.isSuccess()) {
                    TradingBoxOrderVo tradingBoxOrderVo;

                    try {
                        tradingBoxOrderVo = (TradingBoxOrderVo) response;
                    } catch (Exception e) {
                        tradingBoxOrderVo = null;

                        e.printStackTrace();
                    }

                    if (null == tradingBoxOrderVo)
                        return;

                    String entrustTheDirection = tradingBoxOrderVo.getEntrustTheDirection();
                    String authorizedOpeningTimeBegin = tradingBoxOrderVo.getAuthorizedOpeningTimeBegin();
                    String authorizedOpeningTimeEND = tradingBoxOrderVo.getAuthorizedOpeningTimeEND();
                    String preOrderCloseDateBegin = tradingBoxOrderVo.getPreOrderCloseDateBegin();
                    String preOrderCloseDateEnd = tradingBoxOrderVo.getPreOrderCloseDateEnd();

                    String[] openTimeStart = authorizedOpeningTimeBegin.split(" ");
                    String[] openTimeEnd = authorizedOpeningTimeEND.split(" ");
                    String[] equalTimeStart = preOrderCloseDateBegin.split(" ");
                    String[] equalTimeEnd = preOrderCloseDateEnd.split(" ");


                    mBinding.tvContract.setText(tradingBoxOrderVo.getOrders());
                    mBinding.tvDirection.setText(entrustTheDirection.equals("0") ? R.string.text_more : R.string.text_empty);
                    mBinding.tvDirection.setTextColor(entrustTheDirection.equals("0") ? ContextCompat.getColor(this, R.color.color_red)
                            : ContextCompat.getColor(this, R.color.color_green));
                    mBinding.tvOpenTimeStartDate.setText(openTimeStart[0].replace("-", "/"));
                    mBinding.tvOpenTimeStartHour.setText(openTimeStart[1]);
                    mBinding.tvOpenTimeEndDate.setText(openTimeEnd[0].replace("-", "/"));
                    mBinding.tvOpenTimeEndHour.setText(openTimeEnd[1]);
                    mBinding.tvEqualTimeStartDate.setText(equalTimeStart[0].replace("-", "/"));
                    mBinding.tvEqualTimeStartHour.setText(equalTimeStart[1]);
                    mBinding.tvEqualTimeEndDate.setText(equalTimeEnd[0].replace("-", "/"));
                    mBinding.tvEqualTimeEndHour.setText(equalTimeEnd[1]);
                    mBinding.tvAmount.setText(tradingBoxOrderVo.getEntrustTheHandCount());
                    mBinding.tvProfit.setText(String.format(getString(R.string.trading_box_order_profit_line), tradingBoxOrderVo.getEarningsLine()));
                    mBinding.tvLoss.setText(String.format(getString(R.string.trading_box_order_loss_line), tradingBoxOrderVo.getLossLine()));
                    mBinding.tvTime.setText(tradingBoxOrderVo.getCratedTime());
                    mBinding.tvStatus.setText(MarketUtil.getOrderStatus(tradingBoxOrderVo.getStatus()));
                }

                break;
        }
    }

}

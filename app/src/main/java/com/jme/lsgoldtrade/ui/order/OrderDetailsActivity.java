package com.jme.lsgoldtrade.ui.order;

import android.os.Bundle;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityOrderDetailsBinding;
import com.jme.lsgoldtrade.domain.MyOrderVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.TradeBoxTitleUtils;
import com.jme.lsgoldtrade.view.EmptyView;
import com.jme.lsgoldtrade.view.ShareSelectWindow;

import java.util.HashMap;
import java.util.List;

/**
 * 订单详情
 */
@Route(path = Constants.ARouterUriConst.ORDERDETAILS)
public class OrderDetailsActivity extends JMEBaseActivity {

    private ActivityOrderDetailsBinding mBinding;
    private String id;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityOrderDetailsBinding) mBindingUtil;
        initToolbar("订单详情", true);
        setRightNavigation();
        id = getIntent().getStringExtra("id");
    }

    private void setRightNavigation() {
        setRightNavigation("", R.mipmap.function, 0, () -> {
            TradeBoxTitleUtils.popup(this, "");
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        sendRequest(ManagementService.getInstance().orderDetails, map, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "OrderDetails":
                if (head.isSuccess()) {
                    MyOrderVo value;
                    try {
                        value = (MyOrderVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (null == value) {
                        return;
                    }

                    String status = value.getStatus();
                    if ("0".equals(status)) {
                        mBinding.status.setText("委托中");
                    } else if ("1".equals(status)) {
                        mBinding.status.setText("建仓");
                    } else if ("2".equals(status)) {
                        mBinding.status.setText("建仓中");
                    } else if ("3".equals(status)) {
                        mBinding.status.setText("平仓");
                    } else if ("4".equals(status)) {
                        mBinding.status.setText("平仓中");
                    } else if ("5".equals(status)) {
                        mBinding.status.setText("委托完成");
                    } else if ("6".equals(status)) {
                        mBinding.status.setText("撤销中");
                    } else if ("7".equals(status)) {
                        mBinding.status.setText("已撤销");
                    } else if ("8".equals(status)) {
                        mBinding.status.setText("建仓完成");
                    }

                    String entrustTheDirection = value.getEntrustTheDirection();
                    if ("0".equals(entrustTheDirection)) {
                        mBinding.chooseDirection.setText("多");
                        mBinding.chooseDirection.setTextColor(getResources().getColor(R.color.color_red));
                    } else if ("1".equals(entrustTheDirection)) {
                        mBinding.chooseDirection.setText("空");
                        mBinding.chooseDirection.setTextColor(getResources().getColor(R.color.color_green));
                    }
                    mBinding.kaicangjiage.setText(value.getAuthorizedOpeningPrice() + "");
                    mBinding.time.setText(value.getCratedTime());
                    mBinding.yingli.setText("盈利平仓线 " + value.getEarningsLine() + "点");
                    mBinding.kuisun.setText("亏损平仓线 " + value.getLossLine() + "点");
                    mBinding.weituoshu.setText(value.getEntrustTheHandCount());
                    mBinding.pinzhong.setText(value.getOrders());

                    String authorizedOpeningTimeBegin = value.getAuthorizedOpeningTimeBegin();
                    String authorizedOpeningTimeEND = value.getAuthorizedOpeningTimeEND();
                    String preOrderCloseDateBegin = value.getPreOrderCloseDateBegin();
                    String preOrderCloseDateEnd = value.getPreOrderCloseDateEnd();

                    String[] openBegin = authorizedOpeningTimeBegin.split(" ");
                    String[] openEnd = authorizedOpeningTimeEND.split(" ");
                    String[] closeBegin = preOrderCloseDateBegin.split(" ");
                    String[] closeEnd = preOrderCloseDateEnd.split(" ");
                    mBinding.kaicangbeginyear.setText(openBegin[0]);
                    mBinding.kaicangbeginclock.setText(openBegin[1]);
                    mBinding.kaicangcloseyear.setText(openEnd[0]);
                    mBinding.kaicangcloseclock.setText(openEnd[1]);
                    mBinding.pingcangbeginyear.setText(closeBegin[0]);
                    mBinding.pingcangbeginclock.setText(closeBegin[1]);
                    mBinding.pingcangendyear.setText(closeEnd[0]);
                    mBinding.pingcangendclock.setText(closeEnd[1]);
                }
                break;
        }
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

    public class ClickHandlers {

        public void onClickChangeSet() {
//            share();
        }

    }

    public void share() {
        // 实例化试图
        ShareSelectWindow menuWindow = new ShareSelectWindow(this);
        // 显示窗体
        menuWindow.showAtLocation(findViewById(R.id.pinzhong),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);// 设置layout在popupwindow中显示的位置
    }
}

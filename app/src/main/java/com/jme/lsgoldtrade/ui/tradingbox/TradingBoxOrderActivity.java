package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxOrderBinding;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.TradingBoxOrderVo;
import com.jme.lsgoldtrade.domain.TradingBoxResponse;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.view.EmptyView;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的订单
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXORDER)
public class TradingBoxOrderActivity extends JMEBaseActivity implements OnRefreshListener {

    private ActivityTradingBoxOrderBinding mBinding;

    private ConfirmPopupwindow mWindow;
    private TradingBoxOrderAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_order;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_order, true);

        mWindow = new ConfirmPopupwindow(this);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        adapter = new TradingBoxOrderAdapter(R.layout.item_trading_box_order, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);

        getOrderList(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            TradingBoxOrderVo tradingBoxOrderVo = (TradingBoxOrderVo) adapter.getItem(position);

            if (null == tradingBoxOrderVo)
                return;

            switch (view.getId()) {
                case R.id.btn_cancel:
                    if (null != mWindow || !mWindow.isShowing()) {
                        mWindow.setData(getString(R.string.trading_box_order_cancel_message), getString(R.string.text_confirm),
                                (View) -> {
                                    revocateorder(tradingBoxOrderVo.getId());

                                    mWindow.dismiss();
                                });
                        mWindow.showAtLocation(mBinding.recyclerView, Gravity.CENTER, 0, 0);
                    }

                    break;
                case R.id.btn_detail:
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.ORDERDETAILS)
                            .withString("ID", tradingBoxOrderVo.getId())
                            .navigation();

                    break;
            }
        });

    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxOrderBinding) mBindingUtil;
    }

    private void getOrderList(boolean enable) {
        if (enable)
            showLoadingDialog("");

        DTRequest request = new DTRequest(ManagementService.getInstance().getOrderList, new HashMap<>(), false, true);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        TradingBoxResponse dtResponse = (TradingBoxResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
    }

    private void revocateorder(String id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);

        sendRequest(ManagementService.getInstance().revocation, hashMap, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetOrderList":
                if (head.isSuccess()) {
                    List<TradingBoxOrderVo> tradingBoxOrderVoList;

                    try {
                        tradingBoxOrderVoList = (List<TradingBoxOrderVo>) response;
                    } catch (Exception e) {
                        tradingBoxOrderVoList = null;

                        e.printStackTrace();
                    }

                    adapter.setNewData(tradingBoxOrderVoList);

                    if (null == tradingBoxOrderVoList || 0 == tradingBoxOrderVoList.size())
                        adapter.setEmptyView(EmptyView.getEmptyView(this));

                    mBinding.swipeRefreshLayout.finishRefresh(true);
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }
                break;
            case "Revocation":
                if (head.isSuccess()) {
                    showShortToast(R.string.trade_cancel_order_success);

                    getOrderList(true);
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getOrderList(false);
    }
}

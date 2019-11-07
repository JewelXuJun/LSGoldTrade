package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentCurrentHoldPositionsBinding;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ConditionSheetResponse;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.QuerySetStopOrderResponse;
import com.jme.lsgoldtrade.service.ConditionService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.EveningUpPopupWindow;
import com.jme.lsgoldtrade.view.TransactionStopPopupWindow;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

public class CurrentHoldPositionsFragment extends JMEBaseFragment {

    private FragmentCurrentHoldPositionsBinding mBinding;

    private List<FiveSpeedVo> mFiveSpeedVoList;

    private PositionVo mPositionVo;
    private HoldPositionsAdapter mAdapter;
    private EveningUpPopupWindow mEveningUpPopupWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;
    private TransactionStopPopupWindow mTransactionStopPopupWindow;
    private View mEmptyView;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_current_hold_positions;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionsAdapter(mContext, null);
        mEveningUpPopupWindow = new EveningUpPopupWindow(mContext);
        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);
        mTransactionStopPopupWindow = new TransactionStopPopupWindow(mContext, mBinding.tvGotoTransaction);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mPositionVo = (PositionVo) adapter.getItem(position);

            if (null == mPositionVo)
                return;

            switch (view.getId()) {
                case R.id.layout_stop_transaction:
                    String stopOrderFlag = mPositionVo.getStopOrderFlag();

                    if (!TextUtils.isEmpty(stopOrderFlag) && stopOrderFlag.equals("Y"))
                        querySetStopOrder(mPositionVo.getType().equals("多") ? 2 : 1, mPositionVo.getContractId());
                    else
                        showTransactionStopPopupWindow(false, mPositionVo.getContractId(), null);

                    break;
                case R.id.btn_evening_up:
                    showEveningUpPopupWindow(mPositionVo);

                    break;
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentCurrentHoldPositionsBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_STOP_SHEET_UPDATE:
                    Object object = message.getObject2();

                    if (null == object)
                        return;

                    List<String> list = (List<String>) object;

                    if (null == list || 7 != list.size())
                        return;

                    entrustConditionOrder(list);

                    break;
                case Constants.RxBusConst.RXBUS_TRANSACTION_STOP_SHEET_CANCEL_ORDER:
                    Object objectID = message.getObject2();

                    if (null == objectID)
                        return;

                    if (null != mTransactionStopPopupWindow)
                        mTransactionStopPopupWindow.dismiss();

                    if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                        mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_cancel_transaction_stop_sheet_message),
                                mContext.getResources().getString(R.string.text_confirm),
                                (view) -> {
                                    mConfirmPopupwindow.dismiss();

                                    revokeConditionOrder(objectID.toString());
                                });
                        mConfirmPopupwindow.showAtLocation(mBinding.tvGotoTransaction, Gravity.CENTER, 0, 0);
                    }

                    break;
            }
        });
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_margin_small, null);

        TextView tv_empty = mEmptyView.findViewById(R.id.tv_empty);
        tv_empty.setText(R.string.transaction_hold_positions_empty);

        return mEmptyView;
    }

    public void setFloatingList(List<String> list) {
        mAdapter.setList(list);
    }

    public void setFiveSpeedVoList(List<FiveSpeedVo> fiveSpeedVoList) {
        mFiveSpeedVoList = fiveSpeedVoList;

        mAdapter.setFiveSpeedVoList(fiveSpeedVoList);

        if (null != mTransactionStopPopupWindow && mTransactionStopPopupWindow.isShowing())
            mTransactionStopPopupWindow.setFiveSpeedVo(mFiveSpeedVoList);
    }

    public void setCurrentHoldPositionsData(List<PositionVo> positionVoList) {
        if (null == positionVoList) {
            mAdapter.setNewData(null);
            mAdapter.setEmptyView(getEmptyView());

            mBinding.tvGotoTransaction.setVisibility(View.VISIBLE);
        } else {
            List<PositionVo> positionVos = new ArrayList<>();

            for (PositionVo positionVo : positionVoList) {
                if (null != positionVo && positionVo.getPosition() != 0) {
                    positionVos.add(positionVo);
                }
            }

            mAdapter.setNewData(positionVos);

            if (0 == positionVos.size())
                mAdapter.setEmptyView(getEmptyView());

            mBinding.tvGotoTransaction.setVisibility(0 == positionVos.size() ? View.VISIBLE : View.GONE);
        }
    }

    public List<PositionVo> getData() {
        return mAdapter.getData();
    }

    private void showEveningUpPopupWindow(PositionVo positionVo) {
        String contractID = positionVo.getContractId();

        if (TextUtils.isEmpty(contractID) || null == mContract)
            return;

        ContractInfoVo contractInfoVo = mContract.getContractInfoFromID(contractID);

        if (null == contractInfoVo)
            return;

        if (null == mFiveSpeedVoList || 0 == mFiveSpeedVoList.size())
            return;

        FiveSpeedVo fiveSpeedVo = null;

        for (FiveSpeedVo fiveSpeedVoValue : mFiveSpeedVoList) {
            if (null != fiveSpeedVoValue && fiveSpeedVoValue.getContractId().equals(contractID))
                fiveSpeedVo = fiveSpeedVoValue;
        }

        if (null == fiveSpeedVo)
            return;

        if (null != mEveningUpPopupWindow && !mEveningUpPopupWindow.isShowing() && null != contractInfoVo) {
            String lowerLimitPrice = fiveSpeedVo.getLowerLimitPrice();
            String highLimitPrice = fiveSpeedVo.getHighLimitPrice();
            String type = positionVo.getType();
            long minOrderQty = contractInfoVo.getMinOrderQty();
            long maxOrderQty = contractInfoVo.getMaxOrderQty();
            long maxHoldQty = contractInfoVo.getMaxHoldQty();
            long maxAmount = positionVo.getPosition();

            mEveningUpPopupWindow.setData(mUser.getAccount(), contractID,
                    type.equals("多") ? fiveSpeedVo.getFiveBidLists().get(0)[1] : fiveSpeedVo.getFiveAskLists().get(4)[1],
                    type, new BigDecimal(contractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue(),
                    lowerLimitPrice, highLimitPrice, minOrderQty, maxOrderQty, maxHoldQty, maxAmount,
                    (view) -> {
                        String price = mEveningUpPopupWindow.getPrice();
                        String amount = mEveningUpPopupWindow.getAmount();

                        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default))) {
                            showShortToast(R.string.transaction_price_error);
                        } else if (new BigDecimal(price).compareTo(new BigDecimal(lowerLimitPrice)) == -1) {
                            showShortToast(R.string.transaction_limit_down_price_error);
                        } else if (new BigDecimal(price).compareTo(new BigDecimal(highLimitPrice)) == 1) {
                            showShortToast(R.string.transaction_limit_up_price_error);
                        } else if (TextUtils.isEmpty(amount)) {
                            showShortToast(R.string.transaction_number_error);
                        } else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0) {
                            showShortToast(R.string.transaction_number_error_zero);
                        } else if (minOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(minOrderQty)) == -1) {
                            showShortToast(R.string.transaction_limit_min_amount_error);
                        } else if (maxOrderQty == -1 && new BigDecimal(amount).compareTo(new BigDecimal(maxHoldQty == -1 ? maxAmount : Math.min(maxAmount, maxHoldQty))) == 1) {
                            Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
                        } else if (maxOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(Math.min(maxAmount, maxOrderQty))) == 1) {
                            Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
                        } else {
                            limitOrder(contractID, mEveningUpPopupWindow.getPrice(),
                                    mEveningUpPopupWindow.getAmount(), positionVo.getType().equals("多") ? 2 : 1, 1);

                            mEveningUpPopupWindow.dismiss();
                        }
                    });
            mEveningUpPopupWindow.showAtLocation(mBinding.tvGotoTransaction, Gravity.BOTTOM, 0, 0);
        }
    }

    private void showTransactionStopPopupWindow(boolean stopOrderFlag, String contractID, ConditionOrderInfoVo conditionOrderInfoVo) {
        if (null != mTransactionStopPopupWindow && !mTransactionStopPopupWindow.isShowing()) {
            FiveSpeedVo fiveSpeedVo = null;

            for (FiveSpeedVo fiveSpeedVoValue : mFiveSpeedVoList) {
                if (null != fiveSpeedVoValue && fiveSpeedVoValue.getContractId().equals(contractID))
                    fiveSpeedVo = fiveSpeedVoValue;
            }

            mTransactionStopPopupWindow.setData(stopOrderFlag, mPositionVo.getContractId(), fiveSpeedVo,
                    mPositionVo, null == mContract ? null : mContract.getContractInfoFromID(contractID), conditionOrderInfoVo);
            mTransactionStopPopupWindow.showAtLocation(mBinding.tvGotoTransaction, Gravity.BOTTOM, 0, 0);
        }
    }

    private void limitOrder(String contractId, String price, String amount, int bsFlag, int ocFlag) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("entrustPrice", String.valueOf(new BigDecimal(price).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", amount);
        params.put("bsFlag", String.valueOf(bsFlag));
        params.put("ocFlag", String.valueOf(ocFlag));
        params.put("tradingType", "0");

        sendRequest(TradeService.getInstance().limitOrder, params, true);
    }

    private void querySetStopOrder(int bsFlag, String contractId) {
        if (TextUtils.isEmpty(contractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("bsFlag", String.valueOf(bsFlag));
        params.put("contractId", contractId);

        DTRequest request = new DTRequest(ConditionService.getInstance().querySetStopOrder, params, true, false);

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
                        QuerySetStopOrderResponse dtResponse = (QuerySetStopOrderResponse) response.body();

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

    private void entrustConditionOrder(List<String> list) {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("bsFlag", list.get(0));
        params.put("ocFlag", list.get(1));
        params.put("contractId", list.get(2));
        params.put("effectiveTimeFlag", list.get(3));
        params.put("stopProfitPrice", TextUtils.isEmpty(list.get(4)) ? "1" : String.valueOf(new BigDecimal(list.get(4)).multiply(new BigDecimal(100)).longValue()));
        params.put("stopLossPrice", TextUtils.isEmpty(list.get(5)) ? "1" : String.valueOf(new BigDecimal(list.get(5)).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", list.get(6));
        params.put("tradingType", "3");
        params.put("type", "2");

        sendRequest(ConditionService.getInstance().entrustConditionOrder, params, true);
    }

    private void revokeConditionOrder(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);

        sendRequest(ConditionService.getInstance().revokeConditionOrder, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_evening_up_success);

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS_UPDATE, null);
                } else {
                    if (head.getMsg().contains("可用资金不足")) {
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_money_error),
                                    mContext.getResources().getString(R.string.transaction_money_in),
                                    (view) -> ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation());
                            mConfirmPopupwindow.showAtLocation(mBinding.tvGotoTransaction, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
            case "QuerySetStopOrder":
                if (head.isSuccess()) {
                    ConditionOrderInfoVo conditionOrderInfoVo;

                    try {
                        conditionOrderInfoVo = (ConditionOrderInfoVo) response;
                    } catch (Exception e) {
                        conditionOrderInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null != conditionOrderInfoVo)
                        showTransactionStopPopupWindow(true, mPositionVo.getContractId(), conditionOrderInfoVo);
                }

                break;
            case "EntrustConditionOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_setting_success);

                    if (null != mTransactionStopPopupWindow)
                        mTransactionStopPopupWindow.dismiss();

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS_UPDATE, null);
                }

                break;
            case "RevokeConditionOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_cancel_success);

                    if (null != mTransactionStopPopupWindow)
                        mTransactionStopPopupWindow.dismiss();

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS_UPDATE, null);
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickGoToTransaction() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}

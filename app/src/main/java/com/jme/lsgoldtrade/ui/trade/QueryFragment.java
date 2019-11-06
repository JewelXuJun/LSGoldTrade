package com.jme.lsgoldtrade.ui.trade;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentQueryBinding;
import com.jme.lsgoldtrade.domain.DailyStatementVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 查询
 */
public class QueryFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentQueryBinding mBinding;

    private boolean bVisibleToUser = false;

    private DatePickerDialog mDatePickerDialog;

    private int mYear;

    private int mMonth;

    private int mDayOfMonth;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_query;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentQueryBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.tvTime.setText(DateUtil.dateToString(System.currentTimeMillis()));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser)
            dailystatement(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            dailystatement(true);
    }

    private void setDailyStatementData(DailyStatementVo dailyStatementVo) {
        mBinding.tvTodayFloat.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTodayProfitStr()));
        mBinding.tvFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTradingFeeStr()));
        mBinding.tvDeferredFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTdDeferFeeStr()));
    }

    private void dailystatement(boolean enable) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("tradeDate", mBinding.tvTime.getText().toString());

        sendRequest(TradeService.getInstance().dailystatement, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "DailyStatement":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    DailyStatementVo dailyStatementVo;

                    try {
                        dailyStatementVo = (DailyStatementVo) response;
                    } catch (Exception e) {
                        dailyStatementVo = null;

                        e.printStackTrace();
                    }

                    if (null == dailyStatementVo)
                        return;

                    setDailyStatementData(dailyStatementVo);
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        dailystatement(false);
    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickTimeChoose() {
            //时间选择器
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(mContext, (datePicker, year, month, dayOfMonth) ->
                    setQueryTime(year, month, dayOfMonth), mYear, mMonth, mDayOfMonth);

            if (android.os.Build.VERSION.SDK_INT >= 11)
                mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

            mDatePickerDialog.show();
        }

        public void onClickQueryDailyStatement() {

        }

        public void onClickQueryCurrentHoldPosition() {

        }

        public void onClickQueryCurrentEntrust() {

        }

        public void onClickQueryHistoryEntrust() {

        }

        public void onClickQueryCurrentDeal() {

        }

        public void onClickQueryHistoryDeal() {

        }
    }

    private void setQueryTime(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        mBinding.tvTime.setText(DateUtil.dateToString(calendar.getTimeInMillis()));

        dailystatement(false);
    }

    private void getTodayCalendar() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }
}

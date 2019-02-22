package com.jme.lsgoldtrade.ui.trade;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityDailyStatementBinding;
import com.jme.lsgoldtrade.domain.DailyStatementVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.DAILYSTATEMENT)
public class DailyStatementActivity extends JMEBaseActivity {

    private ActivityDailyStatementBinding mBinding;

    private DatePickerDialog mDatePickerDialog;

    private int mYear;
    private int mMonth;
    private int mDayOfMonth;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_daily_statement;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityDailyStatementBinding) mBindingUtil;

        initToolbar(R.string.trade_daily_statement_query, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.tvTime.setText(DateUtil.dateToString(System.currentTimeMillis()));
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
    protected void onResume() {
        super.onResume();

        dailystatement();
    }

    private void getTodayCalendar() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
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

        dailystatement();
    }

    private void setDailyStatementData(DailyStatementVo dailyStatementVo) {
        mBinding.tvCurrentClientInterest.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentBalanceStr()));
        mBinding.tvPreviousCustomerInterest.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getLastTradingBalanceStr()));
        mBinding.tvCurrentAvailableFunds.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentDrawAmonutStr()));
        mBinding.tvTodayFloat.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTodayProfitStr()));
        mBinding.tvCurrentHoldPositionBond.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentPositionMarginStr()));
        mBinding.tvBreakEvenFund.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getMinReserveFundStr()));
        mBinding.tvCurrentOutmoney.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentWithdrawalStr()));
        mBinding.tvCurrentInmoney.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentIncomingsStr()));
        mBinding.tvFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTradingFeeStr()));
        mBinding.tvDeferredFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTdDeferFeeStr()));
    }

    private void dailystatement() {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("tradeDate", mBinding.tvTime.getText().toString());

        sendRequest(TradeService.getInstance().dailystatement, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "DailyStatement":
                if (head.isSuccess()) {
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
                }

                break;
        }
    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickSelectDate() {
            if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
                return;

            getTodayCalendar();

            mDatePickerDialog = new DatePickerDialog(DailyStatementActivity.this, (datePicker, year, month, dayOfMonth) ->
                    setQueryTime(year, month, dayOfMonth), mYear, mMonth, mDayOfMonth);

            if (android.os.Build.VERSION.SDK_INT >= 11)
                mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

            mDatePickerDialog.show();
        }

    }
}

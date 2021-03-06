package com.jme.lsgoldtrade.ui.transaction;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.datai.common.charts.common.Config;
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

/**
 * 日结单
 */
@Route(path = Constants.ARouterUriConst.DAILYSTATEMENT)
public class DailyStatementActivity extends JMEBaseActivity {

    private ActivityDailyStatementBinding mBinding;

    private DatePickerDialog mDatePickerDialog;

    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private long mCurrentTime;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_daily_statement;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_daily_statement_query, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mCurrentTime = System.currentTimeMillis() - Config.DAY;

        mBinding.tvTime.setText(DateUtil.dateToString(mCurrentTime));
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityDailyStatementBinding) mBindingUtil;
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

        mCurrentTime = calendar.getTimeInMillis();

        mBinding.tvTime.setText(DateUtil.dateToString(mCurrentTime));
        mBinding.tvNextDay.setVisibility(mCurrentTime + Config.DAY > System.currentTimeMillis() ? View.GONE : View.VISIBLE);

        dailystatement();
    }

    private void setDailyStatementData(DailyStatementVo dailyStatementVo) {
        mBinding.tvTodayFloat.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTodayProfitStr()));
        mBinding.tvFeeUnit.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTradingFeeStr()));
        mBinding.tvDeferredChargesUnit.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTdDeferFeeStr()));
        mBinding.tvCurrentClientInterest.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getLastTradingBalanceStr()));
        mBinding.tvCurrentAvailableFunds.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentBalanceStr()));
        mBinding.tvCurrentHoldPositionBond.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentPositionMarginStr()));
        mBinding.tvBreakEvenFund.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getMinReserveFundStr()));
        mBinding.tvCurrentOutmoney.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentWithdrawalStr()));
        mBinding.tvCurrentInmoney.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getCurrentIncomingsStr()));
        mBinding.tvFee.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTradingFeeStr()));
        mBinding.tvDeferredCharges.setText(MarketUtil.decimalFormatMoney(dailyStatementVo.getTdDeferFeeStr()));
    }

    private void dailystatement() {
        if (null == mUser || !mUser.isLogin())
            return;

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

        public void onClickPreviousDay() {
            mCurrentTime = mCurrentTime - Config.DAY;

            mBinding.tvTime.setText(DateUtil.dateToString(mCurrentTime));

            dailystatement();

            mBinding.tvNextDay.setVisibility(View.VISIBLE);
        }

        public void onClickNextDay() {
            mCurrentTime = mCurrentTime + Config.DAY;

            mBinding.tvTime.setText(DateUtil.dateToString(mCurrentTime));

            dailystatement();

            mBinding.tvNextDay.setVisibility(mCurrentTime + Config.DAY > System.currentTimeMillis() ? View.GONE : View.VISIBLE);
        }

    }
}

package com.jme.lsgoldtrade.ui.personal;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.bean.UserDetailsVo;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityDetailsBinding;
import com.jme.lsgoldtrade.service.AccountService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 账户明细
 */
@Route(path = Constants.ARouterUriConst.DETAILS)
public class DetailsActivity extends JMEBaseActivity {

    private ActivityDetailsBinding mBinding;

    private DetailsAdapter adapter;

    private DatePickerDialog mDatePickerDialog;

    private int mYear;

    private int mMonth;

    private int mDayOfMonth;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("账户明细", true);
        mBinding = (ActivityDetailsBinding) mBindingUtil;

        adapter = new DetailsAdapter(null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getTodayCalendar();
        mBinding.tvTime.setText(DateUtil.dateToString(System.currentTimeMillis()));
        getDateFromNet(DateUtil.dateToString(System.currentTimeMillis()));
    }

    private void getDateFromNet(String time) {
        HashMap<String, String> params = new HashMap<>();
        params.put("date", time);
        sendRequest(AccountService.getInstance().accountDetailList, params, true);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
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

        //查询时间
        getDateFromNet(DateUtil.dateToString(calendar.getTimeInMillis()));
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "AccountDetailList":
                if (head.isSuccess()) {
                    List<UserDetailsVo> value;

                    try {
                        value = (List<UserDetailsVo>) response;
                    } catch (Exception e) {
                        value = null;

                        e.printStackTrace();
                    }

                    if (null == value)
                        return;

                    adapter.setNewData(value);
                }
                break;
            default:
                break;
        }
    }
}

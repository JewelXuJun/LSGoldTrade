package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityOnlineDurationBinding;
import com.jme.lsgoldtrade.domain.OnlineTimeVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.ONLINEDURATION)
public class OnlineDurationActivity extends JMEBaseActivity {

    private ActivityOnlineDurationBinding mBinding;

    private OnlineTimeVo mOnlineTimeVo;
    private List<OnlineTimeVo> mOnlineTimeVoList;

    private OnlineDurationAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_online_duration;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_online_duration, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new OnlineDurationAdapter(null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        getOnlineTimeList();
        getUserOnlineTime();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            OnlineTimeVo onlineTimeVo = (OnlineTimeVo) adapter.getItem(position);

            if (null == onlineTimeVo)
                return;

            mAdapter.setCurrentTimeSeconds(onlineTimeVo.getTimeSeconds());
            mAdapter.notifyDataSetChanged();

            setUserOnlineTime(onlineTimeVo.getTimeSeconds());
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityOnlineDurationBinding) mBindingUtil;
    }

    private void setOnlineDurationData() {
        if (null == mOnlineTimeVo || null == mOnlineTimeVoList || 0 == mOnlineTimeVoList.size())
            return;

        mAdapter.setCurrentTimeSeconds(mOnlineTimeVo.getTimeSeconds());
        mAdapter.setNewData(mOnlineTimeVoList);
    }

    private void getUserOnlineTime() {
        sendRequest(ManagementService.getInstance().getUserOnlineTime, new HashMap<>(), false);
    }

    private void getOnlineTimeList() {
        sendRequest(ManagementService.getInstance().getOnlineTimeList, new HashMap<>(), true);
    }

    private void setUserOnlineTime(long timeSeconds) {
        if (0 == timeSeconds)
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("timeSeconds", String.valueOf(timeSeconds));

        sendRequest(ManagementService.getInstance().setUserOnlineTime, params, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetUserOnlineTime":
                if (head.isSuccess()) {
                    try {
                        mOnlineTimeVo = (OnlineTimeVo) response;
                    } catch (Exception e) {
                        mOnlineTimeVo = null;

                        e.printStackTrace();
                    }

                    setOnlineDurationData();
                }

                break;
            case "GetOnlineTimeList":
                if (head.isSuccess()) {
                    try {
                        mOnlineTimeVoList = (List<OnlineTimeVo>) response;
                    } catch (Exception e) {
                        mOnlineTimeVoList = null;

                        e.printStackTrace();
                    }

                    setOnlineDurationData();
                }

                break;
        }
    }

}

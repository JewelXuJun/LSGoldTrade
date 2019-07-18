package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAboutDoubtBinding;
import com.jme.lsgoldtrade.domain.AskListVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;
import java.util.List;

/**
 * 相关问题
 */
@Route(path = Constants.ARouterUriConst.ABOUTDOUBT)
public class AboutDoubtActivity extends JMEBaseActivity {

    private ActivityAboutDoubtBinding mBinding;
    private String type;
    private ExpandableItemAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_doubt;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityAboutDoubtBinding) mBindingUtil;
        type = getIntent().getStringExtra("type");
        switch (type) {
            case "1":
                initToolbar("开户相关问题", true);
                break;
            case "2":
                initToolbar("交易相关问题", true);
                break;
            case "3":
                initToolbar("资金相关问题", true);
                break;
            case "4":
                initToolbar("账户相关问题", true);
                break;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(manager);
        adapter = new ExpandableItemAdapter(mContext, R.layout.item_ask_list, null);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getDateFromNet();
    }

    private void getDateFromNet() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("typeId", type);
        sendRequest(ManagementService.getInstance().askList, hashMap, false);
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

    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "AskList":
                if (head.isSuccess()) {
                    List<AskListVo> value;
                    try {
                        value = (List<AskListVo>) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (null == value) {
                        return;
                    }

                    adapter.setNewData(value);
                }
                break;
        }
    }
}

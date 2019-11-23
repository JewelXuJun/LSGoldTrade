package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBankcardBinding;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.service.WithholdAccountService;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.BANKCARD)
public class BankCardActivity extends JMEBaseActivity {

    private ActivityBankcardBinding mBinding;

    private BankCardAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bankcard;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.increment_bankcard, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new BankCardAdapter(null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        getCustomerSignBankList();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityBankcardBinding) mBindingUtil;
    }

    private void getCustomerSignBankList() {
        sendRequest(WithholdAccountService.getInstance().getCustomerSignBankList, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetCustomerSignBankList":
                if (head.isSuccess()) {
                    List<BankVo> bankVoList;

                    try {
                        bankVoList = (List<BankVo>) response;
                    } catch (Exception e) {
                        bankVoList = null;

                        e.printStackTrace();
                    }

                    if (null == bankVoList || 0 == bankVoList.size())
                        return;

                    mAdapter.setNewData(bankVoList);
                }

                break;
        }
    }
}

package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBankcardBinding;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.domain.CheckCanChangeBankResponse;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.WithholdAccountService;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityBankcardBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCustomerSignBankList();
        checkCanBankCard();
    }

    private void getCustomerSignBankList() {
        sendRequest(WithholdAccountService.getInstance().getCustomerSignBankList, new HashMap<>(), true);
    }

    private void checkCanBankCard() {
        sendRequest(WithholdAccountService.getInstance().checkCanChangeBankCard, new HashMap<>(), true);
//        HashMap<String, String> params = new HashMap<>();
//
//
////        showLoadingDialog("");
//
//        DTRequest request = new DTRequest(WithholdAccountService.getInstance().checkCanChangeBankCard, params, false, false);
//
//        Call restResponse = request.getApi().request(request.getParams());
//
//        restResponse.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                Head head = new Head();
//                Object body = "";
//                Log.i("testXin","111111"+response.raw().code());
//                if (response.raw().code() != 200) {
//                    head.setSuccess(false);
//                    head.setCode("" + response.raw().code());
//                    head.setMsg("服务器异常");
//                } else {
//                    if (!request.getApi().isResponseJson()) {
//                        body = response.body();
//                        head.setSuccess(true);
//                        head.setCode("0");
//                        head.setMsg("成功");
//                    } else {
//
//                        CheckCanChangeBankResponse dtResponse = (CheckCanChangeBankResponse) response.body();
//
//                        head = new Head();
//                        head.setCode(dtResponse.getCode());
//                        head.setMsg(dtResponse.getMsg());
//                        body = dtResponse.getValue();
//
//                    }
//                }
//
//                OnResult(request, head, body);
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                Head head = new Head();
//                final Throwable cause = t.getCause() != null ? t.getCause() : t;
//
//                if (cause != null) {
//                    if (cause instanceof ConnectException) {
//                        head.setSuccess(false);
//                        head.setCode("500");
//                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
//                    } else {
//                        head.setSuccess(false);
//                        head.setCode("408");
//                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
//                    }
//                }
//
//                OnResult(request, head, null);
//            }
//        });
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
            case "checkCanChangeBankCard":
                if (head.isSuccess()) {
                    String value;
                    try {
                        value = response.toString();
                    }catch (Exception e){
                        value = null;
                        e.printStackTrace();
                    }

                    if(null==value)
                        return;

                    if("T".equals(value)){
                        //可以更改
                        mBinding.btnCheckCanChangeBank.setVisibility(View.VISIBLE);
                    }else{
                        mBinding.btnCheckCanChangeBank.setVisibility(View.GONE);
                    }

                }
                break;
        }
    }

    public class ClickHandlers {
        public void onClickChange(){
            ARouter.getInstance().build(Constants.ARouterUriConst.SELECTBANKCARD)
                    .withString("bankName","工商银行")
                    .withBoolean("isChangeBank",true)
                    .navigation();
        }
    }
}

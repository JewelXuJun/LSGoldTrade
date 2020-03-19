package com.jme.lsgoldtrade.ui.personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivitySelectBankCardBinding;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.generated.callback.OnClickListener;
import com.jme.lsgoldtrade.service.WithholdAccountService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.SELECTBANKCARD)
public class SelectBankCardActivity extends JMEBaseActivity {

    private ActivitySelectBankCardBinding mBinding;
    private List<BankVo> mBankVoList = new ArrayList<>();
    private String mSelectBankCardName = "";
    private SelectBankCardAdapter adapter;
    private boolean isChangeBank = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_select_bank_card;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.select_bank_card_title, true);
        mSelectBankCardName = getIntent().getStringExtra("bankName");
        isChangeBank = getIntent().getBooleanExtra("isChangeBank",false);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        getBanks("");
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivitySelectBankCardBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void getBanks(String cardNo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cardNo", cardNo);

        sendRequest(WithholdAccountService.getInstance().getBanks, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetBanks":
                if (head.isSuccess()) {
                    List<BankVo> bankVoList;

                    try {
                        bankVoList = (List<BankVo>) response;
                    } catch (Exception e) {
                        bankVoList = null;

                        e.printStackTrace();
                    }
                    if(bankVoList==null||0==bankVoList.size()){
//                        mBinding.llSelectBankCardLayoutDefault.setVisibility(View.INVISIBLE);
                    }else{
//                        if(TextUtils.isEmpty(mSelectBankCardName)||mSelectBankCardName.contains("工商")){
//                            mBinding.llSelectBankCardLayoutDefault.setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_select_bank_card_selection));
//                            mSelectBankCardName = "工商银行";
//                        }else{
//                            mBinding.llSelectBankCardLayoutDefault.setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_select_bank_card_default));
//                        }
//                        mBinding.llSelectBankCardLayoutDefault.setVisibility(View.VISIBLE);
                        mBankVoList.clear();

                        mBankVoList.addAll(bankVoList);
                        if(TextUtils.isEmpty(mSelectBankCardName)) {
                            mBankVoList.get(0).setSelection(true);
                            mSelectBankCardName = mBankVoList.get(0).getBankName();
                        }else{
                            for(BankVo item:bankVoList){
                                if(item.getBankName().equals(mSelectBankCardName))
                                    item.setSelection(true);

                                break;
                            }

                        }

                          adapter = new SelectBankCardAdapter(this,mBankVoList, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = (int) v.getTag();
                                boolean isSelection = mBankVoList.get(position).isSelection();



                                if(!isSelection){
                                    for(BankVo item:mBankVoList){
                                        item.setSelection(false);
                                    }
                                    mBankVoList.get(position).setSelection(true);
                                    mBinding.llSelectBankCardLayoutDefault.setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_select_bank_card_default));
                                    mSelectBankCardName =  mBankVoList.get(position).getBankName();
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(GridLayoutManager gridLayoutManager, int i) {
                                if(i==0){
                                    return 2;
                                }
                                return 1;
                            }
                        });
                        mBinding.selectBankCardRecyclerview.setLayoutManager(gridLayoutManager);

                        mBinding.selectBankCardRecyclerview.setAdapter(adapter);

                        mBinding.selectBankCardRecyclerview.setHasFixedSize(true);
                        mBinding.selectBankCardRecyclerview.setItemAnimator(new DefaultItemAnimator());

                    }

                }

                break;
        }
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_bank_card);
//    }

    public class ClickHandlers {
        public void onClickBtn() {

            if(!TextUtils.isEmpty(mSelectBankCardName)){
                if(!isChangeBank) {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_SELECT_BANK_CARD_SUCCESS, mSelectBankCardName);
                }else {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
                            .withString("Resource", "Trade")
                            .withBoolean("isBindAccount", false)
                            .withBoolean("isChangeBank",true)
                            .withString("bankName",mSelectBankCardName)
                            .navigation();
                }

                finish();
            }
        }

        public void onClickBank() {

        }
    }
}

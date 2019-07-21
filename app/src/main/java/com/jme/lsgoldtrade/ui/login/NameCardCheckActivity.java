package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityNameCardCheckBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.IntentUtils;
import com.jme.lsgoldtrade.util.NormalUtils;

import java.util.HashMap;

/**
 * 身份验证
 */
@Route(path = Constants.ARouterUriConst.NAMECARDCHECK)
public class NameCardCheckActivity extends JMEBaseActivity {

    private ActivityNameCardCheckBinding mBinding;
    private String tag;
    private String name;
    private String idCard;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_name_card_check;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityNameCardCheckBinding) mBindingUtil;
        initToolbar("身份验证", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tag = getIntent().getStringExtra("tag");
        if ("1".equals(tag)) {
            mBinding.btnBind.setText("立即绑定");
        } else {
            mBinding.btnBind.setText("下一步");
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), true);
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
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "WhetherIdCard":
                if (head.isSuccess()) {

                    IdentityInfoVo value;

                    try {
                        value = (IdentityInfoVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }

                    name = value.getName();
                    idCard = value.getIdCard();

                    mBinding.etName.setText(name);
                    mBinding.etNameCard.setText(idCard);
                }
                break;
            case "VerifyIdCard":
                mBinding.btnBind.setClickable(true);
                if (head.isSuccess()) {
                    if ("1".equals(tag)) {
                        IntentUtils.jumpBankSmall(mContext);
                    } else {
                        String name = mBinding.etName.getText().toString().trim();
                        String namecard = mBinding.etNameCard.getText().toString().trim();
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.BINDUSERNAME)
                                .withString("name", name)
                                .withString("card", namecard)
                                .navigation();
                    }
                } else {
                    showShortToast(head.getMsg());
                }
                break;
        }
    }

    public class ClickHandlers {

        public void onClickBind() {
            String name = mBinding.etName.getText().toString().trim();
            String nameCard = mBinding.etNameCard.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                showShortToast("请输入您的名字");
                return;
            }
            if (TextUtils.isEmpty(nameCard)) {
                showShortToast("请输入您的身份证号");
                return;
            }
            if (!NormalUtils.isIdCardNum(nameCard)) {
                showShortToast("请输入有效身份证号");
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("idCard", nameCard);
            sendRequest(TradeService.getInstance().verifyIdCard, params, false);
            mBinding.btnBind.setClickable(false);
        }
    }
}

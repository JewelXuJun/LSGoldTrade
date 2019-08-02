package com.jme.lsgoldtrade.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.igexin.sdk.PushManager;
import com.jme.common.app.BaseApplication;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseActivity;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.ui.main.MainActivity;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.SPLASH)
public class SplashActivity extends JMEBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        super.initView();

        new Handler().postDelayed(() -> gotoMainActivity(), Constants.SPLASH_DELAY_MILLIS);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        if (!TextUtils.isEmpty(SharedPreUtils.getString(this, SharedPreUtils.Token)))
            queryLoginResult();

        AppConfig.TimeInterval_NetWork = SharedPreUtils.getLong(this, SharedPreUtils.TimeInterval_NetWork, AppConfig.Second5);
        AppConfig.TimeInterval_WiFi = SharedPreUtils.getLong(this, SharedPreUtils.TimeInterval_WiFi, AppConfig.Second2);
        AppConfig.Select_ContractId = "Ag(T+D)";
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    private void queryLoginResult() {
        sendRequest(UserService.getInstance().queryLoginResult, new HashMap<>(), false, false, false);
    }

    private void getContractInfo() {
        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("contractId", "");
        parmas.put("accountId", accountID);

        sendRequest(TradeService.getInstance().contractInfo, parmas, false, false, false);
    }

    private void gotoMainActivity() {
        startAnimActivity(MainActivity.class);

        finish();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QueryLoginResult":
                if (head.isSuccess()) {
                    UserInfoVo userInfoVo;

                    try {
                        userInfoVo = (UserInfoVo) response;
                    } catch (Exception e) {
                        userInfoVo = null;

                        e.printStackTrace();
                    }

                    mUser.login(userInfoVo);

                    if (!TextUtils.isEmpty(userInfoVo.getTraderId()))
                        PushManager.getInstance().bindAlias(this, userInfoVo.getTraderId());

                    getContractInfo();
                } else {
                    SharedPreUtils.setString(this, SharedPreUtils.Token, "");
                }

                break;
            case "ContractInfo":
                if (head.isSuccess()) {
                    List<ContractInfoVo> list;

                    try {
                        list = (List<ContractInfoVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    mContract.setContractList(list);
                }

                break;
        }
    }
}

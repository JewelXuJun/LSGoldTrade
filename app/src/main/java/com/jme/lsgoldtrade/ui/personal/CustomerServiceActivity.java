package com.jme.lsgoldtrade.ui.personal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCustomerServiceBinding;
import com.jme.lsgoldtrade.domain.CustomerServiceVo;
import com.jme.lsgoldtrade.domain.QuestListTypeVo;
import com.jme.lsgoldtrade.domain.QuestionGuessVo;
import com.jme.lsgoldtrade.domain.QuestionAskVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 在线客服
 */
@Route(path = Constants.ARouterUriConst.CUSTOMSERVICE)
public class CustomerServiceActivity extends JMEBaseActivity {

    private ActivityCustomerServiceBinding mBinding;

    private QuestionTypeAdapter mQuestionTypeAdapter;
    private QuestionGuessAdapter mQuestionGuessAdapter;

    private CustomerServicesAdapter adapter;

    private List<QuestionAskVo> list = new ArrayList<>();


    private static final int REQUEST_CODE_ASK_CALL_PHONE = 0x126;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCustomerServiceBinding) mBindingUtil;

        initToolbar(R.string.personal_customer_service, true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        adapter = new CustomerServicesAdapter(mContext, R.layout.item_customer_service, list);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mQuestionTypeAdapter = new QuestionTypeAdapter(R.layout.item_question_type, null);
        mBinding.recyclerViewQuestionType.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.recyclerViewQuestionType.setAdapter(mQuestionTypeAdapter);

        mQuestionGuessAdapter = new QuestionGuessAdapter(R.layout.item_question_guess, null);
        mBinding.recyclerViewQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerViewQuestion.setAdapter(mQuestionGuessAdapter);

        getQuestTypeList();
        getGreeting();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mQuestionTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            QuestListTypeVo questListTypeVo = (QuestListTypeVo) adapter.getItem(position);

            if (null == questListTypeVo)
                return;

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.QUESTIONABOUT)
                    .withString("TypeId", questListTypeVo.getId())
                    .navigation();
        });

        mQuestionGuessAdapter.setOnItemClickListener((adapter, view, position) -> {
            QuestionGuessVo.QuestionBean questionBean = (QuestionGuessVo.QuestionBean) adapter.getItem(position);

            if (null == questionBean)
                return;

            getAnswerList(questionBean.getTitle());
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onPause() {
        super.onPause();

        hiddenKeyBoard();
    }

    private void callCustomer() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);

                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent;
        intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + AppConfig.CustomerServicePhone));

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callCustomer();

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                break;
        }
    }

    private void getQuestTypeList() {
        sendRequest(ManagementService.getInstance().questTypeList, new HashMap<>(), true);
    }

    private void getGreeting() {
        sendRequest(ManagementService.getInstance().getGreeting, new HashMap<>(), false);
    }

    private void getAnswerList(String title) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ask", title);

        sendRequest(ManagementService.getInstance().answerList, hashMap, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QuestTypeList":
                if (head.isSuccess()) {
                    List<QuestListTypeVo> questListVoList;

                    try {
                        questListVoList = (List<QuestListTypeVo>) response;
                    } catch (Exception e) {
                        questListVoList = null;

                        e.printStackTrace();
                    }

                    mQuestionTypeAdapter.setNewData(questListVoList);
                }

                break;
            case "GetGreeting":
                if (head.isSuccess()) {
                    QuestionGuessVo questionGuessVo;

                    try {
                        questionGuessVo = (QuestionGuessVo) response;
                    } catch (Exception e) {
                        questionGuessVo = null;

                        e.printStackTrace();
                    }

                    if (null == questionGuessVo)
                        return;

                    mQuestionGuessAdapter.setNewData(questionGuessVo.getQuestionList());
                }

                break;
            case "AnswerList":
                hiddenKeyBoard();

                if (head.isSuccess()) {
                    mBinding.etQuestion.setText("");

                    List<CustomerServiceVo> value;
                    try {
                        value = (List<CustomerServiceVo>) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (null == value) {
                        CustomerServiceVo customerServiceVo = value.get(0);
                        QuestionAskVo questionAskVo = new QuestionAskVo();
                        questionAskVo.setQuestion(customerServiceVo.getTitle());
                        questionAskVo.setAsk("抱歉，暂时无法解决您的问题，请联系人工客服");
                        list.add(questionAskVo);
                        adapter.notifyDataSetChanged();
                        mBinding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        return;
                    }
                    for (int i = 0; i < value.size(); i++) {
                        CustomerServiceVo customerServiceVo = value.get(i);
                        QuestionAskVo questionAskVo = new QuestionAskVo();
                        questionAskVo.setQuestion(customerServiceVo.getTitle());
                        questionAskVo.setAsk(customerServiceVo.getAnwser());
                        list.add(questionAskVo);
                    }
                    adapter.notifyDataSetChanged();
                    mBinding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
                break;
        }
    }

    public class ClickHandlers {

        public void onClickArtificialCustomerService() {
            callCustomer();
        }

        public void onClickChangeGroup() {
            getGreeting();
        }

        public void onClickSend() {
            String question = mBinding.etQuestion.getText().toString().trim();
            if (TextUtils.isEmpty(question)) {
                return;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("ask", question);
            sendRequest(ManagementService.getInstance().answerList, hashMap, false);
        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etQuestion.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

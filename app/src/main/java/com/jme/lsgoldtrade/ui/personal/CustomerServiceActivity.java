package com.jme.lsgoldtrade.ui.personal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCustomerServiceBinding;
import com.jme.lsgoldtrade.domain.CustomerServiceVo;
import com.jme.lsgoldtrade.domain.QuestListTypeVo;
import com.jme.lsgoldtrade.domain.QuestionGuessVo;
import com.jme.lsgoldtrade.domain.QuestionAnswerVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * 在线客服
 */
@Route(path = Constants.ARouterUriConst.CUSTOMSERVICE)
public class CustomerServiceActivity extends JMEBaseActivity {

    private ActivityCustomerServiceBinding mBinding;

    private QuestionTypeAdapter mQuestionTypeAdapter;
    private QuestionGuessAdapter mQuestionGuessAdapter;
    private QuestionAnswerAdapter mQuestionAnswerAdapter;
    private List<List<QuestionAnswerVo>> mQuestionAnswerVoList = new ArrayList<>();

    private boolean bGuessFlag = true;
    private int mPosition = -1;
    private String mGuessValue;

    private Subscription mRxbus;

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

        mQuestionAnswerAdapter = new QuestionAnswerAdapter(this, R.layout.item_question_answer, mQuestionAnswerVoList);
        mBinding.recyclerViewAnswer.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerViewAnswer.setAdapter(mQuestionAnswerAdapter);

        getQuestTypeList();
        getGreeting();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

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

        mQuestionAnswerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            List<QuestionAnswerVo> questionAnswerVoList = (List<QuestionAnswerVo>)adapter.getItem(position);

            if (null == questionAnswerVoList || questionAnswerVoList.size() == 0)
                return;

            hiddenKeyBoard();

            switch (view.getId()) {
                case R.id.tv_change_group:
                    bGuessFlag = false;
                    mPosition = position;
                    mGuessValue = questionAnswerVoList.get(0).getInputQuestion();

                    getGreeting();

                    break;
                case R.id.layout_question_first:
                    QuestionAnswerVo questionAnswerVoFirst = questionAnswerVoList.get(0);

                    if (null == questionAnswerVoFirst)
                        return;

                    getAnswerList(questionAnswerVoFirst.getQuestion());

                    break;
                case R.id.layout_question_second:
                    QuestionAnswerVo questionAnswerVoSecond = questionAnswerVoList.get(1);

                    if (null == questionAnswerVoSecond)
                        return;

                    getAnswerList(questionAnswerVoSecond.getQuestion());

                    break;
                case R.id.layout_question_third:
                    QuestionAnswerVo questionAnswerVoThird = questionAnswerVoList.get(2);

                    if (null == questionAnswerVoThird)
                        return;

                    getAnswerList(questionAnswerVoThird.getQuestion());

                    break;
                case R.id.layout_question_fourth:
                    QuestionAnswerVo questionAnswerVoFourth = questionAnswerVoList.get(3);

                    if (null == questionAnswerVoFourth)
                        return;

                    getAnswerList(questionAnswerVoFourth.getQuestion());

                    break;
            }
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

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_CUSTOMER_SERVICE:
                    callCustomer();

                    break;
            }
        });
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

    private void getQuestTypeList() {
        sendRequest(ManagementService.getInstance().questTypeList, new HashMap<>(), true);
    }

    private void getGreeting() {
        sendRequest(ManagementService.getInstance().getGreeting, new HashMap<>(), false);
    }

    private void getAnswerList(String question) {
        if (TextUtils.isEmpty(question))
            return;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ask", question);

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

                    if (bGuessFlag) {
                        mQuestionGuessAdapter.setNewData(questionGuessVo.getQuestionList());
                    } else {
                        List<QuestionGuessVo.QuestionBean> questionBeanList = questionGuessVo.getQuestionList();
                        List<QuestionAnswerVo> questListVoList = new ArrayList<>();

                        if (null != questionBeanList && questionBeanList.size() > 0) {
                            for (int i = 0; i < questionBeanList.size(); i++) {
                                QuestionGuessVo.QuestionBean questionBean = questionBeanList.get(i);

                                if (null != questionBean) {
                                    QuestionAnswerVo questionAnswerVo = new QuestionAnswerVo();
                                    questionAnswerVo.setInputQuestion(mGuessValue);
                                    questionAnswerVo.setQuestion(questionBean.getTitle());
                                    questionAnswerVo.setAsk(questionBean.getAnwser());
                                    questionAnswerVo.setJumpTxt(questionBean.getJumpTxt());

                                    questListVoList.add(questionAnswerVo);
                                }
                            }
                        }

                        if (null == mQuestionAnswerVoList || mQuestionAnswerVoList.size() <= mPosition)
                            return;

                        mQuestionAnswerVoList.set(mPosition, questListVoList);

                        mQuestionAnswerAdapter.notifyDataSetChanged();
                    }
                }

                break;
            case "AnswerList":
                if (head.isSuccess()) {
                    String question = mBinding.etQuestion.getText().toString();

                    mBinding.etQuestion.setText("");

                    List<CustomerServiceVo> customerServiceVoList;

                    try {
                        customerServiceVoList = (List<CustomerServiceVo>) response;
                    } catch (Exception e) {
                        customerServiceVoList = null;

                        e.printStackTrace();
                    }

                    if (null == customerServiceVoList || 0 == customerServiceVoList.size()) {
                        List<QuestionAnswerVo> questionAnswerVoNoneList = new ArrayList<>();

                        QuestionAnswerVo questionAnswerVo = new QuestionAnswerVo();
                        questionAnswerVo.setInputQuestion("");
                        questionAnswerVo.setQuestion(question);
                        questionAnswerVo.setAsk(getString(R.string.personal_customer_service_cannot_answer));
                        questionAnswerVo.setJumpTxt(getString(R.string.personal_customer_service_personal));

                        questionAnswerVoNoneList.add(questionAnswerVo);

                        mQuestionAnswerVoList.add(questionAnswerVoNoneList);
                    } else {
                        List<QuestionAnswerVo> questionAnswerVoList = new ArrayList<>();

                        int size = customerServiceVoList.size();

                        if (size == 1) {
                            CustomerServiceVo customerServiceVo = customerServiceVoList.get(0);
                            QuestionAnswerVo questionAnswerVo = new QuestionAnswerVo();
                            questionAnswerVo.setInputQuestion("");
                            questionAnswerVo.setQuestion(customerServiceVo.getTitle());
                            questionAnswerVo.setAsk(customerServiceVo.getAnwser());
                            questionAnswerVo.setJumpTxt(customerServiceVo.getJumpTxt());

                            questionAnswerVoList.add(questionAnswerVo);
                        } else {
                            for (int i = 0; i < customerServiceVoList.size(); i++) {
                                CustomerServiceVo customerServiceVo = customerServiceVoList.get(i);
                                QuestionAnswerVo questionAnswerVo = new QuestionAnswerVo();
                                questionAnswerVo.setInputQuestion(question);
                                questionAnswerVo.setQuestion(customerServiceVo.getTitle());
                                questionAnswerVo.setAsk(customerServiceVo.getAnwser());
                                questionAnswerVo.setJumpTxt(customerServiceVo.getJumpTxt());

                                questionAnswerVoList.add(questionAnswerVo);
                            }
                        }

                        mQuestionAnswerVoList.add(questionAnswerVoList);
                    }

                    mQuestionAnswerAdapter.notifyDataSetChanged();

                    mBinding.recyclerViewAnswer.scrollToPosition(mQuestionAnswerAdapter.getItemCount() - 1);

                    new Handler().post(() -> mBinding.nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN));
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickArtificialCustomerService() {
            hiddenKeyBoard();

            callCustomer();
        }

        public void onClickChangeGroup() {
            bGuessFlag = true;

            hiddenKeyBoard();

            getGreeting();
        }

        public void onClickSend() {
            hiddenKeyBoard();

            String question = mBinding.etQuestion.getText().toString().trim();

            if (TextUtils.isEmpty(question))
                showShortToast(R.string.personal_customer_service_empty);
            else
                getAnswerList(question);
        }
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

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etQuestion.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}

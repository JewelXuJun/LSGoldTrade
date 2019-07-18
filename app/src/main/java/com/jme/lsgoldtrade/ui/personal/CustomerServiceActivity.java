package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCustomerServiceBinding;
import com.jme.lsgoldtrade.domain.CustomerServiceVo;
import com.jme.lsgoldtrade.domain.QuestListVo;
import com.jme.lsgoldtrade.domain.QuestVo;
import com.jme.lsgoldtrade.domain.QuestionAskVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.ui.mainpage.AboutQuestAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 在线客服
 */
@Route(path = Constants.ARouterUriConst.CUSTOMSERVICE)
public class CustomerServiceActivity extends JMEBaseActivity {

    private ActivityCustomerServiceBinding mBinding;

    private CustomerServicesAdapter adapter;

    private QuestAdapter questAdapter;

    private List<QuestVo.QuestionListBean> questionList;

    private List<QuestionAskVo> list = new ArrayList<>();

    private AboutQuestAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityCustomerServiceBinding) mBindingUtil;
        initToolbar("在线客服", true);

        questAdapter = new QuestAdapter(mContext, R.layout.item_quest, null);
        mBinding.recyclerViewQuestion.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerViewQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerViewQuestion.setAdapter(questAdapter);

        adapter = new CustomerServicesAdapter(mContext, R.layout.item_customer_service, list);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(adapter);

        mAdapter = new AboutQuestAdapter(R.layout.item_about_quest, null);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);

        mBinding.aboutQuest.setLayoutManager(manager);
        mBinding.aboutQuest.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getWelcomeFromNet();
    }

    private void getWelcomeFromNet() {
        sendRequest(ManagementService.getInstance().questList, new HashMap<>(), false);
        sendRequest(ManagementService.getInstance().welcome, new HashMap<>(), false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        questAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QuestVo.QuestionListBean question = (QuestVo.QuestionListBean) adapter.getItem(position);

                if (null == question)
                    return;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ask", question.getTitle());
                sendRequest(ManagementService.getInstance().ask, hashMap, false);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String type = (position + 1) + "";
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ABOUTDOUBT)
                        .withString("type", type)
                        .navigation();
            }
        });
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
            case "QuestListVo":
                if (head.isSuccess()) {
                    List<QuestListVo> value;
                    try {
                        value = (List<QuestListVo>) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (null == value) {
                        return;
                    }

                    mAdapter.setNewData(value);
                }
                break;
            case "Welcome":
                if (head.isSuccess()) {
                    QuestVo questVo;
                    try {
                        questVo = (QuestVo) response;
                    } catch (Exception e) {
                        questVo = null;
                        e.printStackTrace();
                    }

                    if (null == questVo) {
                        return;
                    }

                    String greeting = questVo.getGreeting();
                    mBinding.welcome.setText(greeting);
                    if (questionList != null && !questionList.isEmpty())
                        questionList.clear();

                    questionList = questVo.getQuestionList();

                    questAdapter.setNewData(questVo.getQuestionList());
                }
                break;
            case "Ask":
                if (head.isSuccess()) {
                    mBinding.etQuestion.setText("");
                    hiddenKeyBoard();
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
                        mBinding.recyclerView.scrollToPosition(adapter.getItemCount()-1);
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
                    mBinding.recyclerView.scrollToPosition(adapter.getItemCount()-1);
                }
                break;
        }
    }

    public class ClickHandlers {

        public void onClickKaihu() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUTDOUBT)
                    .withString("type", "1")
                    .navigation();
        }

        public void onClickJiaoYi() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUTDOUBT)
                    .withString("type", "2")
                    .navigation();
        }

        public void onClickZiJin() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUTDOUBT)
                    .withString("type", "3")
                    .navigation();
        }

        public void onClickZhangHu() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUTDOUBT)
                    .withString("type", "4")
                    .navigation();
        }

        public void onClickChangeGroup() {
            sendRequest(ManagementService.getInstance().welcome, new HashMap<>(), false);
        }

        public void onClickSend() {
            String question = mBinding.etQuestion.getText().toString().trim();
            if (TextUtils.isEmpty(question)) {
                return;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("ask", question);
            sendRequest(ManagementService.getInstance().ask, hashMap, false);
        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etQuestion.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

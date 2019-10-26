package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityQuestionAboutBinding;
import com.jme.lsgoldtrade.domain.QuestionVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;
import java.util.List;

/**
 * 相关问题
 */
@Route(path = Constants.ARouterUriConst.QUESTIONABOUT)
public class QuestionAboutActivity extends JMEBaseActivity {

    private ActivityQuestionAboutBinding mBinding;

    private String mType;

    private QuestionAboutAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_about;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.personal_customer_service_quesiont_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getIntent().getStringExtra("TypeId");

        mAdapter = new QuestionAboutAdapter(this, R.layout.item_question_list, null);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        getQuestionListByType();
    }

    private void getQuestionListByType() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("typeId", mType);

        sendRequest(ManagementService.getInstance().getQuestionListByType, hashMap, true);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityQuestionAboutBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetQuestionListByType":
                if (head.isSuccess()) {
                    List<QuestionVo> questionVoList;

                    try {
                        questionVoList = (List<QuestionVo>) response;
                    } catch (Exception e) {
                        questionVoList = null;

                        e.printStackTrace();
                    }

                    if (null == questionVoList)
                        return;

                    mAdapter.setNewData(questionVoList);
                }

                break;
        }
    }
}

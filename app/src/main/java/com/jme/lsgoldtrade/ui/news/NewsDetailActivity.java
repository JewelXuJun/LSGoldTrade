package com.jme.lsgoldtrade.ui.news;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityNewsDetailBinding;
import com.jme.lsgoldtrade.domain.NoticeVo;
import com.jme.lsgoldtrade.service.UserService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.NEWSDETAILACTIVITY)
public class NewsDetailActivity extends JMEBaseActivity {

    private ActivityNewsDetailBinding mBinding;

    private long mID;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.news_detail_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mID = getIntent().getLongExtra("ID", -10000);

        getNewsDetail();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityNewsDetailBinding) mBindingUtil;
    }

    private void getNewsDetail() {
        if (-10000 == mID)
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(mID));

        sendRequest(UserService.getInstance().noticedetail, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "NoticeDetail":
                if (head.isSuccess()) {
                    NoticeVo.NoticeBean noticeBean;

                    try {
                        noticeBean = (NoticeVo.NoticeBean) response;
                    } catch (Exception e) {
                        noticeBean = null;

                        e.printStackTrace();
                    }

                    if (null == noticeBean)
                        return;

                    String suggest = noticeBean.getSuggest();
                    String content = noticeBean.getContent();

                    mBinding.tvTitle.setText(noticeBean.getTitle());
                    mBinding.tvTime.setText(noticeBean.getCreateTime());

                    if (TextUtils.isEmpty(suggest)) {
                        mBinding.tvContent.setText(content);
                        mBinding.tvContent.setVisibility(View.VISIBLE);
                        mBinding.layoutQuestion.setVisibility(View.GONE);
                    } else {
                        mBinding.tvQuestion.setText(suggest);
                        mBinding.tvAnswer.setText(content);
                        mBinding.tvContent.setVisibility(View.GONE);
                        mBinding.layoutQuestion.setVisibility(View.VISIBLE);
                    }

                }

                break;
        }
    }
}

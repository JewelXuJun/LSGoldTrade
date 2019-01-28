package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentInfoBinding;
import com.jme.lsgoldtrade.domain.NoticePageVo;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends JMEBaseFragment /*implements BaseQuickAdapter.RequestLoadMoreListener*/ {

    private FragmentInfoBinding mBinding;

    private InfoAdapter mAdapter;

    private boolean bVisibleToUser = false;
    private String mType;
   /* private int mCurrentPage = 1;
    private int mTotalPage = 1;*/

    public static Fragment newInstance(String type) {
        InfoFragment fragment = new InfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("InfoType", type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentInfoBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getArguments().getString("InfoType");
        mAdapter = new InfoAdapter(R.layout.item_info, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        initInfoData();
    }

    @Override
    protected void initListener() {
        super.initListener();

//        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentInfoBinding) mBindingUtil;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser && !TextUtils.isEmpty(mType))
            getData(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            getData(false);
    }

    private void initInfoData() {
        if (mType.equals("notice"))
            initNoticeData();
        else if (mType.equals("news"))
            initNewsData();
        else if (mType.equals("activity"))
            initActivityData();
    }

    private void initNoticeData() {
        List<NoticePageVo.NoticeBean> list = new ArrayList<>();

        NoticePageVo.NoticeBean noticeBean1 = new NoticePageVo.NoticeBean();
        noticeBean1.setTitle("关于延长银行间黄金交易询价是从交易时间的公告");
        noticeBean1.setSendTime("2018-01-27 00:00:00");

        NoticePageVo.NoticeBean noticeBean2 = new NoticePageVo.NoticeBean();
        noticeBean2.setTitle("关于修订《上海黄金交易所异常交易健康制度的暂行规定》的公告");
        noticeBean2.setSendTime("2018-01-27 12:30:00");

        NoticePageVo.NoticeBean noticeBean3 = new NoticePageVo.NoticeBean();
        noticeBean3.setTitle("关于继续免收2019年国际会员及国际客户仓储费、出入库费等费用的公告");
        noticeBean3.setSendTime("2018-01-28 00:00:00");

        NoticePageVo.NoticeBean noticeBean4 = new NoticePageVo.NoticeBean();
        noticeBean4.setTitle("关于继续免收2019年库存互换过户费及保管库场外失误清算过户费的公告");
        noticeBean4.setSendTime("2018-01-28 18:00:00");

        NoticePageVo.NoticeBean noticeBean5 = new NoticePageVo.NoticeBean();
        noticeBean5.setTitle("关于调整上海金定价市场参数的通知");
        noticeBean5.setSendTime("2018-01-29 08:00:00");

        list.add(noticeBean1);
        list.add(noticeBean2);
        list.add(noticeBean3);
        list.add(noticeBean4);
        list.add(noticeBean5);

        mAdapter.setNewData(list);
    }

    private void initNewsData() {
        List<NoticePageVo.NoticeBean> list = new ArrayList<>();

        NoticePageVo.NoticeBean noticeBean1 = new NoticePageVo.NoticeBean();
        noticeBean1.setTitle("实物黄金消费旺季来临 黄金一季度有望走强");
        noticeBean1.setSendTime("2018-01-27 07:00:00");

        NoticePageVo.NoticeBean noticeBean2 = new NoticePageVo.NoticeBean();
        noticeBean2.setTitle("黄金美元避险属性争宠 近期金市仍受多重要素制掣");
        noticeBean2.setSendTime("2018-01-27 09:30:00");

        NoticePageVo.NoticeBean noticeBean3 = new NoticePageVo.NoticeBean();
        noticeBean3.setTitle("挑战特朗普！ 又以为竞选者假如2020年的美国大选竞争惨烈");
        noticeBean3.setSendTime("2018-01-28 09:00:00");

        NoticePageVo.NoticeBean noticeBean4 = new NoticePageVo.NoticeBean();
        noticeBean4.setTitle("空头步步紧逼 黄金跌破1280创近一个月新低");
        noticeBean4.setSendTime("2018-01-28 15:00:00");

        NoticePageVo.NoticeBean noticeBean5 = new NoticePageVo.NoticeBean();
        noticeBean5.setTitle("美国政府停摆24日创最长记录 恐进一步延续到2月底");
        noticeBean5.setSendTime("2018-01-29 10:00:00");

        list.add(noticeBean1);
        list.add(noticeBean2);
        list.add(noticeBean3);
        list.add(noticeBean4);
        list.add(noticeBean5);

        mAdapter.setNewData(list);
    }

    private void initActivityData() {
        List<NoticePageVo.NoticeBean> list = new ArrayList<>();

        NoticePageVo.NoticeBean noticeBean1 = new NoticePageVo.NoticeBean();
        noticeBean1.setTitle("岁月入金-上海黄金交易所成立16周年");
        noticeBean1.setSendTime("2018-01-27 17:00:00");

        NoticePageVo.NoticeBean noticeBean2 = new NoticePageVo.NoticeBean();
        noticeBean2.setTitle("2017“中金建行杯”全国黄金投资分析师职业技能竞赛正式报名开赛");
        noticeBean2.setSendTime("2018-01-27 19:30:00");

        NoticePageVo.NoticeBean noticeBean3 = new NoticePageVo.NoticeBean();
        noticeBean3.setTitle("直播：浦发银行指标实战网络讲座");
        noticeBean3.setSendTime("2018-01-28 13:00:00");

        NoticePageVo.NoticeBean noticeBean4 = new NoticePageVo.NoticeBean();
        noticeBean4.setTitle("安银行:玩“赚”贵金属，大奖等你来");
        noticeBean4.setSendTime("2018-01-28 20:00:00");

        NoticePageVo.NoticeBean noticeBean5 = new NoticePageVo.NoticeBean();
        noticeBean5.setTitle("中国银行2016年金交所代理业务手机交易大赛获奖名单");
        noticeBean5.setSendTime("2018-01-29 14:00:00");

        list.add(noticeBean1);
        list.add(noticeBean2);
        list.add(noticeBean3);
        list.add(noticeBean4);
        list.add(noticeBean5);

        mAdapter.setNewData(list);
    }

   /* private int getTotalPage(int totalPage) {
        int pageSize = Integer.parseInt(AppConfig.PageSize_10);

        if (totalPage % pageSize == 0)
            totalPage = totalPage / pageSize;
        else
            totalPage = totalPage / pageSize + 1;

        return totalPage;
    }*/

    private void getData(boolean enable) {
        /*if (mType.equals("notice"))
            noticepage(enable);*/
        initInfoData();
    }

    private void noticepage(boolean enable) {
      /*  HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));
        params.put("pageSize", AppConfig.PageSize_10);

        sendRequest(UserService.getInstance().noticepage, params, enable, false, false);*/
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
           /* case "NoticePage":
                if (head.isSuccess()) {
                    NoticePageVo noticePageVo;

                    try {
                        noticePageVo = (NoticePageVo) response;
                    } catch (Exception e) {
                        noticePageVo = null;

                        e.printStackTrace();
                    }

                    if (null == noticePageVo)
                        return;

                    mTotalPage = getTotalPage(noticePageVo.getTotal());

                    if (0 == mTotalPage)
                        return;

                    List<NoticePageVo.NoticeBean> list = noticePageVo.getList();

                    if (null == list || 0 == list.size())
                        return;

                    if (mCurrentPage == 1) {
                        mAdapter.setNewData(list);
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
                } else {
                    mAdapter.loadMoreFail();
                }

                break;*/
        }
    }

   /* @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (mCurrentPage >= mTotalPage) {
                mAdapter.loadMoreEnd();
            } else {
                mCurrentPage++;

                getData(true);
            }
        }, 0);
    }*/
}

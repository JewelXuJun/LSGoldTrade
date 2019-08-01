package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityFastManagementBinding;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.IntentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 快捷入口管理
 */
@Route(path = Constants.ARouterUriConst.FASTMANAGEMENT)
public class FastManagementActivity extends JMEBaseActivity {

    private ActivityFastManagementBinding mBinding;

    private AddedAdapter mAddedAdapter;
    private NotAddedAdapter mNotAddedAdapter;

    private List<NavigatorVo.NavigatorVoBean> mUsedModulesBeanList = new ArrayList<>();
    private List<NavigatorVo.NavigatorVoBean> mNotUsedModulesBeanList = new ArrayList<>();

    private boolean bEditFlag = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fast_management;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.personal_fast_management_title, true);

        setRightNavigation(getString(R.string.personal_fast_management_edit), 0, R.style.ToolbarThemeBlue, () -> doEdit());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAddedAdapter = new AddedAdapter(R.layout.item_added, null);
        mBinding.recyclerViewAdded.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.recyclerViewAdded.setAdapter(mAddedAdapter);
        mAddedAdapter.showEditorialStatus(false);

        mNotAddedAdapter = new NotAddedAdapter(R.layout.item_not_added, null);
        mBinding.recyclerViewNotAdded.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.recyclerViewNotAdded.setAdapter(mNotAddedAdapter);
        mNotAddedAdapter.showEditorialStatus(false);

        getNavigatorList();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAddedAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            NavigatorVo.NavigatorVoBean navigatorVoBean = (NavigatorVo.NavigatorVoBean) adapter.getItem(position);

            if (null == navigatorVoBean)
                return;

            if (mUsedModulesBeanList.size() > 4) {
                mUsedModulesBeanList.remove(navigatorVoBean);
                mNotUsedModulesBeanList.add(navigatorVoBean);

                mAddedAdapter.notifyDataSetChanged();
                mNotAddedAdapter.notifyDataSetChanged();
            } else {
                showShortToast(R.string.personal_fast_management_length_less);
            }
        });

        mNotAddedAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            NavigatorVo.NavigatorVoBean navigatorVoBean = (NavigatorVo.NavigatorVoBean) adapter.getItem(position);

            if (null == navigatorVoBean)
                return;

            if (mUsedModulesBeanList.size() < 7) {
                mUsedModulesBeanList.add(navigatorVoBean);
                mNotUsedModulesBeanList.remove(navigatorVoBean);

                mAddedAdapter.notifyDataSetChanged();
                mNotAddedAdapter.notifyDataSetChanged();
            } else {
                showShortToast(R.string.personal_fast_management_length);
            }
        });

        mAddedAdapter.setOnItemClickListener((adapter, view, position) -> {
            NavigatorVo.NavigatorVoBean navigatorVoBean = (NavigatorVo.NavigatorVoBean) adapter.getItem(position);

            if (null == navigatorVoBean)
                return;

            IntentUtils.IntentFastTab(this, navigatorVoBean.getCode());
        });

        mNotAddedAdapter.setOnItemClickListener((adapter, view, position) -> {
            NavigatorVo.NavigatorVoBean navigatorVoBean = (NavigatorVo.NavigatorVoBean) adapter.getItem(position);

            if (null == navigatorVoBean)
                return;

            IntentUtils.IntentFastTab(this, navigatorVoBean.getCode());
        });

    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityFastManagementBinding) mBindingUtil;
    }

    private void doEdit() {
        if (bEditFlag) {
            setRightNavigation(getString(R.string.personal_fast_management_edit), 0, R.style.ToolbarThemeBlue, () -> doEdit());

            bEditFlag = false;

            String addValues = "";
            String notAddValues = "";

            if (null == mUsedModulesBeanList || 0 == mUsedModulesBeanList.size()) {
                addValues = "";
            } else {
                for (NavigatorVo.NavigatorVoBean navigatorVoBean : mUsedModulesBeanList) {
                    if (null != navigatorVoBean)
                        addValues = addValues + navigatorVoBean.getCode() + ",";
                }
            }

            if (null == mNotUsedModulesBeanList || 0 == mNotUsedModulesBeanList.size()) {
                notAddValues = "";
            } else {
                for (NavigatorVo.NavigatorVoBean navigatorVoBean : mNotUsedModulesBeanList) {
                    if (null != navigatorVoBean)
                        notAddValues = notAddValues + navigatorVoBean.getCode() + ",";
                }
            }

            if (addValues.endsWith(","))
                addValues = addValues.substring(0, addValues.length() - 1);

            if (notAddValues.endsWith(","))
                notAddValues = notAddValues.substring(0, notAddValues.length() - 1);

            saveNavigatorList(addValues, notAddValues);
        } else {
            setRightNavigation(getString(R.string.personal_fast_management_complete), 0, R.style.ToolbarThemeBlue, () -> doEdit());

            bEditFlag = true;
        }

        mAddedAdapter.showEditorialStatus(bEditFlag);
        mNotAddedAdapter.showEditorialStatus(bEditFlag);

        mAddedAdapter.notifyDataSetChanged();
        mNotAddedAdapter.notifyDataSetChanged();
    }

    private void getNavigatorList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", AppConfig.UUID);
        params.put("token", mUser.getToken());

        sendRequest(ManagementService.getInstance().navigatorList, params, true);
    }

    private void saveNavigatorList(String addValues, String notAddValues) {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", AppConfig.UUID);
        params.put("token", mUser.getToken());
        params.put("usedModuleCodes", addValues);
        params.put("notUsedModuleCodes", notAddValues);

        sendRequest(ManagementService.getInstance().saveNavigatorList, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "NavigatorList":
                if (head.isSuccess()) {
                    NavigatorVo navigatorVo;

                    try {
                        navigatorVo = (NavigatorVo) response;
                    } catch (Exception e) {
                        navigatorVo = null;

                        e.printStackTrace();
                    }

                    if (null == navigatorVo)
                        return;

                    mUsedModulesBeanList = navigatorVo.getUsedModules();
                    mNotUsedModulesBeanList = navigatorVo.getNotUsedModules();

                    mAddedAdapter.setNewData(mUsedModulesBeanList);
                    mNotAddedAdapter.setNewData(mNotUsedModulesBeanList);
                }

                break;
            case "SaveNavigatorList":
                if (head.isSuccess()) {
                    showShortToast(R.string.personal_fast_management_edit_success);

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_FAST_MANAGEMENT_EDIT, null);
                }

                break;
        }
    }

}

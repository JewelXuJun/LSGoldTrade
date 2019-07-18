package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.AppManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.GLobleConstants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityFastEntryBinding;
import com.jme.lsgoldtrade.domain.HomeTabChooseVo;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.domain.SaveNavigatorVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * 快捷入口管理
 */
@Route(path = Constants.ARouterUriConst.FASTENTRY)
public class FastEntryActivity extends JMEBaseActivity {

    private ActivityFastEntryBinding mBinding;

    private ExistAdapter existAdapter;

    private NoExistAdapter noExistAdapter;

    private String existStr;

    private String noExitStr;

    private int bw = 0;

    private List<NavigatorVo.UsedModulesBean> existingList = new ArrayList<>();
    private List<NavigatorVo.UsedModulesBean> noExitingList = new ArrayList<>();
    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private NavigatorVo navigator;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fast_entry;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityFastEntryBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mBinding.recyclerViewYes.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.recyclerViewNo.setLayoutManager(new GridLayoutManager(mContext, 4));

        existAdapter = new ExistAdapter(R.layout.item_exist, null);
        noExistAdapter = new NoExistAdapter(R.layout.item_noexist, null);
        mBinding.recyclerViewYes.setAdapter(existAdapter);
        mBinding.recyclerViewNo.setAdapter(noExistAdapter);
        getDateFromNet();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getDateFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("uuid", GLobleConstants.UUID);
        sendRequest(ManagementService.getInstance().navigatorList, params, false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        existAdapter.setOnChooseTabListener(new ExistAdapter.OnChooseTabListener() {
            @Override
            public void del(NavigatorVo.UsedModulesBean item) {
                existingList.remove(item);
                noExitingList.add(item);
                existAdapter.notifyDataSetChanged();
                noExistAdapter.notifyDataSetChanged();
            }
        });
        noExistAdapter.setOnChooseTabListener(new NoExistAdapter.OnChooseTabListener() {
            @Override
            public void add(NavigatorVo.UsedModulesBean item) {
                if (existingList.size() < 7) {
                    existingList.add(item);
                    noExitingList.remove(item);
                    existAdapter.notifyDataSetChanged();
                    noExistAdapter.notifyDataSetChanged();
                } else {
                    showShortToast("最多选择7个标签");
                }
            }
        });
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "NavigatorList":
                if (head.isSuccess()) {
                    try {
                        navigator = (NavigatorVo) response;
                    } catch (Exception e) {
                        navigator = null;
                        e.printStackTrace();
                    }

                    if (null == navigator)
                        return;
                    existingList = navigator.getUsedModules();
                    List<NavigatorVo.NotUsedModulesBean> notUsedModules = navigator.getNotUsedModules();
                    for (int i = 0; i < notUsedModules.size(); i++) {
                        NavigatorVo.UsedModulesBean usedModulesBean = new NavigatorVo.UsedModulesBean();
                        usedModulesBean.setImageName(notUsedModules.get(i).getImageName());
                        usedModulesBean.setName(notUsedModules.get(i).getName());
                        usedModulesBean.setCode(notUsedModules.get(i).getCode());
                        usedModulesBean.setCreateTime(notUsedModules.get(i).getCreateTime());
                        usedModulesBean.setId(notUsedModules.get(i).getId());
                        usedModulesBean.setIsDefault(notUsedModules.get(i).getIsDefault() + "");
                        usedModulesBean.setUpdateTime(notUsedModules.get(i).getUpdateTime());
                        noExitingList.add(usedModulesBean);
                    }
                    for (int i = 0; i < existingList.size(); i++) {
                        existingList.get(i).setIsShow("1");
                    }
                    existAdapter.setNewData(existingList);
                    noExistAdapter.setNewData(noExitingList);
                }
                break;
            case "SaveNavigatorList":

                break;
        }
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickBack() {
            AppManager.getAppManager().finishActivity();
        }

        public void onClickBw() {
            String text = mBinding.tvbw.getText().toString();
            if ("编辑".equals(text)) {
                if (existAdapter != null) {
                    existAdapter.showChoose(1);
                    noExistAdapter.showChoose(1);
                    mBinding.tvbw.setText("完成");
                    existAdapter.notifyDataSetChanged();
                    noExistAdapter.notifyDataSetChanged();
                }
            } else if ("完成".equals(text)) {
                if (noExistAdapter != null) {
                    existAdapter.showChoose(2);
                    noExistAdapter.showChoose(2);
                    mBinding.tvbw.setText("编辑");
                    existAdapter.notifyDataSetChanged();
                    noExistAdapter.notifyDataSetChanged();

                    existStr = new Gson().toJson(existingList);
                    noExitStr = new Gson().toJson(noExitingList);

                    for (int i = 0; i < existingList.size(); i++) {
                        list1.add(existingList.get(i).getCode());
                    }
                    for (int i = 0; i < noExitingList.size(); i++) {
                        list2.add(noExitingList.get(i).getCode());
                    }

                    String s1 = list1.toString().substring(1, list1.toString().lastIndexOf("]")).replaceAll(", ", ",");
                    String s2 = list2.toString().substring(1, list2.toString().lastIndexOf("]")).replaceAll(", ", ",");

                    //保存用户设置
                    saveUserTab(s1, s2);
                }
            }
        }
    }

    private void saveUserTab(String s1, String s2) {
        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(User.getInstance().getToken())) {
            params.put("token", "");
        } else {
            params.put("token", User.getInstance().getToken());
        }
        params.put("uuid", GLobleConstants.UUID);
        params.put("usedModuleCodes", s1);
        params.put("notUsedModuleCodes", s2);
        sendRequest(ManagementService.getInstance().saveNavigatorList, params, false);
    }
}

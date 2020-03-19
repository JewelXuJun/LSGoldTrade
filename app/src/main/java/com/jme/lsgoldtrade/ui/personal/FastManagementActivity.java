package com.jme.lsgoldtrade.ui.personal;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.heaven7.android.dragflowlayout.DragAdapter;
import com.heaven7.android.dragflowlayout.IDraggable;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityFastManagementBinding;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.IntentUtils;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * 快捷入口管理
 */
@Route(path = Constants.ARouterUriConst.FASTMANAGEMENT)
public class FastManagementActivity extends JMEBaseActivity {

    private ActivityFastManagementBinding mBinding;

    private boolean bEditFlag = false;
    private int mScreenWidth;
    private Subscription mRxbus;
    private int mCallEntry = 0;
    private ConfirmSimplePopupwindow mTradingPasswordConfirmSimplePopupwindow;

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


        mTradingPasswordConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mTradingPasswordConfirmSimplePopupwindow.setOutsideTouchable(false);
        mTradingPasswordConfirmSimplePopupwindow.setFocusable(false);
        getScreenWidth();
    }

    @Override
    protected void initListener() {
        super.initListener();
        initRxBus();
        mBinding.dragFlowLayoutAdded.setOnItemClickListener((dragFlowLayout, child, event, dragState) -> {
            FastTabBean fastTabBean = (FastTabBean) child.getTag();

            if (null != fastTabBean)
                IntentUtils.IntentFastTab(this, fastTabBean.mCode);

            return true;
        });

        mBinding.dragFlowLayoutNotAdded.setOnItemClickListener((dragFlowLayout, child, event, dragState) -> {
            FastTabBean fastTabBean = (FastTabBean) child.getTag();

            if (null != fastTabBean)
                IntentUtils.IntentFastTab(this, fastTabBean.mCode);

            return true;
        });

        mBinding.dragFlowLayoutAdded.setDragAdapter(new DragAdapter<FastTabBean>() {
            @Override
            public int getItemLayoutId() {
                return R.layout.item_added;
            }

            @Override
            public void onBindData(View itemView, int dragState, FastTabBean fastTabBean) {
                itemView.setTag(fastTabBean);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(DensityUtil.dpTopx(FastManagementActivity.this, 5), DensityUtil.dpTopx(FastManagementActivity.this, 5),
                        DensityUtil.dpTopx(FastManagementActivity.this, 5), DensityUtil.dpTopx(FastManagementActivity.this, 5));
                layoutParams.width = (mScreenWidth - DensityUtil.dpTopx(FastManagementActivity.this, 20)) / 4 - DensityUtil.dpTopx(FastManagementActivity.this, 10);
                layoutParams.height = DensityUtil.dpTopx(FastManagementActivity.this, 80);

                RelativeLayout layout = itemView.findViewById(R.id.layout);
                ImageView img = itemView.findViewById(R.id.img);
                TextView tv_tab_name = itemView.findViewById(R.id.tv_tab_name);
                LinearLayout layout_delete = itemView.findViewById(R.id.layout_delete);

                layout.setLayoutParams(layoutParams);
                Picasso.with(mContext)
                        .load(fastTabBean.mImgUrl)
                        .placeholder(R.mipmap.ic_img_default)
                        .error(R.mipmap.ic_img_default)
                        .into(img);
                tv_tab_name.setText(fastTabBean.mTab);
                tv_tab_name.setTextSize(fastTabBean.mTab.length() > 4 ? 12 : 13);
                layout_delete.setVisibility(fastTabBean.bShowOperation ? View.VISIBLE : View.GONE);

                layout_delete.setOnClickListener((view) -> {
                    List<FastTabBean> addedFastTabBeanList = mBinding.dragFlowLayoutAdded.getDragItemManager().getItems();

                    if (null != addedFastTabBeanList) {
                        int size = addedFastTabBeanList.size();

                        if (size > 4) {
                            mBinding.dragFlowLayoutAdded.getDragItemManager().removeItem(fastTabBean);
                            mBinding.dragFlowLayoutNotAdded.getDragItemManager().addItem(fastTabBean);
                        } else {
                            showShortToast(R.string.personal_fast_management_length_less);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public FastTabBean getData(View itemView) {
                return (FastTabBean) itemView.getTag();
            }
        });

        mBinding.dragFlowLayoutNotAdded.setDragAdapter(new DragAdapter<FastTabBean>() {
            @Override
            public int getItemLayoutId() {
                return R.layout.item_not_added;
            }

            @Override
            public void onBindData(View itemView, int dragState, FastTabBean fastTabBean) {
                itemView.setTag(fastTabBean);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(DensityUtil.dpTopx(FastManagementActivity.this, 5), DensityUtil.dpTopx(FastManagementActivity.this, 5),
                        DensityUtil.dpTopx(FastManagementActivity.this, 5), DensityUtil.dpTopx(FastManagementActivity.this, 5));
                layoutParams.width = (mScreenWidth - DensityUtil.dpTopx(FastManagementActivity.this, 20)) / 4 - DensityUtil.dpTopx(FastManagementActivity.this, 10);
                layoutParams.height = DensityUtil.dpTopx(FastManagementActivity.this, 80);

                RelativeLayout layout = itemView.findViewById(R.id.layout);
                ImageView img = itemView.findViewById(R.id.img);
                TextView tv_tab_name = itemView.findViewById(R.id.tv_tab_name);
                LinearLayout layout_add = itemView.findViewById(R.id.layout_add);

                layout.setLayoutParams(layoutParams);
                Picasso.with(mContext)
                        .load(fastTabBean.mImgUrl)
                        .placeholder(R.mipmap.ic_img_default)
                        .error(R.mipmap.ic_img_default)
                        .into(img);
                tv_tab_name.setText(fastTabBean.mTab);
                layout_add.setVisibility(fastTabBean.bShowOperation ? View.VISIBLE : View.GONE);

                layout_add.setOnClickListener((view) -> {
                    List<FastTabBean> addedFastTabBeanList = mBinding.dragFlowLayoutAdded.getDragItemManager().getItems();

                    if (null != addedFastTabBeanList) {
                        int size = addedFastTabBeanList.size();

                        if (size < 7) {
                            mBinding.dragFlowLayoutAdded.getDragItemManager().addItem(fastTabBean);
                            mBinding.dragFlowLayoutNotAdded.getDragItemManager().removeItem(fastTabBean);
                        } else {
                            showShortToast(R.string.personal_fast_management_length);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public FastTabBean getData(View itemView) {
                return (FastTabBean) itemView.getTag();
            }
        });

        mBinding.dragFlowLayoutAdded.prepareItemsByCount(4);
        mBinding.dragFlowLayoutNotAdded.prepareItemsByCount(9);

        getNavigatorList();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityFastManagementBinding) mBindingUtil;
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_ZJHZ_SETPASSWORD:
                    mCallEntry = 1;
                    getUserPasswordSettingInfo();
                    break;
                case Constants.RxBusConst.RXBUS_ZJHZ_SETPASSWORD_SUCCESS:
                    User user = User.getInstance();
                    if (!TextUtils.isEmpty(user.getIsFromTjs()) && user.getIsFromTjs().equals("true")) {
                        if (user.getCurrentUser().getCardType().equals("2") && user.getCurrentUser().getReserveFlag().equals("N"))
                            ARouter.getInstance().build(Constants.ARouterUriConst.BANKRESERVE).navigation();
                        else
                            ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();
                    } else {
                        ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();
                    }
                    break;
                case Constants.RxBusConst.RXBUS_WDDY_SETPASSWORD:
                    if (isForeground()) {
                        mCallEntry = 2;
                        getUserPasswordSettingInfo();
                    }

                    break;
                case Constants.RxBusConst.RXBUS_WDDY_SETPASSWORD_SUCCESS:
                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();

                    break;
                case Constants.RxBusConst.RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD:
                    if (isForeground()) {
                        mCallEntry = 5;
                        getUserPasswordSettingInfo();
                    }
                    break;
                case Constants.RxBusConst.RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD_SUCCESS:
                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();

                    break;
                case Constants.RxBusConst.RXBUS_CJRL_SETPASSWORD:
                    if (isForeground()) {
                        mCallEntry = 6;
                        getUserPasswordSettingInfo();
                    }
                    break;
                case Constants.RxBusConst.RXBUS_CJRL_SETPASSWORD_SUCCESS:
                    ARouter.getInstance().build(Constants.ARouterUriConst.ECONOMICCALENDAR).navigation();

                    break;
                case Constants.RxBusConst.RXBUS_HQYP_SETPASSWORD:
                    if (isForeground()) {
                        mCallEntry = 7;
                        getUserPasswordSettingInfo();
                    }
                    break;
                case Constants.RxBusConst.RXBUS_HQYP_SETPASSWORD_SUCCESS:
                    ARouter.getInstance().build(Constants.ARouterUriConst.MARKETJUDGMENT).navigation();

                    break;
            }
        });
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true, false, false);
    }

    private void doEdit() {
        if (bEditFlag) {
            setRightNavigation(getString(R.string.personal_fast_management_edit), 0, R.style.ToolbarThemeBlue, () -> doEdit());

            bEditFlag = false;

            List<FastTabBean> addedFastTabBeanList = mBinding.dragFlowLayoutAdded.getDragItemManager().getItems();
            List<FastTabBean> notAddedFastTabBeanList = mBinding.dragFlowLayoutNotAdded.getDragItemManager().getItems();

            if (null != addedFastTabBeanList) {
                for (int i = 0; i < addedFastTabBeanList.size(); i++) {
                    FastTabBean fastTabBean = addedFastTabBeanList.get(i);

                    if (null != fastTabBean) {
                        fastTabBean.bShowOperation = false;
                        fastTabBean.bDraggable = false;

                        mBinding.dragFlowLayoutAdded.getDragItemManager().updateItem(i, fastTabBean);
                    }
                }
            }

            if (null != notAddedFastTabBeanList) {
                for (int i = 0; i < notAddedFastTabBeanList.size(); i++) {
                    FastTabBean fastTabBean = notAddedFastTabBeanList.get(i);

                    if (null != fastTabBean) {
                        fastTabBean.bShowOperation = false;
                        fastTabBean.bDraggable = false;

                        mBinding.dragFlowLayoutNotAdded.getDragItemManager().updateItem(i, fastTabBean);
                    }
                }
            }

            saveNavigatorList(getAddValues(), getNotAddValues());
        } else {
            setRightNavigation(getString(R.string.personal_fast_management_complete), 0, R.style.ToolbarThemeBlue, () -> doEdit());

            bEditFlag = true;

            List<FastTabBean> addedFastTabBeanList = mBinding.dragFlowLayoutAdded.getDragItemManager().getItems();
            List<FastTabBean> notAddedFastTabBeanList = mBinding.dragFlowLayoutNotAdded.getDragItemManager().getItems();

            if (null != addedFastTabBeanList) {
                for (int i = 0; i < addedFastTabBeanList.size(); i++) {
                    FastTabBean fastTabBean = addedFastTabBeanList.get(i);

                    if (null != fastTabBean) {
                        fastTabBean.bShowOperation = true;
                        fastTabBean.bDraggable = true;

                        mBinding.dragFlowLayoutAdded.getDragItemManager().updateItem(i, fastTabBean);
                    }
                }
            }

            if (null != notAddedFastTabBeanList) {
                for (int i = 0; i < notAddedFastTabBeanList.size(); i++) {
                    FastTabBean fastTabBean = notAddedFastTabBeanList.get(i);

                    if (null != fastTabBean) {
                        fastTabBean.bShowOperation = true;
                        fastTabBean.bDraggable = true;

                        mBinding.dragFlowLayoutNotAdded.getDragItemManager().updateItem(i, fastTabBean);
                    }
                }
            }

        }

    }

    private String getAddValues() {
        String addValues = "";

        List<FastTabBean> addedFastTabBeanList = mBinding.dragFlowLayoutAdded.getDragItemManager().getItems();

        if (null != addedFastTabBeanList) {
            for (FastTabBean fastTabBean : addedFastTabBeanList) {
                if (null != fastTabBean)
                    addValues = addValues + fastTabBean.mCode + ",";
            }
        }

        if (addValues.endsWith(","))
            addValues = addValues.substring(0, addValues.length() - 1);

        return addValues;
    }

    private String getNotAddValues() {
        String notAddValues = "";

        List<FastTabBean> notAddedFastTabBeanList = mBinding.dragFlowLayoutNotAdded.getDragItemManager().getItems();

        if (null != notAddedFastTabBeanList) {
            for (FastTabBean fastTabBean : notAddedFastTabBeanList) {
                if (null != fastTabBean)
                    notAddValues = notAddValues + fastTabBean.mCode + ",";
            }
        }

        if (notAddValues.endsWith(","))
            notAddValues = notAddValues.substring(0, notAddValues.length() - 1);

        return notAddValues;
    }

    private void getScreenWidth() {
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        mScreenWidth = displayMetrics.widthPixels;
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

                    List<NavigatorVo.NavigatorVoBean> usedModulesBeanList = navigatorVo.getUsedModules();
                    List<NavigatorVo.NavigatorVoBean> notUsedModulesBeanList = navigatorVo.getNotUsedModules();

                    if (null != usedModulesBeanList && usedModulesBeanList.size() > 0) {
                        for (int i = 0; i < usedModulesBeanList.size(); i++) {
                            NavigatorVo.NavigatorVoBean navigatorVoBean = usedModulesBeanList.get(i);

                            if (null != navigatorVoBean && !navigatorVoBean.getCode().equals("DRCC"))
                                mBinding.dragFlowLayoutAdded.getDragItemManager().addItem(
                                        new FastTabBean(navigatorVoBean.getName(), navigatorVoBean.getImageName(), navigatorVoBean.getCode(), false, false));
                        }
                    }

                    if (null != notUsedModulesBeanList && notUsedModulesBeanList.size() > 0) {
                        for (int i = 0; i < notUsedModulesBeanList.size(); i++) {
                            NavigatorVo.NavigatorVoBean navigatorVoBean = notUsedModulesBeanList.get(i);

                            if (null != navigatorVoBean && !navigatorVoBean.getCode().equals("DRCC"))
                                mBinding.dragFlowLayoutNotAdded.getDragItemManager().addItem(
                                        new FastTabBean(navigatorVoBean.getName(), navigatorVoBean.getImageName(), navigatorVoBean.getCode(), false, false));
                        }
                    }

                }

                break;
            case "SaveNavigatorList":
                if (head.isSuccess()) {
                    showShortToast(R.string.personal_fast_management_edit_success);

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_FAST_MANAGEMENT_EDIT, null);
                }

                break;

            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasTimeout = passwordInfoVo.getHasTimeout();
                    String hasSettingDigital = passwordInfoVo.getHasSettingDigital();
                    String hasOpenFingerPrint = passwordInfoVo.getHasOpenFingerPrint();
                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();
                    if (TextUtils.isEmpty(hasSettingDigital) || hasSettingDigital.equals("N")) {
                        if (null != mTradingPasswordConfirmSimplePopupwindow && !mTradingPasswordConfirmSimplePopupwindow.isShowing()) {
                            mTradingPasswordConfirmSimplePopupwindow.setData(mContext.getResources().getString(R.string.security_setting_tips),
                                    mContext.getResources().getString(R.string.personal_setting),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDSETTING).navigation();

                                        mTradingPasswordConfirmSimplePopupwindow.dismiss();
                                    });
                            mTradingPasswordConfirmSimplePopupwindow.showAtLocation(mBinding.getRoot(), Gravity.CENTER, 0, 0);
                        }
                    } else {

                        if (TextUtils.isEmpty(hasTimeout) || hasTimeout.equals("N")) {
                            if (mCallEntry == 1) {
                                //资金划转
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_ZJHZ_SETPASSWORD_SUCCESS, null);
                            } else if (mCallEntry == 2) {
                                //首页过来的 我的订阅
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_WDDY_SETPASSWORD_SUCCESS, null);
                            } else if (mCallEntry == 5) {
                                //首页进入交易匣子
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MAIN_PAGE_TRAIN_BOX_SETPASSWORD_SUCCESS, null);
                            } else if (mCallEntry == 6) {
                                //首页进入 财金日历
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CJRL_SETPASSWORD_SUCCESS, null);
                            } else if (mCallEntry == 7) {
                                //首页进入 行情研判
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_HQYP_SETPASSWORD_SUCCESS, null);
                            }

                            return;
                        }


                        int type = 1;
                        if (!TextUtils.isEmpty(hasOpenFingerPrint) && hasOpenFingerPrint.equals("Y")) {
                            boolean isCanUseFingerPrint = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (FingerprintManagerCompat.from(mContext).isHardwareDetected()
                                        && FingerprintManagerCompat.from(mContext).hasEnrolledFingerprints())
                                    isCanUseFingerPrint = true;
                            }

                            if (isCanUseFingerPrint) {
                                type = 2;
                            } else {
                                if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y"))
                                    type = 3;
                                else
                                    type = 1;
                            }
                        } else if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y")) {
                            type = 3;
                        } else if (passwordInfoVo.getHasTimeout().equals("Y")) {
                            type = 1;
                        }
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
                                .withInt("Type", type)
                                .withInt("callEntry", mCallEntry)
                                .navigation();
                    }
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private static class FastTabBean implements IDraggable {
        private String mTab;
        private String mImgUrl;
        private String mCode;
        private boolean bShowOperation;
        private boolean bDraggable;

        public FastTabBean(String tab, String imgUrl, String code, boolean showOperation, boolean draggable) {
            mTab = tab;
            mImgUrl = imgUrl;
            mCode = code;
            bShowOperation = showOperation;
            bDraggable = draggable;
        }

        @Override
        public boolean isDraggable() {
            return bDraggable;
        }

    }

}

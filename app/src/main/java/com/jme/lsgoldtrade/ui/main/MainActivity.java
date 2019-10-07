package com.jme.lsgoldtrade.ui.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.AppInfoUtil;
import com.jme.common.util.DialogHelp;
import com.jme.common.util.FileUtil;
import com.jme.common.util.NetStateReceiver;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEApplication;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMainBinding;
import com.jme.lsgoldtrade.domain.IdentityInfoVo;
import com.jme.lsgoldtrade.domain.ProtocolVo;
import com.jme.lsgoldtrade.domain.UpdateInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.tabhost.MainTab;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * Created by XuJun on 2018/11/7.
 */
@Route(path = Constants.ARouterUriConst.MAIN)
public class MainActivity extends JMEBaseActivity implements TabHost.OnTabChangeListener {

    private ActivityMainBinding mBinding;

    private long exitTime = 0;
    private boolean mUpdateFlag = false;
    private String mVersion;
    private String mDesc;
    private String mUrl;
    private String mValues;

    private ProgressDialog mProgressDialog;
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private UpdateDialog mDialog;
    private UpdateDialog mForceDialog;
    private ProtocolUpdatePopUpWindow mProtocolUpdatePopUpWindow;
    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;
    private IntentFilter mIntentFilter;
    private NetStateReceiver mStateReceiver;
    private Subscription mRxbus;

    private List<ProtocolVo> mProtocolVoList;

    private static final int MSG_DOWNLOAD_ERROR = 1;
    private final static int REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE = 121;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_DOWNLOAD_ERROR:
                    showShortToast(R.string.version_download_error);

                    if (mUpdateFlag == false) {
                        if (!isFinishing && Constants.DownLoadValues.IsDownLoadDialogShow) {
                            mDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_update),
                                    mUpdateFlag, (view) -> {
                                mDialog.dismiss();

                                if (NetWorkUtils.isNetWorkConnected(MainActivity.this)) {
                                    deleteFile();
                                    checkPermission();
                                } else {
                                    showShortToast(R.string.version_network_error);
                                }
                            });

                            if (null != mDialog && !mDialog.isShowing())
                                mDialog.show();
                        }
                    } else {
                        if (!isFinishing) {
                            if (null == mForceDialog)
                                mForceDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_update),
                                        mUpdateFlag, (view) -> {
                                    mForceDialog.dismiss();

                                    if (NetWorkUtils.isNetWorkConnected(MainActivity.this)) {
                                        deleteFile();
                                        checkPermission();
                                    } else {
                                        showShortToast(R.string.version_network_error);
                                    }
                                });

                            if (null != mForceDialog && !mForceDialog.isShowing())
                                mForceDialog.show();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        setTabHost();

        if (!TextUtils.isEmpty(SharedPreUtils.getString(mContext, SharedPreUtils.Token)))
            getProtocolVersion();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mStateReceiver = new NetStateReceiver();

        mProtocolUpdatePopUpWindow = new ProtocolUpdatePopUpWindow(this);
        mProtocolUpdatePopUpWindow.setOutsideTouchable(false);
        mProtocolUpdatePopUpWindow.setFocusable(false);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(true);
        mConfirmSimplePopupwindow.setFocusable(true);

        registerReceiver(mStateReceiver, mIntentFilter);
        initDownLoadData();

        if (null != mUser && mUser.isLogin())
            checkUserIsTJS();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.tabhost.setOnTabChangedListener(this);

        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityMainBinding) mBindingUtil;
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER:
                case Constants.RxBusConst.RXBUS_CANCELORDERFRAGMENT:
                case Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS:
                    runOnUiThread(() -> mBinding.tabhost.setCurrentTab(2));

                    break;
                case Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS:
                    runOnUiThread(() -> mBinding.tabhost.setCurrentTab(0));

                    break;
                case Constants.RxBusConst.RXBUS_LOGIN_SUCCESS:
                    getProtocolVersion();

                    if (null != mUser && mUser.isLogin() && !TextUtils.isEmpty(mUser.getIsFromTjs()) && mUser.getIsFromTjs().equals("true"))
                        showElectronicCardPopupWindow();

                    break;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mProgressDialog && !mProgressDialog.isShowing())
            getUpDateInfo();

        getTimeLineList();
    }

    private void setTabHost() {
        mBinding.tabhost.setup(this, getSupportFragmentManager(), R.id.fragmentlayout);
        mBinding.tabhost.getTabWidget().setShowDividers(0);

        initTabs();

        mBinding.tabhost.setCurrentTab(0);
    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();

        int size = tabs.length;

        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mBinding.tabhost.newTabSpec(getString(mainTab.getName()));
            View indicator = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_indicator, null);
            TextView title = indicator.findViewById(R.id.tab_title);
            Drawable drawable = ContextCompat.getDrawable(this, mainTab.getIcon());

            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            title.setText(getString(mainTab.getName()));
            tab.setIndicator(indicator);
            tab.setContent(tag -> new View(this));

            mBinding.tabhost.addTab(tab, mainTab.getClassRes(), null);
            mBinding.tabhost.setTag(i);
        }

        mBinding.tabhost.iniIndexFragment(2);
    }

    @Override
    public void onTabChanged(String tabId) {
        int size = mBinding.tabhost.getTabWidget().getTabCount();

        for (int i = 0; i < size; i++) {
            View v = mBinding.tabhost.getTabWidget().getChildAt(i);

            v.setSelected(i == mBinding.tabhost.getCurrentTab() ? true : false);
        }

        supportInvalidateOptionsMenu();
    }

    private void showElectronicCardPopupWindow() {
        if (null != mUser && null != mUser.getCurrentUser()
                && mUser.getCurrentUser().getCardType().equals("2") && mUser.getCurrentUser().getReserveFlag().equals("N")) {
            if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                mConfirmSimplePopupwindow.setData(getResources().getString(R.string.trade_transfer_icbc_electronic_card_message),
                        (view) -> {
                            mConfirmSimplePopupwindow.dismiss();

                            ARouter.getInstance().build(Constants.ARouterUriConst.BANKRESERVE).navigation();
                        });
                mConfirmSimplePopupwindow.showAtLocation(mBinding.tabhost, Gravity.CENTER, 0, 0);
            }
        }
    }

    private void initDownLoadData() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.DownLoadValues.DownLoading);
        filter.addAction(Constants.DownLoadValues.DownLoadError);
        filter.addAction(Constants.DownLoadValues.DownLoadSuccess);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == mProgressDialog)
                    return;

                if (!mProgressDialog.isShowing())
                    return;

                if (intent.getAction().equals(Constants.DownLoadValues.DownLoadSuccess)) {
                    mProgressDialog.dismiss();

                    installApk();
                } else if (intent.getAction().equals(Constants.DownLoadValues.DownLoadError)) {
                    mProgressDialog.dismiss();

                    handler.sendEmptyMessage(MSG_DOWNLOAD_ERROR);
                } else {
                    final int max = intent.getIntExtra("All", 0);
                    final int total = intent.getIntExtra("Total", 0);

                    mProgressDialog.setMax(max);
                    mProgressDialog.setProgress(total);
                    mProgressDialog.setProgressNumberFormat(String.format("%.2fM/%.2fM", (float) (total / (1024.00 * 1024.00)), (float) (max / (1024.00 * 1024.00))));
                }
            }
        };

        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    private void checkStatus() {
        if (FileUtil.IsAppFileExists("TjsApp" + mVersion, getApplicationContext())) {
            long size = FileUtil.getAppFileSize("TjsApp" + mVersion, getApplicationContext());
            long saveSize = SharedPreUtils.getInteger(this, SharedPreUtils.Key_DownLoad_Size, 0);

            if (size != 0 && saveSize != 0 && size == saveSize) {
                if (mUpdateFlag == true) {
                    if (!isFinishing) {
                        if (null == mForceDialog)
                            mForceDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_install),
                                    mUpdateFlag, (view) -> {
                                mForceDialog.dismiss();

                                installApk();
                            });

                        mForceDialog.setUpdateButton(getString(R.string.version_install), (view) -> {
                            mForceDialog.dismiss();

                            installApk();
                        });

                        if (null != mForceDialog && !mForceDialog.isShowing())
                            mForceDialog.show();
                    }
                } else {
                    if (!isFinishing && Constants.DownLoadValues.IsDownLoadDialogShow) {
                        mDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_install),
                                mUpdateFlag, (view) -> {
                            mDialog.dismiss();

                            installApk();
                        });

                        if (null != mDialog && !mDialog.isShowing())
                            mDialog.show();
                    }
                }
            } else {
                deleteFile();

                if (mUpdateFlag == true) {
                    if (!isFinishing) {
                        if (null == mForceDialog)
                            mForceDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_update),
                                    mUpdateFlag, (view) -> {
                                mForceDialog.dismiss();

                                if (NetWorkUtils.isNetWorkConnected(this))
                                    checkPermission();
                                else
                                    showShortToast(R.string.version_network_error);
                            });

                        if (null != mForceDialog && !mForceDialog.isShowing())
                            mForceDialog.show();
                    }
                } else {
                    if (!isFinishing && Constants.DownLoadValues.IsDownLoadDialogShow) {
                        mDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_update),
                                mUpdateFlag, (view) -> {
                            mDialog.dismiss();

                            if (NetWorkUtils.isNetWorkConnected(this))
                                checkPermission();
                            else
                                showShortToast(R.string.version_network_error);
                        });

                        if (null != mDialog && !mDialog.isShowing())
                            mDialog.show();
                    }
                }
            }
        } else {
            if (mUpdateFlag == true) {
                if (!isFinishing) {
                    if (null == mForceDialog)
                        mForceDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_update),
                                mUpdateFlag, (view) -> {
                            mForceDialog.dismiss();

                            if (NetWorkUtils.isNetWorkConnected(this))
                                checkPermission();
                            else
                                showShortToast(R.string.version_network_error);
                        });

                    if (null != mForceDialog && !mForceDialog.isShowing())
                        mForceDialog.show();
                }
            } else {
                if (!isFinishing && Constants.DownLoadValues.IsDownLoadDialogShow) {
                    mDialog = new UpdateDialog(MainActivity.this, mDesc, getString(R.string.version_update),
                            mUpdateFlag, (view) -> {
                        mDialog.dismiss();

                        if (NetWorkUtils.isNetWorkConnected(this))
                            checkPermission();
                        else
                            showShortToast(R.string.version_network_error);
                    });

                    if (null != mDialog && !mDialog.isShowing())
                        mDialog.show();
                }
            }
        }

        if (!mUpdateFlag)
            Constants.DownLoadValues.IsDownLoadDialogShow = false;
    }

    private void installApk() {
        File file;

        try {
            file = new File(getApplicationContext().getExternalCacheDir(), "TjsApp" + Constants.DownLoadValues.DownLoadVersion);
        } catch (Exception e) {
            file = null;

            e.printStackTrace();
        }

        long size = FileUtil.getAppFileSize("TjsApp" + mVersion, getApplicationContext());
        long saveSize = SharedPreUtils.getInteger(this, SharedPreUtils.Key_DownLoad_Size, 0);

        if (file != null && size != 0 && saveSize != 0 && size == saveSize) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setAction(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                intent.setDataAndType(FileProvider.getUriForFile(JMEApplication.getInstance(), getFileProviderAuthority(), file), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }

            if (getPackageManager().queryIntentActivities(intent, 0).size() > 0)
                startActivity(intent);
        } else {
            deleteFile();

            if (!isFinishing)
                DialogHelp.getConfirmDialog(this, getString(R.string.dialog_title), getString(R.string.version_error),
                        getString(R.string.text_confirm), (dialog, which) -> {
                            if (NetWorkUtils.isNetWorkConnected(this))
                                checkPermission();
                            else
                                showShortToast(R.string.version_network_error);
                        }, (dialog, which) -> {
                            if (mUpdateFlag == true)
                                finish();
                            else
                                dialog.dismiss();
                        }).show();
        }
    }

    private void deleteFile() {
        try {
            FileUtil.cleanCustomCache(new File(getApplicationContext().getExternalCacheDir(), "TjsApp" + Constants.DownLoadValues.DownLoadVersion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        if (NetWorkUtils.isNetWorkConnected(this)) {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE);

                    return;
                } else {
                    downLoadApk();
                }
            } else {
                downLoadApk();
            }
        }
    }

    private String getFileProviderAuthority() {
        try {
            for (ProviderInfo provider : getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PROVIDERS).providers) {
                if (FileProvider.class.getName().equals(provider.name) && provider.authority.endsWith("com.jme.lsgoldtrade.file_provider")) {
                    return provider.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {

        }
        return null;
    }

    private void downLoadApk() {
        if (null != mProgressDialog && mProgressDialog.isShowing())
            return;

        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.version_download_progress));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Intent intent = new Intent(this, DownLoadService.class);
        intent.putExtra("Path", mUrl);
        intent.putExtra("VerTxt", mVersion);

        startService(intent);
    }

    private void getUpDateInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("code", String.valueOf(AppInfoUtil.getVersionCode(this)));

        sendRequest(ManagementService.getInstance().getVersionInfo, params, false, false, false);
    }

    private void getTimeLineList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", AppConfig.UUID);
        params.put("token", mUser.getToken());

        sendRequest(ManagementService.getInstance().timeLineList, params, false, false, false);
    }

    private void getProtocolVersion() {
        sendRequest(ManagementService.getInstance().getProtocolVersion, new HashMap<>(), false, false, false);
    }

    private void getWhetherIdCard() {
        sendRequest(TradeService.getInstance().whetherIdCard, new HashMap<>(), false, false, false);
    }

    private void insertRatifyAccord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", mUser.getToken());
        params.put("value", mValues);

        sendRequest(ManagementService.getInstance().insertRatifyAccord, params, true);
    }

    private void checkUserIsTJS() {
        sendRequest(TradeService.getInstance().checkUserIsTJS, new HashMap<>(), false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetVersionInfo":
                if (head.isSuccess()) {
                    UpdateInfoVo updateInfoVo;

                    try {
                        updateInfoVo = (UpdateInfoVo) response;
                    } catch (Exception e) {
                        updateInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == updateInfoVo) {
                        Constants.DownLoadValues.IsNeedDownLoad = false;

                        return;
                    } else {
                        String force = updateInfoVo.getForce();

                        if (force.equals("1")) {
                            Constants.DownLoadValues.IsNeedDownLoad = false;
                        } else {
                            mUpdateFlag = force.equals("3");
                            mVersion = updateInfoVo.getId();
                            mDesc = updateInfoVo.getUpdateContent();

                            String url = updateInfoVo.getDownloadUrl();

                            if (TextUtils.isEmpty(url)) {
                                Constants.DownLoadValues.IsNeedDownLoad = false;

                                return;
                            } else {
                                mUrl = url;

                                Constants.DownLoadValues.IsNeedDownLoad = true;
                                Constants.DownLoadValues.DownLoadUrl = mUrl;
                                Constants.DownLoadValues.DownLoadVersion = mVersion;

                                checkStatus();

                                SharedPreUtils.setLong(this, SharedPreUtils.Key_Close_Time, System.currentTimeMillis());
                            }
                        }
                    }
                }

                break;
            case "TimeLineList":
                if (head.isSuccess()) {
                    String value = "";

                    if (null != response)
                        value = response.toString();

                    SharedPreUtils.setString(this, mUser.isLogin() ? SharedPreUtils.MARKET_SORT_LOGIN : SharedPreUtils.MARKET_SORT_UNLOGIN, value);
                }

                break;
            case "GetProtocolVersion":
                if (head.isSuccess()) {
                    try {
                        mProtocolVoList = (List<ProtocolVo>) response;
                    } catch (Exception e) {
                        mProtocolVoList = null;

                        e.printStackTrace();
                    }

                    if (null == mProtocolVoList || 0 == mProtocolVoList.size())
                        return;

                    mValues = "";

                    for (ProtocolVo protocolVo : mProtocolVoList) {
                        if (null != protocolVo) {
                            ProtocolVo.ZprotocolTypeBean zprotocolTypeBean = protocolVo.getZprotocolTypeList();

                            if (null != zprotocolTypeBean) {
                                mValues = mValues + zprotocolTypeBean.getProtocolVersion() + ",";
                            }
                        }
                    }

                    if (mValues.endsWith(","))
                        mValues = mValues.substring(0, mValues.length() - 1);

                    getWhetherIdCard();
                }

                break;
            case "WhetherIdCard":
                if (head.isSuccess()) {
                    IdentityInfoVo identityInfoVo;

                    try {
                        identityInfoVo = (IdentityInfoVo) response;
                    } catch (Exception e) {
                        identityInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == identityInfoVo)
                        return;

                    String flag = identityInfoVo.getFlag();

                    if (flag.equals("Y")) {
                        if (null != mProtocolUpdatePopUpWindow && !mProtocolUpdatePopUpWindow.isShowing()) {
                            mProtocolUpdatePopUpWindow.setData(mProtocolVoList, identityInfoVo.getName(), identityInfoVo.getIdCard(), (view) -> insertRatifyAccord());
                            mProtocolUpdatePopUpWindow.showAtLocation(mBinding.tabhost, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
            case "InsertRatifyAccord":
                if (head.isSuccess()) {
                    if (null != mProtocolUpdatePopUpWindow && mProtocolUpdatePopUpWindow.isShowing())
                        mProtocolUpdatePopUpWindow.dismiss();

                    showShortToast(R.string.main_protocol_update_success);
                }

                break;
            case "CheckUserIsTJS":
                if (head.isSuccess()) {
                    String value = "";

                    if (null != response)
                        value = response.toString();

                    mUser.setIsFromTjs(value);

                    if (!TextUtils.isEmpty(value) && value.equals("true"))
                        showElectronicCardPopupWindow();
                }

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar snackbar = Snackbar.make(mBinding.fragmentlayout, getString(R.string.main_exit_app), Snackbar.LENGTH_SHORT);
                snackbar.setAction(getString(R.string.text_cancel), v -> exitTime = 0)
                        .setActionTextColor(ContextCompat.getColor(this, R.color.white));
                View snakebarView = snackbar.getView();
                TextView textView = snakebarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();

                exitTime = System.currentTimeMillis();
            } else {
                finish();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                }.start();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();

        mLocalBroadcastManager.unregisterReceiver(mReceiver);
        unregisterReceiver(mStateReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE:
                if (0 != grantResults.length)
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        downLoadApk();

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mProtocolUpdatePopUpWindow && mProtocolUpdatePopUpWindow.isShowing())
            return false;

        return super.dispatchTouchEvent(ev);
    }

}

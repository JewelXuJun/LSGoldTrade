package com.jme.lsgoldtrade.ui.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.AppInfoUtil;
import com.jme.common.util.AppManager;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.common.util.ToastUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMainBinding;
import com.jme.lsgoldtrade.domain.UpdateInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.tabhost.MainTab;
import com.orhanobut.logger.Logger;
import com.jme.lsgoldtrade.util.DialogUtils;
import com.maning.updatelibrary.InstallUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

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

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMainBinding) mBindingUtil;

        setTabHost();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
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
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRADE:
                    runOnUiThread(() -> mBinding.tabhost.setCurrentTab(2));

                    break;
                case Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS:
                    runOnUiThread(() -> mBinding.tabhost.setCurrentTab(0));

                    break;
                case Constants.RxBusConst.RXBUS_CANCELORDERFRAGMENT:
                    runOnUiThread(() -> mBinding.tabhost.setCurrentTab(2));

                    break;
                case Constants.RxBusConst.RXBUS_TRADEFRAGMENT_HOLD:
                    runOnUiThread(() -> mBinding.tabhost.setCurrentTab(2));

                    break;
                case Constants.RxBusConst.RXBUS_CANCEL_MAIN:
                    int currentTab = mBinding.tabhost.getCurrentTab();

                    if (currentTab == 3)
                        showLoginDialog();

                    break;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetVersionInfo":
                if (head.isSuccess()) {
                    UpdateInfoVo value;
                    try {
                        value = (UpdateInfoVo) response;
                    } catch (Exception e) {
                        value = null;

                        e.printStackTrace();
                    }

                    if (null == value)
                        return;

                    String force = value.getForce();
                    if ("2".equals(force)) { //普通更新
                        isUpData(value);
                    } else if ("3".equals(force)) { //强制更新
                        isUpData(value);
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
    }

    private void isUpData(UpdateInfoVo value) {
        View v = View.inflate(this, R.layout.apk_updata_info, null);
        final Dialog dialog = new AlertDialog.Builder(this, R.style.dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        ImageView install_apk_cancel = (ImageView) v.findViewById(R.id.install_apk_cancel);
        TextView install_apk_sure = (TextView) v.findViewById(R.id.install_apk_sure);
        TextView tv_updata_app = (TextView) v.findViewById(R.id.tv_updata_app);
        if ("3".equals(value.getForce())) {
            //点击dialog外部不消失
            dialog.setCancelable(false);
            //还有另一种方法   待试
            //dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        } else if ("2".equals(value.getForce())) {
            //点击dialog外部消失
            dialog.setCancelable(true);
        }
        tv_updata_app.setText(value.getUpdateContent());
        install_apk_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(value.getForce())) {
                    dialog.dismiss();
                } else {
                    DialogUtils.alertTitleDialog(MainActivity.this, "确定", "取消",
                            "取消更新将无法使用", new DialogUtils.SetAlertDialogListener() {
                                @Override
                                public void onPositive() {
                                    AppManager.getAppManager().AppExit(mContext);
                                }

                                @Override
                                public void onNegative() {

                                }
                            });
                }
            }
        });
        install_apk_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] args = new String[]{Permission.READ_EXTERNAL_STORAGE};
                AndPermission.with(MainActivity.this)
                        .permission(args)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                loadApp(value.getDownloadUrl());
                                dialog.dismiss();
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(@NonNull List<String> permissions) {

                            }
                        })
                        .start();
            }
        });
    }

    /**
     * 下载app
     *
     * @param url 下载地址
     */
    private void loadApp(String url) {

        String APK_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MNUpdateAPK/sheyiapp.apk";

        //下载APK
        InstallUtils.with(this)
                //必须-下载地址
                .setApkUrl(url)
                //非必须-下载保存的路径
                .setApkPath(APK_SAVE_PATH)
                //非必须-下载回调
                .setCallBack(new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                        //下载开始
                        ToastUtils.setToast(MainActivity.this, "正在后台下载");
                    }

                    @Override
                    public void onComplete(final String path) {
                        Logger.e("下载完成");
                        InstallUtils.checkInstallPermission(MainActivity.this, new InstallUtils.InstallPermissionCallBack() {
                            @Override
                            public void onGranted() {
                                /**
                                 * 安装APK工具类
                                 * @param context       上下文
                                 * @param filePath      文件路径
                                 * @param callBack      安装界面成功调起的回调
                                 */
                                InstallUtils.installAPK(MainActivity.this, path, new InstallUtils.InstallCallBack() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                    }
                                });
                            }

                            @Override
                            public void onDenied() {
                                //弹出弹框提醒用户
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("温馨提示")
                                        .setMessage("必须授权才能安装APK，请设置允许安装")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //打开设置页面
                                                InstallUtils.openInstallPermissionSetting(MainActivity.this, new InstallUtils.InstallPermissionCallBack() {
                                                    @Override
                                                    public void onGranted() {
                                                        /**
                                                         * 安装APK工具类
                                                         * @param context       上下文
                                                         * @param filePath      文件路径
                                                         * @param callBack      安装界面成功调起的回调
                                                         */
                                                        InstallUtils.installAPK(MainActivity.this, path, new InstallUtils.InstallCallBack() {
                                                            @Override
                                                            public void onSuccess() {
                                                            }

                                                            @Override
                                                            public void onFail(Exception e) {
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onDenied() {
                                                        //还是不允许咋搞？
//                                                        Toast.makeText(context, "不允许安装咋搞？强制更新就退出应用程序吧！", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        })
                                        .create();
                                alertDialog.show();
                            }
                        });
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        //下载中
                        Logger.e("下载中");
                    }

                    @Override
                    public void onFail(Exception e) {
                        //下载失败
                        Logger.e("下载失败");
                    }

                    @Override
                    public void cancle() {
                        //下载取消
                        Logger.e("取消下载");
                    }
                })
                //开始下载
                .startDownload();
    }
}

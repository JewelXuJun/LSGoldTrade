package com.jme.lsgoldtrade.ui.personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEApplication;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityOpenIncrementBinding;
import com.jme.lsgoldtrade.service.WithholdAccountService;
import com.jme.lsgoldtrade.util.IntentUtils;
import com.jme.lsgoldtrade.view.ArrearsMinimalism;
import com.jme.lsgoldtrade.view.ArrearsPayDialog;
import com.jme.lsgoldtrade.view.CloseIncrementPopUpWindow;
import com.jme.lsgoldtrade.view.IncrementAlertDialog;
import com.jme.lsgoldtrade.view.LaterStageIncrenmentDialog;
import com.jme.lsgoldtrade.view.StockUserDialog;
import com.jme.lsgoldtrade.view.StockUserPopUpWindow;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.OPENINCREMENT)
public class OpenIncrementActivity extends JMEBaseActivity {
    private ActivityOpenIncrementBinding mBinding;
    private boolean bAgreeFlag = true;

    private IncrementAlertDialog mDialog;

    private CloseIncrementPopUpWindow mCloseIncrementPopUpWindow;
    private StockUserPopUpWindow mStockUserPopUpWindow;

//    private ArrearsMinimalism mDialog;
//    private ArrearsPayDialog mDialog;
//    private LaterStageIncrenmentDialog mDialog;
//    private StockUserDialog mStockUserDialog;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_open_increment;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.open_increment_title, true);

        mBinding.checkboxOpenIncrementAgree.setChecked(true);

        setAggrementMessage();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);


//        mCloseIncrementPopUpWindow = new CloseIncrementPopUpWindow(this);
//        mStockUserPopUpWindow = new StockUserPopUpWindow(this);
//        mStockUserPopUpWindow.setOutsideTouchable(false);
//        mStockUserPopUpWindow.setFocusable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.checkboxOpenIncrementAgree.setOnCheckedChangeListener((compoundButton, isChecked) -> bAgreeFlag = isChecked);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityOpenIncrementBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void openValueAddedServices() {
        sendRequest(WithholdAccountService.getInstance().openValueAddedServices, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "OpenValueAddedServices":
                if (head.isSuccess()){
                    showArrearsDialog();
                }else {
                    if (head.getCode().equals("1000")) {
                        //用户未进行代扣签约
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
                                .withString("Resource", "Else")
                                .navigation();
                        finish();
                    }
                }
                break;
        }
    }

    public class ClickHandlers {
        public void onClickOpen() {

//                showArrearsDialog("xxxxxx");

//            if (null != mStockUserPopUpWindow && !mStockUserPopUpWindow.isShowing()) {
//
//                mStockUserPopUpWindow.setData((v)->{
//                    Log.i("testXin","laile");
//                    mStockUserPopUpWindow.dismiss();
//                });
//                mStockUserPopUpWindow.showAtLocation(mBinding.getRoot(), Gravity.CENTER, 0, 0);
//            }

            if(!mBinding.checkboxOpenIncrementAgree.isChecked()){
                showShortToast("请阅读并同意《增值服务协议》");
            }else {
                openValueAddedServices();
            }
        }
    }




    private void setAggrementMessage() {
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.transaction_bind_account_increment_agreement));
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick1(), 4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        mBinding.tvIncrementOpenAgree.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvIncrementOpenAgree.setText(spannableString);
    }

    private class TextClick1 extends ClickableSpan {

        @Override
        public void onClick(View widget) {

            avoidHintColor(widget);
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.increment_agreement))
                    .withString("url", "http://www.taijs.com/upload/zzfwxy.htm")
                    .navigation();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            ds.setColor(ContextCompat.getColor(OpenIncrementActivity.this,R.color.transparent));

            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }
    private void avoidHintColor(View view){
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }


    private void showArrearsDialog() {
        if (isFinishing)
            return;

        if (null == mDialog)
            mDialog = new IncrementAlertDialog(mContext,"申请成功,您可于下一交易日使用增值功能",(v)->{
                Log.i("testXin","=====dddd");
                mDialog.dismiss();
                finish();
            });

        if (!mDialog.isShowing()) {
            mDialog.show();
//            mDialog.setData(tx);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mDialog.getWindow().setAttributes(lp);

        }
    }


//    private void showArrearsDialog2() {
//        if (isFinishing)
//            return;
//
//        if (!isForeground())
//            return;
//
//        if (null == mStockUserDialog)
//            mStockUserDialog = new StockUserDialog(mContext);
//
//        if (!mStockUserDialog.isShowing()) {
//            mStockUserDialog.show();
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            WindowManager.LayoutParams lp = mStockUserDialog.getWindow().getAttributes();
//            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
//            mStockUserDialog.getWindow().setAttributes(lp);
//
//        }
//    }

}

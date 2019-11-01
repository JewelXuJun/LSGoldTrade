package com.jme.common.ui.view.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import com.jme.common.R;
import com.jme.common.ui.view.fingerprint.bean.VerificationDialogStyleBean;
import com.jme.common.ui.view.fingerprint.uitls.CipherHelper;

/**
 * Android M == 6.0
 * Created by ZuoHailong on 2019/7/9.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintImplForAndrM implements IFingerprint {

    private final String TAG = FingerprintImplForAndrM.class.getName();
    private Activity context;
    private String type;

    private static FingerprintImplForAndrM fingerprintImplForAndrM;
    //指纹验证框
    private static FingerprintDialog fingerprintDialog;
    //指向调用者的指纹回调
    private FingerprintCallback fingerprintCallback;

    //用于取消扫描器的扫描动作
    private CancellationSignal cancellationSignal;
    //指纹加密
    private static FingerprintManagerCompat.CryptoObject cryptoObject;
    //Android 6.0 指纹管理
    private FingerprintManagerCompat fingerprintManagerCompat;

    @Override
    public void authenticate(Activity context, VerificationDialogStyleBean bean, FingerprintCallback callback, String type) {

        this.context = context;
        this.fingerprintCallback = callback;
        this.type = type;
        //Android 6.0 指纹管理 实例化
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);

        //取消扫描，每次取消后需要重新创建新示例
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> fingerprintDialog.dismiss());

        //调起指纹验证
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, authenticationCallback, null);
        //指纹验证框
        fingerprintDialog = FingerprintDialog.newInstance().setActionListener(dialogActionListener).setDialogStyle(bean);
        fingerprintDialog.show(context.getFragmentManager(), TAG);
    }

    public static FingerprintImplForAndrM newInstance() {
        if (fingerprintImplForAndrM == null) {
            synchronized (FingerprintImplForAndrM.class) {
                if (fingerprintImplForAndrM == null) {
                    fingerprintImplForAndrM = new FingerprintImplForAndrM();
                }
            }
        }
        //指纹加密，提前进行Cipher初始化，防止指纹认证时还没有初始化完成
        try {
            cryptoObject = new FingerprintManagerCompat.CryptoObject(new CipherHelper().createCipher());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fingerprintImplForAndrM;
    }

    /**
     * 指纹验证框按键监听
     */
    private FingerprintDialog.OnDialogActionListener dialogActionListener = new FingerprintDialog.OnDialogActionListener() {

        @Override
        public void onCancel() {//取消指纹验证，通知调用者
            if (fingerprintCallback != null)
                fingerprintCallback.onCancel();
        }

        @Override
        public void onDismiss() {//验证框消失，取消指纹验证
            if (cancellationSignal != null && !cancellationSignal.isCanceled())
                cancellationSignal.cancel();
        }
    };

    /**
     * 指纹验证结果回调
     */
    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);

            if (errMsgId != 5) {//用户取消指纹验证
                String errorMsg = !TextUtils.isEmpty(type) && type.equals("Unlock")
                        ? context.getResources().getString(R.string.fingerprint_verify_error_trade)
                        : context.getResources().getString(R.string.fingerprint_verify_error);

                fingerprintDialog.setTip(errorMsg, R.color.black);
                fingerprintCallback.onFailed(errString.toString());
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);

            fingerprintDialog.setTip(helpString.toString(), R.color.black);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            fingerprintDialog.setTip(context.getString(R.string.fingerprint_verify_success), R.color.fingerprint_success);
            fingerprintCallback.onSucceeded();
            fingerprintDialog.dismiss();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();

            fingerprintDialog.setTip(context.getString(R.string.fingerprint_verify_failed), R.color.black);
            fingerprintCallback.onFailed(context.getString(R.string.fingerprint_verify_failed));
        }
    };

    /*
     * 在 Android Q，Google 提供了 Api BiometricManager.canAuthenticate() 用来检测指纹识别硬件是否可用及是否添加指纹
     * 不过尚未开放，标记为"Stub"(存根)
     * 所以暂时还是需要使用 Andorid 6.0 的 Api 进行判断
     * */
    @Override
    public boolean canAuthenticate(Context context, FingerprintCallback fingerprintCallback) {
        /*
         * 硬件是否支持指纹识别
         * */
        if (!FingerprintManagerCompat.from(context).isHardwareDetected()) {
            fingerprintCallback.onHwUnavailable();
            return false;
        }
        //是否已添加指纹
        if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
            fingerprintCallback.onNoneEnrolled();
            return false;
        }
        return true;
    }

    @Override
    public void dismiss() {
        fingerprintDialog.dismiss();
    }
}

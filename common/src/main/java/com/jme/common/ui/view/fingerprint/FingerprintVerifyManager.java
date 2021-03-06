package com.jme.common.ui.view.fingerprint;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.jme.common.ui.view.fingerprint.bean.VerificationDialogStyleBean;
import com.jme.common.ui.view.fingerprint.uitls.AndrVersionUtil;

/**
 * Created by ZuoHailong on 2019/7/9.
 */
public class FingerprintVerifyManager {

    IFingerprint fingerprint;

    public FingerprintVerifyManager(Builder builder) {
        if (AndrVersionUtil.isAboveAndrP()) {
            if (builder.enableAndroidP)
                fingerprint = FingerprintImplForAndrP.newInstance();
            else
                fingerprint = FingerprintImplForAndrM.newInstance();
        } else if (AndrVersionUtil.isAboveAndrM()) {
            fingerprint = FingerprintImplForAndrM.newInstance();
        } else {//Android 6.0 以下官方未开放指纹识别，某些机型自行支持的情况暂不做处理
            builder.callback.onHwUnavailable();
            return;
        }
        //检测指纹硬件是否存在或者是否可用，若false，不再弹出指纹验证框
        if (!fingerprint.canAuthenticate(builder.context, builder.callback))
            return;
        /**
         * 设定指纹验证框的样式
         */
        // >= Android 6.0
        VerificationDialogStyleBean bean = new VerificationDialogStyleBean();

        // >= Android 9.0
        bean.setTitle(builder.title);
        bean.setSubTitle(builder.subTitle);
        bean.setDescription(builder.description);
        bean.setCancelBtnText(builder.cancelBtnText);

        fingerprint.authenticate(builder.context, bean, builder.callback, builder.type);
    }

    /**
     * UpdateAppManager的构建器
     */
    public static class Builder {

        private FingerprintVerifyManager fingerprintVerifyManager;

        /*必选字段*/
        private Activity context;
        private FingerprintCallback callback;

        private boolean enableAndroidP;//在Android 9.0系统上，是否开启google提供的验证方式及验证框
        private String type;
        private String title;
        private String subTitle;
        private String description;
        private String cancelBtnText;//取消按钮文字

        /**
         * 构建器
         *
         * @param activity
         */
        public Builder(@NonNull Activity activity) {
            this.context = activity;
        }

        /**
         * 指纹识别回调
         *
         * @param callback
         */
        public Builder callback(@NonNull FingerprintCallback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 在 >= Android 9.0 系统上，是否开启google提供的验证方式及验证框
         *
         * @param enableAndroidP
         */
        public Builder enableAndroidP(boolean enableAndroidP) {
            this.enableAndroidP = enableAndroidP;
            return this;
        }

        /**
         * >= Android 9.0 的验证框的主标题
         *
         * @param title
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * >= Android 9.0 的验证框的副标题
         *
         * @param subTitle
         */
        public Builder subTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        /**
         * >= Android 9.0 的验证框的描述内容
         *
         * @param description
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * >= Android 9.0 的验证框的取消按钮的文字
         *
         * @param cancelBtnText
         */
        public Builder cancelBtnText(String cancelBtnText) {
            this.cancelBtnText = cancelBtnText;
            return this;
        }

        public void dismiss() {
            fingerprintVerifyManager.fingerprint.dismiss();
        }

        /**
         * 开始构建
         *
         * @return
         */
        public FingerprintVerifyManager build() {
            fingerprintVerifyManager = new FingerprintVerifyManager(this);

            return fingerprintVerifyManager;
        }
    }

}

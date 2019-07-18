package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class ImageVerifyCodeVo implements Serializable {

    private String kaptchaId;

    private String kaptchaImg;

    public String getKaptchaId() {
        return kaptchaId;
    }

    public void setKaptchaId(String kaptchaId) {
        this.kaptchaId = kaptchaId;
    }

    public String getKaptchaImg() {
        return kaptchaImg;
    }

    public void setKaptchaImg(String kaptchaImg) {
        this.kaptchaImg = kaptchaImg;
    }
}

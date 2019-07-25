package com.jme.lsgoldtrade.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/25 09:52
 * Desc   : description
 */
public class WechatPayVo {


    /**
     * package : Sign=WXPay
     * appid : wx16fbbdbe78667176
     * extdata : 附加信息
     * sign : B227DFF8D7390E86FE0601A8D44A8624FD0DB25DF2452E06D9CC64CCCB0A02C6
     * partnerid : 1312912501
     * prepayid : wx25095003056903a4fb2d3d641243910600
     * noncestr : 3jsnGlGVs0lgQwP5
     * timestamp : 1564019403
     */

    @SerializedName("package")
    private String packageX;
    private String appid;
    private String extdata;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String noncestr;
    private String timestamp;

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getExtdata() {
        return extdata;
    }

    public void setExtdata(String extdata) {
        this.extdata = extdata;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

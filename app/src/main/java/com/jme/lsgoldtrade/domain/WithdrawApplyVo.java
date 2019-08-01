package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/08/01 17:14
 * Desc   : description
 */
public class WithdrawApplyVo implements Serializable {

    /**
     * nickName : Andrew Zhang
     * amount : 1
     * withdrawType : 微信
     */

    private String nickName;
    private String amount;
    private String withdrawType;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }
}

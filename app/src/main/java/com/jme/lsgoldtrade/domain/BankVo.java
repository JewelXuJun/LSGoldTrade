package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class BankVo implements Serializable {

    private String bankNo;

    private String bankName;

    private String acBankId;

    private String logoPath;

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAcBankId() {
        return acBankId;
    }

    public void setAcBankId(String acBankId) {
        this.acBankId = acBankId;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}

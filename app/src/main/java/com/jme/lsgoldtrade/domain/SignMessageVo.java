package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class SignMessageVo implements Serializable {

    private String bankId;

    private String oldReqSeqNo;

    private String returnCode;

    private String returnMessage;

    private String smsOrderId;

    private String transCode;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getOldReqSeqNo() {
        return oldReqSeqNo;
    }

    public void setOldReqSeqNo(String oldReqSeqNo) {
        this.oldReqSeqNo = oldReqSeqNo;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getSmsOrderId() {
        return smsOrderId;
    }

    public void setSmsOrderId(String smsOrderId) {
        this.smsOrderId = smsOrderId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }
}

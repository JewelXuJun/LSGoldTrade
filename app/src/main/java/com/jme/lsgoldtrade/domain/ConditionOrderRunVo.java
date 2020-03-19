package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class ConditionOrderRunVo implements Serializable {

    private int conditionOrderRunNum;

    private int stopOrderRunNum;

    public int getConditionOrderRunNum() {
        return conditionOrderRunNum;
    }

    public void setConditionOrderRunNum(int conditionOrderRunNum) {
        this.conditionOrderRunNum = conditionOrderRunNum;
    }

    public int getStopOrderRunNum() {
        return stopOrderRunNum;
    }

    public void setStopOrderRunNum(int stopOrderRunNum) {
        this.stopOrderRunNum = stopOrderRunNum;
    }
}

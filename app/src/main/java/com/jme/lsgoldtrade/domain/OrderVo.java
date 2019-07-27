package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class OrderVo implements Serializable {

    private boolean hasOrder;

    private int orderNum;

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}

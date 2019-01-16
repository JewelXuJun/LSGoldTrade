package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class OrderHisPageVo implements Serializable {

    private boolean hasNext;

    private String pagingKey;

    private List<OrderPageVo.OrderBean> list;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getPagingKey() {
        return pagingKey;
    }

    public void setPagingKey(String pagingKey) {
        this.pagingKey = pagingKey;
    }

    public List<OrderPageVo.OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderPageVo.OrderBean> list) {
        this.list = list;
    }
}

package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class DealHistoryPageVo implements Serializable {

    private boolean hasNext;

    private String pagingKey;

    private List<DealPageVo.DealBean> list;

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

    public List<DealPageVo.DealBean> getList() {
        return list;
    }

    public void setList(List<DealPageVo.DealBean> list) {
        this.list = list;
    }
}

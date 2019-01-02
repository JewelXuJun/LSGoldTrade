package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class DealHistoryPageVo implements Serializable {

    private String seachKey;

    private List<DealPageVo.DealBean> list;

    public String getSeachKey() {
        return seachKey;
    }

    public void setSeachKey(String seachKey) {
        this.seachKey = seachKey;
    }

    public List<DealPageVo.DealBean> getList() {
        return list;
    }

    public void setList(List<DealPageVo.DealBean> list) {
        this.list = list;
    }

}

package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class TradingBoxHistoryVo implements Serializable {

    private int total;

    private int size;

    private int current;

    private int pages;

    private List<TradingBoxVo> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<TradingBoxVo> getRecords() {
        return records;
    }

    public void setRecords(List<TradingBoxVo> records) {
        this.records = records;
    }
}

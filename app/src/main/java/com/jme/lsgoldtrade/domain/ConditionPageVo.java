package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class ConditionPageVo implements Serializable {

    private int current;

    private int pages;

    private List<conditionOrderInfoBean> records;

    private boolean searchCount;

    private int size;

    private int total;

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

    public List<conditionOrderInfoBean> getRecords() {
        return records;
    }

    public void setRecords(List<conditionOrderInfoBean> records) {
        this.records = records;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class conditionOrderInfoBean implements Serializable {

        private List<ConditionOrderInfoVo> conditionOrderInfoList;

        private String setDate;

        public List<ConditionOrderInfoVo> getConditionOrderInfoList() {
            return conditionOrderInfoList;
        }

        public void setConditionOrderInfoList(List<ConditionOrderInfoVo> conditionOrderInfoList) {
            this.conditionOrderInfoList = conditionOrderInfoList;
        }

        public String getSetDate() {
            return setDate;
        }

        public void setSetDate(String setDate) {
            this.setDate = setDate;
        }
    }
}

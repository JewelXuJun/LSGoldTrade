package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class TransactionDetailVo implements Serializable {

    /**
     *  "page": 1,
     *  "queryMode": 1,
     *  "hasNext": "1",
     *  "records": [
     *             {
     *                 "id": 152206713263489025,
     *                 "customerName": "丁绍成",
     *                 "customerNo": "0001000200003",
     *                 "serialNumber": "152206713263489024",
     *                 "amount": "100",
     *                 "businessStatus": "recharge",
     *                 "status": "true",
     *                 "createdTime": "2019-08-27 17:04:28",
     *                 "orderRemakes": null
     *             },
     *     ],
     */

    private int page;

    private int queryMode;

    private String hasNext;

    private List<RecordsBean> records;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(int queryMode) {
        this.queryMode = queryMode;
    }

    public String getHasNext() {
        return hasNext;
    }

    public void setHasNext(String hasNext) {
        this.hasNext = hasNext;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean implements Serializable {

        private String id;

        private String customerName;

        private String customerNo;

        private String serialNumber;

        private String amount;

        private String businessStatus;

        private String status;

        private String createdTime;

        private String orderRemakes;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBusinessStatus() {
            return businessStatus;
        }

        public void setBusinessStatus(String businessStatus) {
            this.businessStatus = businessStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getOrderRemakes() {
            return orderRemakes;
        }

        public void setOrderRemakes(String orderRemakes) {
            this.orderRemakes = orderRemakes;
        }
    }

}

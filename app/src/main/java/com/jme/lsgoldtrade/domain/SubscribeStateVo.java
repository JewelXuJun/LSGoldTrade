package com.jme.lsgoldtrade.domain;

import java.util.List;

public class SubscribeStateVo {

    /**
     * num : 0
     * list : [
     * {
     * "id":"1132153022261112834",
     * "customerNo":"0001000200002",
     * "serviceId":1000,
     * "subscribeTime":null,
     * "status":null,
     * "createTime":null,
     * "updateTime":null,
     * "sid":null,
     * "serverName":"交易匣子"
     * },
     * {
     * "id":"1132090263955816450",
     * "customerNo":"0001000200002",
     * "serviceId":1000,
     * "subscribeTime":null,
     * "status":null,
     * "createTime":null,
     * "updateTime":null,
     * "sid":null,
     * "serverName":"交易匣子"
     * }
     * ]
     */

    private int num;

    private List<SubscribeBean> list;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<SubscribeBean> getList() {
        return list;
    }

    public void setList(List<SubscribeBean> list) {
        this.list = list;
    }

    public static class SubscribeBean {
        /**
         * "id":"1132153022261112834",
         * "customerNo":"0001000200002",
         * "serviceId":1000,
         * "subscribeTime":null,
         * "status":null,
         * "createTime":null,
         * "updateTime":null,
         * "sid":null,
         * "serverName":"交易匣子"
         */

        private String id;

        private String customerNo;

        private int serviceId;

        private String subscribeTime;

        private String status;

        private String createTime;

        private String updateTime;

        private String sid;

        private String serverName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public String getSubscribeTime() {
            return subscribeTime;
        }

        public void setSubscribeTime(String subscribeTime) {
            this.subscribeTime = subscribeTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
    }
}

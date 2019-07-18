package com.jme.lsgoldtrade.domain;

import java.util.List;

public class IsSubscribeVo {

    /**
     * num : 0
     * list : [{"id":"1132153022261112834","customerNo":"0001000200002","serviceId":1000,"subscribeTime":null,"status":null,"createTime":null,"updateTime":null,"sid":null,"serverName":"交易匣子"},{"id":"1132090263955816450","customerNo":"0001000200002","serviceId":1000,"subscribeTime":null,"status":null,"createTime":null,"updateTime":null,"sid":null,"serverName":"交易匣子"}]
     */

    private int num;
    private List<ListBean> list;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1132153022261112834
         * customerNo : 0001000200002
         * serviceId : 1000
         * subscribeTime : null
         * status : null
         * createTime : null
         * updateTime : null
         * sid : null
         * serverName : 交易匣子
         */

        private String id;
        private String customerNo;
        private int serviceId;
        private Object subscribeTime;
        private Object status;
        private Object createTime;
        private Object updateTime;
        private Object sid;
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

        public Object getSubscribeTime() {
            return subscribeTime;
        }

        public void setSubscribeTime(Object subscribeTime) {
            this.subscribeTime = subscribeTime;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getSid() {
            return sid;
        }

        public void setSid(Object sid) {
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

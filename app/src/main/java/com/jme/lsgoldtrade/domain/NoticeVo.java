package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class NoticeVo implements Serializable {

    private int total;

    private List<NoticeBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<NoticeBean> getList() {
        return list;
    }

    public void setList(List<NoticeBean> list) {
        this.list = list;
    }

    public static class NoticeBean implements Serializable {

        /**
         * "id": 11,
         * "title": "弹出标题5",
         * "content": "测试内容2",
         * "noticeType": 1,
         * "recipient": "1",
         * "recipientType": "C",
         * "noticeTarget": "N",
         * "includeSub": "N",
         * "sendTime": "2018-12-29 00:00:00",
         * "expiryTime": "2019-01-02 00:00:00",
         * "popupFlag": "Y",
         * "publisher": "dyyt",
         * "createTime": "2018-12-29 15:24:00",
         * "updateTime": "2018-12-29 15:24:00",
         * "cFlag": "C"
         */

        private long id;

        private String title;

        private String content;

        private String noticeType;

        private String recipient;

        private String recipientType;

        private String noticeTarget;

        private String includeSub;

        private String sendTime;

        private String expiryTime;

        private String popupFlag;

        private String publisher;

        private String createTime;

        private String updateTime;

        private String cFlag;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getRecipientType() {
            return recipientType;
        }

        public void setRecipientType(String recipientType) {
            this.recipientType = recipientType;
        }

        public String getNoticeTarget() {
            return noticeTarget;
        }

        public void setNoticeTarget(String noticeTarget) {
            this.noticeTarget = noticeTarget;
        }

        public String getIncludeSub() {
            return includeSub;
        }

        public void setIncludeSub(String includeSub) {
            this.includeSub = includeSub;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getExpiryTime() {
            return expiryTime;
        }

        public void setExpiryTime(String expiryTime) {
            this.expiryTime = expiryTime;
        }

        public String getPopupFlag() {
            return popupFlag;
        }

        public void setPopupFlag(String popupFlag) {
            this.popupFlag = popupFlag;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
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

        public String getcFlag() {
            return cFlag;
        }

        public void setcFlag(String cFlag) {
            this.cFlag = cFlag;
        }
    }

}

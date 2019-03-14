package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class InfoVo implements Serializable {

    private int total;

    private int size;

    private int current;

    private int pages;

    private List<InfoBean> records;

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

    public List<InfoBean> getRecords() {
        return records;
    }

    public void setRecords(List<InfoBean> records) {
        this.records = records;
    }

    public static class InfoBean implements Serializable {

        /**
         * "id":"1099875804651200513",
         * "channelId":37,
         * "createUser":11,
         * "cancelUser":null,
         * "checkUser":null,
         * "title":"测试003",
         * "summary":"随意",
         * "titleImg":"https://tjshj.oss-cn-beijing.aliyuncs.com/zzpic14999.jpg",
         * "author":null,
         * "origin":null,
         * "isTop":null,
         * "receiveScope":"a",
         * "state":"d",
         * "pageViews":null,
         * "releaseDate":null,
         * "releaseTimeArea":null,
         * "releaseTime":null,
         * "checkDate":null,
         * "releaseType":null,
         * "sort":100,
         * "startDate":null,
         * "endDate":null,
         * "createTime":"2019-02-25 11:36:41",
         * "updateTime":"2019-02-25 11:36:41",
         * "siteId":null,
         * "viewsRatio":null,
         * "code":null
         */

        private String id;

        private long channelId;

        private String createUser;

        private String cancelUser;

        private String checkUser;

        private String title;

        private String summary;

        private String titleImg;

        private String author;

        private String origin;

        private String isTop;

        private String receiveScope;

        private String state;

        private long pageViews;

        private String releaseDate;

        private String releaseTimeArea;

        private String releaseTime;

        private String checkDate;

        private String releaseType;

        private long sort;

        private String startDate;

        private String endDate;

        private String createTime;

        private String updateTime;

        private long siteId;

        private long viewsRatio;

        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getChannelId() {
            return channelId;
        }

        public void setChannelId(long channelId) {
            this.channelId = channelId;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getCancelUser() {
            return cancelUser;
        }

        public void setCancelUser(String cancelUser) {
            this.cancelUser = cancelUser;
        }

        public String getCheckUser() {
            return checkUser;
        }

        public void setCheckUser(String checkUser) {
            this.checkUser = checkUser;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTitleImg() {
            return titleImg;
        }

        public void setTitleImg(String titleImg) {
            this.titleImg = titleImg;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }

        public String getReceiveScope() {
            return receiveScope;
        }

        public void setReceiveScope(String receiveScope) {
            this.receiveScope = receiveScope;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getPageViews() {
            return pageViews;
        }

        public void setPageViews(long pageViews) {
            this.pageViews = pageViews;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getReleaseTimeArea() {
            return releaseTimeArea;
        }

        public void setReleaseTimeArea(String releaseTimeArea) {
            this.releaseTimeArea = releaseTimeArea;
        }

        public String getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(String releaseTime) {
            this.releaseTime = releaseTime;
        }

        public String getCheckDate() {
            return checkDate;
        }

        public void setCheckDate(String checkDate) {
            this.checkDate = checkDate;
        }

        public String getReleaseType() {
            return releaseType;
        }

        public void setReleaseType(String releaseType) {
            this.releaseType = releaseType;
        }

        public long getSort() {
            return sort;
        }

        public void setSort(long sort) {
            this.sort = sort;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
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

        public long getSiteId() {
            return siteId;
        }

        public void setSiteId(long siteId) {
            this.siteId = siteId;
        }

        public long getViewsRatio() {
            return viewsRatio;
        }

        public void setViewsRatio(long viewsRatio) {
            this.viewsRatio = viewsRatio;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

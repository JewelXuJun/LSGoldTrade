package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class NavigatorVo implements Serializable {

    private List<NotUsedModulesBean> notUsedModules;
    private List<UsedModulesBean> usedModules;

    public List<NotUsedModulesBean> getNotUsedModules() {
        return notUsedModules;
    }

    public void setNotUsedModules(List<NotUsedModulesBean> notUsedModules) {
        this.notUsedModules = notUsedModules;
    }

    public List<UsedModulesBean> getUsedModules() {
        return usedModules;
    }

    public void setUsedModules(List<UsedModulesBean> usedModules) {
        this.usedModules = usedModules;
    }

    public static class NotUsedModulesBean {
        /**
         * code : string
         * createTime : 2019-05-20T02:15:32.362Z
         * id : string
         * imageName : string
         * isDefault : 0
         * name : string
         * sort : 0
         * updateTime : 2019-05-20T02:15:32.362Z
         */

        private String code;
        private String createTime;
        private String id;
        private String imageName;
        private int isDefault;
        private String name;
        private String sort;
        private String updateTime;
        private String isShow;

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class UsedModulesBean {
        /**
         * code : string
         * createTime : 2019-05-20T02:15:32.362Z
         * id : string
         * imageName : string
         * isDefault : 0
         * name : string
         * sort : 0
         * updateTime : 2019-05-20T02:15:32.362Z
         * isShow : 1,2
         */

        private String code;
        private String createTime;
        private String id;
        private String imageName;
        private String isDefault;
        private String name;
        private String sort;
        private String updateTime;
        private String isShow;

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}

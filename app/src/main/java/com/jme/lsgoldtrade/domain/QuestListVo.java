package com.jme.lsgoldtrade.domain;

public class QuestListVo {

    /**
     * id : 1
     * name : 开户问题
     * sort : 1.0
     * pic : https://tjshj.oss-cn-beijing.aliyuncs.com/prod/syscofig/icon-account.png
     */

    private String id;
    private String name;
    private double sort;
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSort() {
        return sort;
    }

    public void setSort(double sort) {
        this.sort = sort;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

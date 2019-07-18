package com.jme.lsgoldtrade.domain;

public class AskListVo {

    /**
     * id : 1140541574867042305
     * title : 开户需要准备什么资料？
     * anwser : 开户需要准备身份证和工商银行的储蓄卡，如无工行储蓄卡，开户前将先申请工商银行电子账户。
     * typeId : 1.0
     * createTime : 2019-06-17 16:47:56
     * jumpTxt : 百度地址
     * isSpread : 0
     */

    private String id;
    private String title;
    private String anwser;
    private double typeId;
    private String createTime;
    private String jumpTxt;
    private String isSpread;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnwser() {
        return anwser;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public double getTypeId() {
        return typeId;
    }

    public void setTypeId(double typeId) {
        this.typeId = typeId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getJumpTxt() {
        return jumpTxt;
    }

    public void setJumpTxt(String jumpTxt) {
        this.jumpTxt = jumpTxt;
    }

    public String getIsSpread() {
        return isSpread;
    }

    public void setIsSpread(String isSpread) {
        this.isSpread = isSpread;
    }
}

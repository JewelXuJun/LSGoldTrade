package com.jme.lsgoldtrade.domain;

public class UsernameVo {

    /**
     * id : 2.0
     * name : 徐军
     * customerNo : 0001000200002
     * memberNo : 0001
     * openTime : 2019-07-04 00:00:00
     * balance : 0.0
     * frozenBalance : 0.0
     * status : 0
     */

    private String id;
    private String name;
    private String customerNo;
    private String memberNo;
    private String openTime;
    private String balance;
    private String frozenBalance;
    private String status;

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

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(String frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

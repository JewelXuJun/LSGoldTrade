package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class PasswordStatusVo implements Serializable {

    /**
     *  "customerNo": "0001000200004",
     *  "hasFirstSetPassword": "N"
     */

    private String customerNo;

    private String hasFirstSetPassword;


    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getHasFirstSetPassword() {
        return hasFirstSetPassword;
    }

    public void setHasFirstSetPassword(String hasFirstSetPassword) {
        this.hasFirstSetPassword = hasFirstSetPassword;
    }
}

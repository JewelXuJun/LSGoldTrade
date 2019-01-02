package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;

public class AccountVo implements Serializable {

    /**
     * "curAccountBalance":1000000,
     * "balance":1000000,
     * "transactionBalance":1000000,
     * "freezeBalance":1000000,
     * "extractableBalance":1000000,
     * "floatProfit":1000000,
     * "owingAmount":1000000,
     * "todayWithdrawal":1000000,
     * "todayIncomings":1000000,
     * "positionMargin":1000000,
     */

    private long curAccountBalance;

    private long balance;

    private long transactionBalance;

    private long freezeBalance;

    private long extractableBalance;

    private long floatProfit;

    private long owingAmount;

    private long todayWithdrawal;

    private long todayIncomings;

    private long positionMargin;

    public long getCurAccountBalance() {
        return curAccountBalance;
    }

    public void setCurAccountBalance(long curAccountBalance) {
        this.curAccountBalance = curAccountBalance;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(long transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public long getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(long freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public long getExtractableBalance() {
        return extractableBalance;
    }

    public void setExtractableBalance(long extractableBalance) {
        this.extractableBalance = extractableBalance;
    }

    public long getFloatProfit() {
        return floatProfit;
    }

    public void setFloatProfit(long floatProfit) {
        this.floatProfit = floatProfit;
    }

    public long getOwingAmount() {
        return owingAmount;
    }

    public void setOwingAmount(long owingAmount) {
        this.owingAmount = owingAmount;
    }

    public long getTodayWithdrawal() {
        return todayWithdrawal;
    }

    public void setTodayWithdrawal(long todayWithdrawal) {
        this.todayWithdrawal = todayWithdrawal;
    }

    public long getTodayIncomings() {
        return todayIncomings;
    }

    public void setTodayIncomings(long todayIncomings) {
        this.todayIncomings = todayIncomings;
    }

    public long getPositionMargin() {
        return positionMargin;
    }

    public void setPositionMargin(long positionMargin) {
        this.positionMargin = positionMargin;
    }

    public String getCurAccountBalanceStr() {
        return MarketUtil.getPriceValue(curAccountBalance);
    }

    public String getBalanceStr() {
        return MarketUtil.getPriceValue(balance);
    }

    public String getTransactionBalanceStr() {
        return MarketUtil.getPriceValue(transactionBalance);
    }

    public String getFreezeBalanceStr() {
        return MarketUtil.getPriceValue(freezeBalance);
    }

    public String getExtractableBalanceStr() {
        return MarketUtil.getPriceValue(extractableBalance);
    }

    public String getFloatProfitStr() {
        return MarketUtil.getPriceValue(floatProfit);
    }

    public String getOwingAmountStr() {
        return MarketUtil.getPriceValue(owingAmount);
    }

    public String getTodayWithdrawalStr() {
        return MarketUtil.getPriceValue(todayWithdrawal);
    }

    public String getTodayIncomingsStr() {
        return MarketUtil.getPriceValue(todayIncomings);
    }

    public String getPositionMarginStr() {
        return MarketUtil.getPriceValue(positionMargin);
    }

}
package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class DailyStatementVo implements Serializable {

    /**
     *  "tradingAmount":1000000,
     *  "currentDrawAmonut":1000000,
     *  "nextDrawAmount":1000000,
     *  "lastPositionMargin":1000000,
     *  "currentPositionMargin":1000000,
     *  "settlementMargin":1000000,
     *  "debt":1000000,
     *  "deposit":1000000,
     *  "todayProfit":1000000,
     *  "spotDeliveryAmount":1000000,
     *  "deferDeliveryAmount":1000000,
     *  "forwardDeliveryAmount":1000000,
     *  "lastPlatinumFreeze":1000000,
     *  "todayPlatinumFreeze":1000000,
     *  "platinumFreezeMargin":1000000,
     *  "todayPackageMargin":1000000,
     *  "packageAmount":1000000,
     *  "tradingFee":1000000,
     *  "storageFee":1000000,
     *  "expirationFee":1000000,
     *  "deferFee":1000000,
     *  "deliveryFee":1000000,
     *  "transportFee":1000000,
     *  "penalty":1000000,
     *  "diff":1000000,
     *  "pledgeFee":1000000,
     *  "rentFee":1000000,
     *  "varietyPremium":1000000,
     *  "slicePremium":1000000,
     *  "etfFee":1000000,
     *  "etfAmount":1000000,
     *  "messageFee":1000000,
     *  "totalAmount":1000000,
     *  "liquidationAmount":1000000,
     *  "lastTradingBalance":1000000,
     *  "currentBalance":1000000,
     *  "currentIncomings":1000000,
     *  "currentWithdrawal":1000000,
     *  "capitalAdequacyRatio":1000000,
     *  "bankTradeFee":1000000,
     *  "exTradeFee":1000000,
     *  "derateFee":1000000,
     *  "tnDeferFee":1000000,
     *  "inoutStorageFee":1000000,
     *  "lateFee":1000000,
     *  "fine":1000000,
     *  "offsetFee":1000000,
     *  "carriageFee":1000000,
     *  "withdrawalFee":1000000,
     *  "transferFee":1000000,
     *  "custodianFee":1000000,
     *  "storageExchangeFee":1000000,
     *  "deliveryDetail":[
     *     {
     *       "deliveryId":"201",
     *       "deliveryName":"Au9999",
     *       "totalStorage":1000000000,
     *       "availableStorage":1000000000,
     *       "extractStorage":1000000000,
     *       "freezeStorage":1000000000,
     *       "pledgeStorage":1000000000,
     *       "manaualFreezeStorage":1000000000,
     *       "transferFreezeStorage":1000000000,
     *       "offsetFreezeStorage":1000000000,
     *       "warehousingDiff":1000000000,
     *       "nonClearingDiff":1000000000,
     *       "totalDiff":1000000000,
     *     }],
     *  "contract_detail":[
     *     {
     *       "contractId":"Ag(T+D)",
     *       "contractName":"白银递延",
     *       "longPosition":1000000,
     *       "shortPosition":1000000
     *       "longOpenAverage":3000000,
     *       "shortOpenAverage":3000000,
     *       "currentDelivery":1000000,
     *      }
     *   ]
     */

    private long tradingAmount;

    private long currentDrawAmonut;

    private long nextDrawAmount;

    private long lastPositionMargin;

    private long currentPositionMargin;

    private long settlementMargin;

    private long debt;

    private long deposit;

    private long todayProfit;

    private long spotDeliveryAmount;

    private long deferDeliveryAmount;

    private long forwardDeliveryAmount;

    private long lastPlatinumFreeze;

    private long todayPlatinumFreeze;

    private long platinumFreezeMargin;

    private long todayPackageMargin;

    private long packageAmount;

    private long tradingFee;

    private long storageFee;

    private long expirationFee;

    private long deferFee;

    private long deliveryFee;

    private long transportFee;

    private long penalty;

    private long diff;

    private long pledgeFee;

    private long rentFee;

    private long varietyPremium;

    private long slicePremium;

    private long etfFee;

    private long etfAmount;

    private long messageFee;

    private long totalAmount;

    private long liquidationAmount;

    private long lastTradingBalance;

    private long currentBalance;

    private long currentIncomings;

    private long currentWithdrawal;

    private long capitalAdequacyRatio;

    private long bankTradeFee;

    private long exTradeFee;

    private long derateFee;

    private long tnDeferFee;

    private long inoutStorageFee;

    private long lateFee;

    private long fine;

    private long offsetFee;

    private long carriageFee;

    private long withdrawalFee;

    private long transferFee;

    private long custodianFee;

    private long storageExchangeFee;

    private List<DeliveryDetailBean> deliveryDetail;

    private List<ContractDetailBean> contract_detail;

    public long getTradingAmount() {
        return tradingAmount;
    }

    public void setTradingAmount(long tradingAmount) {
        this.tradingAmount = tradingAmount;
    }

    public long getCurrentDrawAmonut() {
        return currentDrawAmonut;
    }

    public void setCurrentDrawAmonut(long currentDrawAmonut) {
        this.currentDrawAmonut = currentDrawAmonut;
    }

    public long getNextDrawAmount() {
        return nextDrawAmount;
    }

    public void setNextDrawAmount(long nextDrawAmount) {
        this.nextDrawAmount = nextDrawAmount;
    }

    public long getLastPositionMargin() {
        return lastPositionMargin;
    }

    public void setLastPositionMargin(long lastPositionMargin) {
        this.lastPositionMargin = lastPositionMargin;
    }

    public long getCurrentPositionMargin() {
        return currentPositionMargin;
    }

    public void setCurrentPositionMargin(long currentPositionMargin) {
        this.currentPositionMargin = currentPositionMargin;
    }

    public long getSettlementMargin() {
        return settlementMargin;
    }

    public void setSettlementMargin(long settlementMargin) {
        this.settlementMargin = settlementMargin;
    }

    public long getDebt() {
        return debt;
    }

    public void setDebt(long debt) {
        this.debt = debt;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }

    public long getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(long todayProfit) {
        this.todayProfit = todayProfit;
    }

    public long getSpotDeliveryAmount() {
        return spotDeliveryAmount;
    }

    public void setSpotDeliveryAmount(long spotDeliveryAmount) {
        this.spotDeliveryAmount = spotDeliveryAmount;
    }

    public long getDeferDeliveryAmount() {
        return deferDeliveryAmount;
    }

    public void setDeferDeliveryAmount(long deferDeliveryAmount) {
        this.deferDeliveryAmount = deferDeliveryAmount;
    }

    public long getForwardDeliveryAmount() {
        return forwardDeliveryAmount;
    }

    public void setForwardDeliveryAmount(long forwardDeliveryAmount) {
        this.forwardDeliveryAmount = forwardDeliveryAmount;
    }

    public long getLastPlatinumFreeze() {
        return lastPlatinumFreeze;
    }

    public void setLastPlatinumFreeze(long lastPlatinumFreeze) {
        this.lastPlatinumFreeze = lastPlatinumFreeze;
    }

    public long getTodayPlatinumFreeze() {
        return todayPlatinumFreeze;
    }

    public void setTodayPlatinumFreeze(long todayPlatinumFreeze) {
        this.todayPlatinumFreeze = todayPlatinumFreeze;
    }

    public long getPlatinumFreezeMargin() {
        return platinumFreezeMargin;
    }

    public void setPlatinumFreezeMargin(long platinumFreezeMargin) {
        this.platinumFreezeMargin = platinumFreezeMargin;
    }

    public long getTodayPackageMargin() {
        return todayPackageMargin;
    }

    public void setTodayPackageMargin(long todayPackageMargin) {
        this.todayPackageMargin = todayPackageMargin;
    }

    public long getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(long packageAmount) {
        this.packageAmount = packageAmount;
    }

    public long getTradingFee() {
        return tradingFee;
    }

    public void setTradingFee(long tradingFee) {
        this.tradingFee = tradingFee;
    }

    public long getStorageFee() {
        return storageFee;
    }

    public void setStorageFee(long storageFee) {
        this.storageFee = storageFee;
    }

    public long getExpirationFee() {
        return expirationFee;
    }

    public void setExpirationFee(long expirationFee) {
        this.expirationFee = expirationFee;
    }

    public long getDeferFee() {
        return deferFee;
    }

    public void setDeferFee(long deferFee) {
        this.deferFee = deferFee;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public long getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(long transportFee) {
        this.transportFee = transportFee;
    }

    public long getPenalty() {
        return penalty;
    }

    public void setPenalty(long penalty) {
        this.penalty = penalty;
    }

    public long getDiff() {
        return diff;
    }

    public void setDiff(long diff) {
        this.diff = diff;
    }

    public long getPledgeFee() {
        return pledgeFee;
    }

    public void setPledgeFee(long pledgeFee) {
        this.pledgeFee = pledgeFee;
    }

    public long getRentFee() {
        return rentFee;
    }

    public void setRentFee(long rentFee) {
        this.rentFee = rentFee;
    }

    public long getVarietyPremium() {
        return varietyPremium;
    }

    public void setVarietyPremium(long varietyPremium) {
        this.varietyPremium = varietyPremium;
    }

    public long getSlicePremium() {
        return slicePremium;
    }

    public void setSlicePremium(long slicePremium) {
        this.slicePremium = slicePremium;
    }

    public long getEtfFee() {
        return etfFee;
    }

    public void setEtfFee(long etfFee) {
        this.etfFee = etfFee;
    }

    public long getEtfAmount() {
        return etfAmount;
    }

    public void setEtfAmount(long etfAmount) {
        this.etfAmount = etfAmount;
    }

    public long getMessageFee() {
        return messageFee;
    }

    public void setMessageFee(long messageFee) {
        this.messageFee = messageFee;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getLiquidationAmount() {
        return liquidationAmount;
    }

    public void setLiquidationAmount(long liquidationAmount) {
        this.liquidationAmount = liquidationAmount;
    }

    public long getLastTradingBalance() {
        return lastTradingBalance;
    }

    public void setLastTradingBalance(long lastTradingBalance) {
        this.lastTradingBalance = lastTradingBalance;
    }

    public long getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(long currentBalance) {
        this.currentBalance = currentBalance;
    }

    public long getCurrentIncomings() {
        return currentIncomings;
    }

    public void setCurrentIncomings(long currentIncomings) {
        this.currentIncomings = currentIncomings;
    }

    public long getCurrentWithdrawal() {
        return currentWithdrawal;
    }

    public void setCurrentWithdrawal(long currentWithdrawal) {
        this.currentWithdrawal = currentWithdrawal;
    }

    public long getCapitalAdequacyRatio() {
        return capitalAdequacyRatio;
    }

    public void setCapitalAdequacyRatio(long capitalAdequacyRatio) {
        this.capitalAdequacyRatio = capitalAdequacyRatio;
    }

    public long getBankTradeFee() {
        return bankTradeFee;
    }

    public void setBankTradeFee(long bankTradeFee) {
        this.bankTradeFee = bankTradeFee;
    }

    public long getExTradeFee() {
        return exTradeFee;
    }

    public void setExTradeFee(long exTradeFee) {
        this.exTradeFee = exTradeFee;
    }

    public long getDerateFee() {
        return derateFee;
    }

    public void setDerateFee(long derateFee) {
        this.derateFee = derateFee;
    }

    public long getTnDeferFee() {
        return tnDeferFee;
    }

    public void setTnDeferFee(long tnDeferFee) {
        this.tnDeferFee = tnDeferFee;
    }

    public long getInoutStorageFee() {
        return inoutStorageFee;
    }

    public void setInoutStorageFee(long inoutStorageFee) {
        this.inoutStorageFee = inoutStorageFee;
    }

    public long getLateFee() {
        return lateFee;
    }

    public void setLateFee(long lateFee) {
        this.lateFee = lateFee;
    }

    public long getFine() {
        return fine;
    }

    public void setFine(long fine) {
        this.fine = fine;
    }

    public long getOffsetFee() {
        return offsetFee;
    }

    public void setOffsetFee(long offsetFee) {
        this.offsetFee = offsetFee;
    }

    public long getCarriageFee() {
        return carriageFee;
    }

    public void setCarriageFee(long carriageFee) {
        this.carriageFee = carriageFee;
    }

    public long getWithdrawalFee() {
        return withdrawalFee;
    }

    public void setWithdrawalFee(long withdrawalFee) {
        this.withdrawalFee = withdrawalFee;
    }

    public long getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(long transferFee) {
        this.transferFee = transferFee;
    }

    public long getCustodianFee() {
        return custodianFee;
    }

    public void setCustodianFee(long custodianFee) {
        this.custodianFee = custodianFee;
    }

    public long getStorageExchangeFee() {
        return storageExchangeFee;
    }

    public void setStorageExchangeFee(long storageExchangeFee) {
        this.storageExchangeFee = storageExchangeFee;
    }

    public List<DeliveryDetailBean> getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(List<DeliveryDetailBean> deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

    public List<ContractDetailBean> getContract_detail() {
        return contract_detail;
    }

    public void setContract_detail(List<ContractDetailBean> contract_detail) {
        this.contract_detail = contract_detail;
    }

    public static class DeliveryDetailBean {
        private String deliveryId;

        private String deliveryName;

        private long totalStorage;

        private long availableStorage;

        private long extractStorage;

        private long freezeStorage;

        private long pledgeStorage;

        private long manaualFreezeStorage;

        private long transferFreezeStorage;

        private long offsetFreezeStorage;

        private long warehousingDiff;

        private long nonClearingDiff;

        private long totalDiff;

        public String getDeliveryId() {
            return deliveryId;
        }

        public void setDeliveryId(String deliveryId) {
            this.deliveryId = deliveryId;
        }

        public String getDeliveryName() {
            return deliveryName;
        }

        public void setDeliveryName(String deliveryName) {
            this.deliveryName = deliveryName;
        }

        public long getTotalStorage() {
            return totalStorage;
        }

        public void setTotalStorage(long totalStorage) {
            this.totalStorage = totalStorage;
        }

        public long getAvailableStorage() {
            return availableStorage;
        }

        public void setAvailableStorage(long availableStorage) {
            this.availableStorage = availableStorage;
        }

        public long getExtractStorage() {
            return extractStorage;
        }

        public void setExtractStorage(long extractStorage) {
            this.extractStorage = extractStorage;
        }

        public long getFreezeStorage() {
            return freezeStorage;
        }

        public void setFreezeStorage(long freezeStorage) {
            this.freezeStorage = freezeStorage;
        }

        public long getPledgeStorage() {
            return pledgeStorage;
        }

        public void setPledgeStorage(long pledgeStorage) {
            this.pledgeStorage = pledgeStorage;
        }

        public long getManaualFreezeStorage() {
            return manaualFreezeStorage;
        }

        public void setManaualFreezeStorage(long manaualFreezeStorage) {
            this.manaualFreezeStorage = manaualFreezeStorage;
        }

        public long getTransferFreezeStorage() {
            return transferFreezeStorage;
        }

        public void setTransferFreezeStorage(long transferFreezeStorage) {
            this.transferFreezeStorage = transferFreezeStorage;
        }

        public long getOffsetFreezeStorage() {
            return offsetFreezeStorage;
        }

        public void setOffsetFreezeStorage(long offsetFreezeStorage) {
            this.offsetFreezeStorage = offsetFreezeStorage;
        }

        public long getWarehousingDiff() {
            return warehousingDiff;
        }

        public void setWarehousingDiff(long warehousingDiff) {
            this.warehousingDiff = warehousingDiff;
        }

        public long getNonClearingDiff() {
            return nonClearingDiff;
        }

        public void setNonClearingDiff(long nonClearingDiff) {
            this.nonClearingDiff = nonClearingDiff;
        }

        public long getTotalDiff() {
            return totalDiff;
        }

        public void setTotalDiff(long totalDiff) {
            this.totalDiff = totalDiff;
        }
    }

    public static class ContractDetailBean {
        private String contractId;

        private String contractName;

        private long longPosition;

        private long shortPosition;

        private long longOpenAverage;

        private long shortOpenAverage;

        private long currentDelivery;

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public String getContractName() {
            return contractName;
        }

        public void setContractName(String contractName) {
            this.contractName = contractName;
        }

        public long getLongPosition() {
            return longPosition;
        }

        public void setLongPosition(long longPosition) {
            this.longPosition = longPosition;
        }

        public long getShortPosition() {
            return shortPosition;
        }

        public void setShortPosition(long shortPosition) {
            this.shortPosition = shortPosition;
        }

        public long getLongOpenAverage() {
            return longOpenAverage;
        }

        public void setLongOpenAverage(long longOpenAverage) {
            this.longOpenAverage = longOpenAverage;
        }

        public long getShortOpenAverage() {
            return shortOpenAverage;
        }

        public void setShortOpenAverage(long shortOpenAverage) {
            this.shortOpenAverage = shortOpenAverage;
        }

        public long getCurrentDelivery() {
            return currentDelivery;
        }

        public void setCurrentDelivery(long currentDelivery) {
            this.currentDelivery = currentDelivery;
        }
    }

}

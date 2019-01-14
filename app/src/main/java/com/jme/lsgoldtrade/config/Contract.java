package com.jme.lsgoldtrade.config;

import android.text.TextUtils;

import com.jme.lsgoldtrade.domain.ContractInfoVo;

import java.util.ArrayList;
import java.util.List;

public class Contract {

    private static Contract mContract;

    private List<ContractInfoVo> mList;

    public static Contract getInstance() {
        if (null == mContract)
            mContract = new Contract();

        return mContract;
    }

    public void setContractList(List<ContractInfoVo> list) {
        if (null == list || 0 == list.size())
            return;

        mList = list;
    }

    public List<String> getContractIDList() {
        if (null == mList || 0 == mList.size())
            return null;

        List<String> contractIDList = new ArrayList<>();

        for (ContractInfoVo contractInfoVo : mList) {
            if (null != contractInfoVo)
                contractIDList.add(contractInfoVo.getContractId());
        }

        return contractIDList;
    }

    public String getContractIDListStr() {
        if (null == mList || 0 == mList.size())
            return "";

        String contractIDListStr = "";

        for (ContractInfoVo contractInfoVo : mList) {
            if (null != contractInfoVo)
                contractIDListStr = contractIDListStr + contractInfoVo.getContractId() + ",";
        }

        if (!TextUtils.isEmpty(contractIDListStr) && contractIDListStr.endsWith(","))
            contractIDListStr = contractIDListStr.substring(0, contractIDListStr.length() - 1);

        return contractIDListStr;
    }

    public String getContractNameListStr() {
        if (null == mList || 0 == mList.size())
            return "";

        String contractIDListStr = "";

        for (ContractInfoVo contractInfoVo : mList) {
            if (null != contractInfoVo)
                contractIDListStr = contractIDListStr + contractInfoVo.getName() + ",";
        }

        if (!TextUtils.isEmpty(contractIDListStr) && contractIDListStr.endsWith(","))
            contractIDListStr = contractIDListStr.substring(0, contractIDListStr.length() - 1);

        return contractIDListStr;
    }

    public int getContractIDPosition(String contractId) {
        if (null == mList || 0 == mList.size() || TextUtils.isEmpty(contractId))
            return 0;

        for (int i = 0; i < mList.size(); i++) {
            ContractInfoVo contractInfoVo = mList.get(i);

            if (null != contractInfoVo) {
                if (contractId.equals(contractInfoVo.getContractId()))
                    return i;
            }
        }

        return 0;
    }

    public int getContractNamePosition(String contractName) {
        if (null == mList || 0 == mList.size() || TextUtils.isEmpty(contractName))
            return 0;

        for (int i = 0; i < mList.size(); i++) {
            ContractInfoVo contractInfoVo = mList.get(i);

            if (null != contractInfoVo) {
                if (contractName.equals(contractInfoVo.getName()))
                    return i;
            }
        }

        return 0;
    }

    public ContractInfoVo getContractInfoFromID(String contractId) {
        if (null == mList || 0 == mList.size() || TextUtils.isEmpty(contractId))
            return null;

        for (ContractInfoVo contractInfoVo : mList) {
            if (null != contractInfoVo) {
                if (contractId.equals(contractInfoVo.getContractId()))
                    return contractInfoVo;
            }
        }

        return null;
    }

    public ContractInfoVo getContractInfoFromName(String contractName) {
        if (null == mList || 0 == mList.size() || TextUtils.isEmpty(contractName))
            return null;

        for (ContractInfoVo contractInfoVo : mList) {
            if (null != contractInfoVo) {
                if (contractName.equals(contractInfoVo.getName()))
                    return contractInfoVo;
            }
        }

        return null;
    }

}

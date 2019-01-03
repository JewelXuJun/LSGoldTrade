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

    public ContractInfoVo getContractInfo(String contractId) {
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

}

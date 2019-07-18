package com.jme.common.network;

/**
 * Created by Yanmin on 2015/12/8.
 */
public interface OnResultListener {
    void OnResult(DTRequest request, Head head, Object response);
}

package com.datai.common.charts.tchart;

import com.google.gson.JsonArray;

/**
 * Created by XuJun on 2016/1/29.
 */
public class TData {

    private JsonArray mArray = new JsonArray();

    public void setData(JsonArray jsonArray) {
        mArray = jsonArray;
    }

    public JsonArray getData() {
        return mArray;
    }
}

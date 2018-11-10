package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.DataSet;

/**
 * Class that encapsulates information of a value that has been
 * selected/highlighted and its DataSet index. The SelectionDetail objects give
 * information about the value at the selected index and the DataSet it belongs
 * to. Needed only for highlighting onTouch().
 *
 * @author Philipp Jahoda
 */
public class SelectionDetail {

    public float val;
    public int dataSetIndex;
    public int xIndex;//Modify by yanmin: 增加xIndex，以及对应的构造函数
    public DataSet<?> dataSet;

    public SelectionDetail(float val, int dataSetIndex, DataSet<?> set) {
        this.val = val;
        this.dataSetIndex = dataSetIndex;
        this.dataSet = set;
    }

    public SelectionDetail(float val, int dataSetIndex, int xIndex, DataSet<?> set) {
        this.val = val;
        this.dataSetIndex = dataSetIndex;
        this.xIndex = xIndex;
        this.dataSet = set;
    }
}
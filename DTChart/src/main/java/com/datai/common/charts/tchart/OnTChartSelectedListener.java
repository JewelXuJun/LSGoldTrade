package com.datai.common.charts.tchart;

import java.util.HashMap;

/**
 * Created by Monking on 2016/1/26.
 */
public interface OnTChartSelectedListener {
    /**
     * Called when a value has been selected inside the chart.
     *
     * @param entry The selected Entry.
     * @param index The index in the datasets array of the data object
     *              the Entrys DataSet is in.
     */
    void onTChartValueSelected(float current, int index, float prev);

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    void onTChartNothingSelected();
}

package com.datai.common.charts.chart;

import com.datai.common.charts.kchart.KData;

/**
 * Created by Monking on 2016/1/26.
 */
public interface OnChartListener {
    void onSwitchUnit(boolean showTChart, KData.Unit unit);
}

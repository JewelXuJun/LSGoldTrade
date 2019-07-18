package com.datai.common.charts.kchart;

import android.view.MotionEvent;

/**
 * Created by Monking on 2016/1/26.
 */
public interface OnKChartListener {

//    void onSwitchUnit(KData.Unit unit);

    void onEnding(long oldestTime, KData.Unit unit);

    void onChartSingleTapped(MotionEvent me);
}

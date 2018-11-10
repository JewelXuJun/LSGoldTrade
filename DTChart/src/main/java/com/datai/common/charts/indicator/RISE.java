package com.datai.common.charts.indicator;

/**
 * Created by Administrator on 2016/1/25.
 */
public class RISE extends Indicator {
    public static final String RISE_VALUE = "RISE_VALUE";
    public static final String RISE_PERCENT = "RISE_PERCENT";

    public RISE() {
        super(Type.RISE);
    }

    @Override
    public int[] getParams() {
        return null;
    }

    @Override
    public String[] getKeys() {
        return new String[]{RISE_VALUE, RISE_PERCENT};
    }

    @Override
    public void calc(int index) {
        if (index == 0) {
            putVaule(RISE_VALUE, 0, index);
            putVaule(RISE_PERCENT, 0, index);
            return;
        }

        float close = CLOSE(index);
        float ref = REF(K_CLOSE, index, 1);

        float value = close - ref;
        putVaule(RISE_VALUE, value, index);
        putVaule(RISE_PERCENT, value / ref * 100, index);
    }
}

package com.datai.common.charts.indicator;

/**
 * Created by Monking on 2016/1/19.
 */
public class RSI extends Indicator {
    public static final String K_MAX_CLOSE_LC = "MAX_CLOSE_LC";
    public static final String K_ABS_CLOSE_LC = "ABS_CLOSE_LC";
    public static final String K_RSI1 = "RSI1";
    public static final String K_RSI2 = "RSI2";
    public static final String K_RSI3 = "RSI3";

    private int P_N1 = 6;//2--100
    private int P_N2 = 12;//2--100
    private int P_N3 = 24;//2--100

    public RSI() {
        super(Type.RSI);
    }

    @Override
    public int[] getParams() {
        return new int[]{
                P_N1, P_N2, P_N3
        };
    }

    @Override
    public String[] getKeys() {
        return new String[]{
                K_RSI1, K_RSI2, K_RSI3,
        };
    }

    public void calc(final int index) {
        if (index == 0)
            return;

        float close = CLOSE(index);
        float lc = REF(K_CLOSE, index, 1);
        float max_close_lc = Math.max(close - lc, 0);
        float abs_close_lc = Math.abs(close - lc);

        putVaule(K_MAX_CLOSE_LC, max_close_lc, index);
        putVaule(K_ABS_CLOSE_LC, abs_close_lc, index);

        float rsi1 = SMA(K_MAX_CLOSE_LC, index, P_N1, 1, true) / SMA(K_ABS_CLOSE_LC, index, P_N1, 1, true) * 100;
        float rsi2 = SMA(K_MAX_CLOSE_LC, index, P_N2, 1, true) / SMA(K_ABS_CLOSE_LC, index, P_N2, 1, true) * 100;
        float rsi3 = SMA(K_MAX_CLOSE_LC, index, P_N3, 1, true) / SMA(K_ABS_CLOSE_LC, index, P_N3, 1, true) * 100;

        putVaule(K_RSI1, rsi1, index);
        putVaule(K_RSI2, rsi2, index);
        putVaule(K_RSI3, rsi3, index);
    }

}

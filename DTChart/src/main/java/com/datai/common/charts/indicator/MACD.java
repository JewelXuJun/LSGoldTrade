package com.datai.common.charts.indicator;

/**
 * Created by Monking on 2016/1/19.
 */
public class MACD extends Indicator {
    public static final String K_DIFF = "DIFF";
    public static final String K_DEA = "DEA";
    public static final String K_STICK = "MACD";

    private int P_SHORT = 12;//5--40
    private int P_LONG = 26;//10--200
    private int P_MIDDLE = 9;//2--40

    public MACD() {
        super(Type.MACD);
    }

    @Override
    public int[] getParams() {
        return new int[]{
                P_SHORT, P_LONG, P_MIDDLE,
        };
    }

    @Override
    public String[] getKeys() {
        return new String[]{
                K_DIFF, K_DEA, K_STICK,
        };
    }

    @Override
    public void calc(final int index) {
        float ema_short = EMA(K_CLOSE, index, P_SHORT, true);
        float ema_long = EMA(K_CLOSE, index, P_LONG, true);
        float diff = ema_short - ema_long;

        putVaule(K_DIFF, diff, index);

        float dea = EMA(K_DIFF, index, P_MIDDLE, true);
        putVaule(K_DEA, dea, index);

        float macd = 2 * (diff - dea);
        putVaule(K_STICK, macd, index);
    }
}

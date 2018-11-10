package com.datai.common.charts.indicator;

/**
 * Created by Monking on 2016/1/19.
 */
public class WR extends Indicator {
    public static final String K_WR_1 = "WR1";
    public static final String K_WR_2 = "WR2";
    private int P_N1 = 10;//2--100
    private int P_N2 = 20;//0--100
    private int P_N3 = 80;//0--100

    public WR() {
        super(Type.WR);
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
                K_WR_1, K_WR_2
        };
    }

    @Override
    public void calc(int index) {
        float hhv_n1 = HHV2(K_HIGH, index, P_N1, true);
        float llv_n1 = LLV2(K_LOW, index, P_N1, true);
        float hhv_6 = HHV2(K_HIGH, index, 6, true);
        float llv_6 = LLV2(K_LOW, index, 6, true);
        float close = CLOSE(index);

        float wr1 = 100 * (hhv_n1 - close) / (hhv_n1 - llv_n1);
        float wr2 = 100 * (hhv_6 - close) / (hhv_6 - llv_6);
        if (!Float.isNaN(wr1))
            putVaule(K_WR_1, wr1, index);

        if (!Float.isNaN(wr2))
            putVaule(K_WR_2, wr2, index);
    }
}

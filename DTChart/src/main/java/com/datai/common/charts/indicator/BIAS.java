package com.datai.common.charts.indicator;

/**
 * Created by Monking on 2016/1/19.
 */
public class BIAS extends Indicator {
    public static final String K_BIAS1 = "BIAS1";
    public static final String K_BIAS2 = "BIAS2";
    public static final String K_BIAS3 = "BIAS3";

    private int P_L1 = 6;//1--300
    private int P_L2 = -3;//-50-- -1
    private int P_L3 = 3;//1--50
    private int P_L4 = 12;//1--300
    private int P_L5 = 24;//1--300

    public BIAS() {
        super(Type.BIAS);
    }

    @Override
    public int[] getParams() {
        return new int[]{
                P_L1, P_L2, P_L3, P_L4, P_L5,
        };
    }

    @Override
    public String[] getKeys() {
        return new String[]{
                K_BIAS1, K_BIAS2, K_BIAS3,
        };
    }

    @Override
    public void calc(final int index) {
        float close = CLOSE(index);

        float ma_l1 = MA(K_CLOSE, index, P_L1);
        float ma_l4 = MA(K_CLOSE, index, P_L4);
        float ma_l5 = MA(K_CLOSE, index, P_L5);

        if (ma_l1 >= 0) {
            float bias1 = (close - ma_l1) / ma_l1 * 100;
            putVaule(K_BIAS1, bias1, index);
        }

        if (ma_l4 >= 0) {
            float bias2 = (close - ma_l4) / ma_l4 * 100;
            putVaule(K_BIAS2, bias2, index);

        }
        if (ma_l5 >= 0) {
            float bias3 = (close - ma_l5) / ma_l5 * 100;
            putVaule(K_BIAS3, bias3, index);
        }
    }
}

package com.datai.common.charts.indicator;

/**
 * Created by Monking on 2016/1/19.
 */
public class KDJ extends Indicator {
    public static final String K_RSV = "RSV";
    public static final String K_K = "K";
    public static final String K_D = "D";
    public static final String K_J = "J";

    private int P_N = 9;//1--100
    private int P_M1 = 3;//2--40
    private int P_M2 = 3;//2--40

    public KDJ() {
        super(Type.KDJ);
    }

    @Override
    public int[] getParams() {
        return new int[]{
                P_N, P_M1, P_M2
        };
    }

    @Override
    public String[] getKeys() {
        return new String[]{
                K_K, K_D, K_J,
        };
    }

    @Override
    public void calc(final int index) {
        float llv = LLV2(K_LOW, index, P_N, true);
        float hhv = HHV2(K_HIGH, index, P_N, true);

        float close = CLOSE(index);
        float rsv = 0;
        if(hhv - llv != 0)
            rsv = (close - llv) / (hhv - llv) * 100;

        putVaule(K_RSV, rsv, index);

        float k = SMA(K_RSV, index, P_M1, 1, true);
        putVaule(K_K, k, index);
        float d = SMA(K_K, index, P_M2, 1, true);
        float j = 3 * k - 2 * d;

//        k = Math.max(k, 0);
//        k = Math.min(k, 100);
        putVaule(K_K, k, index);

//        d = Math.max(d, 0);
//        d = Math.min(d, 100);
        putVaule(K_D, d, index);

//        j = Math.max(j, 0);
//        j = Math.min(j, 100);
        putVaule(K_J, j, index);
    }
}

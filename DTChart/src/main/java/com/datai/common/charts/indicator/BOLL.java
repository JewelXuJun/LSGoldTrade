package com.datai.common.charts.indicator;

/**
 * Created by Yanmin on 2016/5/9.
 */
public class BOLL extends Indicator {
    public static final String K_BOLL = "BOLL_";
    public static final String K_MID = K_BOLL + "MID";
    public static final String K_UPPER = K_BOLL + "BOLL_UP";
    public static final String K_LOWER = K_BOLL + "BOLL_LOW";

    private int P_N = 20;//5--300
    private int P_P = 2;//1--10

    public BOLL() {
        super(Type.BOLL);
    }

    @Override
    public int[] getParams() {
        return new int[]{
                P_N, P_P,
        };
    }

    @Override
    public String[] getKeys() {
        return new String[]{
                K_MID, K_UPPER, K_LOWER,
        };
    }

    @Override
    public String getKeyAlias(String key) {
        String prefix = K_BOLL;
        if (key.startsWith(prefix))
            return key.substring(prefix.length());
        return key;
    }

    @Override
    public void calc(int index) {
        float mid = MA(K_CLOSE, index, P_N);
        if (mid < 0)
            return;

        float std = P_P * STD(K_CLOSE, index, P_N);
        float upper = mid + std;
        float lower = mid - std;

        putVaule(K_MID, mid, index);
        putVaule(K_UPPER, upper, index);
        putVaule(K_LOWER, lower, index);
    }
}

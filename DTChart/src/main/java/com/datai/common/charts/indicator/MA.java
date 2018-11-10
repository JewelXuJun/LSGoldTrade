package com.datai.common.charts.indicator;

/**
 * Created by Monking on 2016/1/19.
 */
public class MA extends Indicator {
    public enum Rank {

        MA5(5), MA10(10), MA20(20), MA30(30), MA40(40), MA50(50), MA60(60);

        int _n;

        Rank(int n) {
            _n = n;
        }

        public int N() {
            return _n;
        }

        public String KEY() {
            return K_MA + N();
        }
    }

    public static final String K_MA = "MA";
    private Rank[] ranks;

    public MA(Rank[] _rank) {
        super(Type.MA);
        ranks = _rank;
    }

    @Override
    public int[] getParams() {
        return null;
    }

    @Override
    public String[] getKeys() {
        String[] keys = new String[ranks.length];
        for (int i = 0; i < ranks.length; i++) {
            keys[i] = ranks[i].KEY();
        }
        return keys;
    }

    @Override
    public void calc(int index) {
        for (int i = 0; i < ranks.length; i++) {
            String key = ranks[i].KEY();
            float ma = MA(K_CLOSE, index, ranks[i].N());
            if (ma >= 0) {
                putVaule(key, ma, index);
            }
        }
    }
}

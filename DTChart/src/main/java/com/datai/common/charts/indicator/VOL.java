package com.datai.common.charts.indicator;

/**
 * Created by Yanmin on 2016/5/13.
 */
public class VOL extends Indicator {
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

    public VOL() {
        super(Type.VOL);
        ranks = new Rank[]{Rank.MA5, Rank.MA10};
    }

    @Override
    public int[] getParams() {
        return null;
    }

    @Override
    public String[] getKeys() {
        String[] keys = new String[ranks.length + 1];
        keys[0] = K_VOL;
        for (int i = 0; i < ranks.length; i++) {
            keys[i + 1] = K_VOL + "_" + ranks[i].KEY();
        }
        return keys;
    }

    public String getKeyAlias(String key) {
        String prefix = K_VOL + "_";
        if (key.startsWith(prefix))
            return key.substring(prefix.length());
        return key;
    }

    @Override
    public void calc(int index) {
        for (int i = 0; i < ranks.length; i++) {
            String key = K_VOL + "_" + ranks[i].KEY();
            float ma = MA(K_VOL, index, ranks[i].N());
            if (ma >= 0) {
                putVaule(key, ma, index);
            }
        }
    }
}

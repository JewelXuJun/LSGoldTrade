package com.datai.common.charts.indicator;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Monking on 2016/1/19.
 */
public abstract class Indicator {
    public enum Type {
        MA("MA"),
        RISE("RISE"),
        MACD("MACD"),
        KDJ("KDJ"),
        RSI("RSI"),
        WR("WR"),
        BOLL("BOLL"),
        BIAS("BIAS"),
        VOL("VOL");//成交量

        String describe;

        Type(String _desc) {
            describe = _desc;
        }

        public String toString() {
            return describe;
        }
    }


    public static final String K_TIME = "TIME";
    public static final String K_OPEN = "OPEN";
    public static final String K_CLOSE = "CLOSE";
    public static final String K_HIGH = "HIGH";
    public static final String K_LOW = "LOW";
    public static final String K_VOL = "VOL";
    public static final String K_VOL_MONEY = "VOL_MONEY";

    private final Type mType;
    private List<HashMap<String, Object>> mData;

    public Indicator(Type type) {
        mType = type;
    }

    public Type getType() {
        return mType;
    }

    public void setData(List<HashMap<String, Object>> data) {
        mData = data;
    }

    public abstract int[] getParams();

    public abstract String[] getKeys();

    public abstract void calc(final int index);

    public String getKeyAlias(String key) {
        return key;
    }

    public final void putVaule(final String KEY, final float Vaule, final int index) {
        mData.get(index).put(KEY, Vaule);
    }

    public final Object VALUE(final String KEY, final int index) {
        if (mData == null)
            return null;

        int size = mData.size();
        if (size <= 0 || index < 0 || index >= size)
            return null;

        Object o = mData.get(index).get(KEY);
        return o;
    }

    public final float VALUE_F(final String KEY, final int index) {
        Object o = VALUE(KEY, index);
        if (o == null)
            return 0;
        return (float) o;
    }

    public final float OPEN(final int index) {
        return (float) VALUE("OPEN", index);
    }

    public final float CLOSE(final int index) {
        return (float) VALUE("CLOSE", index);
    }

    public final float HIGH(final int index) {
        return (float) VALUE("HIGH", index);
    }

    public final float LOW(final int index) {
        return (float) VALUE("LOW", index);
    }

    public final float REF(final String KEY, final int index, final int A) {
        Object o = VALUE(KEY, index - A);
        if (o == null)
            return -1;
        return (float) o;
    }

    public final float EMA(final String KEY, final int index, final int N, final boolean bNew) {
        String _KEY = "EMA_" + KEY + "_" + N;
        if (!bNew) {
            Object o = VALUE(_KEY, index);
            if (o != null)
                return (float) o;
        }
        float X = VALUE_F(KEY, index);
        float Y, Y0;
        if (index <= 0)
            Y = X;
        else {
            Y0 = EMA(KEY, index - 1, N, false);
            Y = (2 * X + (N - 1) * Y0) / (N + 1);
        }
        mData.get(index).put(_KEY, Y);
        return Y;
    }

    public float SMA(final String KEY, final int index, final int N, final int M, final boolean bNew) {
        String _KEY = "SMA_" + KEY + "_" + N + "_" + M;
        if (!bNew) {
            Object o = VALUE(_KEY, index);
            if (o != null)
                return (float) o;
        }
        float X = VALUE_F(KEY, index);
        float Y, Y0;
        if (index <= 0)
            Y = (M * X) / N;
        else {
            Y0 = SMA(KEY, index - 1, N, M, false);
            Y = (M * X + (N - M) * Y0) / N;
        }
        putVaule(_KEY, Y, index);
        return Y;
    }

    public float MA(final String KEY, final int index, final int N) {
        String _KEY = "MA_" + KEY + "_" + N;
        Object o = VALUE(_KEY, index);
        if (o != null)
            return (float) o;

        int start = index - N + 1;
        if (start < 0)
            return -1;

        float sum = 0f;
        for (int i = start; i <= index; i++) {
            sum += (float) VALUE(KEY, i);
        }

        float Y = sum / N;
        putVaule(_KEY, Y, index);

        return Y;
    }

    public float AVEDEV(final String KEY, final int index, final int N) {
        int start = index - N + 1;
        if (start < 0)
            return -1;

        float MA_N = MA(KEY, index, N);

        float sum = 0f;
        for (int i = start; i <= index; i++) {
            sum += Math.abs((float) VALUE(KEY, i) - MA_N);
        }

        return sum / N;
    }

    public float VARP(final String KEY, final int index, final int N) {
        int start = index - N + 1;
        if (start < 0)
            return -1;

        float MA_N = MA(KEY, index, N);

        float sum = 0f;
        for (int i = start; i <= index; i++) {
            sum += Math.pow(VALUE_F(KEY, i) - MA_N, 2);
        }

        return sum / N;
    }

    public float VAR(final String KEY, final int index, final int N) {
        int start = index - N + 1;
        if (start < 0)
            return -1;

        float VARP = VARP(KEY, index, N);

        return VARP * N / (N - 1);
    }

    public float STD(final String KEY, final int index, final int N) {
        int start = index - N + 1;
        if (start < 0)
            return -1;

        float VAR = VAR(KEY, index, N);

        return (float) Math.sqrt(VAR);
    }

    public float HHV(final String KEY, final int index, final int N, final boolean bNew) {
        String _KEY = "HHV_" + KEY + "_" + N;
        if (!bNew) {
            Object o = VALUE(_KEY, index);
            if (o != null)
                return (float) o;
        }
        float X = (float) VALUE(KEY, index);
        float Y, Y0;
        if (index <= 0)
            Y = X;
        else {
            Y0 = HHV(KEY, index - 1, N, false);
            Y = Math.max(X, Y0);
        }
        putVaule(_KEY, Y, index);
        return Y;
    }

    public float HHV2(final String KEY, final int index, final int N, final boolean bNew) {
        String _KEY = "HHV_" + KEY + "_" + N;
        if (!bNew) {
            Object o = VALUE(_KEY, index);
            if (o != null)
                return (float) o;
        }

        float Y = 0f;

        for (int i = index; i > index - N; i--) {
            if (i < 0)
                break;
            Object V = VALUE(KEY, i);
            if (V != null) {
                float X = (float) V;
                Y = Math.max(Y, X);
            }
        }
        putVaule(_KEY, Y, index);
        return Y;
    }

    public float LLV(final String KEY, final int index, final int N, final boolean bNew) {
        String _KEY = "LLV_" + KEY + "_" + N;
        if (!bNew) {
            Object o = VALUE(_KEY, index);
            if (o != null)
                return (float) o;
        }
        float X = (float) VALUE(KEY, index);
        float Y, Y0;
        if (index <= 0)
            Y = X;
        else {
            Y0 = LLV(KEY, index - 1, N, false);
            Y = Math.min(X, Y0);
        }
        putVaule(_KEY, Y, index);
        return Y;
    }

    public float LLV2(final String KEY, final int index, final int N, final boolean bNew) {
        String _KEY = "LLV_" + KEY + "_" + N;
        if (!bNew) {
            Object o = VALUE(_KEY, index);
            if (o != null)
                return (float) o;
        }

        float Y = Float.MAX_VALUE;

        for (int i = index; i > index - N; i--) {
            if (i < 0)
                break;
            Object V = VALUE(KEY, i);
            if (V != null) {
                float X = (float) V;
                Y = Math.min(Y, X);
            }
        }
        putVaule(_KEY, Y, index);
        return Y;
    }
}

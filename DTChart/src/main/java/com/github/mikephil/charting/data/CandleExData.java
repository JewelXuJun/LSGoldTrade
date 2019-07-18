package com.github.mikephil.charting.data;

/**
 * Created by Administrator on 2016/1/13.
 */
public class CandleExData {
    private long time;
    private float ma5;
    private float ma10;
    private float ma20;

    //MACD
    private float macd;
    private float diff;
    private float dea;

    //KJD

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getMa5() {
        return ma5;
    }

    public void setMa5(float ma5) {
        this.ma5 = ma5;
    }

    public float getMa10() {
        return ma10;
    }

    public void setMa10(float ma10) {
        this.ma10 = ma10;
    }

    public float getMa20() {
        return ma20;
    }

    public void setMa20(float ma20) {
        this.ma20 = ma20;
    }

    public float getMacd() {
        return macd;
    }

    public void setMacd(float macd) {
        this.macd = macd;
    }

    public float getDiff() {
        return diff;
    }

    public void setDiff(float diff) {
        this.diff = diff;
    }

    public float getDea() {
        return dea;
    }

    public void setDea(float dea) {
        this.dea = dea;
    }
}

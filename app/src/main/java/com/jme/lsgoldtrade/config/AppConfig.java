package com.jme.lsgoldtrade.config;

public class AppConfig {

    public static final String PageSize_10 = "10";

    public static final long Second = 1000;
    public static final long Second2 = 2 * Second;
    public static final long Second5 = 5 * Second;
    public static final long Second10 = 10 * Second;
    public static final long Second15 = 15 * Second;
    public static final long Minute = 60 * Second;
    public static final long Hour = 60 * Minute;
    public static final long Day = 24 * Hour;
    public static final long Week = 7 * Day;
    public static final long Month = 30 * Day;

    public static long TimeInterval_NetWork = Second5;
    public static long TimeInterval_WiFi = Second2;

    public static final long Price_Divisor = 100;
    public static final long Rate_Divisor = 1000000;

    public static final int Lentth_Limit = 2;

}

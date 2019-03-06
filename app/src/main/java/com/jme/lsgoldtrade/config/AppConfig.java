package com.jme.lsgoldtrade.config;

public class AppConfig {

    public static final String PageSize_10 = "10";

    public static final long Second = 1000;
    public static final long Second2 = 2 * Second;
    public static final long Second5 = 5 * Second;
    public static final long Second10 = 10 * Second;
    public static final long Second15 = 15 * Second;
    public static final long Minute = 60 * Second;

    public static final long TimeInterval_SYNC = 5 * Second;

    public static long TimeInterval_NetWork = Second5;
    public static long TimeInterval_WiFi = Second2;

    public static final long Price_Divisor = 100;
    public static final long Rate_Divisor = 1000000;
    public static final long HandWeight_Divisor = 1000000;

    public static final int Length_Limit = 2;
    public static final int MaxLength = 500;

    public static String Select_ContractId = "Ag(T+D)";

}

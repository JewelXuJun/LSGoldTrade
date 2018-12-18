package com.datai.common.charts.common;

import android.graphics.Color;

import com.datai.common.charts.indicator.Indicator;
import com.datai.common.charts.indicator.MA;
import com.datai.common.charts.kchart.KData;

/**
 * Created by Monking on 2016/1/18.
 */
public class Config {
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    public static final int STYLE_DAY = 0;
    public static final int STYLE_NIGHT = 1;

    public static int HighLight_Color = Color.WHITE;
    public static int Border_Color = Color.RED;
    public static float Border_Width = 0.8f;
    public static int Grid_Color = Color.argb(100, 255, 0, 0);//Color.RED;
    public static float Grid_Line_Width = 1f;
    public static int X_Text_Color = Color.RED;
    public static int Y_Text_Color = Color.WHITE;
    public static float VALUE_TEXT_SIZE = 9f;
    public static float Line_Width = 1f;
    public static int Candle_Shadow_Color = Color.WHITE;

    public static int Increasing_Color = Color.rgb(255, 51, 51);//Color.RED;
    public static int Decreasing_Color = Color.rgb(51, 204, 0);//Color.rgb(84, 252, 252);
    //    public static int Time_Color = Color.BLACK;
    public static int Default_Color = Color.BLACK;
    public static int Average_Color = Color.rgb(220, 171, 2);
    public static int Current_Color = Color.WHITE;
    public static int Current_Fill_Color = Color.WHITE;
    public static int Default_KChart_Text_Color = Color.BLACK;

    public static int[] LineColorGroup = {Color.WHITE, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.rgb(136, 136, 136)};

    public static MA.Rank[] MARanks = new MA.Rank[]{MA.Rank.MA5, MA.Rank.MA10, MA.Rank.MA20, MA.Rank.MA30};
    public static Indicator.Type[] Indicators = new Indicator.Type[]{Indicator.Type.VOL, Indicator.Type.MACD, Indicator.Type.KDJ, Indicator.Type.RSI
            , Indicator.Type.WR, Indicator.Type.BIAS, Indicator.Type.BOLL};
//    public static Indicator.Type IndicatorDefault = Indicator.Type.VOL;

    public static int InitVisibleEntryCount = 80;
    public static int MinXRangeDefault = 40;
    public static int MaxXRangeDefault = 200;

    public static long TimeInterval_OneMinute = MINUTE;
    public static long TimeInterval_FiveMinute = 5 * MINUTE;

    public static KData.Unit[] MinuteType = new KData.Unit[]{KData.Unit.MIN1, KData.Unit.MIN5, KData.Unit.MIN15, KData.Unit.MIN30, KData.Unit.MIN60};

    static {
        setStyle(STYLE_DAY);
    }

    public static String ColorToStr(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return "#" + Integer.toHexString(color & 0x00FFFFFF).toUpperCase();
    }

    public static void setStyle(int style) {
        switch (style) {
            case STYLE_DAY:
                HighLight_Color = Color.rgb(34, 34, 34);
                Border_Color = Color.GRAY;
                Grid_Color = Color.argb(100, 136, 136, 136);//Color.GRAY;
                X_Text_Color = Color.rgb(34, 34, 34);//Color.RED;
                Y_Text_Color = Color.rgb(34, 34, 34);
                Candle_Shadow_Color = Color.rgb(84, 102, 18);
                Increasing_Color = Color.rgb(230, 64, 32);//Color.RED;
                Decreasing_Color = Color.rgb(0, 183, 103);//Color.rgb(84, 102, 18);
//                Time_Color = Color.rgb(34, 34, 34);
                Default_Color = Color.rgb(26, 26, 26);
                Current_Color = Color.rgb(75, 148, 250);
                Average_Color = Color.rgb(255, 191, 2);
                Current_Fill_Color = Color.rgb(227, 239, 255);
                Default_KChart_Text_Color = Color.rgb(34, 34, 34);
                LineColorGroup = new int[]{Color.rgb(255, 153, 51), Color.rgb(46, 138, 230), Color.rgb(204, 41, 150), Color.BLACK, Color.GREEN};
                break;
            case STYLE_NIGHT:
                HighLight_Color = Color.WHITE;
                Border_Color = Color.RED;
                Grid_Color = Color.argb(100, 255, 0, 0);//Color.RED;
                X_Text_Color = Color.WHITE;//Color.RED;
                Y_Text_Color = Color.WHITE;
                Candle_Shadow_Color = Color.WHITE;
                Increasing_Color = Color.rgb(233, 48, 48);//Color.RED;
                Decreasing_Color = Color.rgb(0, 163, 0);//Color.rgb(84, 252, 252);
//                Time_Color = Color.BLACK;
                Default_Color = Color.BLACK;
                Average_Color = Color.rgb(220, 171, 2);
                Current_Color = Color.WHITE;
                Default_KChart_Text_Color = Color.BLACK;
                LineColorGroup = new int[]{Color.WHITE, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.rgb(136, 136, 136)};
                break;
        }
    }

}

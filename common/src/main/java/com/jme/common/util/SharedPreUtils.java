package com.jme.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * SharedPreferances工具类
 * Created by zhangzhongqiang on 2015/10/15.
 */
public class SharedPreUtils {

    public static final String Key_VersionCode = "VersionCode";

    public static final String Key_GuideFlag = "GuideFlag";
    public static final String Key_Instruction_GuideFlag = "InstructionGuideFlag";

    public static final String Key_OptionalCommodityCodes = "optionalCommodityCodes";
    public static final String KEY_OptionalPuerCodes = "optionalPuerCodes";
    public static final String KEY_OptionalSpotCodes = "optionalSpotCodes";
    public static final String KEY_OptionalRedwineCodes = "optionalRedwineCodes";
    public static final String KEY_OptionalAllCodes = "optionalAllCodes";
    public static final String KEY_OptionalAllCodesStr = "optionalAllCodesStr";

    public static final String Key_CommodityAccount = "LoginAccount";
    public static final String Key_CommodityPriceVoList = "CommodityPriceVoList";
    public static final String Key_DetailCommodityVoList = "detailCommodityVoList";
    public static final String Key_RedwineDetailCommodityVoList = "RedwineDetailCommodityVoList";
    public static final String Key_SpotDetailCommodityVoList = "SpotDetailCommodityVoList";

    public static final String Key_PuerAccount = "PuerAccount";
    public static final String Key_SpotAccount = "SpotAccount";
    public static final String Key_RedwineAccount = "RedwineAccount";

    public static final String Key_CounterPartyName = "counterPartyName";
    public static final String Key_CounterPartyCode = "counterPartyCode";
    public static final String Key_RedwineCounterPartyName = "RedwineCounterPartyName";
    public static final String Key_RedwineCounterPartyCode = "RedwineCounterPartyCode";
    public static final String Key_SpotCounterPartyName = "SpotCounterPartyName";
    public static final String Key_SpotCounterPartyCode = "SpotCounterPartyCode";

    public static final String Key_DefaultLoginType = "DefaultLoginType";
    public static final String Key_StateCode = "stateCode";
    public static final String Key_DeliveryVoList = "deliveryVoList";

    public static final String Key_CommodityDetailLayer = "CommodityDetailLayer";
    public static final String Key_CommodityTradeLayer = "CommodityTradeLayer";
    public static final String Key_PuerTradeLayer = "PuerTradeLayer";

    public static final String Key_SignStatusVo = "SignStatusVo";
    public static final String Key_ProductVoList = "ProductVoList";
    public static final String Key_PuerList = "Key_PuerList_New";
    public static final String Key_RedwineList = "Key_RedwineList";
    public static final String Key_SpotList = "Key_SpotList";
    public static final String Key_SpotProductVoList = "SpotProductVoList";
    public static final String Key_RedwineProductVoList = "RedwineProductVoList";

    public static final String Key_Setting_WiFi = "SettingWiFi";

    public static final String Key_TimeInterval_NetWork = "TimeIntervalNetWork";
    public static final String Key_TimeInterval_WiFi = "TimeIntervalWiFi";

    public static final String Key_WPMarketListCodeStr = "WPMarketListCodeStr";

    public static final String Key_DownLoad_Size = "DownLoadSize";

    public static final String Key_Close_Time = "CloseTime";

    public static final String Key_APP_EXIT_TIME = "AppExitTime";
    public static final String Key_PHONE_NUMBER = "PhoneNumber";
    public static final String Key_PASSWORD = "Password";
    public static final String Key_SETTING_CIRCLE = "Setting";
    public static final String key_OPEN_ACCOUNT_SID = "OpenAccountSid";
    public static final String KEY_OPEN_ACCOUNT_KEEP_LOGIN = "keep_login";

    public static final String Key_RX_BUS = "RxBus";
    public static final String Key_RX_BUS_TYPE = "RxBusType";
    public static final String Key_RX_BUS_TRADE_TYPE = "RxBusBuyType";
    public static final String Key_RX_BUS_ID = "RxBusId";
    public static final String Key_RX_BUS_NAME = "RxBusName";

    private static long startTime = 0L;
    private static long endTime = 0L;

    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static String serialize(Object object) throws IOException {
        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
//        Log.d("serial", "serialize str =" + serStr);
        endTime = System.currentTimeMillis();
//        Log.d("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deSerialization(String str) throws IOException, ClassNotFoundException {
        startTime = System.currentTimeMillis();
        String redStr = "";
        if (str != null) {
            redStr = java.net.URLDecoder.decode(str, "UTF-8");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        endTime = System.currentTimeMillis();
//        Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
        return object;
    }

    public static void saveObject(String strObject, Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(name, strObject);
        edit.commit();
    }

    public static String getObject(Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(name, null);
    }

    public static void setInteger(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInteger(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);
    }

    public static boolean setStringSet(Context context, String key, Set<String> values) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
        editor.putStringSet(key, values);
        return editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getStringSet(key, new HashSet<String>());
    }

}

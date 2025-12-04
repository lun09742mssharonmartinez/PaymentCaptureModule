package com.paymentcapture.module.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paymentcapture.module.models.PaymentData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据存储工具类
 */
public class DataStorage {

    private static final String PREF_NAME = "payment_data";
    private static final String KEY_DATA_LIST = "data_list";
    private static Gson gson = new Gson();

    /**
     * 保存支付数据
     */
    public static void savePaymentData(Context context, PaymentData data) {
        List<PaymentData> list = loadPaymentData(context);
        list.add(0, data);  // 添加到列表开头

        // 限制最多保存 100 条
        if (list.size() > 100) {
            list = list.subList(0, 100);
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(list);
        prefs.edit().putString(KEY_DATA_LIST, json).apply();
    }

    /**
     * 加载支付数据列表
     */
    public static List<PaymentData> loadPaymentData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_DATA_LIST, null);

        if (json != null) {
            Type type = new TypeToken<List<PaymentData>>(){}.getType();
            return gson.fromJson(json, type);
        }

        return new ArrayList<>();
    }

    /**
     * 清空数据
     */
    public static void clearData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}

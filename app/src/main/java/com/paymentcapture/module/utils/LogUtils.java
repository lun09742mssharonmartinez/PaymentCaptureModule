package com.paymentcapture.module.utils;

import de.robv.android.xposed.XposedBridge;

/**
 * 日志工具类
 */
public class LogUtils {

    private static final String TAG = "PaymentCapture";

    public static void log(String message) {
        XposedBridge.log(TAG + ": " + message);
    }

    public static void logError(String message, Throwable throwable) {
        XposedBridge.log(TAG + " ERROR: " + message);
        if (throwable != null) {
            XposedBridge.log(throwable);
        }
    }
}

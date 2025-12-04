package com.paymentcapture.module.hooks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支付宝 Hook 实现
 * 用于拦截支付链接和二维码数据
 */
public class AlipayHook {

    private static final String TAG = "AlipayHook";

    public static void init(XC_LoadPackage.LoadPackageParam lpparam) {
        
        // Hook 1: 拦截 Activity 跳转，捕获支付链接
        hookActivityIntent(lpparam);
        
        // Hook 2: 拦截二维码扫描结果
        hookQRCodeScan(lpparam);
        
        // Hook 3: 拦截支付页面数据
        hookPaymentPage(lpparam);
        
        // Hook 4: 拦截网络请求（URL）
        hookNetworkRequest(lpparam);
        
        // Hook 5: 拦截支付宝 SDK 调用（重点！）
        hookAlipaySDK(lpparam);
        
        // Hook 6: 拦截支付结果回调
        hookPaymentCallback(lpparam);
    }

    /**
     * Hook Activity Intent，拦截支付链接
     */
    private static void hookActivityIntent(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(
                Activity.class,
                "startActivity",
                Intent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Intent intent = (Intent) param.args[0];
                        
                        if (intent != null) {
                            // 获取 Intent 数据
                            Uri data = intent.getData();
                            String dataString = intent.getDataString();
                            Bundle extras = intent.getExtras();
                            
                            // 检查是否是支付相关的 Intent
                            if (dataString != null && isPaymentUrl(dataString)) {
                                XposedBridge.log(TAG + ": 拦截到支付链接: " + dataString);
                                
                                // 提取支付信息
                                String paymentInfo = extractPaymentInfo(dataString);
                                
                                // 保存或广播数据
                                broadcastPaymentData(dataString, paymentInfo);
                            }
                            
                            // 检查 extras
                            if (extras != null) {
                                for (String key : extras.keySet()) {
                                    Object value = extras.get(key);
                                    if (value != null && value.toString().contains("alipays://") || 
                                        value.toString().contains("qrcode")) {
                                        XposedBridge.log(TAG + ": 拦截到 Extra - " + key + ": " + value);
                                    }
                                }
                            }
                        }
                    }
                }
            );
            XposedBridge.log(TAG + ": Activity Intent Hook 成功");
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": Activity Intent Hook 失败: " + t.getMessage());
        }
    }

    /**
     * Hook 二维码扫描
     */
    private static void hookQRCodeScan(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // 常见的二维码处理类（需要根据实际支付宝版本调整）
            String[] qrCodeClasses = {
                "com.alipay.mobile.scan.as.main.ScanActivity",
                "com.alipay.android.phone.scan.ScanActivity",
                "com.alipay.mobile.qrcode.QRCodeActivity"
            };
            
            for (String className : qrCodeClasses) {
                try {
                    Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);
                    
                    // Hook 所有方法，查找处理二维码结果的方法
                    for (Method method : clazz.getDeclaredMethods()) {
                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                // 记录方法调用和参数
                                StringBuilder sb = new StringBuilder();
                                sb.append("QRCode 方法调用: ").append(method.getName()).append("(");
                                
                                if (param.args != null) {
                                    for (int i = 0; i < param.args.length; i++) {
                                        if (param.args[i] != null) {
                                            String argStr = param.args[i].toString();
                                            if (argStr.contains("http") || argStr.contains("alipay") || 
                                                argStr.length() > 50) {
                                                sb.append("\n  参数").append(i).append(": ").append(argStr);
                                                XposedBridge.log(TAG + ": " + sb.toString());
                                            }
                                        }
                                    }
                                }
                                
                                // 检查返回值
                                if (param.getResult() != null) {
                                    String result = param.getResult().toString();
                                    if (result.contains("http") || result.contains("alipay")) {
                                        XposedBridge.log(TAG + ": QRCode 结果: " + result);
                                        broadcastPaymentData(result, "扫码支付");
                                    }
                                }
                            }
                        });
                    }
                    
                    XposedBridge.log(TAG + ": QRCode Hook 成功 - " + className);
                    
                } catch (Throwable t) {
                    // 类不存在，继续尝试下一个
                }
            }
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": QRCode Hook 失败: " + t.getMessage());
        }
    }

    /**
     * Hook 支付页面
     */
    private static void hookPaymentPage(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // 支付页面相关类（根据实际版本调整）
            String[] paymentClasses = {
                "com.alipay.android.phone.mobilecommon.ui.CashierActivity",
                "com.alipay.mobile.paymentclient.cashier.CashierActivity"
            };
            
            for (String className : paymentClasses) {
                try {
                    Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);
                    
                    // Hook onCreate
                    XposedHelpers.findAndHookMethod(clazz, "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Activity activity = (Activity) param.thisObject;
                            Intent intent = activity.getIntent();
                            
                            if (intent != null) {
                                String url = intent.getDataString();
                                Bundle extras = intent.getExtras();
                                
                                XposedBridge.log(TAG + ": 支付页面打开");
                                
                                if (url != null) {
                                    XposedBridge.log(TAG + ": 支付 URL: " + url);
                                    broadcastPaymentData(url, "支付页面");
                                }
                                
                                if (extras != null) {
                                    for (String key : extras.keySet()) {
                                        Object value = extras.get(key);
                                        XposedBridge.log(TAG + ": 支付参数 - " + key + ": " + value);
                                    }
                                }
                            }
                        }
                    });
                    
                    XposedBridge.log(TAG + ": 支付页面 Hook 成功 - " + className);
                    
                } catch (Throwable t) {
                    // 类不存在，继续
                }
            }
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": 支付页面 Hook 失败: " + t.getMessage());
        }
    }

    /**
     * Hook 网络请求
     */
    private static void hookNetworkRequest(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook OkHttp / HttpURLConnection 等网络库
            
            // 示例：Hook URL 类
            XposedHelpers.findAndHookConstructor(
                "java.net.URL",
                lpparam.classLoader,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String url = (String) param.args[0];
                        if (url != null && (url.contains("alipay") || url.contains("pay"))) {
                            XposedBridge.log(TAG + ": 网络请求 URL: " + url);
                        }
                    }
                }
            );
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": 网络请求 Hook 失败: " + t.getMessage());
        }
    }

    /**
     * 判断是否是支付相关 URL
     */
    private static boolean isPaymentUrl(String url) {
        if (url == null) return false;
        
        return url.contains("alipays://") || 
               url.contains("alipay.com") ||
               url.contains("qrcode") ||
               url.contains("pay") ||
               url.contains("cashier");
    }

    /**
     * 提取支付信息（金额、订单号等）
     */
    private static String extractPaymentInfo(String url) {
        StringBuilder info = new StringBuilder();
        
        // 提取金额
        Pattern amountPattern = Pattern.compile("money=([0-9.]+)|amount=([0-9.]+)|totalAmount=([0-9.]+)");
        Matcher amountMatcher = amountPattern.matcher(url);
        if (amountMatcher.find()) {
            String amount = amountMatcher.group(1);
            if (amount == null) amount = amountMatcher.group(2);
            if (amount == null) amount = amountMatcher.group(3);
            info.append("金额: ").append(amount).append("元\n");
        }
        
        // 提取订单号
        Pattern orderPattern = Pattern.compile("orderId=([^&]+)|orderNo=([^&]+)|tradeNo=([^&]+)");
        Matcher orderMatcher = orderPattern.matcher(url);
        if (orderMatcher.find()) {
            String orderId = orderMatcher.group(1);
            if (orderId == null) orderId = orderMatcher.group(2);
            if (orderId == null) orderId = orderMatcher.group(3);
            info.append("订单号: ").append(orderId).append("\n");
        }
        
        return info.toString();
    }

    /**
     * Hook 支付宝 SDK 调用（核心功能！）
     * 拦截第三方 APP 调用支付宝 SDK 进行支付的场景
     */
    private static void hookAlipaySDK(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // 支付宝 SDK 的核心类：PayTask
            // 第三方 APP 通过这个类调起支付
            String[] sdkClasses = {
                "com.alipay.sdk.app.PayTask",           // 支付宝 SDK 主类
                "com.alipay.sdk.app.H5PayCallback",      // H5 支付回调
                "com.alipay.sdk.app.AuthTask"            // 授权任务
            };
            
            for (String className : sdkClasses) {
                try {
                    Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);
                    
                    // Hook pay() 方法 - 这是支付的入口
                    XposedBridge.hookAllMethods(clazz, "pay", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": ===== SDK 支付调用 =====");
                            
                            // 获取支付订单信息字符串
                            if (param.args != null && param.args.length > 0) {
                                String orderInfo = param.args[0].toString();
                                
                                XposedBridge.log(TAG + ": SDK 订单信息: " + orderInfo);
                                
                                // 解析订单信息
                                String amount = extractFromOrderInfo(orderInfo, "total_amount");
                                String subject = extractFromOrderInfo(orderInfo, "subject");
                                String outTradeNo = extractFromOrderInfo(orderInfo, "out_trade_no");
                                
                                StringBuilder info = new StringBuilder();
                                info.append("\n商品: ").append(subject);
                                info.append("\n金额: ").append(amount);
                                info.append("\n商户订单号: ").append(outTradeNo);
                                
                                XposedBridge.log(TAG + ": SDK 支付详情: " + info.toString());
                                
                                // 广播数据
                                broadcastPaymentData(orderInfo, "SDK支付" + info.toString());
                            }
                        }
                        
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            // 获取支付结果
                            if (param.getResult() != null) {
                                String result = param.getResult().toString();
                                XposedBridge.log(TAG + ": SDK 支付结果: " + result);
                            }
                        }
                    });
                    
                    // Hook payV2() 方法 - 新版支付接口
                    XposedBridge.hookAllMethods(clazz, "payV2", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": SDK V2 支付调用");
                            if (param.args != null && param.args.length > 0) {
                                XposedBridge.log(TAG + ": V2 订单信息: " + param.args[0]);
                                broadcastPaymentData(param.args[0].toString(), "SDK V2支付");
                            }
                        }
                    });
                    
                    XposedBridge.log(TAG + ": SDK Hook 成功 - " + className);
                    
                } catch (Throwable t) {
                    // 类不存在，继续尝试
                }
            }
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": SDK Hook 失败: " + t.getMessage());
        }
    }

    /**
     * Hook 支付结果回调
     */
    private static void hookPaymentCallback(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook 支付结果回调接口
            XposedHelpers.findAndHookMethod(
                "com.alipay.sdk.app.PayTask",
                lpparam.classLoader,
                "fetchOrderInfoFromH5PayUrl",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args != null && param.args[0] != null) {
                            String h5Url = param.args[0].toString();
                            XposedBridge.log(TAG + ": H5 支付 URL: " + h5Url);
                            broadcastPaymentData(h5Url, "H5支付");
                        }
                    }
                }
            );
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": 回调 Hook 失败: " + t.getMessage());
        }
    }

    /**
     * 从订单信息中提取字段
     */
    private static String extractFromOrderInfo(String orderInfo, String key) {
        try {
            // 订单信息格式：key1="value1"&key2="value2"
            int startIndex = orderInfo.indexOf(key + "=\"");
            if (startIndex == -1) {
                startIndex = orderInfo.indexOf(key + "=");
                if (startIndex == -1) return "未知";
            }
            
            startIndex = orderInfo.indexOf("\"", startIndex) + 1;
            if (startIndex == 0) {
                startIndex = orderInfo.indexOf("=", orderInfo.indexOf(key)) + 1;
            }
            
            int endIndex = orderInfo.indexOf("\"", startIndex);
            if (endIndex == -1) {
                endIndex = orderInfo.indexOf("&", startIndex);
            }
            if (endIndex == -1) {
                endIndex = orderInfo.length();
            }
            
            return orderInfo.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "解析失败";
        }
    }

    /**
     * 广播支付数据到模块应用
     */
    private static void broadcastPaymentData(String url, String type) {
        try {
            // TODO: 通过广播或文件将数据传递给模块 UI
            // 这里可以使用 Intent、文件存储、或者 ContentProvider
            
            XposedBridge.log(TAG + ": ========== 捕获到支付数据 ==========");
            XposedBridge.log(TAG + ": 类型: " + type);
            XposedBridge.log(TAG + ": URL: " + url);
            XposedBridge.log(TAG + ": 时间: " + System.currentTimeMillis());
            XposedBridge.log(TAG + ": =====================================");
            
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": 数据广播失败: " + t.getMessage());
        }
    }
}

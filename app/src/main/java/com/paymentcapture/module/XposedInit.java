package com.paymentcapture.module;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import com.paymentcapture.module.hooks.AlipayHook;

/**
 * Xposed 模块入口类
 * 当目标应用加载时，此类会被 Xposed 框架调用
 */
public class XposedInit implements IXposedHookLoadPackage {

    private static final String TAG = "PaymentCapture";
    
    // 目标应用包名
    private static final String ALIPAY_PACKAGE = "com.eg.android.AlipayGphone";  // 支付宝
    private static final String WECHAT_PACKAGE = "com.tencent.mm";               // 微信（可选）

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        
        // 检查是否是目标应用
        if (lpparam.packageName.equals(ALIPAY_PACKAGE)) {
            XposedBridge.log(TAG + ": 检测到支付宝，开始 Hook...");
            
            // 初始化支付宝 Hook
            AlipayHook.init(lpparam);
            
            XposedBridge.log(TAG + ": 支付宝 Hook 完成！");
            
        } else if (lpparam.packageName.equals(WECHAT_PACKAGE)) {
            XposedBridge.log(TAG + ": 检测到微信（暂未实现）");
            // TODO: 实现微信 Hook
        }
    }
}

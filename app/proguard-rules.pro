# ProGuard 规则

# 保留 Xposed API
-keep class de.robv.android.xposed.** { *; }

# 保留模块入口类
-keep class com.paymentcapture.module.XposedInit { *; }

# 保留 Hook 类
-keep class com.paymentcapture.module.hooks.** { *; }

# 保留数据模型
-keep class com.paymentcapture.module.models.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# 保留反射相关
-keepattributes *Annotation*,Signature,Exception,InnerClasses

# 支付链接抓取模块 (Payment Capture Module)

这是一个基于 LSPosed/Xposed 框架的支付链接抓取模块，类似于 X2.apk。

## 功能特性

- ✅ Hook 支付宝应用，拦截支付链接
- ✅ 提取支付二维码数据
- ✅ 显示支付金额、订单信息
- ✅ 支持多种支付场景
- ✅ 模块管理界面

## 环境要求

- Android 8.0+ (API 26+)
- 已 Root 的设备
- LSPosed 框架 或 Xposed 框架
- Magisk (推荐)

## 项目结构

```
PaymentCaptureModule/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paymentcapture/
│   │   │   │   ├── MainActivity.java          # 主界面
│   │   │   │   ├── XposedInit.java           # Xposed 入口
│   │   │   │   ├── hooks/
│   │   │   │   │   ├── AlipayHook.java       # 支付宝 Hook
│   │   │   │   │   └── WeChatHook.java       # 微信 Hook (可选)
│   │   │   │   ├── models/
│   │   │   │   │   └── PaymentData.java      # 支付数据模型
│   │   │   │   └── utils/
│   │   │   │       ├── LogUtils.java         # 日志工具
│   │   │   │       └── DataStorage.java      # 数据存储
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── assets/
│   │       └── xposed_init                    # Xposed 配置
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 编译步骤

1. 安装 Android SDK 和 Android Studio
2. 克隆本项目
3. 打开 Android Studio，导入项目
4. 构建 APK：`./gradlew assembleRelease`
5. 生成的 APK 位于：`app/build/outputs/apk/release/`

## 安装使用

1. **安装 Magisk** 到你的设备
2. **安装 LSPosed** 模块（通过 Magisk）
3. **安装本 APK**
4. 在 LSPosed 中启用本模块，勾选目标应用（支付宝）
5. 重启设备或重启支付宝
6. 打开本应用查看拦截的支付链接

## 注意事项

⚠️ **仅供学习研究使用，请勿用于非法用途**
⚠️ 本工具可能违反支付宝服务条款
⚠️ 使用风险自负

## 技术原理

- 使用 Xposed 框架 Hook Java 方法
- 拦截支付宝内部的支付链接处理方法
- 提取 URL、二维码数据、金额等信息
- 通过广播或文件存储传递数据到模块界面

## License

MIT License - 仅供学习研究

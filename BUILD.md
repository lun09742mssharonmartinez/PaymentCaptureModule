# 编译指南

## 环境准备

1. **安装 Java JDK 11+**
   ```powershell
   # 下载并安装 JDK
   # https://adoptium.net/
   ```

2. **安装 Android Studio**
   - 下载：https://developer.android.com/studio
   - 安装 Android SDK (API 34)

3. **配置环境变量**
   ```powershell
   # 设置 ANDROID_HOME
   $env:ANDROID_HOME = "C:\Users\你的用户名\AppData\Local\Android\Sdk"
   $env:PATH += ";$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\tools"
   ```

## 使用 Android Studio 编译（推荐）

1. 打开 Android Studio
2. 选择 "Open an Existing Project"
3. 选择 `C:\PaymentCaptureModule` 目录
4. 等待 Gradle 同步完成
5. 点击 `Build` -> `Build Bundle(s) / APK(s)` -> `Build APK(s)`
6. APK 位于：`app/build/outputs/apk/release/app-release.apk`

## 使用命令行编译

```powershell
cd C:\PaymentCaptureModule

# 第一次编译需要下载 Gradle
.\gradlew assembleRelease

# 编译完成后，APK 位于：
# app\build\outputs\apk\release\app-release.apk
```

## 签名 APK（可选）

生成的 APK 是 debug 签名的，可以直接安装。如需正式签名：

```powershell
# 生成密钥库
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias

# 签名 APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.jks app-release.apk my-key-alias
```

## 安装到设备

```powershell
# 通过 ADB 安装
adb install app\build\outputs\apk\release\app-release.apk

# 或者直接传输 APK 到手机安装
```

## 常见问题

### 问题 1: Gradle 下载慢
**解决方案**：使用阿里云镜像

在 `build.gradle` 中添加：
```gradle
repositories {
    maven { url 'https://maven.aliyun.com/repository/google' }
    maven { url 'https://maven.aliyun.com/repository/public' }
    maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
}
```

### 问题 2: SDK 未安装
**解决方案**：通过 Android Studio SDK Manager 安装需要的 SDK

### 问题 3: Build Tools 版本不匹配
**解决方案**：修改 `app/build.gradle` 中的 `compileSdk` 和 `targetSdk` 为已安装的版本

## 快速开始脚本

创建 `build.ps1`：
```powershell
# 清理旧编译
.\gradlew clean

# 编译 Release APK
.\gradlew assembleRelease

# 显示 APK 位置
Write-Host "编译完成！APK 位置："
Write-Host "app\build\outputs\apk\release\app-release.apk"
```

运行：
```powershell
.\build.ps1
```

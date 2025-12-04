# 使用教程

## 前置条件

### 1. Root 你的 Android 设备
- **Magisk** (推荐): https://github.com/topjohnwu/Magisk
- 教程：搜索 "Magisk 刷入教程" + 你的设备型号

### 2. 安装 LSPosed 框架
1. 下载 LSPosed: https://github.com/LSPosed/LSPosed/releases
2. 在 Magisk 中刷入 LSPosed 模块
3. 重启设备
4. 打开 LSPosed 应用

## 安装步骤

### 方法一：直付装机流程（类似图片中的方法）

1. **下载虚拟机应用 (Via)**
   - 下载链接：https://wwfj.lanzn.com/iM1fh381p5Si
   - 这是一个框架 APK，类似图片中提到的方式

2. **安装 Magisk**
   - 参考图片步骤 (1)
   - 点击 Magisk 设置 → 打开 Zygisk

3. **安装本模块**
   - 安装编译好的 `app-release.apk`
   - 在 LSPosed 中启用模块

4. **配置模块**
   - 打开 LSPosed
   - 找到 "支付抓包模块"
   - 勾选作用域：支付宝 (`com.eg.android.AlipayGphone`)
   - 重启支付宝或重启设备

### 方法二：标准安装流程

#### 步骤 1: 安装模块
```bash
adb install app-release.apk
```

#### 步骤 2: 在 LSPosed 中启用
1. 打开 LSPosed 应用
2. 点击 "模块" 标签
3. 找到 "支付抓包模块"
4. 启用模块开关

#### 步骤 3: 配置作用域
1. 点击模块进入详情
2. 选择 "应用作用域"
3. 勾选：
   - ✅ 支付宝 (com.eg.android.AlipayGphone)
   - ✅ 系统框架 (可选，用于全局 Hook)

#### 步骤 4: 重启生效
- 方法 1: 重启设备
- 方法 2: 在 LSPosed 中 "重新启动作用域应用"

## 使用方法

### 1. 打开模块应用
- 查看模块状态
- 应显示 "✅ 模块已激活"

### 2. 进行支付操作
在支付宝中进行以下操作，链接会被自动拦截：
- 扫描二维码支付
- 点击支付链接
- 转账操作
- 收款操作

### 3. 查看拦截数据
- 打开模块应用
- 查看拦截记录列表
- 显示：支付链接、金额、订单号、时间

### 4. 提取支付链接
- 长按列表项可复制完整链接
- 可用于：
  - 生成支付二维码
  - 分析支付参数
  - 重放支付请求（研究用）

## 工作原理

```
支付宝应用
    ↓
支付链接生成 (alipays://...)
    ↓
【Xposed Hook 拦截】← 本模块
    ↓
提取数据：URL、金额、订单号
    ↓
保存到模块数据库
    ↓
模块界面显示
```

## 查看日志

使用 Logcat 查看详细拦截日志：

```bash
# 实时查看模块日志
adb logcat | findstr "PaymentCapture"

# 或在 LSPosed 中查看日志
# LSPosed → 日志 → 搜索 "PaymentCapture"
```

## 常见问题

### Q1: 模块显示 "未激活"
**A:** 
- 确认已在 LSPosed 中启用模块
- 确认已勾选支付宝作用域
- 重启设备

### Q2: 没有拦截到数据
**A:**
- 查看 Logcat 日志确认 Hook 是否成功
- 支付宝版本可能不兼容，需要调整 Hook 代码
- 确认支付宝确实生成了支付链接

### Q3: 支付宝闪退
**A:**
- 某些 Hook 可能导致冲突
- 在 `AlipayHook.java` 中注释掉部分 Hook
- 重新编译安装

### Q4: 如何支持其他应用？
**A:**
- 在 `XposedInit.java` 中添加目标包名
- 创建对应的 Hook 类（如 WeChatHook.java）
- 分析目标应用的支付流程和关键类

## 高级功能

### 1. 导出拦截数据
可以在 `MainActivity.java` 中添加导出功能：
```java
// 导出为 JSON 文件
public void exportData() {
    List<PaymentData> list = DataStorage.loadPaymentData(this);
    String json = new Gson().toJson(list);
    // 写入文件...
}
```

### 2. 实时推送到服务器
在 `AlipayHook.java` 中添加：
```java
// 发送 HTTP 请求到服务器
private void sendToServer(String url, String data) {
    // 实现 HTTP POST...
}
```

### 3. 自动生成二维码
使用 ZXing 库生成二维码图片显示支付链接

## 法律声明

⚠️ **重要提醒**：
- 本工具仅供学习研究使用
- 不得用于非法获取他人支付信息
- 不得用于任何商业或非法用途
- 使用本工具可能违反支付宝服务条款
- 使用风险自负，开发者不承担任何责任

## 技术支持

如遇问题，请提供：
1. 设备型号和 Android 版本
2. Magisk 和 LSPosed 版本
3. 支付宝版本
4. Logcat 日志
5. 详细问题描述

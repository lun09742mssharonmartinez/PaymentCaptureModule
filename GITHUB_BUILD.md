# 云端编译教程

## 🌐 使用 GitHub Actions 自动编译

### 步骤 1: 创建 GitHub 仓库

1. **访问 GitHub**
   - 登录 https://github.com
   - 点击右上角 "+" → "New repository"

2. **创建仓库**
   - Repository name: `PaymentCaptureModule`
   - 选择 Public 或 Private
   - 不要勾选 "Initialize this repository with a README"
   - 点击 "Create repository"

### 步骤 2: 上传代码到 GitHub

在本地项目目录打开终端，运行：

```powershell
cd C:\PaymentCaptureModule

# 初始化 Git
git init

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit - Payment Capture Module"

# 添加远程仓库（替换 YOUR_USERNAME 为你的 GitHub 用户名）
git remote add origin https://github.com/YOUR_USERNAME/PaymentCaptureModule.git

# 推送到 GitHub
git push -u origin main
```

**如果推送失败，可能需要：**
```powershell
# 改为 master 分支
git branch -M main

# 或使用 SSH
git remote set-url origin git@github.com:YOUR_USERNAME/PaymentCaptureModule.git
```

### 步骤 3: GitHub Actions 自动编译

1. **推送代码后，GitHub Actions 会自动触发编译**
   - 访问你的仓库页面
   - 点击 "Actions" 标签
   - 查看编译进度

2. **等待编译完成（约 3-5 分钟）**
   - ✅ 绿色对勾 = 编译成功
   - ❌ 红色叉 = 编译失败

3. **下载 APK**
   - 点击成功的 workflow
   - 滚动到底部 "Artifacts" 区域
   - 下载：
     - `PaymentCapture-debug.zip` - Debug 版本
     - `PaymentCapture-release.zip` - Release 版本

### 步骤 4: 安装 APK

1. 解压下载的 ZIP 文件
2. 将 APK 传输到手机
3. 安装 APK
4. 在 LSPosed 中启用模块

## 🔄 手动触发编译

如果想手动触发编译：

1. 访问 GitHub 仓库的 "Actions" 页面
2. 选择 "Android CI - 自动编译 APK"
3. 点击右侧 "Run workflow" 按钮
4. 选择分支，点击绿色 "Run workflow"

## 🚀 一键上传脚本

我已为您创建了便捷脚本：

```powershell
# Windows PowerShell
.\upload-to-github.ps1
```

## ❓ 常见问题

### Q: 编译失败怎么办？
A: 
1. 查看 Actions 日志找到错误信息
2. 常见错误：
   - Gradle 版本问题
   - 依赖下载失败
   - 语法错误

### Q: 需要 GitHub 账号吗？
A: 是的，需要免费的 GitHub 账号

### Q: 私有仓库可以用吗？
A: 可以！私有仓库的 Actions 也能用（有免费额度）

### Q: 编译要多久？
A: 通常 3-5 分钟，首次编译可能需要 10 分钟（下载依赖）

### Q: 有编译次数限制吗？
A: 
- Public 仓库：无限制
- Private 仓库：每月 2000 分钟免费额度

## 🎯 下一步

编译成功后：
1. 下载 APK
2. 查看 [USAGE.md](USAGE.md) 了解如何使用
3. 安装到手机测试

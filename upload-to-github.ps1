# 一键上传到 GitHub 并自动编译

Write-Host "=== 上传项目到 GitHub ===" -ForegroundColor Green
Write-Host ""

# 检查是否已配置 Git
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Host "❌ 未安装 Git！" -ForegroundColor Red
    Write-Host "请先下载安装: https://git-scm.com/download/win"
    exit 1
}

# 获取 GitHub 用户名
Write-Host "📝 请输入您的 GitHub 用户名: " -NoNewline -ForegroundColor Yellow
$username = Read-Host

if ([string]::IsNullOrWhiteSpace($username)) {
    Write-Host "❌ 用户名不能为空！" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "仓库地址将是: https://github.com/$username/PaymentCaptureModule" -ForegroundColor Cyan
Write-Host ""
Write-Host "⚠️ 请确保您已在 GitHub 上创建了名为 'PaymentCaptureModule' 的仓库！" -ForegroundColor Yellow
Write-Host "   如果还没创建，请访问: https://github.com/new" -ForegroundColor Yellow
Write-Host ""
Write-Host "按任意键继续..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

Write-Host ""
Write-Host "1️⃣ 初始化 Git 仓库..." -ForegroundColor Green

# 检查是否已经是 Git 仓库
if (Test-Path ".git") {
    Write-Host "   ✅ Git 仓库已存在" -ForegroundColor Green
} else {
    git init
    Write-Host "   ✅ Git 初始化完成" -ForegroundColor Green
}

Write-Host ""
Write-Host "2️⃣ 添加文件到 Git..." -ForegroundColor Green
git add .
Write-Host "   ✅ 文件添加完成" -ForegroundColor Green

Write-Host ""
Write-Host "3️⃣ 提交更改..." -ForegroundColor Green
git commit -m "Initial commit - Payment Capture Module with SDK Hook"
Write-Host "   ✅ 提交完成" -ForegroundColor Green

Write-Host ""
Write-Host "4️⃣ 设置远程仓库..." -ForegroundColor Green
$remoteUrl = "https://github.com/$username/PaymentCaptureModule.git"

# 检查是否已有 origin
$hasOrigin = git remote | Select-String "origin"
if ($hasOrigin) {
    git remote set-url origin $remoteUrl
    Write-Host "   ✅ 远程仓库地址已更新" -ForegroundColor Green
} else {
    git remote add origin $remoteUrl
    Write-Host "   ✅ 远程仓库已添加" -ForegroundColor Green
}

Write-Host ""
Write-Host "5️⃣ 推送到 GitHub..." -ForegroundColor Green
Write-Host "   (如果要求登录，请输入 GitHub 用户名和 Personal Access Token)" -ForegroundColor Yellow
Write-Host ""

# 确保在 main 分支
git branch -M main

# 推送
git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "✅ 上传成功！" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "🎉 下一步操作：" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. 访问 Actions 页面查看编译进度：" -ForegroundColor White
    Write-Host "   https://github.com/$username/PaymentCaptureModule/actions" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "2. 等待约 3-5 分钟编译完成" -ForegroundColor White
    Write-Host ""
    Write-Host "3. 下载编译好的 APK：" -ForegroundColor White
    Write-Host "   - 点击成功的 workflow" -ForegroundColor White
    Write-Host "   - 滚动到底部下载 Artifacts" -ForegroundColor White
    Write-Host ""
    Write-Host "按任意键在浏览器中打开 GitHub Actions 页面..." -ForegroundColor Yellow
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    Start-Process "https://github.com/$username/PaymentCaptureModule/actions"
} else {
    Write-Host ""
    Write-Host "❌ 推送失败！" -ForegroundColor Red
    Write-Host ""
    Write-Host "可能的原因：" -ForegroundColor Yellow
    Write-Host "1. GitHub 仓库不存在 - 请先创建仓库" -ForegroundColor White
    Write-Host "2. 认证失败 - 需要 Personal Access Token" -ForegroundColor White
    Write-Host "3. 网络问题 - 检查网络连接" -ForegroundColor White
    Write-Host ""
    Write-Host "💡 创建 Personal Access Token：" -ForegroundColor Cyan
    Write-Host "   1. 访问: https://github.com/settings/tokens" -ForegroundColor White
    Write-Host "   2. 点击 'Generate new token (classic)'" -ForegroundColor White
    Write-Host "   3. 勾选 'repo' 权限" -ForegroundColor White
    Write-Host "   4. 生成后复制 token" -ForegroundColor White
    Write-Host "   5. 推送时用 token 代替密码" -ForegroundColor White
}

Write-Host ""
Write-Host "按任意键退出..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Android SDK 命令行安装脚本

Write-Host "=== Android SDK 命令行工具安装 ===" -ForegroundColor Green

# 1. 下载 SDK Command Line Tools
$sdkUrl = "https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip"
$downloadPath = "$env:TEMP\android-cmdtools.zip"
$sdkPath = "$env:LOCALAPPDATA\Android\Sdk"

Write-Host "1. 创建 SDK 目录..."
New-Item -ItemType Directory -Path $sdkPath -Force | Out-Null

Write-Host "2. 下载 SDK Command Line Tools..."
Write-Host "   下载地址: $sdkUrl"
Write-Host "   这可能需要几分钟..."
# Invoke-WebRequest -Uri $sdkUrl -OutFile $downloadPath

Write-Host ""
Write-Host "⚠️ 由于网络原因，请手动下载：" -ForegroundColor Yellow
Write-Host "   1. 访问: https://developer.android.com/studio#command-tools"
Write-Host "   2. 下载 'Command line tools only' for Windows"
Write-Host "   3. 解压到: $sdkPath\cmdline-tools\latest\"
Write-Host ""
Write-Host "3. 然后运行以下命令安装必要组件："
Write-Host ""
Write-Host "   cd $sdkPath\cmdline-tools\latest\bin"
Write-Host "   .\sdkmanager.bat 'platform-tools' 'platforms;android-34' 'build-tools;34.0.0'"
Write-Host ""

@echo off
chcp 65001 >nul
echo ============================================
echo   固原市妇幼保健院儿童健康管理系统 - 前端启动
echo ============================================
echo.

cd /d "%~dp0yudao-ui\yudao-ui-admin-vue3"

echo [1/2] 安装依赖...
call pnpm install
if %errorlevel% neq 0 (
    echo.
    echo [错误] 依赖安装失败
    pause
    exit /b 1
)

echo.
echo [2/2] 启动前端开发服务器...
call pnpm run dev

pause

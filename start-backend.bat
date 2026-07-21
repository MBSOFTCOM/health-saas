@echo off
chcp 65001 >nul
echo ============================================
echo   固原市妇幼保健院儿童健康管理系统 - 后端启动
echo ============================================
echo.

cd /d "%~dp0"

echo [1/2] 编译打包...
call mvn package -DskipTests -pl yudao-server -am
if %errorlevel% neq 0 (
    echo.
    echo [错误] 编译失败，请检查上方错误信息
    pause
    exit /b 1
)

echo.
echo [2/2] 启动后端服务...
cd yudao-server
java -jar target/yudao-server.jar --spring.profiles.active=local

pause

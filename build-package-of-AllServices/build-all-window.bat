@echo off
setlocal

echo Starting build process for all microservices...

:: List of your project directories
set SERVICES=discovery-server_Eureka spring-cloud-config auth-service customer-service loan-service api-gateway

for %%s in (%SERVICES%) do (
    echo.
    echo ----------------------------------------------------
    echo Building: %%s
    echo ----------------------------------------------------
    cd %%s
    call mvn clean package -DskipTests
    if %errorlevel% neq 0 (
        echo Build failed for %%s!
        pause
        exit /b %errorlevel%
    )
    cd ..
)

echo.
echo ----------------------------------------------------
echo All builds completed successfully!
echo ----------------------------------------------------
pause
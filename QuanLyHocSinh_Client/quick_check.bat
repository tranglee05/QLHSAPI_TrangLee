@echo off
cd /d "D:\Documents\Nam 3 Ki 2\CongNghePhanMem\QuanLyHocSinh"
echo Checking Java syntax...
javac -cp "src/main/java" -d target/classes src/main/java/View/Dat/QuanLyGiaoVienPanel.java 2>&1
if %ERRORLEVEL% EQU 0 (
    echo QuanLyGiaoVienPanel.java compiled successfully
) else (
    echo ERROR in QuanLyGiaoVienPanel.java
)

javac -cp "src/main/java" -d target/classes src/main/java/Controller/Dat/GiaoVienController.java 2>&1
if %ERRORLEVEL% EQU 0 (
    echo GiaoVienController.java compiled successfully
) else (
    echo ERROR in GiaoVienController.java
)

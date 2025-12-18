#!/bin/bash

# Javac Plugin 編譯和測試腳本

set -e

PROJECT_DIR="/Users/fatesaikou/testJV/MyJavacPlugingTest"
cd "$PROJECT_DIR"

echo "=========================================="
echo "Step 1: 清理舊的編譯結果"
echo "=========================================="
rm -rf plugin-out out
mkdir -p plugin-out out

echo ""
echo "=========================================="
echo "Step 2: 編譯插件"
echo "=========================================="
javac -d plugin-out plugin/MyPlugin.java
echo "插件編譯完成!"

echo ""
echo "=========================================="
echo "Step 3: 複製 SPI 服務配置"
echo "=========================================="
cp -r plugin/META-INF plugin-out/
echo "SPI 配置複製完成!"

echo ""
echo "=========================================="
echo "Step 4: 使用插件編譯 HelloWorld"
echo "=========================================="
javac -processorpath plugin-out '-Xplugin:MyPlugin -Xmp:Verbose:OFF -Xmp:ShowPhases:ON -Xmp:ShowMethods:ON -Xmp:ShowF
ields:OFF -Xmp:Prefix:[AUDIT]' -d out src/HelloWorld.java

echo ""
echo "=========================================="
echo "Step 5: 執行編譯後的程式"
echo "=========================================="
java -cp out HelloWorld

echo ""
echo "=========================================="
echo "全部完成!"
echo "=========================================="

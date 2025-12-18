# Javac Plugin 測試專案

這個專案展示如何使用 `javac -Xplugin` 功能來創建和使用自定義的編譯器插件。

## 專案結構

```
MyJavacPlugingTest/
├── src/
│   └── HelloWorld.java          # 測試用的 Java 程式
├── plugin/
│   ├── MyPlugin.java            # 自定義插件程式碼
│   └── META-INF/
│       └── services/
│           └── com.sun.source.util.Plugin  # SPI 服務配置
└── README.md
```

## 使用步驟

### 1. 編譯插件

```bash
cd /Users/fatesaikou/testJV/MyJavacPlugingTest

# 創建輸出目錄
mkdir -p plugin-out

# 編譯插件（需要加入 --add-exports 來存取內部 API）
javac -d plugin-out plugin/MyPlugin.java

# 複製 SPI 服務配置
cp -r plugin/META-INF plugin-out/
```

### 2. 使用插件編譯 HelloWorld

```bash
# 創建輸出目錄
mkdir -p out

# 使用插件編譯（指定插件路徑）
javac -processorpath plugin-out -Xplugin:MyPlugin -d out src/HelloWorld.java
```

### 3. 執行編譯後的程式

```bash
java -cp out HelloWorld
```

## 插件功能說明

`MyPlugin` 插件會在編譯過程中：

1. **顯示編譯階段** - 監聽並顯示 PARSE、ENTER、ANALYZE、GENERATE 等階段
2. **分析語法樹** - 在 ANALYZE 階段完成後掃描 AST
3. **列出類別資訊** - 顯示發現的類別名稱
4. **列出成員變數** - 顯示類別的成員變數及其類型
5. **列出方法** - 顯示類別中定義的方法

## 傳遞參數給插件

可以在插件名稱後面加上參數：

```bash
javac -processorpath plugin-out -Xplugin:"MyPlugin arg1 arg2" -d out src/HelloWorld.java
```

## 編譯事件階段說明

| 階段 | 說明 |
|------|------|
| PARSE | 解析原始碼為語法樹 |
| ENTER | 將符號輸入符號表 |
| ANALYZE | 語義分析（型別檢查等） |
| GENERATE | 產生位元組碼 |

## 注意事項

- 插件使用 Java SPI (Service Provider Interface) 機制載入
- 需要在 `META-INF/services/com.sun.source.util.Plugin` 檔案中註冊插件類別名稱
- 插件可以存取 `com.sun.source.util` 和 `com.sun.source.tree` 套件中的 API

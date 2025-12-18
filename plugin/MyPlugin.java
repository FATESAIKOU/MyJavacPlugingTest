import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;

/**
 * 自定義 javac 插件範例
 * 
 * 此插件會在編譯過程中：
 * 1. 列出所有被編譯的類別名稱
 * 2. 列出所有方法名稱
 * 3. 顯示編譯的各個階段
 */
public class MyPlugin implements Plugin {

    @Override
    public String getName() {
        // 插件名稱，用於 javac -Xplugin:MyPlugin
        return "MyPlugin";
    }

    @Override
    public void init(JavacTask task, String... args) {
        System.out.println("===========================================");
        System.out.println("MyPlugin 初始化!");
        System.out.println("===========================================");
        
        // 顯示傳入的參數
        if (args.length > 0) {
            System.out.println("插件參數:");
            for (String arg : args) {
                System.out.println("  - " + arg);
            }
        }
        
        // 註冊 TaskListener 來監聽編譯事件
        task.addTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent event) {
                System.out.println("[開始] " + event.getKind() + 
                    (event.getSourceFile() != null ? " - " + event.getSourceFile().getName() : ""));
            }

            @Override
            public void finished(TaskEvent event) {
                System.out.println("[完成] " + event.getKind() + 
                    (event.getSourceFile() != null ? " - " + event.getSourceFile().getName() : ""));
                
                // 當 ANALYZE 階段完成時，掃描語法樹
                if (event.getKind() == TaskEvent.Kind.ANALYZE) {
                    CompilationUnitTree compilationUnit = event.getCompilationUnit();
                    if (compilationUnit != null) {
                        System.out.println("\n--- 分析語法樹 ---");
                        compilationUnit.accept(new CodeAnalyzer(), null);
                        System.out.println("--- 分析完成 ---\n");
                    }
                }
            }
        });
    }
    
    /**
     * 語法樹掃描器 - 用於遍歷和分析程式碼結構
     */
    private static class CodeAnalyzer extends TreeScanner<Void, Void> {
        
        private String currentClass = "";
        
        @Override
        public Void visitClass(ClassTree node, Void p) {
            currentClass = node.getSimpleName().toString();
            System.out.println("發現類別: " + currentClass);
            
            // 列出類別成員變數
            for (Tree member : node.getMembers()) {
                if (member instanceof VariableTree) {
                    VariableTree variable = (VariableTree) member;
                    System.out.println("  - 成員變數: " + variable.getName() + 
                        " (類型: " + variable.getType() + ")");
                }
            }
            
            return super.visitClass(node, p);
        }
        
        @Override
        public Void visitMethod(MethodTree node, Void p) {
            String methodName = node.getName().toString();
            if (!"<init>".equals(methodName)) {  // 排除建構子
                System.out.println("  - 方法: " + methodName + "()");
            } else {
                System.out.println("  - 建構子: " + currentClass + "()");
            }
            return super.visitMethod(node, p);
        }
        
        @Override
        public Void visitVariable(VariableTree node, Void p) {
            // 這裡可以處理區域變數
            return super.visitVariable(node, p);
        }
    }
}

/**
 * 測試用的 Hello World 程式
 * 用於驗證 javac plugin 是否正常運作
 */
public class HelloWorld {
    
    private String message = "Hello, World!";
    
    public static void main(String[] args) {
        HelloWorld hello = new HelloWorld();
        hello.sayHello();
        hello.sayGoodbye();
    }
    
    public void sayHello() {
        System.out.println(message);
    }
    
    public void sayGoodbye() {
        System.out.println("Goodbye!");
    }
}

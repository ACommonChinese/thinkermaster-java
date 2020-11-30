# Runnable

通过继承Thread可以实现自定义线程, 通过实现Runnable接口, 也可以实现自定义线程, 实现Runnable接口需要实现run方法, 然后和Thread对象关联:  

```java
CustomRun r = new CustomRun();
new Thread(r).start();
```  

```java
public class CustomRun implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }
}
```  

```java
public class Test {
    public static void main(String[] args) {
        CustomRun run = new CustomRun();
        Thread thread = new Thread(run);
        thread.start();
        for (int i = 0; i < 20; i++) {
            System.out.println("main: " + i);
        }
    }
}
```

Log:  

```
main: 0
Thread-0: 0
Thread-0: 1
Thread-0: 2
Thread-0: 3
Thread-0: 4
Thread-0: 5
main: 1
Thread-0: 6
main: 2
main: 3
main: 4
Thread-0: 7
Thread-0: 8
Thread-0: 9
main: 5
main: 6
main: 7
main: 8
...
```


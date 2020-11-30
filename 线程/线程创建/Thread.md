# Thread

自定义线程: 可以将类声明为Thread的子类, 并重写run方法, 然后调用start方法启动线程.  

```java
public class CustomThread extends Thread {
    @Override
    public void run() {
        // 当调用start时, java虚拟机调用此线程的run方法
        // 结果是两个线程并发地运行;
        // 多次启动一个线程是非法的, 特别是当线程已经结束执行后, 不能再重新启动
        // java的线程属于抢占式调度, 可以设置线程的优先级
        for (int i = 0; i < 20; i++) {
            System.out.println("thread run: " + i);
        }
    }
}

public class Test {
    public static void main(String[] args) {
        CustomThread thread = new CustomThread();
        thread.start();
        for (int i = 0; i < 20; i++) {
            System.out.println("main run: " + i);
        }
    }
}
```


Log:  

```
main run: 0
main run: 1
main run: 2
main run: 3
main run: 4
main run: 5
thread run: 0
main run: 6
main run: 7
main run: 8
main run: 9
main run: 10
main run: 11
thread run: 1
thread run: 2
thread run: 3
thread run: 4
thread run: 5
main run: 12
thread run: 6
thread run: 7
thread run: 8
thread run: 9
main run: 13
thread run: 10
thread run: 11
thread run: 12
thread run: 13
thread run: 14
thread run: 15
thread run: 16
thread run: 17
thread run: 18
thread run: 19
main run: 14
main run: 15
main run: 16
main run: 17
main run: 18
main run: 19
```

Thread有很多方法:
- setName(String name)
- getName()
- sleep(long millis) 类方法
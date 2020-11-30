# Labmda参数和返回值

Labmda可以做为参数和返回值  

### Lambda作为参数  

```java
public class RunnableDemo {
    private static void startThread(Runnable task) {
        new Thread(task).start();
    }

    public static void main(String[] args) {
        startThread(() -> System.out.println("线程执行!"));
    }
}
```

### Lambda作为返回值  

```java
import java.util.Arrays;
import java.util.Comparator;

public class ComparatorDemo {
    private static Comparator<String> newComparator() {
        return ((a, b) -> a.length() - b.length());
    }

    public static void main(String[] args) {
        String[] array = { "abc", "ab", "abcd" };
        System.out.println(Arrays.toString(array));
        Arrays.sort(array, newComparator());
        System.out.println(Arrays.toString(array)); // [ab, abc, abcd]
    }
}
```  

再示例:  

```
interface Calculate<T> {
    public abstract T cal(T a, T b);
}

public class CalDemo {

    private static <R> R calculate(R a, R b, Calculate<R> cal) {
        return cal.cal(a, b);
    }

    public static void main(String[] args) {
        System.out.println(calculate(1, 2, (a, b) -> a + b));
        System.out.println(calculate(2.3, 4.6, (a, b) -> a * b));
        System.out.println(calculate(3, 4, (a, b) -> a * b));
    }
}
```
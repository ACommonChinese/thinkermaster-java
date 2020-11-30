# Supplier

`java.util.function`包下有大量的函数式接口, 比如 Supplier: 


```java
package java.util.function;

/**
 * Represents a supplier of results.
 * ...
 * @param <T> the type of results supplied by this supplier
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
```

该接口是生产型接口, 指定接口泛型是什么类型, 那么实现接口的get方法就返回什么类型的对象  


```java
import java.util.function.Supplier;

public class SupplierDemo {
    // 要求实现Supplier<String>接口的类提供一个返回String的get方法
    public static String getString(Supplier<String> supplier) {
        return supplier.get();
    }

    public static void main(String[] args) {
        String result = getString(() -> {
            return "Hello China!";
        });
        System.out.println(result);
    }
}
```

再举个例子: 

```java
import java.util.function.Supplier;

public class SupplierDemo2 {
    // 使用Supplier接口作为方法参数类型, 通过Lambda表达式求出int数组中最大值
    public static void main(String[] args) {
        int[] arr = {100, 0, 23, 46, 99, 81, 26, -100, -32};
        int maxValue = getMax(() -> {
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        });
        System.out.println(maxValue); // 100
    }

    public static int getMax(Supplier<Integer> supplier) {
        return supplier.get();
    }
}
```

再举个示例:  

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class Student implements Supplier<Integer> {
    private String name;
    private int score;
    public Student(String name, int score) {
        this.score = score;
        this.name = name;
    }

    @Override
    public Integer get() {
        return score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}

public class SupplierDemo3 {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<Student>() {
            {
                add(new Student("小明", 100));
                add(new Student("小红", 99));
                add(new Student("大刘", 99));
                add(new Student("三丰", 88));
            }
        };
        list.sort((s1, s2) -> {
            return s1.get() - s2.get();
        });
        System.out.println(list);
    }
}
```


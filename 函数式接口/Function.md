# Function

`java.util.function.Function<T,R>`接口用来根据一个类型的数据得到另一个类型的数据, 前者称为前置条件, 后者称为后置条件

### apply

根据类型为T的参数获取类型为R的结果

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
    
    ...
}
```

示例: 将`string`类型转换为`Integer`类型  

```java
import java.util.function.Function;

public class FunctionDemo {
    private static void method(String string, Function<String, Integer> function) {
        int num = function.apply(string);
        System.out.println(num + 20);
    }

    public static void main(String[] args) {
        method("10", s -> Integer.parseInt(s));
    }
}
```

###andThen

`Function`接口中有一个默认的`andThen`方法, 用来组合操作, 表示"先做什么, 再做什么", 和`Consumer`中的`andThen`类似  

```java
@FunctionalInterface
public interface Function<T, R> {
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
    		Objects.requireNonNull(after);
    		return (T t) -> after.apply(apply(t));
		}
    
    ...
}
```

示例:  

第一个操作: 把一个字符串转成int
第二个操作: 乘以10
第三个操作: 加上5

```java
import java.util.function.Function;

public class FunctionDemo {
    private static void method(String string, Function<String, Integer> one, Function<Integer, Integer> two, Function<Integer, Integer> three) {
        int num = one.andThen(two).andThen(three).apply(string);
        System.out.println(num); // 105
    }

    public static void main(String[] args) {
        method("10", Integer::parseInt, i -> i *= 10, i -> i += 5);
    }
}
```

示例:  
1. 将字符串截取数字年龄部分, 得到字符串
2. 将上一步的字符串转成int
3. 将上一步的int累加100, 得到int

```java
import java.util.function.Function;

public class FunctionDemo {
    private static void method(String string, Function<String, String> one, Function<String, Integer> two, Function<Integer, Integer> three) {
        int num = one.andThen(two).andThen(three).apply(string);
        System.out.println(num); // 25
    }

    public static void main(String[] args) {
        String str = "成龙,20";
        method(str, s -> s.split(",")[1], s -> Integer.parseInt(s), i -> i += 5);
    }
}
```

示例:  

把姓张的同学名字前后加上__, 并得到个数

```java
import java.util.ArrayList;
import java.util.List;
public class StreamDemo2 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>() {
            {
                add("张三丰");
                add("张宇");
                add("大刘");
                add("张无忌");
                add("乔峰");
                add("刘德华");
            }
        };
        long count = list.stream().filter(name -> name.startsWith("张")).map(name -> "__" + name + "__").count();
        System.out.println(count); // 3
    }
}
```

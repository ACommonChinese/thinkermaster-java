# Consumer

`java.util.function.Consumer<T>`接口与Supplier接口相反, 它不是生产一个数据, 而是消费一个数据, 其数据类型由泛型决定, 抽象方法: `accept`, 源代码:  

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```

示例:  

```java
import java.util.function.Consumer;

public class ConsumerDemo {
    // 实现Consumer接口的对象提供accept(T)方法
    private static void consumeString(String name, Consumer<String> consumer) {
        consumer.accept(name);
    }

    public static void main(String[] args) {
        /**
        consumeString("Hello", new Consumer<String>() {
            @Override
            public void accept(String s) {
            }
        });
         */
        consumeString("Hello World, Hello China!", s -> {
            String reverse = new StringBuffer(s).reverse().toString();
            System.out.println(reverse); // !anihC olleH ,dlroW olleH
        } );
    }
}
```

### andThen

Consumer接口中有一个默认default方法andThen, 如果一个方法的参数和返回值都是`Consumer`类型, 那么就可以实现链式的组合调用, andThen的源码:  

```java
default Consumer<T> andThen(Consumer<? super T> after) {
    Objects.requireNonNull(after);
    return (T t) -> { accept(t); after.accept(t); };
}
```

注: `java.util.Objects`的`requireNonNull`静态方法将会在参数为null时主动抛出`NullPointerException`异常.  

示例:  

```java
import java.util.function.Consumer;

/**
 * andThen需要多个实现Consumer接口的Lambda或对象, 可以把多个Consumer接口组合到一起, 在对数据进行消费
 */
public class AndThenDemo {
    public static void main(String[] args) {
        Consumer<String> consumer1 = (s -> {
            System.out.println("1: " + s);
        });
        Consumer<String> consumer2 = (s -> {
            System.out.println("2: " + s);
        });
        Consumer<String> consumer3 = (s -> {
            System.out.println("3: " + s);
        });

        String string = "hello";

        // consumer1.accept(string);
        // consumer2.accept(string);
        // consumer3.accept(string);

        // 上面三句代码可以下面这一句表示
        consumer1.andThen(consumer2).andThen(consumer3).accept(string);
        /**
         * 1: hello
         * 2: hello
         * 3: hello
         */
    }
}
```

示例:  
下面的字符串数组中存有多条信息, 使用Consumer接口按如下格式打印:  

```java
String[] array = {"陆小曼,女", "徐志摩,男", "林徽因,女", "梁思成,男"};
```

```
姓名: xxx 性别: xx
```

```java
import java.util.function.Consumer;

public class AndThenDemo2 {
    public static void main(String[] args) {
        String[] array = {"陆小曼,女", "徐志摩,男", "林徽因,女", "梁思成,男"};
        printInfo(array,
                s -> System.out.print("姓名: " + s.split(",")[0] + " "),
                s -> System.out.println("性别: " + s.split(",")[1]));
        /**
         * 姓名: 陆小曼 性别: 女
         * 姓名: 徐志摩 性别: 男
         * 姓名: 林徽因 性别: 女
         * 姓名: 梁思成 性别: 男
         */
    }

    private static void printInfo(String[] array, Consumer<String> one, Consumer<String> two) {
        for (String string : array) {
            one.andThen(two).accept(string);
        }
    }
}
```
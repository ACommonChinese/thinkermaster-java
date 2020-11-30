# Predicate

`java.util.function.Predicate<T>`接口包含一个方法:  `public abstract boolean test(T t)`  

```java
import java.util.function.Predicate;

public class PredicateTest {
    private static void method(Predicate<String> predicate) {
        boolean isLong = predicate.test("HelloWorld!");
        System.out.println("字符串很长吗: " + isLong);
    }

    public static void main(String[] args) {
        /**
        method(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.length() > 5;
            }
        });
         */

        method(s -> s.length() > 5);
    }
}
```

###and

and方法是Predicate接口的一个默认方法:  

```java
default Predicate<T> and(Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) && other.test(t);
}
```

将多个`Predicate`条件使用"与"逻辑连接起来实现"并且"的效果时, 可以使用default方法`and`  

示例: 判断一个字符串既包含"H", 又包含"ll", 又包含"d": 

```java
import java.util.function.Predicate;

public class PredicateTest {
    private static void method(Predicate<String> one, Predicate<String> two, Predicate<String> three) {
        boolean isValid = one.and(two).and(three).test("HelloWorld");
        System.out.println("字符串符合要求吗: " + isValid);
    }

    public static void main(String[] args) {
        method(s -> s.contains("H"), s -> s.contains("ll"), s -> s.contains("d"));
    }
}
```

示例: 过滤出以"牛"开头的男同志

```java
import java.util.ArrayList;
import java.util.function.Predicate;

public class PredicateTest {
    // 过滤出以"牛"开头的男同志
    public static ArrayList<String> filter(String[] arr, Predicate<String> one, Predicate<String> two) {
        ArrayList<String> list = new ArrayList<>();
        for (String string : arr) {
            if (one.and(two).test(string)) {
                list.add(string);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String[] array = {"牛娃,男", "宝宝,女", "本山,男", "李小龙,男"};
        ArrayList list = filter(array,
                (String s) -> {
                    return s.split(",")[1].contentEquals("男");
                },
                (String s) -> {
                    return s.split(",")[0].startsWith("牛");
                });
        System.out.println(list);
    }
}
```

###or
and方法是Predicate接口的一个默认方法, 与`and`类似, 默认方法`or`实现逻辑关系中的"或"  

```java
default Predicate<T> or(Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) || other.test(t);
}
```

示例: 判断一个字符串是长度大于5, 或以"Tiger"开头, 或以"cat"结束

```java
import java.util.function.Predicate;

public class PredicateTest {
    private static void method(Predicate<String> one, Predicate<String> two, Predicate<String> three) {
        boolean isValid = one.or(two).or(three).test("Tiger is robust");
        System.out.println("字符串符合要求吗: " + isValid);
    }

    public static void main(String[] args) {
        method(s -> s.length() > 5, s -> s.startsWith("Tiger"), s -> s.endsWith("cat"));
    }
}
```

###negate

`negate`方法也是`Predicate`接口的一个默认方法, 实现逻辑关系中的"取反"  

```java
default Predicate<T> negate() {
    return (t) -> !test(t);
}
```

示例:  

```java
import java.util.function.Predicate;

public class PredicateTest {
    private static void method(Predicate<String> predicate) {
        boolean isValid = predicate.negate().test("Hello world");
        System.out.println("字符串符合要求吗: " + isValid); // true
    }

    public static void main(String[] args) {
        method(s -> s.length() < 5);
    }
}
```
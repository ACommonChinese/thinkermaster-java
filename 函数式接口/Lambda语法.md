# Lambda语法

### Java 8新增的Lambda表达式

语法:

```
(参数列表) -> { 代码块 } 
// 如果只有一个参数, ()可以省略
// 如果代码块只有一条return语句, 可以省略return关键字
```

其实Lambda表达式就是代替了匿名内部类的烦琐语法.

```java

interface Eatable {
    void eat();
}

@FunctionalInterface
interface Flyable {
    void fly(String weather);
}

interface Addable {
    int add(int a, int b);
}

public class MyDemo {
    public void eat(Eatable e) {
        e.eat();
    }
    public void fly(Flyable f) {
        f.fly("晴");
    }
    public void add(Addable a) {
        a.add(2, 3);
    }

    /**
     * Lambda表达式可推导, 可省略
     凡是根据上下文推导出来的内容, 都可以省略: 
     1. (参数列表): 括号中参数列表的数据类型, 可以省略
     2. (参数列表): 括号中参数如果只有一个, 那么参数类型和()都可以省略
     3. {代码块}: 如果{}中的代码只有一行, 无论是否有返回值, 都可以省略({}, return, 分号都不写)
     */
    public static void main(String[] args) {
        MyDemo demo = new MyDemo();
        // Lambda表达式的代码块只有一条语句,可以省略花括号
        demo.eat(() -> System.out.println("Call Eatable::eat();"));
        // 只有一个参数的情况下,可以省略
        // 但是由于有两条语句,花括号不可省略
        demo.fly(weather -> {
            System.out.println("今天天气:" + weather);
            System.out.println("可以起飞!");
        });
        // 只有一条语句,可省略花括号,即使有return, 也可以省略
        demo.add((m, n) -> m + n);
    }
}
```

### Lambda表达式与函数式接口

Lambdda表达式的目标必须是函数式接口(@FunctionalInterface), 函数式接口代表只包含一个抽象方法的接口,  Java 8专门为函数式接口提供了@FunctionalInterface注解, 该注解对程序功能没有任何作用, 它只是用于告诉编译器执行更严格检查 -- 检查该接口必须是函数式接口,否则编译器就会报错.

```
@FunctionalInterface
interface Personable {
    void deepThink();
}

public class MyDemo {
    public static void main(String[] args) {
        Object person = () -> {
            // Error: Target type of lambda conversion must be an interface
            // 经测试发现, 其实上面的@FunctionalInterface去掉依然报此错误
            System.out.println("Person can deep think.");
        };
        person.deepThink();
    }
}
```

修改上面的代码可以把Object person改成Personable person或强转:

```java
public static void main(String[] args) {
        Object person = (Personable)() -> {
            // Error: Target type of lambda conversion must be an interface
            System.out.println("Person can deep think.");
        };
        ((Personable)person).deepThink();
}
```

### 方法引用与构造器引用

如果Lambda表达式的代码块只有一条代码, 可以在代码块中使用方法引用和构造器引用.

**引用类方法**

```java
@FunctionalInterface
interface Converter {
    Integer convert(String from);
}

public class MyDemo {
    public static void main(String[] args) {
        Converter converter = from -> Integer.valueOf(from);
        // 上句代码可以使用类引用形式:
        // Converter converter = Integer::valueOf
        System.out.println(converter.convert("123"));
    }
}
```

**引用特定对象的实例方法**
```java
Converter conveter = from -> "Hello world".indexOf(from);

相当于:
Converter converter = "Hello world"::indexOf;
```

**引用某类对象的实例方法**

```java
public class MyDemo {
    public static void main(String[] args) {
        MyTest mt = (a, b, c) -> a.substring(b, c);
        // 上面这一句相当于:
        // MyTest mt = String::subString;
        // 这和a.subString(b, c)效果一样
        String str = mt.test("hello world", 2, 3);
        System.out.println(str);
    }
}

@FunctionalInterface
interface MyTest {
    String test(String a, int b, int c);
}
```

**引用构造器**

```java
@FunctionalInterface
interface GetPersonInterface {
    Person getPerson(String name);
}

public class Person {
    private String name;
    public Person() {}
    public Person(String name) {
        this.name = name;
    }
    public void printInfo() {
        System.out.println(this.name);
    }

    public static void main(String[] args) {
        GetPersonInterface p1 = name -> new Person(name);
        GetPersonInterface p2 = Person::new;
        p1.getPerson("da liu 1").printInfo(); // da liu 1
        p2.getPerson("da liu 2").printInfo(); // da liu 2
    }
}
```

### 对数组进行排序 

```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

```java
import java.util.Arrays;
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        Person[] arr = {
                new Person("大刘", 30),
                new Person("小龙", 28),
                new Person("小明", 34),
                new Person("成龙", 67)
        };
        // 按年龄升序排序
        Arrays.sort(arr, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge(); // 升序
            }
        });
        for (Person person : arr) {
            System.out.println(person.getAge());
        }
        System.out.println("---------------------");
        // 按年龄降序排序
        Arrays.sort(arr, (Person p1, Person p2) -> {
            return p2.getAge() - p1.getAge();
        });
        for (Person person : arr) {
            System.out.println(person.getAge());
        }
    }
}
```
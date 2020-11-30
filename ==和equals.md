# ==和equals

当使用==判断两个变量是否相等时, 如果两个变量是基本数据类型, 且都是数值类型(不一定要求数据类型严格相同), 则只要两相变量的值相等,就将返回true
但对于引用类型变量,只有它们指向同一个对象时,==判断才会返回true

```java
public class Demo {
    public static void main(String[] args) {
        int i = 65;
        float j = 65.0f;
        char ch = 'A';
        System.out.println(i == j); // true
        System.out.println(i == ch); // true
        String str1 = new String("hello");
        String str2 = new String("hello");
        System.out.println(str1 == str2); // false
    }
}
```

### 常量池

"hello"直接量和new String("hello")是有区别的, 当直接使用形如"hello"的字符串直接量时,JVM将会使用常量池来管理这些字符串; 当使用new String("hello")时, JVM会先使用常量池来管理"hello"直接量, 再调用String类的构造器来创建一个新的String对象, 新创建的String对象被保存在堆内存中(即运行时内存中). 换句话说,new String("hello")一共产生了两个字符串对象.

常量池(constant pool)专门用于管理在编译时被确定并被保存在已编译的.class文件中的一些数据. 它包括了关于类, 方法, 接口中的常量, 还包括字符串常量. JVM常量池保证相同的字符串直接量只有一个, 不会产生多个副本.

```java
public class Demo {
    public static void main(String[] args) {
        String s1 = "你好China!";
        String s2 = "你好";
        String s3 = "China!";
        // s4表示的字符串值在编译时就可以确定下来
        // s4直接引用常量池中的"你好China!"
        String s4 = "你好" + "China!";
        // s5类似于s4, 也是使用常量池中的字符串
        String s5= "你" + "好" + "China!";
        // s6不能在编译时就确定下来, 不是引用上量池中的字符串
        String s6 = s2 + s3;
        // 使用new调用构造器创建一个新的String对象
        // s7引用堆内存中新创建的String对象
        String s7 = new String("你好China!");
        System.out.println(s1 == s4); // true
        System.out.println(s1 == s5); // true
        System.out.println(s1 == s6); // false
        System.out.println(s1 == s7); // false
    }
}
```

如果只需要判断两个String的字符序列是否相同, 应当使用equals方法, 而不是==, 对于引用型对象, equals和==的意义相同, 只有两个引用变量指向同一个对象时才会返回true, 因此Object提供的这个equals方法一般用来重写, 而String就重写了equals方法.

equals方法一般如下重写:

```java
class Person {
    private String name;
    private String idStr;
    public Person() {}
    public Person(String name, String idStr) {
        this.name = name;
        this.idStr = idStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj != null && obj.getClass() == Person.class) {
            Person personObj = (Person)obj;
            return personObj.idStr.equals(this.idStr);
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO://一般重写equals方法的同时也会重写hashCode方法
        // 用于把此对象放入Set集合时有用
        return super.hashCode();
    }
}

public class Demo {
    public static void main(String[] args) {
       Person p1 = new Person("张三丰", "123");
       Person p2 = new Person("张四丰", "123");
       Person p3 = new Person("老王", "897");
       System.out.println(p1.equals(p2)); // true
       System.out.println(p1.equals(p3)); // false
    }
}
```








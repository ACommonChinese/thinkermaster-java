# instanceof

obj instanceof(aClass)用于判断obj是否是aClass的实例或aClass子类的实例.

```java
public class Demo {
    public static void main(String[] args) {
        String txt = "hello";
        System.out.println("1: " + (txt instanceof String)); // true
        System.out.println("2: " + (txt instanceof Object)); // true
        System.out.println("3: " + (txt.getClass().equals(String.class))); // false
        System.out.println("4: " + (txt.getClass().equals(Object.class))); // false

        // System.out.println("5: " + (txt instanceof Math)); // 无法编译通过, 因为在编译期就可以明确知道String不可能是Math
        System.out.println("5: " + ((Object)txt instanceof Math)); // 结果为false, 但编译通过, 因为编译器不知道Object是否有可能是Math
    }
}
```
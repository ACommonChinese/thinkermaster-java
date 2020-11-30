# LinkedHashSet

Set是无序的, LinkedHashSet是有序的, 添加顺序决定了其顺序

```java
import java.util.HashSet;
import java.util.LinkedHashSet;

public class LinkedHashSetDemo {
    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();
        set.add("iOS");
        set.add("Android");
        set.add("WinPhone");
        System.out.println(set); // [WinPhone, iOS, Android]
        // HashSet是无序不重复集合
        // LinkedHashSet是有序不重复集合
        LinkedHashSet<String> linkedSet = new LinkedHashSet<>();
        linkedSet.add("iOS");
        linkedSet.add("Android");
        linkedSet.add("WinPhone");
        System.out.println(linkedSet); // 可以看到, 和添加顺序相同: [iOS, Android, WinPhone]
    }
}
```

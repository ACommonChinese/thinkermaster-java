# LinkedList

LinkedList是一个双向链表, 它提供了一些便捷的方法方便操作首尾元素.  

```java
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class IteratorDemo {
    public static void main(String[] args) {
        // show1();
        show2();
    }

    public static void show2() {
        LinkedList<String> list = new LinkedList<>();
        list.add("a");
        list.add("b");
        String first = list.getFirst();
        String last = list.getLast();
        // list.clear();
        if (!list.isEmpty() && list.size() >= 2) {
            String str = list.get(1);
            System.out.println(str);
        }
    }

    public static void show1() {
        LinkedList<String> list = new LinkedList<>();
        list.add("a");
        list.add("b");
        System.out.println(list);
        list.addFirst("1");
        System.out.println(list);
        list.push("2"); // 同addFirst()
        System.out.println(list);
        list.addLast("3"); // 同add()
        System.out.println(list);
    }
}
```
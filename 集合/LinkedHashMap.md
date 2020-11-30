# LinkedHashMap

Map的默认也是无序的, LinkedHashMap是有序的集合, 和添加顺序一致  

```java
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LinkedHashMapDemo {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("小明", 100);
        map.put("小红", 99);
        map.put("张三", 88);
        map.put(null, null);
        System.out.println(map); // {null=null, 张三=88, 小明=100, 小红=99}

        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("小明", 100);
        linkedHashMap.put("小红", 99);
        linkedHashMap.put("张三", 88);
        linkedHashMap.put(null, null);
        System.out.println(linkedHashMap); // {小明=100, 小红=99, 张三=88, null=null}
    }
}
```
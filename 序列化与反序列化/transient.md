# transient

通过在实例变量前面使用transient(瞬态)关键字修饰, 可以指定Java序列化时无须理会该实例变量.  

```java
import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    // age不参与序列化
    // transient只能修饰成员变量
    private transient int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // setters & getters
```

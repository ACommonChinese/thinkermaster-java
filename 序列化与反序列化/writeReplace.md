# writeReplace

可以在序列化对象时将该对象替换成其他对象, 应为序列化类提供如下方法:  

```
private/protected/public Object writeReplace() throws ObjectStreamException
```

此方法由序列化机制调用, java的序列化机制保证在序列化某个对象之前, 先调用该对象的writeReplace()方法, 如果该方法返回另一个Java对象, 则系统转为序列化另一个对象.  

**Person.java**  

```java
import java.io.*;
import java.util.ArrayList;

public class Person implements Serializable {
    private String name;
    private int age;
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

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private Object writeReplace() throws ObjectStreamException {
        ArrayList<Object> list = new ArrayList<>();
        list.add(name);
        list.add(age);
        return list;
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

**Test.java**   

```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        write();
        read();
    }

    private static void write() {
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("person.txt"))) {
            Person per = new Person("孙悟空", 500);
            outStream.writeObject(per);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read() {
        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("person.txt"))) {
            List list = (ArrayList) inStream.readObject();
            System.out.println(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```
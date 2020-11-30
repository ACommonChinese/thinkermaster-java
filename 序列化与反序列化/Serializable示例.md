# Serializable示例

**com.daliu.Person.java**  
```java
package com.daliu;

import java.io.Serializable;

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
        System.out.println("构造器被调用");
        this.name = name;
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

**com.daliu.SerializableDemo.java**  

```java
package com.daliu;

import java.io.*;

public class SerializableDemo {
    public static void writePerson() {
        try (
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("person.txt")) ) {
            Person person = new Person("大刘", 30);
            outputStream.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Person readPerson() {
        System.out.println("read person begin .........");
        try (ObjectInput inputStream = new ObjectInputStream(new FileInputStream("person.txt"))) {
            Person person = (Person)inputStream.readObject();
            System.out.println("read person end .........");
            return person;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        writePerson();
        System.out.println(readPerson());
    }
}
```

**注:**  
1. 反序列化读取的是java对象的数据, 而不是java类, 因此采用反序列化恢复java对象时, 必须提供该java对象所属的class文件. 否则会引发ClassNotFound异常  
2. Person类只有一个有参构造器, 当反序列化读取Java对象时, 并没有看到程序调用该构造器. 这表明反序列化机制无须通过构造器来初始化Java对象  
3. 当一个可序列化类有多个父类时(包括直接父类和间接父类), 这些父类要么有无参数的构造器, 要么也是可序列化的----否则反序列化时将抛出InvalidClassException异常. 如果父类是不可序列化的, 只是带有无参数的构造器, 则该父类中定义的成员变量值不会序列化到二进制流中.  


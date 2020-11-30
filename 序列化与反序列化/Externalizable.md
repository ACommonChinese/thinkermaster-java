# Externalizable

除了Serializable之外, Java还提供了另一种实现Externalizable接口的序列化机制, 这种序列化方式完全由程序员决定存储和恢复对象数据.   

Externalizable有两个方法:  

- void writeExtern(ObjectOutput out): 序列化, 通过调用ObjectOutput的父接口DataOutput的方法保存基本类型的实例变量值, 调用ObjectOutput的writeObject()方法保存引用类型的实例变量值
- void readExtern(ObjectInput in): 反序列化, 通过调用ObjectInput的父接口DataInput的方法保存基本类型的实例变量值, 调用ObjectIntput的readObject()方法保存引用类型的实例变量值  

**Person.java**  
```java
import java.io.*;

public class Person implements Externalizable {
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

    public Person() {}

    public Person(String name, int age) {
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(new StringBuffer(name).reverse().toString());
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name = new StringBuffer((String) in.readObject()).reverse().toString();
        this.age = in.readInt();
    }
}
```

**Teacher.java**  
```java
import java.io.*;

public class Teacher implements Externalizable {
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(student);
        out.writeObject(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.student = (Person) in.readObject();
        this.name = (String) in.readObject();
    }

    public Teacher() {}

    private String name;
    private Person student;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public Teacher(String name, Person person) {
        this.name = name;
        this.student = person;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", student=" + student +
                '}';
    }
}
```

**Test.java**  

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Test {
    public static void main(String[] args) {
        Person person = new Person("孙悟空", 30);
        Teacher teacher = new Teacher("唐僧", person);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("extern.txt"));
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream("extern.txt"))) {
                oos.writeObject(teacher);
                System.out.println(ois.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

注: 

- 并不是使用Externalizable接口就不能使用Serializable, 上面示例把Person改成implements Serializable并删掉Externalizable接口的两个实现方法同样可以  
- 当使用 Externalizable 机制反序列化对象时, 程序会先使用public的无参数构造器创建实例, 然后才执行readExternal()方法进行反序列化, 因此实现 Externalizable 接口的序列化类必须提供 public 的无参数构造器  






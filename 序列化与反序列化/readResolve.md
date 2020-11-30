# readResolve

```java
ANY-ACCESS-MODIFIER Object readResolve() throws ObjectStreamException
```

这个方法会紧接着readObject()之后被调用, 该方法的返回值将会代替反序列化的对象, 而原来readObject()反序列化的对象将会被立即丢弃  

由于反序列化的本质是从序列化文件中读取数据创建一个新的对象, 而该对象的成员变量数据和序列化时保持一致, 那么应用于枚举和单例就可能存在问题.  
readResolve()方法在序列化单例类, 枚举类时尤其重要, 如果定义枚举类使用Java 5提供的enum来定义没有问题, 但如果应用中有早期遗留下来的枚举类, 例如下面的Orientation类就是一个枚举类:  

```java
import java.io.*;

public class Orientation implements Serializable {
    public static final Orientation HORIZONTAL = new Orientation(1);
    public static final Orientation VERTICAL = new Orientation(2);
    private int value;
    private Orientation(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static void main(String[] args) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("orientation.txt"))) {
            oos.writeObject(Orientation.HORIZONTAL);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("orientation.txt"));
            Orientation orientation = (Orientation)ois.readObject();
            if (orientation == Orientation.HORIZONTAL) {
                System.out.println("YES");
            } else {
                System.out.println("NO"); // NO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

上面程序NO被打印.  
为了解决这种问题, 可以使用readResolve():  

```java
private Object readResolve() throws ObjectStreamException {
    if (value == 1) {
        return HORIZONTAL;
    }
    if (value == 2) {
        return VERTICAL;
    }
    return null;
}
```

注:  与writeReplace()方法类似的是, readResolve()方法也可以使用任意的访问控制符, 因此父类的readResolve()方法可能被其子类继承. 这样利用readResolve()就会存在一个明显的缺点, 当父类已经实现了readResolve(), 子类将无从下手: 如果父类包含一个protected或public的readResolve()方法, 而且子类没有重写该方法, 将会使得子类反序列化时得到一个父类的对象----这显然不是程序要的结果, 而且不易发现这种错误. 总是让子类重写resolve()方法无疑是一个负担, 因此对于要被作为父类继承的类而言, 实现readResolve()方法可能有一些潜在的风险.  
通常的建议是, 对于final类重写readResolve()方法不会有任何问题; 否则, 重写readResolve()方法时尽量使用private修饰该方法
# Java9过滤功能

Java9(Java SE 9)为ObjectInputStream增加了setObjectInputFilter()和getObjectInputFilter()方法, 其中setObjectInputFilter()用于为对象输入流设置过滤器. 当程序通过ObjectInputStream反序列化对象时, 过滤器的checkInput()方法会自动激发, 用于检查序列化数据是否有效  

checkInput()可有三种返回值:  
1. Status.REJECTED: 拒绝恢复
2. Status.ALLOWED: 允许恢复
3. Status.UNDECIDED: 未决定状态, 程序继续执行检查

ObjectInputStream将会根据ObjectInputFilter的检查结果决定是否执行反序列化, 如果checkInput()返回Status.REJECTED, 反序列化会被阻止; 如果checkInput()方法返回Status.ALLOWED, 程序将可执行反序列化  

```java
import sun.misc.ObjectInputFilter;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class FilterTest {
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.txt"))) {
            ois.setObjectInputFilter((info) -> {
                System.out.println("执行数据过滤");
                ObjectInputFilter filter = ObjectInputFilter.Config.getSerialFilter();
                if (filter != null) {
                    // 首先使用ObjectInputFilte执行默认的检查
                    ObjectInputFilter.Status status = filter.checkInput(info);
                    // 如果默认检查的结果不是Status.UNDECIDED
                    if (status != ObjectInputFilter.Status.UNDECIDED) {
                        return status;
                    }
                }
                // 如果要恢复的对象不是1个
                if (info.references() != 1) {
                    // 不允许恢复对象
                    return ObjectInputFilter.Status.REJECTED;
                }
                if (info.serialClass() != null && info.serialClass() != Person.class) {
                    // 如果恢复的类不是Person类
                    return ObjectInputFilter.Status.REJECTED;
                }
                return ObjectInputFilter.Status.UNDECIDED;
            });
            // 从输入流中读取一个Java对象, 并将其强制类型转换为Person类
            Person p = (Person)ois.readObject();
            System.out.println("name: " + p.getName() + "\n 年龄为: " + p.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
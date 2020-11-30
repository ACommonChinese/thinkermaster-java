# Properties

`public class Properties extends Hashtable<Object,Object>`  

Properties是唯一一个和IO流直接相关的集合类, 该类表示了一个持久的属性集. Properties可保存在流中或从流中加载. 属性列表中每个键及对应的值都是一个字符串.  

因为Properties继承于Hashtable, 所以可对Propertis对象应用put和putAll方法, 但不建议使用这两个方法, 因为它们允许调用者插入键值不是String的项, 而一般使用setProperty方法.    

Properties中的load方法把硬盘中保存的文件读取到集合中, store方法把集合中的临时数据持久化到硬盘中存储.  

### 写入

```
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

public class PropertiesDemo {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.setProperty("name", "大刘");
        prop.setProperty("age", "32");
        prop.setProperty("country", "中国");
        prop.setProperty("sex", "男");
        for (String key : prop.stringPropertyNames()) {
            System.out.println(key + " ---> " + prop.getProperty(key));
        }

        File file = new File("/Users/liuweizhen/Desktop/Tmp/person.properties");

        /**
         * void store(OutputStream out, String comment) 使用它不可以有中文, 注释不可使用中文
         * void store(Writer writer, String comment) 可使用中文件, 但注释comment不可使用中文, 一般使用""
         */
        try (FileWriter writer = new FileWriter(file)) {
            prop.store(writer, "test");
            /**
             * 如果不存在/Users/liuweizhen/Desktop/Tmp/person.properties文件, store方法会创建
             * 生成的文件内容如下:
             #test
             #Sat Feb 22 15:21:21 CST 2020
             age=32
             name=大刘
             sex=男
             country=中国
             */
        } catch (IOException e) {

        }
    }
}
```

### 读取

```java
import java.io.*;
import java.util.Properties;

public class PropertiesDemo {
    public static void main(String[] args) {
        Properties prop = new Properties();
        File file = new File("/Users/liuweizhen/Desktop/Tmp/person.properties");
        try(FileReader reader = new FileReader(file)) {
            /**
             * void load(InputStream inStream) 不能读取含有中文的键值对
             * void load(Reader reader)
             * 注: 存储键值对的文件中:
             *     键值默认的连接符号可以是=和空格
             *     可以使用#注释
             *     键值默认都是字符串, 不再加引号
             */
            prop.load(reader);
            for (String key : prop.stringPropertyNames()) {
                System.out.println(key + " ---> " + prop.getProperty(key));
            }
        } catch (IOException e) {

        }
    }
}
```
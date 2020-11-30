# JAR

JAR: Java Archieve File, 是一种压缩文件，与ZIP兼容，其压缩机制和zip完全相同，与ZIP区别是JAR包含了一个名为META-INF/MANIFEST.MF的清单文件，此文件可由生成JAR文件时由系统自动创建。

只要在CLASSPATH环境变量中添加JAR文件，Java虚拟机就可以自动在内存中解压JAR包，并查找所需要的类或包层次对应的路径结构。

```
test
	a
		Test.java
		Test.class
	b
		Test.java
		Test.class

对应的jar包结构：
test.jar
	META-INF
		MANIFEST.MF
	a
		Test.java
		Test.class
	b
		Test.java
		Test.class
```

jar常用命令：

```java
选项:
    -c  创建新档案(档案指jar包)
    -t  列出档案目录
    -x  从档案中提取指定的 (或所有) 文件
    -u  更新现有档案
    -v  在标准输出中生成详细输出
    -f  指定档案文件名
    -m  包含指定清单文件中的清单信息
    -n  创建新档案后执行 Pack200 规范化
    -e  为捆绑到可执行 jar 文件的独立应用程序
        指定应用程序入口点
    -0  仅存储; 不使用任何 ZIP 压缩
    -P  保留文件名中的前导 '/' (绝对路径) 和 ".." (父目录) 组件
    -M  不创建条目的清单文件
    -i  为指定的 jar 文件生成索引信息
    -C  更改为指定的目录并包含以下文件
如果任何文件为目录, 则对其进行递归处理。
清单文件名, 档案文件名和入口点名称的指定顺序
与 'm', 'f' 和 'e' 标记的指定顺序相同。

示例 1: 将两个类文件归档到一个名为 classes.jar 的档案中: 
       jar cvf classes.jar Foo.class Bar.class 
示例 2: 使用现有的清单文件 'mymanifest' 并
           将 foo/ 目录中的所有文件归档到 'classes.jar' 中: 
       jar cvfm classes.jar mymanifest -C foo/ .
```

**创建JAR文件**

```java
jar cf test.jar test
```

**创建JAR文件,并显示压缩过程**
```java
jar cvf test.jar test # 使用test生成test.jar
```

**不使用清单文件**
```java
jar cvfM test.jar test # 生成的jar包不括清单文件META-INF/MANIFEST.MF
```

**自定义清单内容**
```java
jar cvfm testjar hello.mf test # 这会把hello.mf的内容添加到META-INF/MANIFEST.MF文件中
```

清单文件的内容格式要求如下:
1. 每行只能定义一个key-value对, 以回车表示一行的结束
2. 键值对形如 `key: value`, 其中key后面紧跟`:`和`<空格>`
3. 文件开头不能有空格,
4. 文件必须以一个空行结束

形如:

```java
key1: value1 # 开头不可以是空格
key2: value2
key3: value3
key4: value4
# 注意这里是一个空行
```

自定义的清单文件不一定是以.mf后缀, 只要是文本文件即可, 比如把自定义清单文件写为a.txt, 内容如上所示, 则:

```java
jar cvfm test.jar a.txt test
```

这会把a.txt中的内容合到META-INF/MANIFEST.MF, 合并后的内容类似:

```java
Manifest-Version: 1.0
key1: value1
key2: value2
key3: value3
key4: value4
Created-By: 1.8.0_131 (Oracle Corporation)

```

**查看jar包内容**
```java
jar tf test.jar
```
如果jar包中的内容较多, 可以把内容输出到一个文件中:
```java
jar tf test.jar > a.txt
```

**查看jar包详细信息**
```java
jar tvf test.jar
```

目录:

```
test
	Test.class
  Cat.class
  Dog.class
  ...
META-INF
	MANIFEST.MF
```

**带提示信息解压缩**
```java
jar xvf test.jar
```

**更新JAR文件**

```java
jar uf test.jar Hello.class
# 更新时显示详细信息: jar uvf test.jar Hello.class
````
上面的意义是: 更新test.jar中的Hello.class文件(如果test.jar中已存在则替换)

**创建可执行的JAR包**

当一个应用程序开发成功后，大致有如下三种发布方式：
1. 使用平台相关的编译器将程序编译成针对当前平台的可执行文件
2. 为应用编辑一个批处理文件，比如`run.command`中写入`java XXX`
3. (JAVA)制作可执行JAR包

创建可执行JAR包的关键在于告诉JAVA命令哪个类是主类，JAVA命令有一个`-e`选项，用于指定JAR包中运行程序的主类的类名。

```
jar cvfe hello.jar test.Test test
```
上面的命令把test目录下的所有文件件压缩到hello.jar包中，并指定使用`test.Test`作为程序的入口。
**注：**如果主类带包名，必须指定完整类名。运行可执行JAR包可使用命令：`java -jar hello.jar`

cn/com/daliu

```
package cn.com.daliu;

public class Main {
    public static void main(String[] args) {
        Person.think();
    }
}

package cn.com.daliu;

public class Person {
    public static void think() {
        System.out.println("person can deep think");
    }
}
```

可以进入到cn/com/daliu/中调用`javac *.java`编译成字节码文件，并退出到cn上一层目录，使用`java cn.com.daliu.Main`运行项目。

下面做可执行JAR包：
1. 退出到cn的上一级目录
2. `jar cvfe run.jar cn.com.daliu.Main cn` 把cn目录打包成可执行jar包，主类名为`cn/com/daliu/Main.class`，`.java`文件可以全删除，但`.class`文件必须全部存在
3. 运行可执行jar包：`java -jar run.jar`

可以使用unzip命令解压jar包，对于上面的可执行jar包`run.jar`，解压：

```
unzip run.jar -d ./xxx # 假设存在xxx目录
```
这会把run.jar中所有文件解压在`xxx`目录下，并且和cn同一级目录生成META-INF文件夹及META-INF/MANIFEST.MF清单文件：

```java
Manifest-Version: 1.0
Created-By: 1.8.0_151 (Oracle Corporation)
Main-Class: cn.com.daliu.Main

```

除了JAR包，Java还可能生成WAR包和EAR包，其中WAR是Web应用：Web Archive File, EAR是企业应用Enterprise Archive File.
WAR包和EAR包的压缩格式及压缩方式与JAR包完全一样，只是改变了文件后缀名而已。





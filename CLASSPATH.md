# CLASSPATH

当使用`java HelloWorld`命令运行Java程序时(HelloWorld.class字节码), 在JDK 1.5之前, JRE会在CLASSPATH环境变量指定的路径下寻找字节码文件,  即使当前路径已经包含了HelloWorld.class, 系统依然找不到. 
JDK1.5之后的版本, 完全可以不设置CLASSPATH环境变量, 而且一旦设置, 系统只会找CLASSPATH指定的路径搜索Java类. 这意味着如果CLASSPATH环境变量中不包括一个(.), JRE不会在当前路径下搜索Java类.

可以使用`-classpath`选项临时指定搜索路径:

```java
java -classpath dir1:dir2:dir3...:dirN Java类 # 多个路径之间在Windows上以分号;隔开, Linux平台上以冒号(:)隔开.
```
如果在运行Java程序时指定了-classpath选项, JRE仅会搜索-classpath指定的路径, 其他全忽略. 如果想使用CLASSPATH和-classpath指定的搜索路径都有效, 一般使用-classpath包含CLASSPATH:

```shell
java -classpath %CLASSPATH%:.:dir1:dir2:...:dirN Java类
```


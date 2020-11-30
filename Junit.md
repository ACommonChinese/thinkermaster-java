# Junit

```java
package cn.com.cal;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    public int sub(int a, int b) {
        return a - b;
    }
}
```

```java
package cn.com.test;

import cn.com.cal.Calculator;
import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator cal = new Calculator();
        int result = cal.add(1, 2);
        // 断言结果为3
        Assert.assertEquals(result, 3);
        // assert(result == 3);
    }

    @Test
    public void testSub() {
        Calculator cal = new Calculator();
        int result = cal.add(1, 2);
        assert(-1 == result);
    }
}
```

当然, 这需要导入jar包, 如果使用IDEA, 可以直接按提示导入即可. 也可以下载后手动导入: 

File > Project Structure > Modules > + > JARS or directories... > OK  

如果是基于maven的项目, 可以直接让maven管理:  

```xml
<!--pom.xml-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
```

###before after

```java
@Before
public void start() {
    // 方法名随意
    // 在所有测试方法执行前执行
    System.out.println("start");
}

@After
public void end() {
    // 方法名随意
    // 在所有测试方法执行后执行
    // 即使测试方法发生异常, 也会执行
    System.out.println("end");
}
```
# OGNL

Object Graphic Navigation Language   
对象     图      导航       语言

是应用于Java中的一个开源的表达式语言（Expression Language），它被广泛用在Struts2, MyBatis等框架中，作用是对数据进行访问，它拥有类型转换、访问对象方法、操作集合对象等功能。webwork2和现在的Struts2.x中使用OGNL取代原来的EL来做界面数据绑定，所谓界面数据绑定，也就是把界面元素（例如一个textfield,hidden)和对象层某个类的某个属性绑定在一起，修改和显示自动同步。 

------------------------------------------------

在MyBatis中使用ognl表达式解析对象字段的值, `#{}或者${}`括号中的值为pojo属性名称, 比如 `#{name}`,这相当于调用对象的getName()方法.

```java
person.getComputer().getPrice() -> #{preson.computer.price}
```

MyBatis中一个示例:

```java
public class QueryVo {
    public String getNameContainsDragon() {
        return "%龙%";
    }
}
```

```java
// -- IUserDao.java --
/// 传递对象作为查询条件, 根据QueryVo中的条件查询用户
List<User> findUserByVo(QueryVo vo);
```

```xml
<!--IUserDao.xml-->
<!--查询姓名中包含有“龙”的user-->
<select id="findUserByVo2" parameterType="QueryVo" resultType="User">
    select * from user where username like #{nameContainsDragon} <!--根据OGNL规范，这里相当于调用方法: voObj.getNameContainsDragon()-->
</select>
```

```java
// -- Test.java --
@Test
public void testFindByQueryVo() {
    QueryVo vo = new QueryVo();
    List<User> users = userDao.findUserByVo(vo); // select * from user where username like vo.nameContainsDragon
    for (User u : users) {
        System.out.println(u);
    }
}
```


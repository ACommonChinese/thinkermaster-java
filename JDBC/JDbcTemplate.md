# JDbcTemplate

Spring框架对JDBC进入了简单的封装, 提供了一个JDbcTemplate对象简化JDBC的开发  

Spring JDBC的[文档](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html)
[源码](https://github.com/spring-projects/spring-framework)
[spring-jdbc下载](https://jar-download.com/?search_box=spring-jdbc)

Spring的JDBCTemplate位于spring-jdbc-x.x.x.RELEASE下, 而spring-jdbc依赖于:
spring-beans-x.x.x.jar
spring-core-x.x.x.jar
spring-tx-x.x.x.jar
commons-logging-x.x.jar

也可以方便的使用[Maven集成](https://mvnrepository.com/artifact/org.springframework/spring-jdbc)  

- new JdbcTemplate(dataSource)
- update()
- query()
- queryForMap()
- queryForList()
- queryForObject()

示例:  

```java
package com.daliu.test;

import cn.com.jdbc.DruildUtils;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCTemplateDemo {
    public static void main(String[] args) {
        JdbcTemplate template = new JdbcTemplate(DruildUtils.getDataSource());
        String sql = "update user set address = ? where username = ?";
        int count = template.update(sql, "河南商丘", "李连杰");
        System.out.println("影响的记录数: " + count);
        // 不需要建立连接和释放资源, JdbcTemplate内部已做好处理
    }

    JdbcTemplate template = new JdbcTemplate(DruildUtils.getDataSource());

    @Test
    // 查询全部数据, 封装为一个个Map
    public void test1() {
        String sql = "select * from user";

        // Exception: Incorrect result size: expected 1, actual 9
        // 这种方式查底的结果集只能是一行
        // Map<String, Object> map = template.queryForMap(sql);

        List<Map<String, Object>> users = template.queryForList(sql);
        for (Map<String, Object> user : users) {
            System.out.println(user);
        }
    }

    @Test
    // 查询全部数据, 封装为一个个Map
    public void test2() {
        String sql = "select * from user";

        // Exception: Incorrect result size: expected 1, actual 9
        // 这种方式查底的结果集只能是一行
        // Map<String, Object> map = template.queryForMap(sql);

        List<Map<String, Object>> users = template.queryForList(sql);
        for (Map<String, Object> user : users) {
            System.out.println(user);
        }
    }

    // 查询全部数据, 封装为List<User>
    @Test
    public void test3() {
        String sql = "select * from user";
        List<User> users = template.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setSex(resultSet.getString("sex").charAt(0));
                user.setAddress(resultSet.getString("address"));
                return user;
            }
        });

        // 注: 下面这种写法是错误的:
        // List<User> users = template.queryForList(sql, User.class);
        // 这种写法第二个class参数只支持String, Integer之类的基本的单数据类型

        for (User user : users) {
            System.out.println(user);
        }
    }

    // 查询全部数据, 封装为List<User>
    @Test
    public void test4() {
        String sql = "select * from user";
        // 这种方式要求Bean的字段名和数据库中的字段名保持一致
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test5() {
        String sql = "select count(*) from user";
        Long num = template.queryForObject(sql, Long.class);
        System.out.println(num);
    }
}
```
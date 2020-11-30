# PreparedStatement

`conn.createStatement()`生成的Statement是静态sql, 所有的sql语句是在要执行的时候都是事先拼接好的, 易产生sql注入问题, 而PreparedStatement是Statement的子接口，它可以预编译SQL语句并存储在PreparedStatement对象中。参数使用占位符表示, 在执行时赋值. 

```java
PreparedStatement pstmt = conn.prepareStatement("...");
// 参数绑定
```

### 示例  

```java
package cn.com.jdbc;

import java.sql.*;
import java.util.Scanner;

public class LoginDemo {
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        // 查看数据表是是否存在此用户
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from user where username = ? and password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username); // 索引从1开始
            pstmt.setString(2, password);
            resultSet = pstmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, pstmt, conn);
        }
        return false;
    }

    public static void main(String[] args) {
        // 键盘录入, 接收用户名和密码, 判断用户是否存在
        System.out.print("输入用户名:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.print("输入密码:");
        String password = scanner.nextLine();
        boolean isSuccess = new LoginDemo().login(username, password);
        if (isSuccess) {
            System.out.println("输入成功");
        } else {
            System.out.println("输入错误, 请重新输入");
            System.out.println("---------------");
            main(null);
        }
    }
}
```

### 再次示例

```java
public class MySQLInfo {
    public static String getDriver() {
        return "com.mysql.cj.jdbc.Driver";
    }
    public static String getUrl() {
        return "jdbc:mysql://127.0.0.1:3306/daliu?characterEncoding=utf-8&useSSL=false";
    }
    public static String getUser() {
        return "root";
    }
    public static String getPass() {
        return "110";
    }
}
```

```java
package cn.com.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class PreparedStatementTest {
    public void insert() throws Exception {
        Class.forName(MySQLInfo.getDriver());
        try(
                Connection conn = DriverManager.getConnection(MySQLInfo.getUrl(), MySQLInfo.getUser(),  MySQLInfo.getPass());
                PreparedStatement pstmt = conn.prepareStatement("insert into student_2 values(null, ?, ?)");
        ) {
            long start = System.currentTimeMillis();
            // 插入100条记录
            for (int i = 0; i < 100; i++) {
                // 如果清楚参数类型，可以使用setXxx传入参数
                // 如果不清楚，则使用setObject方法传入
                pstmt.setString(1, "大刘_" + i);
                pstmt.setInt(2, i+1);
                pstmt.executeUpdate();
            }
            System.out.println("耗时：" + (System.currentTimeMillis() - start) + " milli seconds");
        }
    }

    public static void main(String[] args) throws Exception {
        PreparedStatementTest manage = new PreparedStatementTest();
        manage.insert();
    }
}
```


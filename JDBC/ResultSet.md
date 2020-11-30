# ResultSet

### 常用API

- next(): 定位到下一行，移动后如果指向有效记录，返回true
- getXxx(): Xxx可以代表数据类型: 比如getInt() getString()
- getXxx(int): 参数代表列的编号, 编号从1开始, 如getString(1)
- getXxx(String): 参数代表列名称, 比如: getDouble("score")
- previous(): 定位到上一行，移动后如果指向有效记录，返回true
- absolute(int row): 将结果集记录指针移到第row行，row如果为负则移到倒数第row行。如果移动后的记录指针指向一条有效记录，则返回true
- first: 定位到首行，移动后如果指向有效记录，返回true
- beforeFirst(): 将记录指针定位到首行之前，这是ResultSet的初始状态，即位于第一行之前
- last(): 定位到最后一行，移动后如果指向有效记录，返回true
- afterLast(): 定位到最后一行之后
- close(): 释放ResultSet对象

--------------------------------------------
示例: 

```java
package cn.com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Demo1 {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        /**
         * +----+-----------+---------------------+------+--------------------+
         * | id | username  | birthday            | sex  | address            |
         * +----+-----------+---------------------+------+--------------------+
         * |  1 | 刘威振    | 1988-02-01 08:23:12 | 1    | 河南郑州           |
         * |  2 | 李小龙    | 1968-02-01 07:23:12 | 1    | 香港               |
         * |  3 | 李连杰    | 1967-02-01 07:23:22 | 1    | 山东烟台           |
         * |  4 | 成龙      | 1967-02-01 02:23:22 | 1    | 四川成都           |
         * +----+-----------+---------------------+------+--------------------+
         */
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/daliu?characterEncoding=utf-8&useSSL=false", "root", "110");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from user;");
        ) {
            // ResultSet光标最初位于第一行之前;
            // 第一次调用next使第1行成为当前行
            // 当调用next方法返回false时, 光标位于最后一行之后
            while (rs.next()) {
                // 虽然id是integer类型, 但依然可以通过getString获取
                String name = rs.getString(1) + "\t";
                // int name = rs.getInt(1) + "\t";
                String username = rs.getString(2) + "\t";
                String birthday = rs.getString(2) + "\t";
                String address = rs.getString(3);
                System.out.println(name + username + birthday + address);
            }
        }
    }
}
```

默认结果集ResultSet是不可更新的，如果希望创建可更新的ResultSet, 则必须在创建Statement或PreparedStatement时传入额外的参数：

- resultSetType: 控制ResultSet的类型
  - ResultSet.FORWARD_ONLY: 记录指针只能向前移动，这是JDK1.4以前的默认值
  - ResultSet.TYPE_SCROLL_INSENSITIVE: 记录指针可以自由移动，但底层数据改变不会影响ResultSet的内容
  - ResultSet.TYPE_SCROLL_SENSITIVE: 记录指针可以自由移动，底层数据改变会影响ResultSet的内容
  - 注: ResultSet.TYPE_SCROLL_SENSITIVE仅针对已经取出来的记录的更改（update、delete）敏感，对新增（insert）的数据不敏感
- resultSetConcurrency: 控制ResultSet的并发类型
  - ResultSet.CONCUR_READ_ONLY: 只读的并发模式
  - ResultSet.CONCUR_UPDATABLE: 可更新的并发模式, 可以把数据同步更改到底层数据库

注：TYPE_SCROLL_INSENSITIVE 和 TYPE_SCROLL_SENSITIVE 需要数据库驱动的支持，对于有些数据库来说，这两个常量并没有太大的区别

```java
// 可滚动、可更新的结果集
pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
```

这样的Statement的执行结果得到的就是可更新的结果集。更新的方法是，把ResultSet的游标移动到你要更新的行，然后调用updateXXX()，这个方法XXX的含义和getXXX()是相同的。updateXXX（）方法，有两个参数，第一个是要更新的列，可以是列名或者序号。第二个是要更新的数据，这个数据类型要和XXX相同。每完成对一行的update要调用updateRow()完成对数据库的写入，而且是在ResultSet的游标没有离开该修改行之前，否则修改将不会被提交。

**注：** 可更新的结果集需要满足如下条件：
- 所有数据都应该来自一个表
- 选出的数据集必须包含主键列

```java
package cn.com.mysql;

import java.sql.*;

public class ResultSetTest {
    public void query(String sql) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/daliu?characterEncoding=utf-8&useSSL=false", "root", "daliu8807");
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = pstmt.executeQuery();
        ) {
            rs.last();
            int rowCount = rs.getRow();
            for (int i = rowCount; i > 0; i--) {
                rs.absolute(i);
                System.out.println(rs.getInt(1)
                        + "\t"
                        + rs.getString(2)
                        + "\t"
                        + rs.getFloat(3));
                // 修改记录指针所指记录、第2列的值
                // 这会影响到底层表格的数据，第2列的值
                rs.updateString(2, "学生名" + i);
                // 提交修改
                rs.updateRow();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        ResultSetTest manage = new ResultSetTest();
        manage.query("select * from student_2");
    }
}
```
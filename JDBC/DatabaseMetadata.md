# DatabaseMetadata

使用DatabaseMetaData可以分析数据库信息

前面我们使用ResultSetMetaData获取SQL查询结果的信息，而DatabaseMetaData用JDBC提供用来封装数据库连接对应数据库的信息。通常由驱动程序供应商提供实现。

示例：通过DatabaseMetadata分析前当Connection连接对应数据库的一些基本信息，包括当前数据库包含多少数据表，存储过程，student表的数据列、主键、外键等信息

```java
import java.sql.*;

public class DatabaseMetadataTest {
    public void info() throws Exception {
        Class.forName(MySQLInfo.driver);
        try (
                Connection conn = DriverManager.getConnection(MySQLInfo.url, MySQLInfo.user, MySQLInfo.pass);
                ) {
            DatabaseMetaData metaData = conn.getMetaData();

            // 表类型
            ResultSet resultSet = metaData.getTableTypes();
            System.out.println("---- 表类型信息 ----");
            printResultSet(resultSet);

            // 全部数据表
            //    ResultSet getTables(
            //      String catalog,
            //      String schemaPattern,
            //      String tableNamePattern,
            //      String types[]
            //    )
            resultSet = metaData.getTables("select_test", null, "%", "TABLE");
            System.out.println("---- 当前数据库的数据表信息 ----");
            printResultSet(resultSet);

            // 全部存储过程
            resultSet = metaData.getProcedures("select_test", null, "%");
            System.out.println("---- 当前数据库的存储过程 ----");
            printResultSet(resultSet);

            // 表格student和teacher之间的外键约束
            resultSet = metaData.getCrossReference("select_test", null, "teacher", null, null, "student");
            System.out.println("---- 表格student和teacher之间的外键约束 ----");
            printResultSet(resultSet);

            // 表格student下的全部数据
            resultSet = metaData.getColumns("select_test", null, "student", "%");
            System.out.println("---- 表格student下的全部数据 ----");
            printResultSet(resultSet);
        }
    }

    public void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 打印所有列
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            System.out.println(metaData.getColumnName(i+1) + "\t");
        }
        System.out.println();
        // 打印ResultSet里的全部数据
        while (resultSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(resultSet.getString(i+1) + "\t");
            }
            System.out.println();
        }
        resultSet.close();
    }
}
```
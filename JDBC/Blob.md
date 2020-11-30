# Blob 

Blob (Binary Long Object)是二进制长对象的意思，用于存储大文件，典型的Blob内容是一张图片或一个声音文件，由于它们的特殊性，必须使用特殊的方式来存储。  
用普通的SQL语句不能直接存储图片等二进制数据，因为Blob常量无法表示。 
所以要将Blob数据插入数据库需要使用PreparedStatement的 setBinaryStream(int parameterIndex, InputStream x)  

示例：  
选择图片，存入数据库并显示图片,  首先，在数据库中创建一张表：  

```sql
/**
* MySQL的四种BLOB类型
* 类型 大小(单位：字节)
* TinyBlob 最大 255
* Blob 最大 65K
* MediumBlob 最大 16M
* LongBlob 最大 4G
*/
mysql> create table img_table(id int auto_increment primary key, name varchar(255), data mediumblob);
```

并配置pom.xml:

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.11</version>
    </dependency>
</dependencies>
```

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
        return "daliu8807";
    }
}
```

```java
public class ImageHolder {
    /// 图片id
    private int id;
    /// 图片名字
    private String name;

    public ImageHolder() {}
    public ImageHolder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

```java
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件过滤器
 */
public class ExtensionFileFilter extends FileFilter {
    private String description = "";
    private ArrayList<String> extensions = new ArrayList<>();

    // 自定义过滤器，添加文件扩展名
    public void addExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
            extensions.add(extension.toLowerCase());
        }
    }
    public void addExtensions(List<String> extensions) {
        for (String e : extensions) {
            this.addExtension(e);
        }
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // 继承FileFilter类必须实现的抽象方法
    // 返回该文件过滤器的描述文本
    @Override
    public String getDescription() {
        return null;
    }

    // 继承FileFilter类必须实现的抽象方法
    // 判断该文件过滤器是否接受该文件
    @Override
    public boolean accept(File f) {
        // 如果该文件是路径(目录), 接受
        if (f.isDirectory()) return true;
        String name = f.getName().toLowerCase();
        for (String extension : extensions) {
            if (name.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
```

```java
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class BlobTest {
    JFrame frame = new JFrame("图片管理程序");
    private DefaultListModel<ImageHolder> imageModel = new DefaultListModel();
    private JList<ImageHolder> imageList;
    private JTextField filePath;
    private JButton browserBn;
    private JButton uploadBn;
    private JLabel imageLabel;
    JFileChooser chooser;
    ExtensionFileFilter filter;
    private static Connection conn;
    private static PreparedStatement insert;
    private static PreparedStatement query;
    private static PreparedStatement queryAll;

    public BlobTest() {
        this.imageList = new JList(this.imageModel);
        this.filePath = new JTextField(26);
        this.browserBn = new JButton("...");
        this.uploadBn = new JButton("上传");
        this.imageLabel = new JLabel();
        this.chooser = new JFileChooser(".");
        this.filter = new ExtensionFileFilter();
    }

    public void prepareSql() {
        try {
            Class.forName(MySQLInfo.getDriver());
            conn = DriverManager.getConnection(MySQLInfo.getUrl(), MySQLInfo.getUser(), MySQLInfo.getPass());
            insert = conn.prepareStatement("insert into img_table values(null, ?, ?)", 1);
            query = conn.prepareStatement("select data from img_table where id=?");
            queryAll = conn.prepareStatement("select id, name from img_table");
        } catch (Exception var2) {
            System.out.println("prepareSql exception");
            var2.printStackTrace();
        }

    }

    public void prepareData() throws SQLException {
        // 注：在Mac下实验filter出现一个问题：就是图片不可以隐藏文件名，否则找不到文件
        this.filter.addExtensions(new ArrayList(Arrays.asList("jpg", "jpeg", "gif", "png")));
        this.filter.setDescription("图片文件(*.jpg, *.jpeg, *.gif, *.png)");
        // 为文件选择器添加过
        this.chooser.addChoosableFileFilter(this.filter);
        this.chooser.setAcceptAllFileFilterUsed(false);
        this.fillListModel();
        this.filePath.setEditable(false);
        this.imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void prepareUI() {
        /**
         ************************************
         *                      *           *
         *                      *           *
         *                      *           *
         *                      *           *
         *       imgLabel       *  图片列表 *
         *                      *           *
         *                      *           *
         *                      *           *
         * filePath ... upload  *************
         */
        JPanel jp = new JPanel();
        jp.add(this.filePath);
        jp.add(browserBn);
        jp.add(this.uploadBn);
        this.browserBn.addActionListener((event) -> {
            int result = this.chooser.showDialog(this.frame, "浏览图片文件上传");
            if (result == 0) {
                this.filePath.setText(this.chooser.getSelectedFile().getPath());
            }

        });
        this.uploadBn.addActionListener((event) -> {
            if (this.filePath.getText().trim().length() > 0) {
                this.upload(this.filePath.getText());
                this.filePath.setText("");
            }
        });
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        left.add(new JScrollPane(this.imageLabel), BorderLayout.CENTER);
        left.add(jp, "South");
        this.frame.add(left);

        this.imageList.setFixedCellWidth(160);
        this.frame.add(new JScrollPane(this.imageList), "East");
        this.imageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 如果鼠标双击
                if (e.getClickCount() >= 2) {
                    ImageHolder current = (ImageHolder)imageList.getSelectedValue();
                    try {
                        showImage(current.getId());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.frame.setSize(620, 400);
        this.frame.setDefaultCloseOperation(3);
        this.frame.setVisible(true);
    }

    public void fillListModel() throws SQLException {
        ResultSet rs = queryAll.executeQuery();
        Throwable var2 = null;

        try {
            this.imageModel.clear();
            while(rs.next()) {
                this.imageModel.addElement(new ImageHolder(rs.getInt(1), rs.getString(2)));
            }
        } catch (Throwable var11) {
            var2 = var11;
            throw var11;
        } finally {
            if (rs != null) {
                if (var2 != null) {
                    try {
                        rs.close();
                    } catch (Throwable var10) {
                        var2.addSuppressed(var10);
                    }
                } else {
                    rs.close();
                }
            }
        }
    }

    public void init() throws SQLException {
        this.prepareSql();
        this.prepareData();
        this.prepareUI();
    }

    public void upload(String fileName) {
        String imageName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf(46));
        File file = new File(fileName);

        try {
            InputStream is = new FileInputStream(file);
            Throwable var5 = null;

            try {
                insert.setString(1, imageName);
                insert.setBinaryStream(2, is, (int)file.length());
                int effect = insert.executeUpdate();
                if (effect == 1) {
                    fillListModel();
                }
            } catch (Throwable var15) {
                var5 = var15;
                throw var15;
            } finally {
                if (is != null) {
                    if (var5 != null) {
                        try {
                            is.close();
                        } catch (Throwable var14) {
                            var5.addSuppressed(var14);
                        }
                    } else {
                        is.close();
                    }
                }

            }
        } catch (Exception var17) {
            System.out.println("读取图片文件失败");
            var17.printStackTrace();
        }

    }

    public void showImage(int id) throws SQLException {
        query.setInt(1, id);
        ResultSet rs = query.executeQuery();
        Throwable var3 = null;

        try {
            if (rs.next()) {
                Blob imgBlob = rs.getBlob(1);
                ImageIcon icon = new ImageIcon(imgBlob.getBytes(1L, (int)imgBlob.length()));
                this.imageLabel.setIcon(icon);
            }
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (rs != null) {
                if (var3 != null) {
                    try {
                        rs.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    rs.close();
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        (new BlobTest()).init();
    }
}
```
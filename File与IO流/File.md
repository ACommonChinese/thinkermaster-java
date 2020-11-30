# File与IO流

java.io.File是java对文件和目录的抽象表示形式, 使用它可以:  
- 创建文件/文件夹
- 删除文件/文件夹
- 获取文件/文件夹
- 判断文件/文件夹是否存在 
- 遍历
- 获取文件大小
- ...

### 静态方法

```java
import java.io.File;
public class Test {
    public static void main(String[] args) {
        System.out.println(File.pathSeparatorChar); // windows是; linux是:
        System.out.println(File.pathSeparator);     // 同上, 不过形式是字符串
        System.out.println(File.separatorChar);     // windows是\ linux是/
        System.out.println(File.separator);         // 同上, 不过形式是字符串
    }
}
```

### 构造方法  
```java
File​(File parent, String child)	
Creates a new File instance from a parent abstract pathname and a child pathname string.

File​(String pathname)	
Creates a new File instance by converting the given pathname string into an abstract pathname.

File​(String parent, String child)	
Creates a new File instance from a parent pathname string and a child pathname string.

File​(URI uri)	
Creates a new File instance by converting the given file: URI into an abstract pathname.
```

```java
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        /**
         * 绝对路径: 完整路径, 以盘符(C:\\.. windows)或(/... linux)开头
         * 相对路径: 相对于当前项目的根目录
         *
         * 注:
         * 路径不区分大小写, 无论windows或linux, 比如在桌面上是不能同时新建hello和HELLO这两个目录的
         * 文件名不推荐使用\或/, 如果一定要, 则需要在之前带上\转义
         */
        File file = new File("hello.txt");
        System.out.println(file);
        try {
            file.createNewFile();
            System.out.println(file.getAbsolutePath()); // /Users/liuweizhen/IdeaProjects/EmptyDemo/hello.txt
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("parent", "child");
        try {
            file2.createNewFile(); // 这会导致crash, 因为必须保证parent目录是存在的
            System.out.println(file2.getAbsolutePath()); // /Users/liuweizhen/IdeaProjects/EmptyDemo/hello.txt
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file3 = new File("parent");
        file3.mkdir();
        file3 = new File("parent", "child");
        try {
            file3.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file4 = new File(file3, "hello.md");
        try {
            file4.createNewFile();
        } catch (IOException e) {

        }
    }
}
```

### 目录的遍历  

File类有两个遍历方法:  
- public String[] list(): 返回String数组, 表示File目录中所所有子文件或目录
- public File[] listFiles(): 返回File数组, 表示File目录中所所有子文件或目录

这两个方法不递归, 只返回目录下一层所有的文件和子目录, 我们结合上面的方法做一个小示例, 返回一个目录下所有的文件(递归)  

```java
public class FileList {
    /**
     * 获取file下所有文件(不含文件夹)
     * @param file
     * @return 文件列表
     */
    public static List<File> allFile(File file) {
        if (file == null) {
            return null;
        }
        ArrayList<File> files = new ArrayList<>();
        if (file.isFile()) {
            files.add(file);
        }
        else if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                File f = fileArray[i];
                files.addAll(FileList.allFile(f));
            }
        }
        return files;
    }
}
```


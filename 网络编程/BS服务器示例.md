# BS服务器示例

新建工程BS

### TCPServer1

```java
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer1 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream(); // 客户端(浏览器)的网络字节输入流
        byte[] bytes = new byte[1024];
        int len = 0;
        // inputStream.read会阻塞程序
        // 当第二次访问或关闭浏览器时, while循环结束
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        System.out.println("end of program.");
    }
}
```

运行此程序并在浏览器上输入 `127.0.0.1:8080`发现Console打印为: 

```
...
GET / HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36
Sec-Fetch-Dest: document
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Accept-Encoding: gzip, deflate, br
Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7
Cookie: Idea-362fdedf=8bf84632-20ec-4f72-817a-cbc8c5085084; Idea-362fdee0=9058b556-e54a-4487-817f-633013f38268


end of program.
```

假设请求的是 `http://127.0.0.1:8080/BS/web/index.html`, 则Console打印的第一行就是: 

`GET /BS/web/index.html HTTP/1.1`, 下面读取得到这个path `BS/web/index.html` 并给浏览器返回这个index.html     

### TCPServer2

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer2 {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        Socket socket = server.accept();
        InputStream inputStream = socket.getInputStream(); // 客户端(浏览器)的网络字节输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String firstLine = reader.readLine(); // GET /BS/web/index.html HTTP/1.1
        String[] arr = firstLine.split(" ");
        String path = arr[1].substring(1); // BS/web/index.html

        FileInputStream fis = new FileInputStream(path);
        OutputStream out = socket.getOutputStream();
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write("Content-Type:text/html\r\n".getBytes());
        out.write("\r\n".getBytes());
        // 读取本地文件并写入到server
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        fis.close();
        out.close();
        socket.close();
        reader.close();
        server.close();
    }
    // path不区分大小写, 因此BS可写成bs
    // http://127.0.0.1:8080/bs/web/index.html
}
```

如果是上面这种写法, 浏览器请求一次之后, server就停了, 那么如果html文件中有图片就不会显示出来, 因为浏览器解析服务器回写的html页面, 页面中如果有图片, 那么浏览器就会单独的开启一个线程读取服务器的图片
服务器就不可停机, 需一直处于监听状态, 客户端请求一次, 服务器就回写一次.  

### TCPServer3

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer3 {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);

        while (true) {
            Socket socket = server.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = socket.getInputStream(); // 客户端(浏览器)的网络字节输入流
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String firstLine = reader.readLine(); // GET /BS/web/index.html HTTP/1.1
                        String[] arr = firstLine.split(" ");
                        String path = arr[1].substring(1); // BS/web/index.html
                        System.out.println("请求: " + path);
                        FileInputStream fis = new FileInputStream(path);
                        OutputStream out = socket.getOutputStream();
                        out.write("HTTP/1.1 200 OK\r\n".getBytes());
                        out.write("Content-Type:text/html\r\n".getBytes());
                        out.write("\r\n".getBytes());
                        // 读取本地文件并写入到server
                        int len = 0;
                        byte[] bytes = new byte[1024];
                        while ((len = fis.read(bytes)) != -1) {
                            out.write(bytes, 0, len);
                        }
                        fis.close();
                        out.close();
                        socket.close();
                        reader.close();
                    }
                    catch (IOException e) {

                    }
                }
            }).start();
        }
        // server.close();
    }
    // path不区分大小写, 因此BS可写成bs
    // http://127.0.0.1:8080/bs/web/index.html
}
```

把线程的代码单独封装出来:  

### TCPServer3 & SendThread

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer3 {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);

        while (true) {
            Socket socket = server.accept();
            SendThread sendThread = new SendThread();
            sendThread.setSocket(socket);
            Thread thread = new Thread(sendThread);
            long start = System.currentTimeMillis();
            System.out.println("start = " + start);
            thread.start();
            try {
                thread.join();;
            }
            catch (InterruptedException e) {

            }
            long end = System.currentTimeMillis();
            System.out.println("end = " + end);
            System.out.println("end - Start:" + (end - start));
        }
        // server.close();
    }
    // path不区分大小写, 因此BS可写成bs
    // http://127.0.0.1:8080/bs/web/index.html
}
```

```java
import java.io.*;
import java.net.Socket;

public class SendThread implements Runnable {
    private Socket socket;
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream(); // 客户端(浏览器)的网络字节输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String firstLine = reader.readLine(); // GET /BS/web/index.html HTTP/1.1
            String[] arr = firstLine.split(" ");
            String path = arr[1].substring(1); // BS/web/index.html
            System.out.println("请求: " + path);
            FileInputStream fis = new FileInputStream(path);
            OutputStream out = socket.getOutputStream();
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-Type:text/html\r\n".getBytes());
            out.write("\r\n".getBytes());
            // 读取本地文件并写入到server
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            fis.close();
            out.close();
            socket.close();
            reader.close();
        }
        catch (IOException e) {

        }
    }
}
```

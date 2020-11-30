# TCP客户端

```java
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * TCP通信的客户端: 向服务器发送连接请求, 读取服务器回写的数据
 */
public class Client {
    /**
     * Socket(String host, int port)
     * 参数:
     *   host: 服务器主机名称/ip地址
     *   port: 服务器的端口号
     *
     * 成员方法:
     *   OutputStream getOutputStream() 返回此套接字的输出流
     *   InputStream getInputStream() 返回此套接字的输入流
     *   void close() 关闭套接字
     */
    public static void main(String[] args) throws IOException {
        // 这一步会和server建立连接, 如果server未启动, 会报异常: Connection refused (Connection refused)
        // 这会让server的accept继续信下执行
        Socket socket = new Socket("127.0.0.1", 8888);
        /**
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("你好服务器".getBytes());

        // 读取服务器发送过来的消息
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = inputStream.read(bytes);
        System.out.println(new String(bytes, 0, len));

        socket.close();
    }
}
```
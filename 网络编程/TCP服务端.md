# TCP服务端

```java
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP通信的服务器端
 * Server必须清楚是哪一个客户端发来的请求, 可以使用accept方法获取到请求的客户端对象Socket:
 * Socket accept() 侦听并接收到此套接字的连接
 */
public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("will accept()");
        Socket socket = serverSocket.accept(); // 获取到请求的客户端对象Socket
        System.out.println("did accept()");
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = inputStream.read(bytes);
        if (len < 0) {
            // 如果client只是建了一个socket: new Socket("127.0.0.1", 8888
            // 但什么也没有做, 则从inputStream中读不出任何东西, len为-1
            System.out.println("inputStream no data");
            socket.close();
            serverSocket.close();
            return;
        }
        System.out.println(new String(bytes, 0, len));

        // 输出给client
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("收到谢谢".getBytes());
        socket.close();
        serverSocket.close();
    }
}
```
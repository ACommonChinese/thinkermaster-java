# IO流体系

| 分类  | 字节输入流  | 字节输出流 | 字符输入流 | Center Aligned  |
| :--------- |:------------ | :--------- | :--------- |:------------ |
| 抽象基类 | *InputStream* | *OutputStream* | *Reader* | *Writer* |
| 访问文件 | **FileInputStream** | **FileOutputStream** | **FileReader** | **FileWriter** |
| 访问数组 | **ByteArrayInputStream** | **ByteArrayOutputStream** | **CharArrayReader** | **CharArrayWriter** |
| 访问管道 | **PipedInputStream** | **PipedOutputStream** | **PipedReader** | **PipedWriter** |
| 访问字符串 |  | | **StringReader** | **StringWriter** |
| 缓冲流 | BufferedInputStream | BufferedOutputStream | BufferedReader | BufferedWriter |
| 转换流 | |  | InputStreamReader | OutputStreamWriter |
| 对象流 | ObjectInputStream | ObjectOutputStream |    |    |
| 抽象基类 |*FilterInputStream*|*FilterOutputStream*| *FilterReader* | *FilterWriter* |
| 打印流 |  |PrintStream|    | PrintWriter |
| 推回输入流 | PushbackInputStream |  | PushbackReader |  |
| 特殊流 | DataInputStream | DataOutputStream |  |  |

另外还有用于音频文件, 加解密等的流:  
AudioInputStream, CipherInputStream, DeflaterInputStream, ZipInputStream …  
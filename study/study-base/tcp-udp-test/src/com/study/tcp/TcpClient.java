package com.study.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream outputStream = null;
        FileInputStream inputStream = null;
        InputStream in = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 8899);
            outputStream = socket.getOutputStream();
            inputStream = new FileInputStream(new File("D:\\a.jpg"));
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buf)) != -1){
                outputStream.write(buf, 0, len);
            }
            socket.shutdownOutput();    //客户端传送完毕后,必须关闭传送流,告知服务器

            in = socket.getInputStream();
            byte[] bufIn = new byte[1024];
            int lenIn = in.read(bufIn);
            System.out.println(new String(bufIn, 0, lenIn));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

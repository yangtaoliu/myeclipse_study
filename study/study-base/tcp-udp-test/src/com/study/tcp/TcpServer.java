package com.study.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8899);
            while (true){
                final Socket socket = serverSocket.accept();
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println(inetAddress.getHostAddress() + "连接成功！");
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("d:/" + System.currentTimeMillis() + ".jpg"));
                            byte[] buf = new byte[1024];
                            int len = -1;
                            while ((len = inputStream.read(buf)) != -1){
                                bufferedOutputStream.write(buf, 0, len);
                            }
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write("图片上传成功！".getBytes());
                            outputStream.close();

                            bufferedOutputStream.close();
                            inputStream.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

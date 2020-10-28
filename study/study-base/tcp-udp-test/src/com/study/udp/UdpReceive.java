package com.study.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpReceive {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(12345);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            //此方法在接收到数据报前一直阻塞。数据报包对象的 length 字段包含所接收信息的长度。如果信息比包的长度长，该信息将被截短。
            socket.receive(packet);
            //获取对方的主机信息
            InetAddress address = packet.getAddress();
            System.out.println(address.getHostAddress());
            //获取数据内容
            byte[] data = packet.getData();
            System.out.println("数据内容:" + new String(data, 0, packet.getLength()));
            //获取数据长度
            int length = packet.getLength();
            System.out.println("数据长度:" + length);

            //获取接收端口号
            int port = packet.getPort();
            System.out.println("接收端口号是:" + port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

package com.study.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSend {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            byte[] buf = "hello,UDP".getBytes();
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 12345);
            socket = new DatagramSocket();
            socket.send(packet);
            System.out.println("发送数据成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

}

package org.academiadecodigo.apiores.tcpclient;

public class RunClient {
    public static void main(String[] args) {

        TcpClient tcpClient = new TcpClient();
        tcpClient.connectToServer();
    }

}

package org.academiadecodigo.apiores.tcpclient;

import java.io.IOException;

public class ReadThread implements Runnable {

    private TcpClient tcpClient;

    public void setTcpClient(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void run() {
        try{
            while (!tcpClient.getClientSocket().isClosed()){
                System.out.println(tcpClient.getReader().readLine());
            }
        }catch (IOException a){
            a.getMessage();
        }
    }
}

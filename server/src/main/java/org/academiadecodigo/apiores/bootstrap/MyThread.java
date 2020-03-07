package org.academiadecodigo.apiores.bootstrap;

import java.io.IOException;
import java.net.Socket;

public class MyThread implements Runnable {

    private Socket clientSocket;
    private Server server;


    public MyThread(Socket clientSocket){
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        try{
           server.serveClient(clientSocket);
        }catch (IOException a){
            a.getMessage();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}

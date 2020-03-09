package org.academiadecodigo.apiores.tcpclient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpClient {

    private int port;
    private String host;
    private PrintWriter outPut;
    private Socket clientSocket;
    private Scanner clientReader;
    private BufferedReader reader;
    private ExecutorService threadPool = Executors.newFixedThreadPool(1);


    public void connectToServer(){

        try{
            getUserInput();

            clientSocket = new Socket(host, port);
            outPut = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            ReadThread read = new ReadThread();
            read.setTcpClient(this);

            while (!clientSocket.isClosed()){

                threadPool.submit(read);

                messageChat();
            }
        }catch (IOException a){
            a.getMessage();
        }
        closeAll();
    }


    public void getUserInput(){
        clientReader = new Scanner(System.in);

        System.out.println("Host");
        host = clientReader.next();

        System.out.println("Port");
        port = clientReader.nextInt();

    }


    public void messageChat(){
        clientReader = new Scanner(System.in);

        String message = clientReader.nextLine();

        outPut.println(message);

        /*if (message.equals("/quit")){
            System.out.println("Leaving quiz");
            close();
            System.out.println(nickName + " have left the quiz");
        }*/
    }


    public Socket getClientSocket(){
        return clientSocket;
    }


    public BufferedReader getReader(){
        return reader;
    }


    public void closeAll(){
        close(outPut);
        close(reader);
        close(clientSocket);
        close(clientSocket);
    }


    public void close(Closeable closeable){
        if (closeable == null){
            return;
        }
        try {
            closeable.close();
        }catch (IOException a){
            a.getMessage();
        }
    }
}

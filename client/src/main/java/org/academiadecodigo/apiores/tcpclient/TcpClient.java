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
    private String nickName;
    private ExecutorService threadPool = Executors.newCachedThreadPool();


    public void connectToServer(){

        try{
            getUserInput();

            clientSocket = new Socket(host, port);
            outPut = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outPut.println(nickName);

            System.out.println("<<<<<<-WELCOME " + nickName.toUpperCase() + "->>>>>>");

            while (!clientSocket.isClosed()){
                     threadPool.submit(new ReadThread());

                messageChat();
            }
        }catch (IOException a){
            a.getMessage();
        }

    }


    public void getUserInput(){
        clientReader = new Scanner(System.in);

        System.out.println("Host");
        host = clientReader.next();

        System.out.println("Port");
        port = clientReader.nextInt();

        System.out.println("Your name");
        nickName = clientReader.next();

    }


    public void messageChat(){
        clientReader = new Scanner(System.in);

        String message = clientReader.nextLine();

        outPut.println(message);

        if (message.equals("/quit")){
            System.out.println("Leaving quiz");
            close();
            System.out.println(nickName + " have left the quiz");
        }
    }


    public Socket getClientSocket(){
        return clientSocket;
    }


    public BufferedReader getReader(){
        return reader;
    }


    public void close(){
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

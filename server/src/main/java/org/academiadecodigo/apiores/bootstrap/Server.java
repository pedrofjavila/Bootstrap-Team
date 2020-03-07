package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import javax.print.DocFlavor;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Integer.parseInt;

public class Server {

    private int portNumber = 8080;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String msgReceived = "";
    private BufferedReader in;
    private InputStream inputStream;

    Prompt prompt;
    private MenuInputScanner scanner;
    private LinkedList<Socket> listUser;
    private ExecutorService cachedPool;
    private PrintWriter out;
    private ReadFile readFile;
    private String username;
    private MyThread myThread;
    private int correct;
    private int total;

    private int players = 0;



    public Server() throws IOException {
        this.serverSocket = new ServerSocket(portNumber);
        listUser = new LinkedList<>();

    }


    public void start() {

        cachedPool = Executors.newFixedThreadPool(2);
        System.out.println("----Waiting for connection----\n");

        while (serverSocket.isBound()) {
            //Socket clientSocket = null;

            try {

                clientSocket = serverSocket.accept();

                System.out.println(clientSocket);


                System.out.println("-----Connection accepted------");
                cachedPool.submit(new MyThread());


                //inputStream= new DataInputStream(clientSocket.getInputStream());
                //outputStream = new PrintStream(clientSocket.getOutputStream());
                //prompt = new Prompt(inputStream,outputStream);

            } catch (IOException a) {
                close(clientSocket);
            }

        }

    }


    public void serveClient(Socket clientSocket) throws IOException {

        out = new PrintWriter(clientSocket.getOutputStream(), true);

        out.println("What is your name?");


        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread.currentThread().setName(in.readLine());

        System.out.println(Thread.currentThread().getName() + " is connected\n");
        out.println("\nWelcome to the best quiz " + Thread.currentThread().getName() + "!!!!!!");

       // inputStream = new DataInputStream(clientSocket.getInputStream());
        //outputStream = new PrintStream(clientSocket.getOutputStream());
        //prompt = new Prompt(inputStream, outputStream);

        try {
            readFile.read();
            total = readFile.getAllQuestions().size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //msgReceived = in.readLine();
        out.println("Please wait for another player");

        players++;


        //countPlayers.add(clientSocket);
        int numberOfPlayers = 2;

        while (players < numberOfPlayers) {
            System.out.println(" ");
        }

        out.println("Can start the game");

        //String clientMessage = in.readLine();

        System.out.println(Thread.currentThread().getName());

        while (readFile.getAllQuestions().size() > 0) {
            try {

                 readFile.startQuestions();


               // verifyAnswer(prompt.getUserInput(readFile.menu(readFile.randomQuestion(readFile.getAllQuestions()))));

                System.out.println("prompt");

            } catch (Exception a) {
                a.getMessage();
            }

        }

        out.println("You got " + correct + " out of " + total);
        close(clientSocket);


    }

    private void verifyAnswer(Integer answer,PrintStream printStream) {

        if (answer == parseInt(readFile.getCorrect())) {

            out.println("Right!! " + readFile.getExplanation());

            correct++;

        } else {

            out.println("The right one was " + readFile.getCorrect() + "\n" + readFile.getExplanation());
        }

    }

    public void close(Closeable closeable) {

        if (closeable == null) {

            return;
        }

        try {
            closeable.close();
        } catch (IOException a) {
            System.out.println("Error closing stream" + a.getMessage());
        }
    }
    public void startdgdh(){

       try {
           readFile.startQuestions();
       }catch (Exception ed){
           ed.getMessage();
       }
    }


    public class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                PrintStream printStream = new PrintStream(clientSocket.getOutputStream());
                Prompt prompt = new Prompt(clientSocket.getInputStream(),printStream);
                readFile = new ReadFile(clientSocket);
                readFile.setPrompt(prompt);
                listUser.add(clientSocket);
                  readFile.read();
                  startdgdh();
                 // verifyAnswer(prompt.getUserInput(readFile.menu(readFile.randomQuestion(readFile.getAllQuestions()))),printStream);

               // serveClient(clientSocket);

            } catch (Exception a) {
                a.getMessage();
            }
        }

        public Socket getClientSocket() {
            return clientSocket;
        }
    }



}

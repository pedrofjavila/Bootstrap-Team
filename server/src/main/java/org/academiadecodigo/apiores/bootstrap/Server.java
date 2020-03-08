package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import javax.print.DocFlavor;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Integer.parseInt;

public class Server {

    private int portNumber = 8080;
    private ServerSocket serverSocket;
    //private Socket clientSocket;
    private String msgReceived = "";
    private BufferedReader in;
    private InputStream inputStream;

    private Prompt prompt;
    private MenuInputScanner scanner;
    private HashMap<Integer, Socket> listUser;
    private ExecutorService cachedPool;
    private PrintWriter out;
    private ReadFile readFile;
    private String username;
    private MyThread myThread;
    private int correct;
    private int total;
    private int numberOfPlayers = 0;

    private static int players = 0;


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(portNumber);
        listUser = new HashMap<>();

    }


    public void start() {

        cachedPool = Executors.newFixedThreadPool(2);
        System.out.println("----Waiting for connection----\n");
        Socket clientSocket = null;


        while (serverSocket.isBound()) {
            //Socket clientSocket = null;

            try {


                clientSocket = serverSocket.accept();

                System.out.println(clientSocket);


                System.out.println("-----Connection accepted------");
                cachedPool.submit(new MyThread(clientSocket));


                //inputStream= new DataInputStream(clientSocket.getInputStream());
                //outputStream = new PrintStream(clientSocket.getOutputStream());
                //prompt = new Prompt(inputStream,outputStream);

            } catch (IOException a) {
                close(clientSocket);
            }

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


    public void waitplayer() {

    }


    public class MyThread implements Runnable {

        PrintStream printStream;
        Prompt prompt;
        Socket clientSocket;

        public MyThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void serveClient(Socket clientSocket) throws IOException {

            printStream.println("What is your name?");


            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread.currentThread().setName(in.readLine());

            numberOfPlayers++;

            //System.out.println(Thread.currentThread().getName() + " is connected\n");

            printStream.println("\nWelcome to the best quiz " + Thread.currentThread().getName() + "!!!!!!");

            printStream.println("Waiting for another player...");

            //while (numberOfPlayers < 2) {

            //}

            listUser.put(numberOfPlayers, clientSocket);

            System.out.println(listUser.size());

           // while (listUser.size() < 2) {
                System.out.println("1");

            //}


            printStream.println("Can start the game");

            //String clientMessage = in.readLine();


        }

        @Override
        public void run() {
            try {

                printStream = new PrintStream(clientSocket.getOutputStream());
                prompt = new Prompt(clientSocket.getInputStream(), printStream);
                readFile = new ReadFile();

               readFile.setPrompt(prompt);


                serveClient(getClientSocket(clientSocket));
                readFile.startQuestions();
                // verifyAnswer(prompt.getUserInput(readFile.menu(readFile.randomQuestion(readFile.getAllQuestions()))),printStream);

                // serveClient(clientSocket);

            } catch (Exception a) {
                a.getMessage();
            }
        }

        public Socket getClientSocket(Socket clientSocket) {
            return clientSocket;
        }


    }


}

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
    private PrintStream outputStream;
    private Prompt prompt;
    private MenuInputScanner scanner;
    private LinkedList<Socket> listUser;
    private ExecutorService cachedPool;
    private PrintWriter out;
    private ReadFile readFile;
    private String username;
    private MyThread myThread;


    public Server(ReadFile readFile) throws IOException {
        this.serverSocket = new ServerSocket(portNumber);
        listUser = new LinkedList<>();
        this.readFile = readFile;
    }


    public void start() {

        cachedPool = Executors.newFixedThreadPool(4);
        System.out.println("----Waiting for connection----\n");

        while (serverSocket.isBound()) {
       //     clientSocket = null;

            try {

                clientSocket = serverSocket.accept();

                System.out.println(clientSocket);

                listUser.add(clientSocket);

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


    public void serveClient(Socket clientSocket) throws IOException {

        out = new PrintWriter(clientSocket.getOutputStream(),true);

        out.println("What is your name?");


        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread.currentThread().setName(in.readLine());

        System.out.println(Thread.currentThread().getName() + " is connected\n");
        out.println("\nWelcome to the best quiz " +  Thread.currentThread().getName() +"!!!!!!");

        //msgReceived = in.readLine();

        try {
            readFile.startQuestions();
            //prompt.getUserInput(readFile.menu()

        }catch (Exception a){
            a.getMessage();
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


    public class MyThread implements Runnable {

        private Socket clientSocket;

        private MyThread(Socket clientSocket) {
            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {
            try {

                //StringInputScanner login = new StringInputScanner();
                //login.setMessage(Messages.LOGIN_USER);
                //String username = prompt.getUserInput(login);
                //System.out.println(Thread.currentThread().getName());
                serveClient(clientSocket);
                // out = new PrintWriter(clientSocket.getOutputStream());
            } catch (IOException a) {
                a.getMessage();
            }
        }
        public Socket getClientSocket() {
            return clientSocket;
        }
    }



}

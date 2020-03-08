package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import javax.swing.plaf.TableHeaderUI;
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
    private String msgReceived = "";
    private BufferedReader in;
    private InputStream inputStream;

    Prompt prompt;
    private MenuInputScanner scanner;
    private HashMap<Integer,Socket> listUser;
    private ExecutorService cachedPool;
    private PrintWriter out;
    private ReadFile readFile;
    private String username;
    private ServerThread myThread;
    private int correct;
    private int total;
    private int numberOfPlayers = 0;

    private static int players = 0;


    private ServerSocket serverSocket1 = null;
    private HashMap<Integer, Integer> playerOption;

    private HashMap<Socket, Integer> socketMap;
    private int playerId;







    public Server() throws IOException {
        this.serverSocket = new ServerSocket(portNumber);
        listUser = new HashMap<>();
        socketMap = new HashMap<>();

    }


    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        System.out.println("----Waiting for connection----\n");

        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new ServerThread(clientSocket));
                System.out.println("New Player Connected");
            }catch (IOException e) {
                e.printStackTrace();
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


    public void waitplayer(){

    }


    public class ServerThread implements Runnable {
        private Socket playerSocket;

        public ServerThread(Socket playerSocket) {
            this.playerSocket = playerSocket;
        }


        @Override
        public void run() {
           serveClient(playerSocket);
            /*
            try {


                printStream= new PrintStream(clientSocket.getOutputStream());
                prompt = new Prompt(clientSocket.getInputStream(),printStream);
                readFile = new ReadFile(clientSocket);

                readFile.setPrompt(prompt);


                serveClient(clientSocket);
                readFile.startQuestions(prompt);
                 // verifyAnswer(prompt.getUserInput(readFile.menu(readFile.randomQuestion(readFile.getAllQuestions()))),printStream);

               // serveClient(clientSocket);

            } catch (Exception a) {
                a.getMessage();
            }
            */
        }



        public void serveClient(Socket clientSocket) {
            PrintStream out;
            InputStream in;

            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new PrintStream(clientSocket.getOutputStream(), true);
                Prompt prompt = new Prompt(in, out);
                out.println("What is your name?");

                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Thread.currentThread().setName(input.readLine());

                System.out.println(Thread.currentThread().getName());

                playerId++;

                socketMap.put(clientSocket, playerId);

                beginning(out, prompt);

                readFile = new ReadFile(clientSocket);

                System.out.println("SERVERCLINT");
                readFile.read();
                System.out.println(readFile.getAllQuestions().size());

                int countQuestions = 0;

                while(readFile.getAllQuestions().size() != 0 && countQuestions < 5){

                        System.out.println("\n \n Current Thread " + Thread.currentThread().getName());

                        readFile.menu(readFile.randomQuestion(readFile.getAllQuestions()) , prompt);

                        countQuestions ++;
                    System.out.println( "nyumber of threads in use "+Thread.activeCount());


                        if(readFile.getAllQuestions().size() == 0) {
                            out.println("\nDon't have more questions\n");
                            break;
                        }
                }


                out.println("Acertaste " + readFile.getCertas() +" de " + countQuestions);

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Player Disconnected");
            }
            /*
            try{
                out.close();
                in.close();
            }catch (IOException e){

            }
            */


        }


        public void beginning(PrintStream out, Prompt prompt){

            System.out.println("Beginning");

            String[] options = {"Start", "Exit"};

            MenuInputScanner menuInputScanner0 = new MenuInputScanner(options);
            menuInputScanner0.setMessage("Select a option");

            printWelcomeMessage(out);

            int playerPick0 = prompt.getUserInput(menuInputScanner0);

            if (playerPick0 == 2){
                out.println("You are quiting the game");
                System.exit(0);
            }

            if (socketMap.size() < 2){
                out.println("Need one more player please wait");
                System.out.println("Server: size " + socketMap.size());
            }
            System.out.println("Server: size " + socketMap.size());

            System.out.println("Thread antes do while socket " + Thread.currentThread().getName());

            while (socketMap.size() < 2){
                System.out.println("2");
                continue;
            }
            System.out.println("\n\nThread depois do while socket" + Thread.currentThread().getName());


        }



        private void printWelcomeMessage(PrintStream out) {
            String welcomeMessage = "WELCOMEEEEE";

            for (int i = 0; i < welcomeMessage.length(); i++) {
                try {
                    out.println(welcomeMessage.charAt(i));
                    Thread.sleep(0, 500);
                }catch (InterruptedException e){
                    System.err.println("Problem printing welcome slowly");
                }
            }
        }

    }



}

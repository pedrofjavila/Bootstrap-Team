package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private int portNumber = 8080;
    private ServerSocket serverSocket;
    private HashMap<Integer, Socket> listUser;
    private HashMap<Socket, Integer> socketMap;
    private int playerId;


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(portNumber);
        listUser = new HashMap<>();
        socketMap = new HashMap<>();

    }


    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        System.out.println("----Waiting for connection----\n");


        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new ServerThread(clientSocket));

                System.out.println("New Player Connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public class ServerThread implements Runnable {
        private Socket playerSocket;

        public ServerThread(Socket playerSocket) {
            this.playerSocket = playerSocket;
        }


        @Override
        public void run() {
            serveClient(playerSocket);

        }


        public void serveClient(Socket clientSocket) {

            PrintStream out;
            InputStream in;

            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new PrintStream(clientSocket.getOutputStream(), true);
                Prompt prompt = new Prompt(in, out);

                out.println("  ______               __         __          ______                         __      _                        \n" +
                        "  / ____/  ____ _  ____/ /  ___   / /_        /_  __/  ___   ____ _  _____   / /_    (_)   ____    ____ _         \n" +
                        " / /      / __ `/ / __  /  / _ \\ / __/         / /    / _ \\ / __ `/ / ___/  / __ \\  / /   / __ \\  / __ `/     \n" +
                        "/ /___   / /_/ / / /_/ /  /  __// /_          / /    /  __// /_/ / / /__   / / / / / /   / / / / / /_/ /          \n" +
                        "\\____/   \\__,_/  \\__,_/   \\___/ \\__/         /_/     \\___/ \\__,_/  \\___/  /_/ /_/ /_/   /_/ /_/  \\__, /  \n" +
                        "                                                                                               /____/             \n");

                out.println("What is your name?");

                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                Thread.currentThread().setName(input.readLine());

                System.out.println(Thread.currentThread().getName());


                out.println(Messages.WELCOME_MSG + Thread.currentThread().getName() + "!!" +
                        "\n" + Messages.WELCOME_RULES + "\n" + Messages.WELCOME_RULES2 + "\n");

                playerId++;

                socketMap.put(clientSocket, playerId);

                System.out.println("Socket Map " + socketMap.keySet().size());


                beginning(out, prompt);

                ReadFile readFile = new ReadFile(clientSocket, out);

                readFile.read();

                System.out.println(readFile.getAllQuestions().size());



                int countQuestions = 0;

                while (readFile.getAllQuestions().size() != 0 && countQuestions < readFile.getAllQuestions().size()) {

                    System.out.println("\n \n Current Thread " + Thread.currentThread().getName());

                    readFile.menu(readFile.randomQuestion(readFile.getAllQuestions()), prompt);

                    countQuestions++;


                    if (readFile.getAllQuestions().size() == 0) {
                        out.println("\nDon't have more questions\n");
                        break;
                    }
                }


                out.println(Messages.SCORE + readFile.getCorrectAnswer() + " of " + countQuestions);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Player Disconnected");
            }

        }


        public void beginning(PrintStream out, Prompt prompt) {

            String[] options = {"Start", "Exit"};

            MenuInputScanner menuInputScanner0 = new MenuInputScanner(options);
            menuInputScanner0.setMessage(Messages.PLAYER_IS_READY);

            int playerPick0 = prompt.getUserInput(menuInputScanner0);

            if (playerPick0 == 2) {
                out.println("You are quiting the game");
                System.exit(0);
            }

            if (socketMap.size() < 2) {
                out.println(Messages.GAME_JOIN_WAITING);
                System.out.println("Server: size " + socketMap.size());
            }
            System.out.println("Server: size " + socketMap.size());


            while (socketMap.size() < 2) {
                System.out.println("2");
            }
            out.println("\n" + Messages.PLAYER_READY);

        }


    }


}

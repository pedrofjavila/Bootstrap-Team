package org.academiadecodigo.apiores.bootstrap.reader;

import org.academiadecodigo.apiores.bootstrap.Messages;
import org.academiadecodigo.apiores.bootstrap.Server;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class ReadFile {

    private PrintStream printStream;
    private BufferedReader bufferedReader;
    private String line = "";
    private String[] options = new String[7];
    private String question;
    private String[] finalOptions = new String[4];
    private String correct = "";
    private Socket client;
    private int correctAnswer = 0;
    private ArrayList<String[]> allQuestions = new ArrayList<>();
    private String explanation = "";
    private HashMap<Thread, PrintStream> threadStreamMap = new HashMap<>();
    private HashMap<Socket, PrintStream> socketPrintStreamHashMap = new HashMap<>();


    public ReadFile(Socket socket, PrintStream printStream) {
        this.client = socket;
        this.printStream = printStream;

        try {
            threadStreamMap.put(Thread.currentThread(), new PrintStream(socket.getOutputStream()));

            socketPrintStreamHashMap.put(socket, new PrintStream(socket.getOutputStream()));


        } catch (IOException e) {
            System.err.println("Problem with socket stream connection");
        }
    }


    public ArrayList<String[]> read() throws Exception {

        bufferedReader = new BufferedReader(new FileReader("src/main/resources/questions.txt"));

        int i = 0;

        while ((line = bufferedReader.readLine()) != null) {

            options[i] = line;
            i++;

            if (i % 7 == 0) {
                allQuestions.add(options);
                i = 0;
                options = new String[7];
            }

        }

        return allQuestions;
    }

    public String[] randomQuestion(ArrayList<String[]> allQuestions) {

        String[] temp;
        int random = (int) (Math.random() * allQuestions.size());

        temp = allQuestions.get(random);

        allQuestions.remove(random);

        return temp;

    }


    public void menu(String[] options, Prompt prompt) {

        try {
            int j = 0;
            question = options[0];
            correct = options[options.length - 2];
            explanation = options[options.length - 1];

            for (int i = 1; i < options.length - 2; i++) {
                finalOptions[j] = options[i];
                j++;
            }


            MenuInputScanner menuInputScanner = new MenuInputScanner(finalOptions);

            menuInputScanner.setMessage(question);

            int answer = prompt.getUserInput(menuInputScanner);

            printStream = threadStreamMap.get(Thread.currentThread());

            if (answer == parseInt(correct)) {

                printStream.println(Messages.QUESTION_RIGHT + explanation);
                correctAnswer++;


            } else {
                printStream.println(Messages.QUESTION_WRONG +"\nThe right one was " + correct + "\n" + explanation);

            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public synchronized ArrayList<String[]> getAllQuestions() {
        return allQuestions;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

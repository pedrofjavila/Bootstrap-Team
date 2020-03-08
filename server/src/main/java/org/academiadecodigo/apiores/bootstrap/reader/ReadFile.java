package org.academiadecodigo.apiores.bootstrap.reader;

import org.academiadecodigo.apiores.bootstrap.Server;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class ReadFile {

    private InputStream input;
    private PrintStream printStream;
    private Server.ServerThread myThread;

    private Prompt prompt;

    private BufferedReader bufferedReader;

    private String line = "";
    private String[] options = new String[7];
    private String question;
    private String[] finalOptions = new String[4];
    private String correct = "";
    private Socket client;
    private int certas = 0;
    private String name;

    private ArrayList <String[]> allQuestions = new ArrayList<>();
    private String explanation = "";

    private HashMap<Thread, PrintStream> threadStreamMap = new HashMap<>();

    private HashMap<Socket, PrintStream> socketPrintStreamHashMap = new HashMap<>();

    private HashMap<Socket, Integer> socketMap;


    public ReadFile(Socket socket) {
        client = socket;

        try {
            threadStreamMap.put(Thread.currentThread(), new PrintStream(socket.getOutputStream()));

            socketPrintStreamHashMap.put(socket,new PrintStream(socket.getOutputStream()));

        } catch (IOException e) {
            System.err.println("Problem with socket stream connection");
        }
    }



    public ArrayList <String []> read () throws Exception{

        bufferedReader = new BufferedReader(new FileReader("src/main/resources/questions.txt"));

        int i = 0;

        while ((line = bufferedReader.readLine()) != null){

            options[i] = line;
            i++;

            if (i % 7 == 0){
                allQuestions.add(options);
                i = 0;
                options = new String[7];
            }

        }

        return allQuestions;
    }

    public String[] randomQuestion (ArrayList <String[]> allQuestions){

        String[] temp;
        int random = (int) (Math.random() * allQuestions.size());

        temp = allQuestions.get(random);

        allQuestions.remove(random);


        return temp;

    }


    public void menu (String[] options, Prompt prompt) {


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

                //printStream = socketPrintStreamHashMap.get(client);



                if (answer == parseInt(correct)) {

                    printStream.println("Right!! " + explanation);
                    certas++;


                } else {
                    printStream.println("The right one was " + correct + "\n" + explanation);

                }

            } catch (Exception e) {
            //    System.out.println("waiting went wrong");
            }

    }

    public void startQuestions(HashMap socketMap) throws Exception{

        this.socketMap = socketMap;

        System.out.println("test");
        //allQuestions = read();
        System.out.println(allQuestions.size());

        while (allQuestions.size() != 0){
            menu(randomQuestion(allQuestions), prompt);

        }
    }

    public synchronized ArrayList<String[]> getAllQuestions() {
        return allQuestions;
    }

    public String getCorrect() {
        return correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }

    public int getCertas() {
        return certas;
    }
}

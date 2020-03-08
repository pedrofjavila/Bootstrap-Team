package org.academiadecodigo.apiores.bootstrap.reader;

import org.academiadecodigo.apiores.bootstrap.Server;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ReadFile {

    private InputStream input;
    private PrintStream output;
    private Server.MyThread myThread;

    private Prompt prompt;

    private BufferedReader bufferedReader;

    private String line = "";
    private String[] options = new String[7];
    private String question;
    private String[] finalOptions = new String[4];
    private String correct = "";
    private Socket client;
    private int certas;
    private String name;

    private ArrayList <String[]> allQuestions = new ArrayList<>();
    private String explanation = "";
    public ReadFile(Socket socket){
        client = socket;
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


    public void menu (String[] options, Prompt prompt){

        int j = 0;

        question = options[0];
        correct = options[options.length -2];
        explanation = options [options.length -1];


        for (int i = 1; i < options.length -2; i++){

                finalOptions[j] = options[i];
                j++;
        }

        MenuInputScanner menuInputScanner = new MenuInputScanner(finalOptions);

        menuInputScanner.setMessage(question);

        int answer = prompt.getUserInput(menuInputScanner);

        try{

        PrintStream printStream = new PrintStream(client.getOutputStream());

        if (answer == parseInt(correct)){

                printStream.println("Right!! " + explanation);


        }
        else{
            printStream.println("The right one was " + correct + "\n" + explanation);
        }
    }catch (IOException ef){
        ef.getMessage();
    }

}

    public void startQuestions(Prompt prompt) throws Exception{

       // PrintWriter out = new PrintWriter(myThread.getClientSocket().getOutputStream(), true);

        //out.println("Hello");
        System.out.println("test");
        allQuestions = read();
        while (allQuestions.size() != 0){
            menu(randomQuestion(allQuestions), prompt);
        }
    }

    public ArrayList<String[]> getAllQuestions() {
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

}

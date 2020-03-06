package org.academiadecodigo.apiores.bootstrap.reader;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ReadFile {

    private Prompt prompt = new Prompt(System.in, System.out);

    private BufferedReader bufferedReader;

    private String line = "";
    private String[] options = new String[7];
    private String question;
    private String[] finalOptions = new String[4];
    private String correct = "";
    private ArrayList <String[]> allQuestions = new ArrayList<>();
    private String explanation = "";



    private ArrayList <String []> read () throws Exception{

        bufferedReader = new BufferedReader(new FileReader("/Users/codecadet/Desktop/bootstrap/Bootstrap-Team/src/main/resources/questions.txt"));

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

    private String[] randomQuestion (ArrayList <String[]> allQuestions){

        String[] temp;
        int random = (int) (Math.random() * allQuestions.size());

        temp = allQuestions.get(random);

        allQuestions.remove(random);


        return temp;

    }


    private void menu (String[] options){

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

        if (options[answer].equals(correct)){
            System.out.println("Right!! " + explanation);
        }
        else{
            System.out.println("The right one was " + correct + "\n" + explanation);
        }

    }

    public void startQuestions() throws Exception{
        allQuestions = read();

        while (allQuestions.size() != 0){
            menu(randomQuestion(allQuestions));
        }
    }

}

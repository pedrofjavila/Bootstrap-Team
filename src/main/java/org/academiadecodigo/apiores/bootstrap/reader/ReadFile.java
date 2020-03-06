package org.academiadecodigo.apiores.bootstrap.reader;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class ReadFile {

    private Prompt prompt = new Prompt(System.in, System.out);

    private BufferedReader bufferedReader;

    private String line = "";
    private String[] options = new String[5];
    private String question;
    private String[] finalOptions = new String[4];



    public String[] read () throws Exception{

        bufferedReader = new BufferedReader(new FileReader("/Users/codecadet/Desktop/bootstrap/Bootstrap-Team/src/main/resources/questions.txt"));

        int i = 0;

        while ((line = bufferedReader.readLine()) != null){

            options[i] = line;
            i++;

        }

        return options;
    }


    public void menu (String[] options){

        int j = 0;

        for (int i = 0; i < options.length; i++){
            if (i == 0){
                question = options[i];
            }
            else {
                finalOptions[j] = options[i];
                j++;
            }
        }

        MenuInputScanner menuInputScanner = new MenuInputScanner(finalOptions);

        menuInputScanner.setMessage(question);

        int answer = prompt.getUserInput(menuInputScanner);

        System.out.println(answer);

    }

}

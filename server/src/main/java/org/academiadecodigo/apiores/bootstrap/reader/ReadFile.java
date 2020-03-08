package org.academiadecodigo.apiores.bootstrap.reader;

import org.academiadecodigo.apiores.bootstrap.Messages;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ReadFile {

    private Prompt prompt = new Prompt(System.in, System.out);
    private BufferedReader bufferedReader;
    private String line = "";
    private String[] options = new String[7];
    private String question;
    private String[] finalOptions = new String[4];
    private String correct = "";
    private ArrayList<String[]> allQuestions = new ArrayList<>();
    private String explanation = "";
    private Messages message;
    private int score;


    private ArrayList<String[]> read() throws Exception {

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

    private String[] randomQuestion(ArrayList<String[]> allQuestions) {

        String[] temp;
        int random = (int) (Math.random() * allQuestions.size());

        temp = allQuestions.get(random);

        allQuestions.remove(random);


        return temp;

    }


    private void menu(String[] options) {

        int j = 0;

        question = options[0];
        correct = options[options.length - 2];
        explanation = options[options.length - 1];


        for (int i = 1; i < options.length - 2; i++) {

            finalOptions[j] = options[i];
            j++;
        }

        MenuInputScanner menuInputScanner = new MenuInputScanner(finalOptions);
        int questionsLeft = allQuestions.size() + 1;
        menuInputScanner.setMessage(question);
        System.out.println("Questions left to answer : " + questionsLeft);
        int answer = prompt.getUserInput(menuInputScanner);

        if (answer == parseInt(correct)) {
            System.out.println(message.QUESTION_RIGHT + "\n" + explanation);
            score += 10;
        } else {
            System.out.println(message.QUESTION_WRONG + "\nThe correct answer was number: " + correct + "\n" + explanation);
            score -= 10;
        }
    }

    public void startQuestions() throws Exception {
        allQuestions = read();
        confirmation();
        while (allQuestions.size() != 0) {
            menu(randomQuestion(allQuestions));

        }

        System.out.println(message.SCORE + score);
    }

    public void confirmation() {

        String[] choices = {"Hell yeah!", "Yes!", "I guess...", "Not really, but since I'm here..."};
        MenuInputScanner confirm = new MenuInputScanner(choices);
        confirm.setMessage(message.WELCOME_RULES + "\n" + message.WELCOME_RULES1 + "\n" + message.WELCOME_RULES2 + "\n" + message.PLAYER_IS_READY);
        prompt.getUserInput(confirm);
        System.out.println(message.PLAYER_READY);

    }

    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }
}

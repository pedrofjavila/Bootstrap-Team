package org.academiadecodigo.apiores.bootstrap;

;import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;


import java.io.IOException;

public class Main {
    private static Messages messages = new Messages();

    // private static Prompt prompt = new Prompt(System.in, System.out);

    public static void main(String[] args) {


        try {
            Server server = new Server();
            server.start();
            ReadFile readFile = new ReadFile();
            System.out.println(messages.WELCOME_MSG);
            readFile.startQuestions();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

}


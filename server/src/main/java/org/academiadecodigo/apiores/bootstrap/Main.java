package org.academiadecodigo.apiores.bootstrap;

;import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;


public class Main {
    private static Messages messages = new Messages();

   // private static Prompt prompt = new Prompt(System.in, System.out);

    public static void main(String[] args) {


        ReadFile readFile = new ReadFile();

        try {
            System.out.println(messages.WELCOME_MSG);
            readFile.confirmation();
            readFile.startQuestions();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}

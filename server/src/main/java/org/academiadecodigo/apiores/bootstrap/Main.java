package org.academiadecodigo.apiores.bootstrap;

;import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;


public class Main {
    private static Messages messages = new Messages();

    private static Prompt prompt = new Prompt(System.in, System.out);

    public static void main(String[] args) {


        ReadFile readFile = new ReadFile();

        try {
            System.out.println(messages.WELCOME_MSG);
            confirmation();
            readFile.startQuestions();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void confirmation() {

        String[] choices = {"Hell yeah!", "Yes!", "I guess...", "Not really, but since I'm here..."};
        MenuInputScanner confirm = new MenuInputScanner(choices);
        confirm.setMessage(messages.WELCOME_RULES + "\n" + messages.WELCOME_RULES1 + "\n" + messages.WELCOME_RULES2 + "\n" + messages.PLAYER_IS_READY);
        prompt.getUserInput(confirm);
        System.out.println(messages.PLAYER_READY);
    }
}

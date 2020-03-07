package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        ReadFile readFile = new ReadFile();

        Server server = null;
        try {
            server = new Server(readFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();

        /*


        try{
            readFile.startQuestions();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    */
    }
}

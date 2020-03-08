package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {



        try {
            Server server = new Server();
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }



        /*


        try{
            readFile.startQuestions();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    */
    }
}

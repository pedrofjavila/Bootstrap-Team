package org.academiadecodigo.apiores.bootstrap;

import org.academiadecodigo.apiores.bootstrap.reader.ReadFile;

public class Main {

    public static void main(String[] args) {

        ReadFile readFile = new ReadFile();

        try{

            readFile.menu(readFile.read());

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

}

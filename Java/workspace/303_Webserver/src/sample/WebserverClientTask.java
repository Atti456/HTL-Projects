package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class WebserverClientTask extends Task {


    private boolean exit = false;
    private String line = "bla";


    public WebserverClientTask(String line) {
        this.line = line;
    }

    @Override
    protected Object call() throws Exception {

        try(Socket serverSocket = new Socket("localhost", 8800);
            PrintWriter bw = new PrintWriter(new OutputStreamWriter(serverSocket.getOutputStream()),true);
            BufferedReader dis = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))
        ){
            System.out.println("Verbindung hergestellt mit " + serverSocket.getRemoteSocketAddress());

            //String schicken
            while (!exit){

                if (line.equals("q")){
                    exit = true;
                }
                bw.println(line);       //Schicken

                String strline = "bla";
                String str="bla";

                do {
                    strline = dis.readLine();
                    System.out.println("Empfange: " + strline);


                    str = "Empfange: " + strline + "\n";


                } while (strline.compareTo("</html>") != 0 || strline.compareTo("</html> ") != 0);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Verbindung beendet");
        return null;
    }


}

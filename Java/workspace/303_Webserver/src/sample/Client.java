package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        System.out.println("Verbinde zu localhost auf Port 8800");
        //Verbindungsaufbau zu Server

        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        String line;

        try(Socket serverSocket = new Socket("localhost", 8800);
            PrintWriter bw = new PrintWriter(new OutputStreamWriter(serverSocket.getOutputStream()),true);
            BufferedReader dis = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))
        ){
            System.out.println("Verbindung hergestellt mit " + serverSocket.getRemoteSocketAddress());

            //String schicken
            while (!exit){
                System.out.println("? ");
                line = sc.next();
                if (line.equals("q")){
                    exit = true;
                }
                bw.println(line);       //Schicken

                String strline = "";

                do {
                    strline = dis.readLine();
                    System.out.println("Empfange: " + strline);
                } while (strline.compareTo("</html>") != 0 && strline.compareTo("</html> ") != 0);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Verbindung beendet");
    }
}

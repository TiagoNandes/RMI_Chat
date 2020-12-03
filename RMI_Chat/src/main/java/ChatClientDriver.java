/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ChatClientDriver {
    public static void main(String[] args) throws IOException, NotBoundException {
        String chatServerURL = "rmi://localhost/RMIChatServer";
        ChatServerIF chatServer = (ChatServerIF) Naming.lookup(chatServerURL);
        //Instanciate new chatServer and pass the name (args )


        new Thread(new ChatClient(chatServer)).start();

//        new Thread(new ChatClient(args[0], args[1], chatServer)).start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ChatClientDriver {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        String chatServerURL = "rmi://localhost/RMIChatServer";
        ChatServerIF chatServer = (ChatServerIF) Naming.lookup(chatServerURL);
        //Instanciate new chatServer and pass the name (args )
        new Thread(new ChatClient(args[0], chatServer)).start();
    }
}

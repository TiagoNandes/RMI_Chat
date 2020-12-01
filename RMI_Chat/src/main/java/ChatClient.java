/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChatClient extends UnicastRemoteObject implements ChatClientIF, Runnable {
    private static final long serialVersionUID = 1L;
    private ChatServerIF chatServer;
    private String name = null;
    protected ChatClient(String name, ChatServerIF chatServer) throws RemoteException {
        this.name = name;
        this.chatServer = chatServer;
        chatServer.registerChatClient(this, name);
    }
    
    public static void clearScreen() {  
         //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
     }
    
    public void retrieveMessage(String message, boolean firstMessage) throws RemoteException {
        if(firstMessage == true){
            clearScreen();
        }
        
        System.out.println(message);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();
            
            if(message.startsWith("/")){
                try {
                
                    chatServer.broadcastMessage(name + " : " + message, "private");
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                
                try {
                
                    chatServer.broadcastMessage(name + " : " + message, "public");
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        }
    }
    
}

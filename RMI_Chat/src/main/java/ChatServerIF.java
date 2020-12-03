/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author TOUCHIT_02
 */
public interface ChatServerIF extends Remote {
    void registerChatClient(ChatClientIF chatClient, String name) throws RemoteException;
    void broadcastMessage(String message, String type) throws RemoteException;
    void sendFile(String message, String type) throws IOException;
    void sendFileBetweenServer(String username,String clientpath, String filename, String toRoot) throws IOException;
    // Login
    String login(String num1, String num2, ChatClientIF novo)
            throws RemoteException, FileNotFoundException;

    // Register
    String register(String username, String password)
            throws RemoteException, FileNotFoundException;
}

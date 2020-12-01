/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author TOUCHIT_02
 */
public interface ChatServerIF extends Remote {
    void registerChatClient(ChatClientIF chatClient, String name) throws RemoteException;
    void broadcastMessage(String message, String type) throws RemoteException;
}

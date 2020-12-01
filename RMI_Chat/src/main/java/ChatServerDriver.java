/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 *
 * @author TOUCHIT_02
 */
public class ChatServerDriver {
    
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        //New instance of ChatServer is going to be bound to the "RMIChatServer"
        Naming.rebind("RMIChatServer", new ChatServer());
    }
    
}

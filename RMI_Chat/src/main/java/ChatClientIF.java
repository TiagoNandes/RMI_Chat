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
public interface ChatClientIF extends Remote {
    void retrieveMessage(String message, boolean firstMessage) throws RemoteException;
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TOUCHIT_02
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerIF {
    private static final long serialVersionUID = 1L; //?????
    private ArrayList<ChatClientIF> chatClients;
    List<String> names = new ArrayList<String>();
    List<String> messages = new ArrayList<String>();
    
    
    protected ChatServer() throws RemoteException { //?????
        chatClients = new ArrayList<ChatClientIF>();
    }
    
    public synchronized void registerChatClient(ChatClientIF chatClient, String name)
            throws RemoteException {
        
        //codigo de registo 
        
        
        
        //add chat client instance to the chat client array ist
        this.chatClients.add(chatClient);
        names.add(name);

        System.out.println("chatClient : " + chatClients);
        System.out.println("nAMEEEEES : " + names);
        
        
        
        
        
        loadMessages();
    }
    
    
    
    public synchronized void broadcastMessage(String message, String type) throws RemoteException{
       
        
        //private message 
        if(type.equals("private")){
            
            
            System.out.println("mensagem privada: " + message);
            

            int firstIndex = message.indexOf("/");

            String first = message.substring(0, firstIndex); // from 0 to 3
            
            String rest = message.substring(firstIndex+1); // after the space to the rest of the line
   
            rest = rest.trim();
            
              
            int recipientIndex = rest.indexOf(" ");
            
            String recipient = rest.substring(0, recipientIndex);
            recipient = recipient.trim();
            
            int restIndex = rest.indexOf(" ");
            
            rest = rest.substring(restIndex + 1);
                    
            String finalMessage = "[private] " + first + rest;
            
            System.out.println("recipient: " + recipient);
            System.out.println("first: " + first);
            System.out.println("rest: " + rest);
            System.out.println("finalMessage: " + finalMessage);
           
            
            
            //Encontrar o index do user com o nome indicado 
            
            int index = 0;
            int recepientIndex = 0;
            for (String instance : names){
                if (instance.contains(recipient)){
                  System.out.println("ENCONTREI O ARTISTA : " + instance + "index: " + index);
                  recepientIndex = index;
                }
                else{
                    System.out.println("Entrei no ELSE");
                }
                index++;
            }
            
                    
             //go through all the chat clients and retrieve message passing the message to all the clients
            messages.add(finalMessage);
            System.out.println("-----------------");
            System.out.println(messages);
            System.out.println("-----------------");
    
            String listString = "";
        
            boolean firstMessage = true;

            for (String msg : messages)
            {
                listString = msg + "\t";

                int i = 0;
                while (i < chatClients.size()) {
                    chatClients.get(recepientIndex).retrieveMessage(listString, firstMessage);
                }
                firstMessage = false;
            }
            
        //public message
        } else {
            
            //go through all the chat clients and retrieve message passing the message to all the clients
            messages.add(message);
            System.out.println("-----------------");
            System.out.println(messages);
            System.out.println("-----------------");
            
            String listString = "";
        
            boolean firstMessage = true;

            for (String msg : messages)
            {
                listString = msg + "\t";

                int i = 0;
                while (i < chatClients.size()) {
                    chatClients.get(i++).retrieveMessage(listString, firstMessage);
                }
                firstMessage = false;
            }
        }
      
        /*
        int i = 0;
        while (i < chatClients.size()) {
            chatClients.get(i++).retrieveMessage(message);
        }*/
    }
    
    
    public synchronized void loadMessages() throws RemoteException{
        //go through all the chat clients and retrieve message passing the message to all the clients
        
        String listString = "";
        
        boolean firstMessage = true;
      
        for (String msg : messages)
        {
            listString = msg + "\t";
            
            int i = 0;
            while (i < chatClients.size()) {
                chatClients.get(i++).retrieveMessage(listString, firstMessage);
            }
            firstMessage = false;
        }
        
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

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
            System.out.println("message: " + message);
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
              //  while (i > chatClients.size()) {
                    chatClients.get(recepientIndex).retrieveMessage(listString, firstMessage);
                  //  i++;
                //}
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

   /* public synchronized void broadcastMessage(String message, String type) throws RemoteException{
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
                //  while (i > chatClients.size()) {
                chatClients.get(recepientIndex).retrieveMessage(listString, firstMessage);
                //  i++;
                //}
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
    }*/
    public  void loadMessages() throws RemoteException{
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
    public  void sendFile(String name, String message) throws IOException {

        int recepientIndex =findUserByName(name,  message);

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
          //  while (i < chatClients.size()) {
        System.out.print("AQUI"+messages);
                chatClients.get(recepientIndex).retrieveMessage(listString, true);
           // }
            firstMessage = false;
        }

        /*FILES +++++++++++++++++++++*///to upload a file
        String environment;
        String hostname;
        int portnumber;
        String clientpath;
        String serverpath;
        String upload = "upload";
        String download = "download";
        String dir= "dir";
        String mkdir= "mkdir";
        String rmdir= "rmdir";
        String rm= "rm";
        String shutdown= "shutdown";
        String[] splitStr = message.trim().split("\\s+");
       // String toFile= splitStr[1];
        if(upload.equals(splitStr[2]))
        {
            clientpath= splitStr[3];
            serverpath = splitStr[4];

            File clientpathfile = new File(clientpath);
            byte [] mydata=new byte[(int) clientpathfile.length()];
            FileInputStream in=new FileInputStream(clientpathfile);
            System.out.println("uploading to server...");
            in.read(mydata, 0, mydata.length);
            uploadFileToServer(mydata, serverpath, (int) clientpathfile.length());

            in.close();
        }
        //to download a file
        if(download.equals(splitStr[2]))
        {
            serverpath = splitStr[3];
            clientpath= splitStr[4];

            byte [] mydata = downloadFileFromServer(serverpath);
            System.out.println("downloading...");
            File clientpathfile = new File(clientpath);
            FileOutputStream out=new FileOutputStream(clientpathfile);
            out.write(mydata);
            out.flush();
            out.close();
        }

        //to list all the files in a directory
        if(dir.equals(splitStr[2]))
        {
            serverpath = splitStr[3];
            String[] filelist = listFiles(serverpath);
            for (String i: filelist)
            {
                System.out.println(i);
            }
        }

        //to create a new directory
        if(mkdir.equals(splitStr[2]))
        {
            serverpath = splitStr[3];
            boolean bool = createDirectory(serverpath);
            System.out.println("directory created :" + bool);
        }

        //to remove/delete a directory
        if(rmdir.equals(splitStr[2]) || rm.equals(splitStr[2]))
        {
            serverpath = splitStr[3];
            boolean bool = removeDirectoryOrFile(serverpath);
            System.out.println("directory deleted :" + bool);
        }
        //to shutdown the client
        if(shutdown.equals(splitStr[2]))
        {
            System.exit(0);
            System.out.println("Client has shutdown. Close the console");
        }

        /*+++++++++++++++++++++++++++++++++++++++++++++++++++*/
    }
    public int findUserByName(String name, String message) throws RemoteException{

        String[] splitStr = message.trim().split("\\s+");
        String toFile= splitStr[1];

        //Encontrar o index do user com o nome indicado

        int index = 0;
        int recepientIndex = 0;
        for (String instance : names){
            if (instance.contains(toFile)){
                System.out.println("ENCONTREI O ARTISTA : " + instance + "index: " + index);
                recepientIndex = index;
            }
            else{
                System.out.println("Entrei no ELSE");
            }
            index++;
        }
        return recepientIndex;
    }
    public void uploadFileToServer(byte[] mydata, String serverpath, int length) throws RemoteException {

        try {
            File serverpathfile = new File(serverpath);
            FileOutputStream out=new FileOutputStream(serverpathfile);
            byte [] data=mydata;

            out.write(data);
            out.flush();
            out.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        System.out.println("Done writing data...");

    }

    public byte[] downloadFileFromServer(String serverpath) throws RemoteException {

        byte [] mydata;

        File serverpathfile = new File(serverpath);
        mydata=new byte[(int) serverpathfile.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverpathfile);
            try {
                in.read(mydata, 0, mydata.length);
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return mydata;

    }


    public String[] listFiles(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.list();

    }

    public boolean createDirectory(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.mkdir();

    }

    public boolean removeDirectoryOrFile(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.delete();

    }

    public String login (String num1, String num2, ChatClientIF nextClient)
            throws RemoteException, FileNotFoundException {
        String bi = "";

        File inputFile = new File("USERDATA.txt");

        try {
            Scanner in = new Scanner(new File("USERDATA.txt"));

            while (in.hasNextLine())
            {
                String s = in.nextLine();
                String[] sArray = s.split(",");

                System.out.println(sArray[0]); //Just to verify that file is being read
                System.out.println(sArray[1]);
                String[] details = {num1, "localhost", "clientServiceName"};

                if (num1.trim().equals(sArray[0]) && num2.trim().equals(sArray[1]))
                {
                    System.out.print(num1 + "Logged In!");
                    bi = "Sucesso!";

                   // chatters.addElement(new Chatter(details[0], nextClient));
                   // nextClient.messageFromServer("[Server] : Hello " + details[0] + " you are now free to chat.\n");
                    System.out.println("-----------------");
                    registerChatClient(nextClient, num1);
                    break;
                }
                else
                {
                    bi = "Array Falhou " + sArray[0] + "-" + sArray[1];
                }
            }

            //in.close();

        } catch (FileNotFoundException e) {
            System.out.print("Falhou" + e);
            bi = "ERRO" + num1 + "-" + num2;
        }
       // return bi;
        return bi;
    }
    public String register (String username, String password)
            throws RemoteException, FileNotFoundException {

        String result = "";

        boolean isUserTaken = false;

        //      -----------------------


        try {
            Scanner readFile = new Scanner(new File("USERDATA.txt"));

            while (readFile.hasNextLine())
            {
                String s = readFile.nextLine();
                String[] sArray = s.split(",");

                System.out.println(sArray[0]); //Just to verify that file is being read
                System.out.println(sArray[1]);


                if (username.trim().equals(sArray[0]))
                {
                    isUserTaken = true;
                }
            }

            if(isUserTaken){
                result = "Ups! Username is already taken.";
            }else{
                try {
                    FileWriter myWriter = new FileWriter("USERDATA.txt",true);
                    myWriter.write(username + "," + password + "\n");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                    result = "sucesso!";
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }

            //in.close();

        } catch (FileNotFoundException e) {
            System.out.print("Falhou" + e);
        }

        return result;
    }

    public void sendFileBetweenServer(String username,String clientpath, String filename, String toRoot) throws IOException {


        String pathServer = "C:\\Users\\sarav\\IdeaProjects\\RmiChatProject\\RMI_Chat\\src\\main\\java\\serverfolder\\";
        pathServer= pathServer+filename;

        File clientpathfile = new File(clientpath);
        byte [] mydata=new byte[(int) clientpathfile.length()];
        FileInputStream in=new FileInputStream(clientpathfile);
        System.out.println("uploading to server...");
        in.read(mydata, 0, mydata.length);

        uploadFileToServer(mydata, pathServer,(int) clientpathfile.length());
        //downloadFileFromServer();
        System.out.println(pathServer);
        byte [] mydata1 = downloadFileFromServer(pathServer);
        System.out.println("downloading...");
        File serverpathfile = new File(pathServer);
        FileOutputStream out=new FileOutputStream(toRoot);
        out.write(mydata1);
        out.flush();
        out.close();





        //Notify user
        int index = 0;
        int recepientIndex = 0;
        for (String instance : names){
            if (instance.contains(username)){
                recepientIndex = index;
            }
            index++;
        }

        //go through all the chat clients and retrieve message passing the message to all the clients
        messages.add("File received");
        System.out.println("-----------------");
        System.out.println(messages);
        System.out.println("-----------------");

        String listString = "";

        boolean firstMessage = true;

        for (String msg : messages)
        {
            listString = msg + "\t";

            int i = 0;
            //  while (i < chatClients.size()) {
            System.out.print("AQUI"+messages);
            chatClients.get(recepientIndex).retrieveMessage(listString, true);
            // }
            firstMessage = false;
        }
    }
}

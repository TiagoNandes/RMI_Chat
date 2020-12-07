import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ChatServer extends UnicastRemoteObject implements ChatServerIF {
    //private static final long serialVersionUID = 1L; //?????
    private ArrayList<ChatClientIF> chatClients;
    List<String> names = new ArrayList<String>();
    List<String> messages = new ArrayList<String>();

    
    protected ChatServer() throws RemoteException {
        chatClients = new ArrayList<ChatClientIF>();
        messages.add("*************CHAT*************");
    }
    
    public synchronized void registerChatClient(ChatClientIF chatClient, String name)
            throws RemoteException {
        
        //codigo de registo
        this.chatClients.add(chatClient);
        names.add(name);

        System.out.println("chatClient : " + chatClients);
        System.out.println("nAMEEEEES : " + names);
        
        broadcastMessage(name + " has logged in");
    }
    
    
    
    public synchronized void broadcastMessage(String message) throws RemoteException{

        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("[d MMM HH:mm] ");
        String strDate = formatter.format(date).toString();

        message = strDate + message;

        messages.add(message);

        String listString = "";

        for (String msg : messages)
        {
            listString = msg + "\t";
            //private message
            if(listString.contains("/")){
                handleprivatemessage(listString);
            //public message
            } else {
                loadMessages(listString);
            }

        }

    }


    public  void loadMessages(String message) throws RemoteException{

        boolean firstMessage = true;

        int i = 0;
        while (i < chatClients.size()) {
            chatClients.get(i++).retrieveMessage(message, firstMessage);
        }

    }

    public int findUserByName(String name) throws RemoteException{
        //Encontrar o index do user com o nome indicado
        int index = 0;
        int recepientIndex = -1;
        for (String instance : names){
            if (instance.contains(name)){
                recepientIndex = index;
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
                    System.out.print(num1 + "Logged In! \n" );

                    bi = "Sucesso!";

                   // chatters.addElement(new Chatter(details[0], nextClient));
                   // nextClient.messageFromServer("[Server] : Hello " + details[0] + " you are now free to chat.\n");
                    System.out.println("-----------------");

                    //handle second login with the same account
                    if(names.contains(num1)){
                        int userNamesIndex = names.indexOf(num1);
                        chatClients.set(userNamesIndex, nextClient);
                    }else{
                        registerChatClient(nextClient, num1);
                    }
                    break;
                }
                else
                {
                    bi = "Erro";
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


        } catch (FileNotFoundException e) {
            System.out.print("Falhou" + e);
        }

        return result;
    }

    public void sendFileBetweenServer(String sender, String username,String clientpath, String filename, String toRoot) throws IOException {

        String pathServer = System.getProperty("user.dir") + "\\serverfolder\\";
        pathServer= pathServer+filename;

        File clientpathfile = new File(clientpath);
        byte [] mydata=new byte[(int) clientpathfile.length()];
        FileInputStream in=new FileInputStream(clientpathfile);
        System.out.println("uploading to server...");
        in.read(mydata, 0, mydata.length);

        uploadFileToServer(mydata, pathServer,(int) clientpathfile.length());
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

        //Broadcast file received message on the receiver's end
        broadcastMessage(sender + " : /" + username + " File received from " + sender);

        String listString = "";

        boolean firstMessage = true;

        for (String msg : messages)
        {
            listString = msg + "\t";

            chatClients.get(recepientIndex).retrieveMessage(listString, true);

            firstMessage = false;
        }
    }

    public void handleprivatemessage(String message) throws RemoteException{
        int firstIndex = message.indexOf("/");
        String first = message.substring(0, firstIndex);
        String rest = message.substring(firstIndex+1);
        rest = rest.trim();
        int recepientIndex = rest.indexOf(" ");
        String recipient = rest.substring(0, recepientIndex);
        recipient = recipient.trim();
        int restIndex = rest.indexOf(" ");
        rest = rest.substring(restIndex + 1);
        String finalMessage = "[private] " + first + rest;

        //System.out.println("message: " + message);
        //System.out.println("recipient: " + recipient);
        //System.out.println("first: " + first);
        //System.out.println("rest: " + rest);
        //System.out.println("finalMessage: " + finalMessage);

        recepientIndex= findUserByName(recipient);

        if(recepientIndex == -1){
            broadcastMessage("User " + recipient + " is offline or does not exist!");
        }else{
            loadAllMesages(finalMessage, recepientIndex);
        }

    }

    public void loadAllMesages(String message, int recepientIndex) throws RemoteException{

        boolean firstMessage = true;
        chatClients.get(recepientIndex).retrieveMessage(message, firstMessage);

    }
}

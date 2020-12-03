/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
    private String pass = null;

    protected ChatClient(ChatServerIF chatServer) throws IOException {
        this.name = name;
        this.pass = pass;
        this.chatServer = chatServer;
        checkLoginState(chatServer, this, name);
    }

    public static void clearScreen() {
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
        }
    }

    /**
     * MENU INICIAL REGISTO E LOGIN PASSA AO MENU SEGUINTE
     ***************************************/
    public void checkLoginState(ChatServerIF chatServer, ChatClient client, String name) throws IOException {
        boolean check = false;

        DataInputStream din = new
                DataInputStream(System.in);
        System.out.println
                ("1 - Register");
        System.out.println
                ("2 - Login");
        System.out.println
                ("3 - Exit");
        System.out.println();
        System.out.print("Choice : ");

        String line = din.readLine();
        Integer choice = new Integer(line);

        int value = choice.intValue();
        if (value == 1) {
            boolean checkequalspass = false;
            while (checkequalspass == false) {
                Scanner register = new Scanner(System.in);

                System.out.print("Username : ");
                String username = register.nextLine();

                System.out.print("Password : ");
                String password = register.nextLine();

                System.out.print("Confirm password : ");
                String password2 = register.nextLine();
                if (password.equals(password2)) {
                    chatServer.register(username, password);
                    System.out.println("Registo check");
                    checkequalspass = true;
                    value = 2;
                }
                //register.close();
            }
        }
        if (value == 2) {
            while (!check) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("*********************LOGIN***************************\n");
                System.out.print("USERNAME************************\n");
                String name1 = scanner.next();
                System.out.print("PASSWORD************************\n");
                String pass = scanner.next();
                String loginState = chatServer.login(name1, pass, client);
                // scanner.close();
                String success = "Sucesso!";

                if (loginState.toLowerCase().trim().equals(success.toLowerCase().trim())) {
                    System.out.println(loginState);
                    mainmenu(chatServer, name1);
                    check = true;
                }
            }
        }
    }

    public void retrieveMessage(String message, boolean firstMessage) throws RemoteException {
        if (firstMessage == true) {
            clearScreen();
        }

        System.out.println(message);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();

            if (message.startsWith("/")) {
                if (message.startsWith("/file")) {
                    try {
                        System.out.println("CHATCLIENT" + message);
                        chatServer.sendFile(name, message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {

                        chatServer.broadcastMessage(name + " : " + message, "private");
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    public void mainmenu(ChatServerIF chatServer, String name1) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean choosen = false;
        while (true){
        System.out.println("1 - Send File");
        System.out.println("2 - Send Message");
            int choice = Integer.parseInt(scanner.nextLine());
        if(choice == 1){
            fileMenu(chatServer, name1);
        }
            if(choice == 2){
                System.out.println("/PERSONSNAME YOURMESSAGE ---> Send Private Message");
                System.out.println("YOURMESSAGE ---> Send Private Message");
                Scanner scanner1 = new Scanner(System.in);
                String message = scanner1.nextLine();

                if (message.startsWith("/")) {
                        try {

                            chatServer.broadcastMessage(name1 + " : " + message, "private");
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                } else {

                    try {

                        chatServer.broadcastMessage(name1 + " : " + message, "public");
                    } catch (RemoteException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
              //  fileMenu(chatServer);
            }
            else{
            System.out.println("Invalid value");
        }

        }
    }
    public void fileMenu(ChatServerIF chatServer, String name1) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("To whom do you want to send your file? write users name\n");
        String username = scanner.nextLine();
        System.out.print("Insert the root path in you pc\n");
        String fromRoot = scanner.nextLine();
        System.out.print("Insert the file name with extension\n");
        String fileName = scanner.nextLine();
        System.out.print("The file will be in downloads\n");

        String home = System.getProperty("user.home");
        File file = new File(home+"\\Downloads\\" + fileName );
        String toRoot =home+"\\Downloads\\" + fileName;
        System.out.println(toRoot);
                //scanner.next();
        chatServer.sendFileBetweenServer(username, fromRoot,fileName,toRoot);
        mainmenu(chatServer, name1);
    }
}

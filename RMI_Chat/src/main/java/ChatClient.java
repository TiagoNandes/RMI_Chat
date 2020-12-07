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
        boolean isMenuActive = true;

        while (isMenuActive) {

            DataInputStream din = new DataInputStream(System.in);
            System.out.println
                    ("1 - Register");
            System.out.println
                    ("2 - Login");
            System.out.println
                    ("0 - Exit");
            System.out.println();
            System.out.print("Choice : ");

            String choice = din.readLine();
            //String choice = new Integer(line);

            //int value = choice.intValue();

            String value = choice.trim();

            if (value.equals("1")) {
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
                        String ret = chatServer.register(username, password);
                        System.out.println("Registo check: " + ret);
                        checkequalspass = true;
                        //value = "2";
                    } else {
                        System.out.println("The passwords do not match!");
                        System.out.println("Try again: ");
                        System.out.println("");
                    }
                }
            } else if (value.equals("2")) {
                while (!check) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("*********************LOGIN***************************\n");
                    System.out.print("USERNAME************************\n");
                    String name1 = scanner.next();
                    System.out.print("PASSWORD************************\n");
                    String pass = scanner.next();
                    String loginState = chatServer.login(name1, pass, client);
                    String success = "Sucesso!";
                    System.out.println(loginState);
                    if (loginState.toLowerCase().trim().equals(success.toLowerCase().trim())) {
                        mainmenu(chatServer, name1);
                        check = true;
                        isMenuActive = false;
                    }
                }
            } else if (value.equals("0")) {
                System.exit(0);
            } else {
                System.out.println("Please choose a valid option.");
            }
        }
    }

    public void retrieveMessage(String message, boolean firstMessage) throws RemoteException {
        if (firstMessage == true) {
           // clearScreen();
        }

        System.out.println(message);
    }

    public void run() {


    }

    public void mainmenu(ChatServerIF chatServer, String name1) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean choosen = false;
        boolean isMenuVisible = true;
            while (isMenuVisible){
            System.out.println("1 - Send File");
            System.out.println("2 - Send Message");
            System.out.println("0 - Exit");
            String choice = scanner.nextLine();
            if(choice.equals("1")){
                fileMenu(chatServer, name1);
                isMenuVisible = false;
            }
            else if(choice.equals("2")){
                System.out.println("/PERSONSNAME YOURMESSAGE ---> Send Private Message");
                System.out.println("YOURMESSAGE ---> Send Public Message");

                chatServer.broadcastMessage(name1 + " has joined the chat");

                boolean isChatActive = true;

                while (isChatActive) {

                    Scanner scanner1 = new Scanner(System.in);
                    String message = scanner1.nextLine();
                    isMenuVisible = false;

                    if (message.startsWith("/back")) {
                        isChatActive = false;
                        isMenuVisible = true;
                        chatServer.broadcastMessage(name1 + " has left the chat");
                    } else if (message.startsWith("/")) {
                        try {
                            chatServer.broadcastMessage(name1 + " : " + message);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        try {
                            chatServer.broadcastMessage(name1 + " : " + message);
                        } catch (RemoteException ex) {
                            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else if(choice.equals("0")){
                System.exit(0);
            }else{
                System.out.println("Please choose a valid option.");
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
        chatServer.sendFileBetweenServer(name1, username, fromRoot,fileName,toRoot);
        mainmenu(chatServer, name1);
    }
}

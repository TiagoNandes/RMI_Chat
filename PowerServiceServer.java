import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import Objects.Users ;

//
// PowerServiceServer
//
// Server for a RMI service that calculates powers
//
public class PowerServiceServer extends UnicastRemoteObject
implements PowerService
{
    public PowerServiceServer () throws RemoteException
    {
        super();
    }

    // Calculate the square of a number
    public String square (int number )
    throws RemoteException
    {
        String numrep = String.valueOf(number);
        String bi = new String (numrep);
        
        // Square the number
       // bi.multiply(bi);

        return (bi);
    }

    // Calculate the power of a number
    public String login (String num1, String num2)
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


                if (num1.trim().equals(sArray[0]) && num2.trim().equals(sArray[1]))
                {
                    System.out.print(num1 + "Logged In!");
                    bi = "Sucesso!";
                    break;
                }
                else
                {
                    //System.out.print("Falhou" + num1 + "-" + num2);
                    bi = "Array Falhou " + sArray[0] + "-" + sArray[1];
                }
            }

            //in.close();

        } catch (FileNotFoundException e) {
            System.out.print("Falhou" + e);
            bi = "ERRO" + num1 + "-" + num2;
        }

/*
        String inpUser = "sara";
        String inpPass = "1234"; // gets input from user

        if (inpUser.equals(num1) && inpPass.equals(num2)) {
            System.out.print("Sucesso" + num1 + "-" + num2);
            bi = "Sucesso" + num1 + "-" + num2;
        } else {
            System.out.print("Falhou" + num1 + "-" + num2);
            bi = "Falhou" + num1 + "-" + num2;
        }



        for(int i =0;i< users1.length;i++ ){
            bi += users1[i];
            if(users1[i].name == num1){
                if(users1[i].password==num2){
                    bi = true;
                }
            }
        }*/
	//bi = bi.num2;

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

    public static void main ( String args[] ) throws Exception
    {
        // Assign a security manager, in the event that dynamic
	// classes are loaded
        if (System.getSecurityManager() == null)
            System.setSecurityManager ( new RMISecurityManager() );

        // Create an instance of our power service server ...
        PowerServiceServer svr = new PowerServiceServer();

        // ... and bind it with the RMI Registry
        Naming.bind ("PowerService", svr);

        System.out.println ("Service bound....");


    }
}
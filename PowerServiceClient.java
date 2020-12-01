import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.Scanner;

//
//
// PowerServiceClient
//
//
public class PowerServiceClient 
{
	public static void main(String args[]) throws Exception
	{
		// Check for hostname argument
		if (args.length != 1)
		{
			System.out.println
			("Syntax - PowerServiceClient host");
			System.exit(1);
		}

		// Assign security manager
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager
			(new RMISecurityManager());
		}

		// Call registry for PowerService
		PowerService service = (PowerService) Naming.lookup
			("rmi://" + args[0] + "/PowerService");

		DataInputStream din = new 
			DataInputStream (System.in);

		for (;;)
		{
			System.out.println 
			  ("1 - Register");
			System.out.println 
			  ("2 - Login");
			System.out.println 
			  ("3 - Exit"); System.out.println();
			System.out.print ("Choice : ");

			String line = din.readLine();
			Integer choice = new Integer(line);
			
			int value = choice.intValue();

			switch (value)
			{
			case 1:

				Scanner register = new Scanner(System.in);

			  System.out.print ("Username : ");
				String username  = register.nextLine();

			  System.out.print("Password : ");
				String password  = register.nextLine();

				System.out.print("Confirm password : ");
				String password2  = register.nextLine();

				if (password.equals(password2)){
					// Call remote method
					System.out.println("Answer : " + service.register(username, password));
				}
				else{
					System.out.println("The passwords are not equal!");
					break;
				}

				
			  break;
			case 2:

				Scanner in = new Scanner(System.in);

				System.out.print ("Username : ");
				// Get username from user's input
				String name = in.nextLine();

			  	System.out.print ("Password  : ");
				// Get username from user's input
				String pass = in.nextLine();

				// Call remote method
				System.out.println("Answer : " + service.login(name, pass));

			  break;
			case 3:
			  System.exit(0);
			default :
			  System.out.println ("Invalid option");
			break;
			}
		}
	}

}
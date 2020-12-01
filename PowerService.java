import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//
// PowerService Interface
//
// Interface for a RMI service that calculates powers
//
public interface PowerService extends Remote
{
	// Calculate the square of a number
	public String square (int number )
		throws RemoteException;

	// Login
	public String login  (String num1, String num2)
			throws RemoteException, FileNotFoundException;

	// Register
	public String register  (String username, String password)
			throws RemoteException, FileNotFoundException;
}
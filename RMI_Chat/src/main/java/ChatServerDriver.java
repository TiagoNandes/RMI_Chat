import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class ChatServerDriver {
    
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        //New instance of ChatServer is going to be bound to the "RMIChatServer"
        Naming.rebind("RMIChatServer", new ChatServer());
    }
    
}

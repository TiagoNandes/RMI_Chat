import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientIF extends Remote {
    void retrieveMessage(String message, boolean firstMessage) throws RemoteException;
}

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerIF extends Remote {
    void registerChatClient(ChatClientIF chatClient, String name) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;

    void sendFileBetweenServer(String sender, String username,String clientpath, String filename, String toRoot) throws IOException;
    // Login
    String login(String num1, String num2, ChatClientIF novo)
            throws RemoteException, FileNotFoundException;

    // Register
    String register(String username, String password)
            throws RemoteException, FileNotFoundException;
}

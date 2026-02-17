import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient_itf extends Remote{
    // Revevoir un message
    public void receiveMessage(String from, String message) throws RemoteException;

    // Rejoindre le chat
    public void joinChat(String name) throws RemoteException;

    // Quitter le chat
    public void leaveChat(String name) throws RemoteException;

    public void serverShutdown() throws RemoteException;
}
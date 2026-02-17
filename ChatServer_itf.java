import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServer_itf extends Remote {
    // Rejoindre le chat
    public void joinChat(String name,ChatClient_itf client) throws RemoteException;

    // Quitter le chat
    public void leaveChat(String name) throws RemoteException;

    // Envoyer un message
    public void sendMessage(String message,String name) throws RemoteException;
    
    // Recup l'historique des messages
    public List<String> getHistory() throws RemoteException;

    // Effacer l'historique des messages
    public void clearHistory(String name) throws RemoteException;

    public List<String> getUsers() throws RemoteException;

    public void sendPm(String from, String to, String message) throws RemoteException;
    
    public void shutdown() throws RemoteException;
}
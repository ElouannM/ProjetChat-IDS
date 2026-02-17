import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerImpl implements ChatServer_itf{
    // Clients
    private ConcurrentHashMap<String,ChatClient_itf> clients;
    // Historique
    private ArrayList<String> history;
    private String serverName;
    File file;


    public ChatServerImpl(String serverName){
        this.serverName = serverName;
        clients = new ConcurrentHashMap<>();
        history = new ArrayList<>();// Cree l'historique

        // Gestion de l'historique
        file = new File("history"+serverName.replace(" ", "_")+".txt");
        if(file.exists()){
            try {
                // Lecture de l'ancien historique
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null){
                    history.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la lecture de l'historique");
            }

        }

    }

    public void joinChat(String name,ChatClient_itf client) throws RemoteException {
        if(clients.containsKey(name)){
            throw new RemoteException("Username already taken");
        }
        String message = name + " joined the chat";
        history.add(message);
        saveToHist(message);
        for( ChatClient_itf c : clients.values()){
            try {
                c.joinChat(name);
            } catch (RemoteException e) {
            }
        }
        clients.put(name,client);
    }

    public void leaveChat(String name) throws RemoteException {
        clients.remove(name);  
        String message = name + " left the chat";
        history.add(message);
        saveToHist(message);
        for( ChatClient_itf c : clients.values()){
            try {
                c.leaveChat(name);
            } catch (RemoteException e) {
            }
        }
    }

    public void sendMessage(String message,String name) throws RemoteException {
        history.add(name + ": " + message);
        saveToHist(name + ": " + message);
        for( ChatClient_itf client : clients.values()){
            try {
                client.receiveMessage(name, message);
            } catch (RemoteException e) {
            }
        }
    }

    public void saveToHist(String message) {
        
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(message + "\n");
            writer.close();
        } catch (Exception e) {
            
        }

    }

    public List<String> getHistory() throws RemoteException{
        return history;
    }

    public void clearHistory(String name) throws RemoteException{
        history.clear();
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.close();
        } catch (Exception e) {
        }
        sendMessage(" cleared the history", name);
    }


    public List<String> getUsers() throws RemoteException{
        return new ArrayList<>(clients.keySet());
    }


    public void sendPm(String from, String to, String message) throws RemoteException{
        ChatClient_itf client = clients.get(to);
        if(client != null){
            client.receiveMessage("[PM from " + from + "]", message);
        }
    }


    public void shutdown() throws RemoteException{
        for( ChatClient_itf client : clients.values()){
            try {
                client.serverShutdown();
            } catch (RemoteException e) {
            }
        }
    }
}
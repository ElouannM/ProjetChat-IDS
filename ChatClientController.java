import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;



public class ChatClientController  implements ChatClient_itf{
    private ChatServer_itf server;
    private ChatClient_itf client_stub;
    private String username,serverName;
    private ClientVue vue;

    public ChatClientController(String host, int port,String name,String serverName) throws Exception{
        username = name;
        this.serverName = serverName;
        Registry registry = LocateRegistry.getRegistry(host, port); 
        server = (ChatServer_itf) registry.lookup(serverName);
        
        client_stub = (ChatClient_itf) UnicastRemoteObject.exportObject(this,0);

        server.joinChat(username,client_stub);
        }

    public void setVue(ClientVue vue){
        this.vue = vue;
    }

    
    public void sendMessage(String message){
        try {
            switch(message.split(" ")[0]){
                case "/quit":
                    leaveChat();
                    System.exit(0);
                    break;
                case "/hist":
                    List<String> history = server.getHistory();
                    for(String msg : history){
                        vue.afficherMessage(msg,"Historique");
                }
                break;
                case "/clear":
                    server.clearHistory(getUserName());
                    vue.afficherMessage("clear", "Historique");
                    break;
                case "/users":
                    List<String> users = server.getUsers();
                    for(String user : users){
                        vue.afficherMessage(user,"Utilisateurs");
                    }
                    break;
                case "/pm":
                    String[] parts = message.split(" ", 3);
                    if(parts.length < 3){
                        vue.afficherMessage("Usage: /pm user message", "Erreur");
                        break;
                    }
                    String toUser = parts[1];
                    String mess = parts[2];
                    server.sendPm(username, toUser, mess);
                    vue.afficherMessage(message, mess);
                    break;
                default:
                    server.sendMessage(message, username);
                    break;

            }

            //server.sendMessage(message, username);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi du message");
        }
    }

    public void leaveChat(){
        try {
            server.leaveChat(username);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du leaveChat");
        }
    }


    public String getUserName(){
        return this.username;
    }

    public String getServerName(){
        return this.serverName;
    }

    @Override
    public void receiveMessage(String from, String message) throws RemoteException{
        vue.afficherMessage(message,from);
    }

    @Override
    public void joinChat(String name) throws RemoteException {
        vue.joinChat(name);
    }

    @Override
    public void leaveChat(String name) throws RemoteException {
        vue.leaveChat(name);
    }

    public void serverShutdown() throws RemoteException {
        vue.serverShutdown();
}


}
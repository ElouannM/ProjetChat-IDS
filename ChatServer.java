import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



public class ChatServer {
    public static void main(String [] args){
        try {
            

            Registry registry = null;
            String serverName = "Server Service";
            if (args.length > 0 && args.length < 2){
		        registry= LocateRegistry.getRegistry(Integer.parseInt(args[0])); 
            }
            else if (args.length >= 2){
                StringBuilder sb = new StringBuilder();
                for(int i = 1;i < args.length;i++){
                    sb.append(args[i]);
                    if(i != args.length - 1){
                        sb.append(" ");
                    }
                }
                serverName = sb.toString();
                registry= LocateRegistry.getRegistry(Integer.parseInt(args[0]));
                
            }
            else{
                registry = LocateRegistry.getRegistry();
            }

            ChatServerImpl server = new ChatServerImpl(serverName);
            ChatServer_itf servStub = (ChatServer_itf) UnicastRemoteObject.exportObject(server,0);
            registry.rebind(serverName, servStub);


            final String finalServerName = serverName;
            final Registry finalRegistry = registry;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Arrêt du serveur...");
                server.shutdown();     
                finalRegistry.unbind(finalServerName);        
                UnicastRemoteObject.unexportObject(server, true); 
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

            System.out.println ("Server ready: " + serverName);

            

        } catch (Exception e) {
            System.err.println("Error on server :" + e) ;
		    e.printStackTrace();
        }
    }
}
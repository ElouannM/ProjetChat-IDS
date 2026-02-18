import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ChatClient {

    public static void main(String args[]){
        

            if (args.length < 2) {
                System.out.println("Usage: java Client <rmiregistry host> <rmiregistry port>");
                return;}
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            SwingUtilities.invokeLater(() -> {
                try {


                    Registry registry = LocateRegistry.getRegistry(host, port);
                    String[] services = registry.list();

                    String serverChoice = JOptionPane.showInputDialog(null,"Choisissez un serveur :","Connexion au chat", JOptionPane.PLAIN_MESSAGE, null, services, services[0]).toString();
                    if(serverChoice == null || serverChoice.isEmpty()){
                        System.exit(1);
                    }

                    String username = JOptionPane.showInputDialog(null,"Entrez votre nom d'utilisateur:","Connexion au chat", JOptionPane.PLAIN_MESSAGE);
                    if(username == null || username.isEmpty()){
                        System.exit(1);
                    }
                    //System.out.println(username);
                    ChatClientController control = new ChatClientController(host, port, username, serverChoice);

                    ClientVue vue = new ClientVue(control);
                    vue.setVisible(true);
                    vue.setFocusable(true);

                } catch (Exception e) {
                    e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Erreur de connexion:\n" + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
                }
            });
         

    }


}



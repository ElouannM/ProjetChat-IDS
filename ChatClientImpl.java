import java.rmi.RemoteException; 

public class ChatClientImpl implements ChatClient_itf {

    @Override
    public void receiveMessage(String from, String message) throws RemoteException{
        System.out.println(from + ": "+ message);
    }

    @Override
    public void joinChat(String name) throws RemoteException {
        System.out.println(name + "has joined");
    }

    @Override
    public void leaveChat(String name) throws RemoteException {
        System.out.println(name + "has left");
    }

    public void serverShutdown() throws RemoteException {
        System.out.println("Server is shutting down");
    }
}
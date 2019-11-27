import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectionManager {

    private ServerSocket serverSocket;
    private int port;
    private ArrayList<Client> connections = new ArrayList<>();
    private Thread listenerThread;
    private Thread timeoutThread;
    private int idCounter = 0;
    private boolean listening = false;

    public ConnectionManager(int port){
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Socket opened on port " + port);
            listen();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("ERROR STARTING SERVER");
        }
    }

    /*
    Starts listening for new incoming connections
    New connections are added to the "connections" ArrayList
     */
    private void listen(){
            listenerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Socket connectionSocket = serverSocket.accept();
                            System.out.println("New connection");
                            Client newClient = new Client(idCounter, connectionSocket);
                            connections.add(newClient);
                            idCounter++;
                            sendDataToClient(newClient, "ID "+newClient.getId()+"\n"
                                    +"SETGAMEMODE "+ Game.gamemode.getMode()+"\n"
                                    +"STATUS "+Game.getStatus()+"\n");
                            ArrayList<Player> playerList = Main.game.getPlayers();
                            for(int i=0;i<playerList.size();i++){
                                Player nextPlayer = playerList.get(i);
                                sendDataToClient(newClient,"PLAYERJOINED "+nextPlayer.getUsername()+" "+nextPlayer.getId()+" "+nextPlayer.getLooks()+"\n");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Listener stopped due to IOException");
                    }
                }
            });
            listenerThread.start();

            timeoutThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        for (int i = 0; i < connections.size(); i++) {
                            Client nextClient = connections.get(i);
                            if (System.currentTimeMillis() - nextClient.getLastUpdated() > 60000) {
                                System.out.println("Client " + nextClient.getId() + " timed out.");
                                try {
                                    nextClient.getSocket().close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Player player = Main.game.getPlayer(nextClient);
                                if(player!=null) {
                                    Main.game.removePlayer(player);
                                    sendDataToAllButSelf("PLAYERLEAVE "+player.getUsername()+" "+player.getId()+"\n",nextClient);
                                }
                                connections.remove(nextClient);
                            }
                        }
                       // System.out.println("Checked for timeouts");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            timeoutThread.start();



    }

    /*
    Reads data from all connected clients
    */
    public ArrayList<ClientData> readAllData(){
        ArrayList<ClientData> dataList = new ArrayList<>();
        for(int i=0;i<connections.size();i++){
            try {
                Client nextClient = connections.get(i);
                if(!nextClient.getSocket().isClosed()) {
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(nextClient.getSocket().getInputStream()));
                    ClientData clientData = new ClientData(nextClient);
                    while (inFromClient.ready()) {
                        String nextLine = inFromClient.readLine();
                        clientData.addLine(nextLine);
                    }
                    if (clientData.getData().size() > 0) {
                        dataList.add(clientData);
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Error reading data from client.");
            }
        }
        return dataList;
    }

    public void sendDataToAllButSelf(String data,Client exception){
        Client currentClient = null;

        try {
            for(int i=0;i<connections.size();i++){
                currentClient = connections.get(i);
                if(currentClient.getId()!=exception.getId()) {
                    DataOutputStream outToClient = new DataOutputStream(currentClient.getSocket().getOutputStream());
                    outToClient.writeBytes(data + "\n");
                }
            }
        }catch(IOException e){
            // e.printStackTrace();
            if(currentClient!=null) {
                System.out.println("Client " + currentClient.getId() + " is disconnected.");
            }
        }
    }

    public void sendDataToAll(String data){
        Client currentClient = null;

        try {
            for(int i=0;i<connections.size();i++){
                    currentClient = connections.get(i);
                    DataOutputStream outToClient = new DataOutputStream(currentClient.getSocket().getOutputStream());
                    outToClient.writeBytes(data + "\n");
                }
        }catch(IOException e){
            // e.printStackTrace();
            if(currentClient!=null) {
                System.out.println("Client " + currentClient.getId() + " is disconnected.");
            }
        }
    }

    public void sendDataToClient(Client client,String data){
        try {
            DataOutputStream outToClient = new DataOutputStream(client.getSocket().getOutputStream());
            outToClient.writeBytes(data+"\n");
        }catch(IOException e){
           // e.printStackTrace();
            System.out.println("Client "+client.getId()+" is disconnected.");
        }
    }

    public Client getClientByID(String id){
        for(int i=0;i<connections.size();i++){
            Client nextClient = connections.get(i);
            if(nextClient.getId() == Integer.parseInt(id)){
                return nextClient;
            }
        }
        return null;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<Client> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Client> connections) {
        this.connections = connections;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }
}

import java.net.Socket;

public class Client {
    private int id;
    private Socket socket;
    private long lastUpdated;

    public Client(int id, Socket socket){
        this.id = id;
        this.socket = socket;
        lastUpdated = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setLastUpdated(long time){
        lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}

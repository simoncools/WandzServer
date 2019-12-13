import java.net.Socket;

public class Client {
    private int id;
    private Socket socket;
    private long lastUpdated;
    private int score;

    public Client(int id, Socket socket){
        this.id = id;
        this.socket = socket;
        lastUpdated = System.currentTimeMillis();
        score = 0;
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

    public void addScore(int score){
        this.score += score;
    }

    public int getScore(){
        return this.score;
    }

    public void resetSCore(){
        score = 0;
    }
}

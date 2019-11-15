import java.net.Socket;

public class Client {
    private int id;
    private Socket socket;

    public Client(int id, Socket socket){
        this.id = id;
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }
}

import java.util.ArrayList;

public class ClientData {
    private ArrayList<String> data = new ArrayList<>();
    private Client client;

    public ClientData(Client client){
        this.client = client;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void addLine(String line){
        data.add(line);
    }

    public Client getClient() {
        return client;
    }
}

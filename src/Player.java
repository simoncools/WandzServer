import Spells.Spell;

public class Player {
    private String username;
    private int id;
    private Client client;

    public Player(String username, int id, Client client){
        this.username = username;
        this.id = id;
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

}

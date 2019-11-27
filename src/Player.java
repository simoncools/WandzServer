import Spells.Spell;

public class Player {
    private String username;
    private int id;
    private Client client;
    private String looks;

    public Player(String username, int id, Client client,String looks){
        this.username = username;
        this.id = id;
        this.client = client;
        this.looks = looks;
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

    public String getLooks() {
        return looks;
    }
}

import Gamemodes.Gamemode;

import java.util.ArrayList;

public class Game {
    public static boolean started;
    public static ArrayList<Player> playerList;
    public static Gamemode gamemode;

    public Game(Gamemode gamemode){
        this.started = false;
        this.gamemode = gamemode;
        this.playerList = new ArrayList<>();
    }

    public void addPlayer(Player player){
        playerList.add(player);
    }

    public Player getPlayer(Client client){
        for(int i=0;i<playerList.size();i++){
            Player nextPlayer = playerList.get(i);
            if(nextPlayer.getClient().getId() == client.getId()){
                return nextPlayer;
            }
        }
        return null;
    }

    public void removePlayer(Player player){
        playerList.remove(player);
    }

    public void setGamemode(Gamemode newMode){
        gamemode = newMode;
    }

    public Gamemode getGamemode(){
        return gamemode;
    }

    public ArrayList<Player> getPlayers(){
        return playerList;
    }

    public void setStarted(boolean started){
        this.started = started;
    }

    public static String getStatus(){
        if(started){
            return "STARTED";
        }else{
            return "NOTSTARTED";
        }
    }
}

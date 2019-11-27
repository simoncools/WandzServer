import Gamemodes.CorruptedWizard;
import Gamemodes.Elimination;
import Gamemodes.SpiralDefense;
import Gamemodes.TeamElimination;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Main {

    public static Game game;

    public static void main(String argv[]) throws Exception {
        ConnectionManager conMan = new ConnectionManager(6789);
        game = new Game(new Elimination());
        while (true) {
            ArrayList<ClientData> allData = conMan.readAllData();
            for (int i = 0; i < allData.size(); i++) {
                ArrayList<String> nextList = allData.get(i).getData();
                for (int k = 0; k < nextList.size(); k++) {
                    String nextLine = nextList.get(k);
                    String fullCommand = nextLine;
                    System.out.println("Command from client "+allData.get(i).getClient().getId()+": "+fullCommand);
                    if (nextLine.startsWith("HIT")) {
                        //command structure : HIT [casterID] [victimID] [spellID]
                        nextLine = nextLine.replace("HIT ", "");
                        String[] arguments = nextLine.split(" ");
                        if (arguments.length == 3) {
                            Client client = conMan.getClientByID(arguments[0]);
                            if (client != null) {
                                conMan.sendDataToClient(client, fullCommand);
                            }
                        }
                    }
                    else if (nextLine.startsWith("SETGAMEMODE")) {
                        nextLine = nextLine.replace("SETGAMEMODE ", "");
                        String arguments[] = nextLine.split(" ");
                        if (arguments.length == 1) {
                            String mode = arguments[0];
                            if (mode.equals("ELIMINATION") || mode.equals("TEAMELIMINATION") || mode.equals("SPIRALDEFENSE") || mode.equals("CORRUPTEDWIZARD")) {
                                if (mode.equals("ELIMINATION")) {
                                    game.setGamemode(new Elimination());
                                }
                                if (mode.equals("TEAMELIMINATION")) {
                                    game.setGamemode(new TeamElimination());
                                }
                                if (mode.equals("CORRUPTEDWIZARD")) {
                                    game.setGamemode(new CorruptedWizard());
                                }
                                if (mode.equals("SPIRALDEFENSE")) {
                                    game.setGamemode(new SpiralDefense());
                                }
                                conMan.sendDataToAll(fullCommand);
                            }
                        }
                    }
                    else if (nextLine.startsWith("JOIN")) {
                        nextLine = nextLine.replace("JOIN ", "");
                        String arguments[] = nextLine.split(" ");
                        if (arguments.length == 2) {
                            String username = arguments[0];
                            String looks = arguments[1];
                            Client client = allData.get(i).getClient();
                            int id = client.getId();
                            Player player = new Player(username, id, client,looks);
                            ArrayList<Player> playerList = game.getPlayers();
                            boolean notAdded = true;
                            for(int q=0;q<playerList.size();q++){
                                Player nextPlayer = playerList.get(q);
                                if(nextPlayer.getId()==player.getId()){
                                    notAdded = false;
                                    System.out.println("Player already in list");
                                }

                            }
                            if(notAdded){
                                game.addPlayer(player);
                                conMan.sendDataToAll("PLAYERJOINED "+player.getUsername()+" "+player.getId()+" "+player.getLooks()+"\n");
                            }
                        }
                    }
                    else if (nextLine.startsWith("LEAVE")) {
                        Client client = allData.get(i).getClient();
                        Player player = game.getPlayer(client);
                        game.removePlayer(player);
                        if(player!=null){
                        conMan.sendDataToAll("PLAYERLEAVE "+player.getUsername()+" "+player.getId()+"\n");
                        }
                    }
                    else if (nextLine.startsWith("START")) {
                        game.setStarted(true);
                        conMan.sendDataToAll("START");
                    }
                    else if (nextLine.startsWith("STOP")) {
                        game.setStarted(false);
                        conMan.sendDataToAll("STOP");
                    }
                    else if (nextLine.startsWith("KEEPALIVE")){
                        Client client = allData.get(i).getClient();
                        client.setLastUpdated(System.currentTimeMillis());
                    }
                }

            }
            Thread.sleep(100);
        }
    }
}

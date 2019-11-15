import Gamemodes.CorruptedWizard;
import Gamemodes.Elimination;
import Gamemodes.SpiralDefense;
import Gamemodes.TeamElimination;

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
                                conMan.sendDataToAllButSelf(fullCommand, allData.get(i).getClient());
                            }
                        }
                    }
                    else if (nextLine.startsWith("JOIN")) {
                        nextLine = nextLine.replace("JOIN ", "");
                        String arguments[] = nextLine.split(" ");
                        if (arguments.length == 1) {
                            String username = arguments[0];
                            Client client = allData.get(i).getClient();
                            int id = client.getId();
                            Player player = new Player(username, id, client);
                            game.addPlayer(player);
                            conMan.sendDataToAllButSelf("PLAYERJOINED "+player.getUsername()+" "+player.getId()+"\n",client);
                        }
                    }
                    else if (nextLine.startsWith("LEAVE")) {
                        Client client = allData.get(i).getClient();
                        game.removePlayer(client.getId());
                        Player player = game.getPlayer(client);
                        if(player!=null){
                        conMan.sendDataToAllButSelf("PLAYERLEAVE "+player.getUsername()+" "+player.getId()+"\n",client);
                        }
                    }
                    else if (nextLine.startsWith("START")) {
                        game.setStarted(true);
                        conMan.sendDataToAllButSelf("START", allData.get(i).getClient());
                    }
                    else if (nextLine.startsWith("STOP")) {
                        game.setStarted(false);
                        conMan.sendDataToAllButSelf("STOP", allData.get(i).getClient());
                    }
                }

            }
            Thread.sleep(100);
        }
    }
}
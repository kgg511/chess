package dataAccess;

import Model.Game;

import java.util.ArrayList;

//talks to the Game DB
public class GameDAO {


    private ArrayList<Game> GameDB = new ArrayList<Game>();
    Game createGame(int gameID, String whiteUsername, String blackUsername, String gameName,
                    chess.ChessGame game){
        return new Model.Game(gameID, whiteUsername, blackUsername, gameName, game);
    }

    //TODO: how to autofill id?
    boolean insertGame(Game game){
        this.GameDB.add(game);
        return true;
    }

    boolean deleteGame(Game game){
        boolean removed = this.GameDB.remove(game);
        return removed;
    }

    ArrayList<Game> getGames(){
        return GameDB;
    }

    Game getGameById(int id){
        for(Game g: GameDB){
            if(g.getGameID() == id){return g;}
        }
        return null;
    }

    boolean updateGame(Game game){
        //updates game based on id if it exists
        Game old;
        if((old = getGameById(game.getGameID())) != null){
            deleteGame(old);
            insertGame(game);
            //would this logic work?
            //no weird issues due to references?
        }

    }



    void clearGame(){
        GameDB = new ArrayList<Game>();
    }


}

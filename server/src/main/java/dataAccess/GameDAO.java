package dataAccess;

import dataAccess.interfaces.GameDAOInterface;
import model.GameData;

import java.util.ArrayList;

//talks to the Game DB
public class GameDAO implements GameDAOInterface {
    private static GameDAO instance = null;
    public static ArrayList<GameData> gameDB = new ArrayList<GameData>();

    public static synchronized GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    public int insertGame(GameData game) throws DataAccessException{
        this.gameDB.add(game);
        return game.gameID();
    }

    public ArrayList<GameData> getGames() throws DataAccessException{
        return this.gameDB;
    }
    public GameData getGameById(int id) throws DataAccessException{
        for(GameData g: gameDB){
            if(g.gameID() == id){return g;}
        }
        return null;
    }
    public GameData getGameByName(String gameName) throws DataAccessException{
        for(GameData g: gameDB){
            if(g.gameName().equals(gameName)){return g;}
        }
        return null;
    }
    public boolean updateGame(GameData game) throws DataAccessException{
        //updates game based on id if it exists
        GameData old = getGameById(game.gameID());
        if(old != null){
            this.gameDB.set(this.gameDB.indexOf(old), game);
            return true;
        }
        return false; //game not in database
    }
    public int numGames() throws DataAccessException{
        return gameDB.size();
    }
}

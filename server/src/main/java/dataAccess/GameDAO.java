package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.ArrayList;

//talks to the Game DB
public class GameDAO {
    private static GameDAO instance;
    private static ArrayList<GameData> gameDB = new ArrayList<GameData>();

    public static synchronized GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }
    public GameData createGame(int gameID, String whiteUsername, String blackUsername, String gameName,
                        chess.ChessGame game){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
    public int insertGame(GameData game) throws DataAccessException{
        this.gameDB.add(game);
        return game.gameID();
    }
    public boolean isEmpty(){
        return this.gameDB.size() == 0;
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
    public void clearGame() throws DataAccessException {
        gameDB.clear();
    }
    public int numGames() throws DataAccessException{
        return gameDB.size();
    }
}

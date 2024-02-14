package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.ArrayList;

//talks to the Game DB
public class GameDAO {
    private static GameDAO instance;
    private static ArrayList<GameData> GameDB = new ArrayList<GameData>();

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

    //TODO: how to autofill id?
    public int insertGame(GameData game) throws DataAccessException{
        this.GameDB.add(game);
        return game.gameID();
    }

    public boolean deleteGame(GameData game) throws DataAccessException{
        boolean removed = this.GameDB.remove(game);
        return removed;
    }

    public boolean isEmpty(){
        return this.GameDB.size() == 0;
    }

    public ArrayList<GameData> getGames() throws DataAccessException{
        //for each game, take out the ChessGame
        return this.GameDB;
        //ArrayList<ChessGame> gamez = new ArrayList<>();
        //for(GameData g: GameDB){
         //   gamez.add(g.game());
        //}
        //return gamez;
    }

    public GameData getGameById(int id) throws DataAccessException{
        for(GameData g: GameDB){
            if(g.gameID() == id){return g;}
        }
        return null;
    }

    public boolean updateGame(GameData game) throws DataAccessException{
        //updates game based on id if it exists
        GameData old = getGameById(game.gameID());
        if(old != null){
            deleteGame(old);
            insertGame(game);
            return true;
        }
        return false; //game not in database
    }

    public void clearGame() throws DataAccessException {
        GameDB.clear();
    }

    public int numGames() throws DataAccessException{
        return GameDB.size();
    }

}

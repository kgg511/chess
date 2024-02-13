package dataAccess;

import chess.ChessGame;
import model.GameData;

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
        GameDB = new ArrayList<>();
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    //TODO: how to autofill id?
    public boolean insertGame(GameData game) {
        this.GameDB.add(game);
        return true;
    }

    public boolean deleteGame(GameData game) throws DataAccessException{
        boolean removed = this.GameDB.remove(game);
        return removed;
    }

    public ArrayList<ChessGame> getGames() throws DataAccessException{
        //for each game, take out the ChessGame
        ArrayList<ChessGame> gamez = new ArrayList<>();
        for(GameData g: GameDB){
            gamez.add(g.game());
        }
        return gamez;
    }

    public GameData getGameById(int id) throws DataAccessException{
        for(GameData g: GameDB){
            if(g.gameID() == id){return g;}
        }
        return null;
    }

    public boolean updateGame(GameData game) throws DataAccessException{
        //updates game based on id if it exists
        GameData old;
        if((old = getGameById(game.gameID())) != null){
            deleteGame(old);
            insertGame(game);
            return true;
            //would this logic work?
            //no weird issues due to references?
        }
        return false; //game not in database
    }

    public void clearGame(){
        GameDB = new ArrayList<GameData>();
    }


}

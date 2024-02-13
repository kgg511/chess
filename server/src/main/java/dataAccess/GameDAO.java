package dataAccess;

import model.GameData;

import java.util.ArrayList;

//talks to the Game DB
public class GameDAO {

    private ArrayList<GameData> GameDB = new ArrayList<GameData>();
    public GameData createGame(int gameID, String whiteUsername, String blackUsername, String gameName,
                        chess.ChessGame game){
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

    public ArrayList<GameData> getGames() throws DataAccessException{
        return GameDB;
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

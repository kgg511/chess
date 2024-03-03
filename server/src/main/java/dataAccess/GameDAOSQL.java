package dataAccess;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import java.sql.ResultSet;
public class GameDAOSQL extends SQLShared{

    public GameDAOSQL() throws ResponseException, DataAccessException{
        configureDatabase(createStatements); //create the database
    }

    private final String[] createStatements = { //the game is json string
            """
            CREATE TABLE IF NOT EXISTS game (
               `gameId` integer auto_increment,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `gameObj` TEXT NOT NULL,
              PRIMARY KEY (`gameId`)
            )
            """
    };
    //var json = new Gson().toJson(pet);

    public GameData createGame(int gameID, String whiteUsername, String blackUsername, String gameName,
                               ChessGame game){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public int insertGame(GameData game) throws ResponseException, DataAccessException{
        //var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, gameObj) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(game.game());
        int id = executeUpdate(statement, game.whiteUsername(),
                game.blackUsername(), game.gameName(), json);
        return id;
    }

    public ArrayList<GameData> getGames() throws ResponseException, DataAccessException{
        ArrayList<GameData> games = null;
        String sql = "SELECT * FROM game";
        try(var conn = DatabaseManager.getConnection()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                games = new ArrayList<>();
                while(rs.next()){
                    int gameId = rs.getInt("gameId");
                    String wUsername = rs.getString("whiteUsername");
                    String bUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    var json = rs.getString("gameObj");
                    chess.ChessGame game = new Gson().fromJson(json, chess.ChessGame.class);

                    games.add(new GameData(gameId, wUsername, bUsername, gameName, game));
                }
                return games;
            }
        } //a username can have multiple authTokens, but one authToken should only have one username
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getAuth: %s, %s", sql, e.getMessage()));
        }
    }

    public GameData getGameById(int id) throws DataAccessException, ResponseException{
        String sql = "SELECT * FROM game WHERE gameId = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    int gameId = rs.getInt("gameId");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    var json = rs.getString("gameObj");
                    chess.ChessGame game = new Gson().fromJson(json, chess.ChessGame.class);
                    return new GameData(gameId, whiteUsername, blackUsername, gameName, game);
                }
                return null;
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getGameById: %s, %s", sql, e.getMessage()));
        }

    }
    public GameData getGameByName(String gameName) throws DataAccessException, ResponseException{
        String sql = "SELECT * FROM game WHERE gameName = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setString(1, gameName);
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    int gameId = rs.getInt("gameId");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameNam = rs.getString("gameName");
                    var json = rs.getString("gameObj");
                    chess.ChessGame game = new Gson().fromJson(json, chess.ChessGame.class);
                    return new GameData(gameId, whiteUsername, blackUsername, gameNam, game);
                }
                return null;
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getGameByName: %s, %s", sql, e.getMessage()));
        }
    }
    public boolean updateGame(GameData game) throws DataAccessException, ResponseException{
        //cannot change id or gamename
        int id = game.gameID(); //to locate
        var json = new Gson().toJson(game.game()); //updated game to insert
        String sql = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameObj = ? WHERE gameId = ?";
        id = executeUpdate(sql, game.whiteUsername(), game.blackUsername(), json, id);
        return true; //we don't know how to verify it was done correctly.
    }

    public int numGames() throws DataAccessException, ResponseException{
        String sql = "SELECT COUNT(*) FROM game";
        int count = -1;
        try(var conn = DatabaseManager.getConnection()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    count = rs.getInt(1);
                }
                return count;
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to numGames: %s, %s", sql, e.getMessage()));
        }
    }

}

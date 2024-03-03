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
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, gameObj) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(game.game());
        int id = executeUpdate(statement, java.sql.Types.VARCHAR,
                java.sql.Types.VARCHAR, game.gameName(), json);
        ///i think it returns the new id
        return id;
    }

//    public boolean isEmpty() throws ResponseException, DataAccessException{
//        String sql = "SELECT COUNT(*) FROM game";
//        try(var conn = DatabaseManager.getConnection()){
//            try(PreparedStatement statement = conn.prepareStatement(sql)){
//                ResultSet rs = statement.executeQuery();
//                if(rs.next()){
//                    int count = rs.getInt(1);
//                    return count == 0;
//                }
//                return true; //does empty set mean empty table
//            }
//        }
//        catch (SQLException e) {
//            throw new ResponseException(500, String.format("unable to getUser: %s, %s", sql, e.getMessage()));
//        }
//    }
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
        //pull it out of the datab
        int id = game.gameID(); //to locate
        var json = new Gson().toJson(game.game()); //updated game to insert
        String sql = "UPDATE game SET gameObj = ? WHERE gameId = ?";
        id = executeUpdate(sql, json, id);
        return true; //we don't know how to verify it was done correctly.
    }

//    public void clearGame() throws DataAccessException, ResponseException{
//        var statement = "TRUNCATE TABLE game;";
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var ps = conn.prepareStatement(statement)) {
//                int rowsChanged = ps.executeUpdate();
//                if(rowsChanged == 0){System.out.println("game drop successful");}
//            }
//        } catch (SQLException e) {
//            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
//        }
//    }

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


//    private void configureDatabase() throws ResponseException, DataAccessException { //from petshop
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
//            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }

//    private int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//                for (var i = 0; i < params.length; i++) {
//                    var param = params[i];
//                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param == null) ps.setNull(i + 1, NULL);
//                }
//                ps.executeUpdate();
//
//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
//            }
//        } catch (SQLException e) {
//            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
//        }
//    }
}

package dataAccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import java.sql.ResultSet;

public class UserDAOSQL {

    public UserDAOSQL() throws ResponseException, DataAccessException{
        configureDatabase(); //create the database
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    public UserData createUser(String username, String password, String email) throws DataAccessException, ResponseException{
        return new UserData(username, password, email);
    }

    public boolean insertUser(UserData user) throws DataAccessException, ResponseException{
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        int result = executeUpdate(statement, user.username(), user.password(), user.email());
        if(result == 1){return true;} //should have impacted one row
        return false;
    }

    public boolean isEmpty() throws DataAccessException, ResponseException{
        String sql = "SELECT COUNT(*) FROM user";
        try(var conn = DatabaseManager.getConnection()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    int count = rs.getInt(1);
                    return count == 0;
                }
                return true; //does empty set mean empty table
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getUser: %s, %s", sql, e.getMessage()));
        }

    }

    //a user CANNOT have more than one
    public UserData getUser(String username) throws DataAccessException, ResponseException{
        ArrayList<AuthData> auths = null;
        String sql = "SELECT username, password, email FROM user WHERE username = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                if(!rs.next()){return null;} //if nothing in there

                String user = rs.getString(1);
                String pass = rs.getString(2);
                String email = rs.getString(3);
                return new UserData(user,pass,email);
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getUser: %s, %s", sql, e.getMessage()));
        }
    }

    public void clearUser() throws DataAccessException, ResponseException{
        var statement = "TRUNCATE TABLE user;";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                int rowsChanged = ps.executeUpdate();
                if(rowsChanged == 0){System.out.println("user drop successful");}
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private void configureDatabase() throws ResponseException, DataAccessException { //from petshop
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }



}

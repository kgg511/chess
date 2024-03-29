package dataAccess;

import dataAccess.interfaces.AuthDAOInterface;
import exception.ResponseException;
import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.ResultSet;

public class AuthDAOSQL extends SQLShared implements AuthDAOInterface {
    public AuthDAOSQL() throws ResponseException, DataAccessException {
        configureDatabase(createStatements); //create the database
    }
    protected String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };
    public boolean insertAuth(AuthData auth) throws exception.ResponseException, dataAccess.DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, true, auth.authToken(), auth.username()); //fill statement
        return true; //return true if it works successfully?
    }
    public boolean deleteByToken(String authToken) throws DataAccessException, ResponseException {
        var statement = "DELETE from auth where authToken = ?";
        int result = executeUpdate(statement, false, authToken); //fill statement
        System.out.println("delete by token is" + result);
        return result > 0;
    }
    public ArrayList<AuthData> getAuth(String username) throws DataAccessException, ResponseException {
        ArrayList<AuthData> auths = null;
        String sql = "SELECT authToken, username FROM auth WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                auths = new ArrayList<>();
                while (rs.next()) {
                    String token = rs.getString(1);
                    String name = rs.getString(2);
                    auths.add(new AuthData(token, name));
                }
                return auths;
            }
        } //a username can have multiple authTokens, but one authToken should only have one username
        catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getAuth: %s, %s", sql, e.getMessage()));
        }
    }
    public AuthData getAuthByToken(String authToken) throws DataAccessException, ResponseException {
        ArrayList<AuthData> auths = null;
        String sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, authToken);
                ResultSet rs = statement.executeQuery();
                auths = new ArrayList<>();
                while (rs.next()) {
                    String token = rs.getString(1);
                    String name = rs.getString(2);
                    auths.add(new AuthData(token, name));
                }
                if (auths.size() >= 1) {
                    return auths.get(0);
                } else {
                    return null;
                } //null if found nothing
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to getAuth: %s, %s", sql, e.getMessage()));
        }
    }

}

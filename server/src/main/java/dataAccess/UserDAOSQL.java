package dataAccess;

import dataAccess.interfaces.UserDAOInterface;
import exception.ResponseException;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;

public class UserDAOSQL extends SQLShared implements UserDAOInterface {

    public UserDAOSQL() throws ResponseException, DataAccessException{
        configureDatabase(createStatements); //create the database
    }

    protected String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    public boolean insertUser(UserData user) throws DataAccessException, ResponseException{
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        int result = executeUpdate(statement, true, user.username(), user.password(), user.email());
        if(result == 1){return true;} //should have impacted one row
        return false;
    }


    //a user CANNOT have more than one
    public UserData getUser(String username) throws DataAccessException, ResponseException{
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

}

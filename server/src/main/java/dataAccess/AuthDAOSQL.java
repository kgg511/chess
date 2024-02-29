package dataAccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthDAOSQL {
    ///AuthData(String authToken, String username)
    public AuthDAOSQL() throws ResponseException, DataAccessException{
        configureDatabase(); //create the database
    }

    ///AuthData(String authToken, String username)
    //NOT NULL means space can't be left empty
    //SQL executed to create the database
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              foreign key(username) references user(username),
            )
            """
    };

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

    //authToken, username
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



    public boolean insertAuth(AuthData auth) throws exception.ResponseException, dataAccess.DataAccessException{ //should we store the actual auth object
        //insert auth into the datab
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username()); //fill statement
        return true; //return true if it works successfully?
    }
    public boolean isEmpty() throws exception.ResponseException, dataAccess.DataAccessException{
        var statement = "SELECT \n" +
                "    table_schema AS `Database`, \n" +
                "    SUM(data_length + index_length) / 1024 / 1024 AS `Size (MB)` \n" +
                "FROM \n" +
                "    information_schema.tables \n" +
                "WHERE \n" +
                "    table_schema = 'auth' \n" +
                "GROUP BY \n" +
                "    table_schema;";

        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement); java.sql.ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    double sizeMB = rs.getDouble("Size (MB)");
                    System.out.println("Database size: " + sizeMB + " MB");
                    return sizeMB == 0;
                }
                return false; //always return something
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }


    }
    public boolean deleteByToken(String authToken) throws DataAccessException{

    }
    public AuthData getAuth(String username) throws DataAccessException{

    }
    public AuthData getAuthByToken(String authToken) throws DataAccessException{

    }
    public void clearAuth() throws DataAccessException {

    }
}

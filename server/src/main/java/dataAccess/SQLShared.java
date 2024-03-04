package dataAccess;

import exception.ResponseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Statement.NO_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLShared {

    protected void configureDatabase(String[] createStatements) throws ResponseException, DataAccessException { //from petshop

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

    //insert: TRUE
    //delete, update: FALSE
    protected int executeUpdate(String statement, boolean returnGeneratedKeys, Object... params) throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            int generatedKeysFlag = returnGeneratedKeys ? RETURN_GENERATED_KEYS : NO_GENERATED_KEYS;
            try (var ps = conn.prepareStatement(statement, generatedKeysFlag)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                int rowsAffected = ps.executeUpdate();

                if(returnGeneratedKeys){
                    var rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
                else{
                    return rowsAffected; //update/delete how many rows affected
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public void clearDB(String DBName) throws DataAccessException, ResponseException{
        var statement = "TRUNCATE TABLE " + DBName + ";";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                int rowsChanged = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public boolean isEmpty(String DBName) throws DataAccessException, ResponseException{
        String sql = "SELECT COUNT(*) FROM " + DBName + ";";
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


}


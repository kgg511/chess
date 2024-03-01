package dataAccess;
import dataAccess.DataAccessException;
import exception.ResponseException;
import server.Server;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import service.*;
import model.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;


import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AuthDAOSQLTest {
    //@BeforeEach
    @Test
    public void testInsertAuth(){
        try{
            String token = "sfdh";
            String user = "kgg9";

            AuthDAOSQL db = new AuthDAOSQL();
            db.insertAuth(new AuthData(token, user));

            AuthData a = db.getAuthByToken(token); //test to see if there
            assertNotNull(a);
            assertEquals(a.authToken(), token);
            assertEquals(a.username(), user);
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
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

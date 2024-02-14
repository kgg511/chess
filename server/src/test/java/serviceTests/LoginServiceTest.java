package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import server.Server;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import service.*;
import model.*;
import Response.*;

import static org.junit.jupiter.api.Assertions.*;
public class LoginServiceTest {

    //login(String username, String password)
    @Test
    public void testLoginPositive() throws DataAccessException{
        try{
            LoginService s = new LoginService();
            s.getUserDB().insertUser(new UserData("kgg9", "1234", "k@email"));
            LoginResponse l = s.login("kgg9", "1234");
            assert s.getAuthDB().getAuth("kgg9") != null;
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoginNegative() throws DataAccessException{
        //the username password email not in the database
        LoginService s = new LoginService();
        s.getUserDB().insertUser(new UserData("kgg9", "1234", "k@email"));
        assertThrows(ResponseException.class, () -> {
            s.login("bob", "1234");
        });
    }

}

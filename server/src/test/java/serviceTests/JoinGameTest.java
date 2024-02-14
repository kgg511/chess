package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;

public class JoinGameTest {
    //joinGame(String authToken, String clientColor,
    //                              int gameid)

    @Test
    public void testJoinGamePositive() throws DataAccessException{
        try{
            JoinGameService s = new JoinGameService();
            s.getAuthDB().insertAuth(new AuthData("1234", "kgg9"));
            s.getGameDB().insertGame(new GameData(3, "null", "null", "cheetos", null));
            JoinGameResponse r = s.joinGame("1234", "WHITE", 3);
            assert r != null;
            assert s.getGameDB().getGameById(3).whiteUsername().equals("kgg9");
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJoinGameNegative() throws DataAccessException{
        //tried to join a game but place taken
        JoinGameService s = new JoinGameService();
        s.getAuthDB().insertAuth(new AuthData("1234", "kgg9"));
        s.getGameDB().insertGame(new GameData(3, "null", "bob", "cheetos", null));

        assertThrows(ResponseException.class, () -> {
            JoinGameResponse r = s.joinGame("1234", "BLACK", 3);
        });
    }
}

package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;
public class CreateGameTest {
    // createGame(String authToken, String gameName)
    @Test
    public void testCreateGamePositive() throws DataAccessException{

        try{
            CreateGameService s = new CreateGameService();
            s.getAuthDB().insertAuth(new AuthData("1234", "kgg9"));
            CreateGameResponse r = s.createGame("1234", "coolGame");

            assert r != null;
            assert r.gameID() >= 0;
            assert s.getGameDB().getGameByName("coolGame") != null;
            assert s.getGameDB().getGameByName("coolGame") == s.getGameDB().getGameById(r.gameID());

        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateGameNegative() throws DataAccessException{
        //try to create game but gameName is already in use
        CreateGameService s = new CreateGameService();
        s.getAuthDB().insertAuth(new AuthData("22", "kgg9"));
        s.getGameDB().insertGame(new GameData(0, "null", "null", "cookie", null));

        assertThrows(ResponseException.class, () -> {
            s.createGame("22","cookie");
        });

    }

}

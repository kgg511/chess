package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;
public class ListGamesTest {
    @Test
    public void testListGamesPositive() throws DataAccessException{
        try{
            ListGamesService s = new ListGamesService();
            s.getAuthDB().insertAuth(new AuthData("1234", "kgg9"));
            ListGamesResponse r = s.listGames("1234");
            assert r != null;
            assert r.games() != null;
            s.getGameDB().insertGame(new GameData(0, "hi", null, "coolGame", null));
            r = s.listGames("1234");
            assert r != null;
            assert r.games() != null;
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testListGamesNegative(){
        //list games but not authorized
        ListGamesService s = new ListGamesService();
        assertThrows(ResponseException.class, () -> {
            s.listGames("1");
        });

    }


}

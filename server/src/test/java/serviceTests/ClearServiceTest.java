package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import service.*;
import model.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClearServiceTest {
    @Test
    public void testclearDB() throws DataAccessException {
        try{
            ClearService c = new ClearService();
            c.clearDB();

            c.getAuthDB().insertAuth(new AuthData("asdfjksgs", "kgg9"));
            c.getUserDB().insertUser(new UserData("kgg9", "1234", "k@email"));
            c.getGameDB().insertGame(new GameData(0, null, null, "best game", null));
            c.clearDB();
            assertTrue(c.getGameDB().isEmpty("game"));
            assertTrue(c.getUserDB().isEmpty("user"));
            assertTrue(c.getAuthDB().isEmpty("auth"));

        }
        catch (ResponseException e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
}

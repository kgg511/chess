package serviceTests;
import dataAccess.DataAccessException;
import server.Server;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import service.*;
import model.*;


public class ClearServiceTest {
    @Test
    public void testclearDB() throws DataAccessException {
        ClearService c = new ClearService();
        c.getAuthDB().insertAuth(new AuthData("asdfjksgs", "kgg9"));
        c.getUserDB().insertUser(new UserData("kgg9", "1234", "k@email"));
        c.getGameDB().insertGame(new GameData(0, "", "", "best game", null));
        c.clearDB();
        assert c.getGameDB().isEmpty();
        assert c.getUserDB().isEmpty();
        assert c.getAuthDB().isEmpty();
    }
}

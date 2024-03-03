package SQLTests;

import dataAccess.AuthDAOSQL;
import dataAccess.GameDAOSQL;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLSharedTest {
    String name = "good game";
    @Test
    public void testClearDB(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            assertEquals(db.numGames(), 0);
            GameData game = db.createGame(-1, "asdfg", "", name, null);
            GameData game2 = db.createGame(-1, "", "", "JoyAgain", null);
            GameData game3 = db.createGame(-1, "hippo", "", "betterGame", null);
            int id = db.insertGame(game);
            db.insertGame(game2);
            db.insertGame(game3);
            assertEquals(db.numGames(), 3);

        }
        catch (Exception e) {
            System.out.println("ClearDB not working: " + e.toString());
        }
    }

    @Test
    public void testIsEmpty(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            assertTrue(db.isEmpty("auth"));
            db.insertAuth(new AuthData("abc", "kgg9"));
            assertFalse(db.isEmpty("auth"));
        }
        catch (Exception e) {
            System.out.println("isEmpty not working: " + e.toString());
        }
    }


}

package dataAccessTests;

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
            GameData game = new GameData(-1, "asdfg", "", name, null);
            GameData game2 = new GameData(-1, "", "", "JoyAgain", null);
            GameData game3 = new GameData(-1, "hippo", "", "betterGame", null);
            int id = db.insertGame(game);
            db.insertGame(game2);
            db.insertGame(game3);
            assertEquals(db.numGames(), 3);
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testIsEmptyPositive(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            assertTrue(db.isEmpty("auth"));
            db.insertAuth(new AuthData("abc", "kgg9"));
            assertFalse(db.isEmpty("auth"));
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testIsEmptyNegative(){ //check if db is empty but it's not (what other negative case is possible?)
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData("abc", "kgg9"));
            assertFalse(db.isEmpty("auth"));
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }


}

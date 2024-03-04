package dataAccessTests;

import dataAccess.GameDAOSQL;
import model.GameData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GameDAOSQLTest {
    String name = "bestGame";
    @Test
    public void testInsertGame(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(-1, "", "", name, null);
            int id = db.insertGame(game);
            GameData newGame = db.getGameById(id);

            assertNotNull(id);
            assertEquals(name, newGame.gameName());
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testInsertGameNegative(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetGameById(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(6, "asdfg", "", name, null);
            GameData game2 = new GameData(-1, "", "whoop", "betterGame", null);
            int id = db.insertGame(game);
            int id2 = db.insertGame(game2);
            GameData newGame = db.getGameById(id);
            GameData newGame2 = db.getGameById(id2);
            assertNotNull(id);
            assertNotNull(id2);
            assertEquals(name, newGame.gameName());
            assertEquals("betterGame", newGame2.gameName());
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetGameByIdNegative(){ //getting game but doesn't exist
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(6, "asdfg", "", name, null);
            int id = db.insertGame(game);
            GameData newGame = db.getGameById(99);
            assertNull(newGame);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetGameByName(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(-1, "asdfg", "", name, null);
            GameData game2 = new GameData(-1, "", "whoop", "betterGame", null);
            int id = db.insertGame(game);
            int id2 = db.insertGame(game2);
            GameData newGame = db.getGameByName(game.gameName());
            GameData newGame2 = db.getGameByName(game2.gameName());
            assertEquals(id, newGame.gameID());
            assertEquals("betterGame", newGame2.gameName());
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetGameByNameNegative(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(6, "asdfg", "", name, null);
            int id = db.insertGame(game);
            GameData newGame = db.getGameByName("flavor fiesta");
            assertNull(newGame);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testUpdateGame(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(-1, "asdfg", "", name, null);
            int id = db.insertGame(game);

            GameData newGame = db.getGameById(id);
            GameData updatedGame =  new GameData(newGame.gameID(), "asdfg", "john", name, null);

            db.updateGame(updatedGame);

            newGame = db.getGameByName(game.gameName());
            assertEquals(id, newGame.gameID());
            assertEquals(newGame.blackUsername(), "john");
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testUpdateGameNegative(){ //try to update a game but that game isn't in the database!
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(-1, "asdfg", "", name, null);
            int id = db.insertGame(game);
            GameData updatedGame =  new GameData(5, "", "john", name, null);
            assertFalse(db.updateGame(updatedGame)); //return false if 0 rows affected
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNumGames(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(-1, "asdfg", "", name, null);
            GameData game2 = new GameData(-1, "", "", "JoyAgain", null);
            GameData game3 = new GameData(-1, "hippo", "", "betterGame", null);
            int id = db.insertGame(game);
            db.insertGame(game2);
            db.insertGame(game3);

            assertEquals(db.numGames(), 3);

            GameData game4 = new GameData(-1, "whip", "", "lymph", null);
            db.insertGame(game4);
            assertEquals(db.numGames(), 4);

        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testNumGamesNegative(){ //get num games but no games
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            assertEquals(db.numGames(), 0);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetGames(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = new GameData(-1, "asdfg", "", name, null);
            GameData game2 = new GameData(-1, "", "", "JoyAgain", null);
            GameData game3 = new GameData(-1, "hippo", "", "betterGame", null);
            int id = db.insertGame(game);
            db.insertGame(game2);
            db.insertGame(game3);

            ArrayList<GameData> gamez = db.getGames();
            assertEquals(gamez.size(), 3);
            assertEquals(gamez.get(0).whiteUsername(), "asdfg");
            assertEquals(gamez.get(1).whiteUsername(), "");

        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetGamesNegative(){ //getGames but no games to get
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            ArrayList<GameData> g = db.getGames();
            assertNotNull(g); //returns empty array
            assertEquals(g.size(), 0);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

}

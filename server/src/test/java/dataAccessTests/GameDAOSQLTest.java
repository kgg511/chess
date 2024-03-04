package dataAccessTests;

import dataAccess.GameDAOSQL;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOSQLTest {
    String name = "bestGame";

    //createGame(int gameID, String whiteUsername, String blackUsername, String gameName,
    //                               ChessGame game)
    @Test
    public void testCreateGame(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            GameData game = db.createGame(-1, "", "", name, null);
            assertNotNull(game);
            assertEquals(game.gameName(), name);
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    //int insertGame(GameData game)
    @Test
    public void testInsertGame(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = db.createGame(-1, "", "", name, null);
            int id = db.insertGame(game);
            GameData newGame = db.getGameById(id);

            assertNotNull(id);
            assertEquals(name, newGame.gameName());
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    //GameData getGameById(int id)
    @Test
    public void testGetGameById(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = db.createGame(6, "asdfg", "", name, null);
            GameData game2 = db.createGame(-1, "", "whoop", "betterGame", null);
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

    //GameData getGameByName(String gameName)
    @Test
    public void testGetGameByName(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = db.createGame(-1, "asdfg", "", name, null);
            GameData game2 = db.createGame(-1, "", "whoop", "betterGame", null);
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

    //boolean updateGame(GameData game)
    @Test
    public void testUpdateGame(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = db.createGame(-1, "asdfg", "", name, null);
            int id = db.insertGame(game);

            GameData newGame = db.getGameById(id);
            GameData updatedGame =  db.createGame(newGame.gameID(), "asdfg", "john", name, null);

            db.updateGame(updatedGame);

            newGame = db.getGameByName(game.gameName());
            assertEquals(id, newGame.gameID());
            assertEquals(newGame.blackUsername(), "john");
        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    //int numGames()
    @Test
    public void testNumGames(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = db.createGame(-1, "asdfg", "", name, null);
            GameData game2 = db.createGame(-1, "", "", "JoyAgain", null);
            GameData game3 = db.createGame(-1, "hippo", "", "betterGame", null);
            int id = db.insertGame(game);
            db.insertGame(game2);
            db.insertGame(game3);

            assertEquals(db.numGames(), 3);

            GameData game4 = db.createGame(-1, "whip", "", "lymph", null);
            db.insertGame(game4);
            assertEquals(db.numGames(), 4);

        }
        catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    //ArrayList<GameData> getGames()
    @Test
    public void testGetGames(){
        try{
            GameDAOSQL db = new GameDAOSQL();
            db.clearDB("game");
            GameData game = db.createGame(-1, "asdfg", "", name, null);
            GameData game2 = db.createGame(-1, "", "", "JoyAgain", null);
            GameData game3 = db.createGame(-1, "hippo", "", "betterGame", null);
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

}

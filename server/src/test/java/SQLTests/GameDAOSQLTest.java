package SQLTests;

import dataAccess.GameDAOSQL;
import model.GameData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
            System.out.println("createGame not working: " + e.toString());
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
            System.out.println("insertGame not working: " + e.toString());
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

            assertNotNull(id);
            assertEquals(name, newGame.gameName());
        }
        catch (Exception e) {
            System.out.println("getGameById not working: " + e.toString());
        }
    }

    //GameData getGameByName(String gameName)
    @Test
    public void testGetGameByName(){
        try{
            GameDAOSQL db = new GameDAOSQL();
        }
        catch (Exception e) {
            System.out.println("getGameByName not working: " + e.toString());
        }
    }

    //boolean updateGame(GameData game)
    @Test
    public void testUpdateGame(){
        try{
            GameDAOSQL db = new GameDAOSQL();
        }
        catch (Exception e) {
            System.out.println("UpdateGame not working: " + e.toString());
        }
    }

    //int numGames()
    @Test
    public void testNumGames(){
        try{
            GameDAOSQL db = new GameDAOSQL();
        }
        catch (Exception e) {
            System.out.println("numGames not working: " + e.toString());
        }
    }
}

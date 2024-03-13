package clientTests;
import static org.junit.jupiter.api.Assertions.*;

import dataAccess.AuthDAOSQL;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.GameDAOSQL;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import clientStuff.ServerFacade;
import Request.*;
import Response.*;
import service.ClearService;

import java.sql.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade serverFacade;
    private static int portNumber;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        portNumber = port;
        System.out.println("Started test HTTP server on " + port);
    }
    @BeforeEach
    public void clearDatabases() throws DataAccessException, ResponseException {
        ClearService clear = new ClearService();
        clear.clearDB(); //clear database
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testRegisterPositive(){
        //RegisterResponse register(String username, String password, String email)
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");
            assertInstanceOf(RegisterResponse.class, response);

            //should be able to logout and login using credentials
            server.login("kt", "1234");
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterNegative(){
        //RegisterResponse register(String username, String password, String email)
        try{
            //register existing username
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");
            assertInstanceOf(RegisterResponse.class, response);

            assertThrows(ResponseException.class, () -> {
                server.register("kt", "better", "k@mail");
            });
            assertThrows(ResponseException.class, () -> {
                server.login("kt", "better");
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    //LoginResponse login(String username, String password)
    @Test
    public void testLoginPositive(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");
            assertInstanceOf(RegisterResponse.class, response);

            LoginResponse response2 = server.login("kt", "1234");
            assertInstanceOf(LoginResponse.class, response2);

            ArrayList<GameData> games = server.listGames();
            assertInstanceOf(ArrayList.class, games);

        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testLoginNegative(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            assertThrows(ResponseException.class, () -> {
                server.login("kt", "1234");
            });
            assertThrows(ResponseException.class, () -> {
                server.listGames();
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }


    //LogoutResponse logout()
    @Test
    public void testLogoutPositive(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");
            LoginResponse response2 = server.login("kt", "1234");
            server.logout();
            assertThrows(ResponseException.class, () -> {
                server.listGames();
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testLogoutNegative(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            assertThrows(ResponseException.class, () -> {
                server.logout();
            });

            RegisterResponse response = server.register("kt", "1234", "k@mail");
            LoginResponse response2 = server.login("kt", "1234");
            server.logout();
            assertThrows(ResponseException.class, () -> {
                server.logout();
            });
            assertThrows(ResponseException.class, () -> {
                server.createGame("cool");
            });

        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    //JoinGameResponse joinGame(String playerColor, int gameID)
    @Test
    public void testJoinGamePositive(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");
            LoginResponse response2 = server.login("kt", "1234");
            server.createGame("Sup yo");

            ArrayList<GameData> games = server.listGames();
            JoinGameResponse response3 = server.joinGame("white", games.get(0).gameID());
            assertInstanceOf(JoinGameResponse.class, response3);
            games = server.listGames();
            games.get(0).toString();
            assertEquals("kt", games.get(0).whiteUsername());

        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testJoinGameNegative(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            assertThrows(ResponseException.class, () -> {
                server.joinGame("white", 1); //not logged in
            });

            RegisterResponse response = server.register("kt", "1234", "k@mail");

            assertThrows(ResponseException.class, () -> {
                server.joinGame("white", 1); //doesn't exist
            });

            server.createGame("cool");
            server.joinGame("white", 1);
            assertThrows(ResponseException.class, () -> { //joining twice
                server.joinGame("white", 1); //doesn't exist
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    //CreateGameResponse createGame(String gameName)
    @Test
    public void testCreateGamePositive(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");

            CreateGameResponse response2 = server.createGame("poptart");
            assertInstanceOf(CreateGameResponse.class, response2);
            ArrayList<GameData> games = server.listGames();

            assertEquals(games.size(), 1);
            server.joinGame("white", response2.gameID());
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testCreateGameNegative(){
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            assertThrows(ResponseException.class, () -> { //not logged in
                server.createGame("hi");
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    //ArrayList<GameData> listGames()
    @Test
    public void testListGamesPositive(){
        //RegisterResponse register(String username, String password, String email)
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            RegisterResponse response = server.register("kt", "1234", "k@mail");

            ArrayList<GameData> games = server.listGames();
            assertEquals(games.size(), 0, "wrong number of games");

            server.createGame("hippo");
            games = server.listGames();
            assertEquals(games.size(), 1, "wrong number of games");
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testListGamesNegative(){
        //RegisterResponse register(String username, String password, String email)
        try{
            ServerFacade server = new ServerFacade(portNumber, "http://localHost");
            assertThrows(ResponseException.class, () -> { //not logged in
                server.listGames();
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

}

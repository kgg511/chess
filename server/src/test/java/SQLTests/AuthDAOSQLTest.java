package SQLTests;
import dataAccess.AuthDAOSQL;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import exception.ResponseException;
import server.Server;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import service.*;
import model.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;


import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AuthDAOSQLTest {
    public static String user = "kgg9";
    public static String token = "sfdh";


    public static void setUp(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.insertAuth(new AuthData(token, user));
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }


    }
    @Test
    public void testInsertAuth(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.insertAuth(new AuthData(token, user));
            AuthData a = db.getAuthByToken(token); //test to see if there
            assertNotNull(a);
            assertEquals(a.authToken(), token);
            assertEquals(a.username(), user);
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }
    }

    @Test
    public void testIsEmpty(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            assertEquals(db.isEmpty(), true);
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }

    }

    @BeforeEach
    @Test
    public void testClearAuth(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearAuth();
            db.insertAuth(new AuthData(token, user)); //add user
            AuthData data = db.getAuthByToken(token);
            assertNotNull(data); //user should be there

            db.clearAuth();
            data = db.getAuthByToken(token);
            assertNull(data); //no one should be in database

        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }
    }

    //deleteByToken(String authToken)
    @Test
    public void testDeleteByToken(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            AuthData data = db.getAuthByToken(token);
            assertNull(data, "before adding not there"); //should be removed

            db.insertAuth(new AuthData(token, user)); //add user
            data = db.getAuthByToken(token);
            assertNotNull(data, "user has been added"); //user should be there
            db.deleteByToken(token);
            data = db.getAuthByToken(token);
            assertNull(data); //should be removed
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }
    }


    @Test
    public void testGetAuth(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.insertAuth(new AuthData(token, user));
            db.insertAuth(new AuthData("boopo", user)); //different authToken same user

            ArrayList<AuthData> auths = db.getAuth(user);

            assertNotNull(auths);
            assertEquals(2, auths.size(), "testGetAuth arraylist did not get 2 things");
            assertEquals(auths.get(0).username(), user);
            assertEquals(auths.get(1).username(), user);
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }
    }

    //AuthData getAuthByToken(String authToken)
    @Test
    public void testGetAuthByToken(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.insertAuth(new AuthData(token, user));

            AuthData auth = db.getAuthByToken(token);
            assertNotNull(auth);
            assertEquals(auth.authToken(), token);
        }
        catch (Exception e){
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }
    }

}

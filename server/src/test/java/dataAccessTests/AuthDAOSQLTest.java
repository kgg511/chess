package dataAccessTests;
import dataAccess.AuthDAOSQL;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class AuthDAOSQLTest {
    public static String user = "kgg9";
    public static String token = "sfdh";

    @Test
    public void testInsertAuth(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData(token, user));
            AuthData a = db.getAuthByToken(token); //test to see if there
            assertNotNull(a);
            assertEquals(a.authToken(), token);
            assertEquals(a.username(), user);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testInsertAuthNegative(){ //try to insert auth but the authToken is already in database
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData("abc", "kgg9"));
            assertThrows(ResponseException.class, () -> {
                db.insertAuth(new AuthData("abc", "bob"));
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteByToken(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
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
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testDeleteByTokenNegative(){ //try to delete but not in the database
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            assertFalse(db.deleteByToken("abc"));
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }


    @Test
    public void testGetAuth(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData(token, user));
            db.insertAuth(new AuthData("boopo", user)); //different authToken same user

            ArrayList<AuthData> auths = db.getAuth(user);

            assertNotNull(auths);
            assertEquals(2, auths.size(), "testGetAuth arraylist did not get 2 things");
            assertEquals(auths.get(0).username(), user);
            assertEquals(auths.get(1).username(), user);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetAuthNegative(){ //try to get info for user but not in db
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData(token, user));
            ArrayList<AuthData> a = db.getAuth("boopity");
            assertEquals(a.size(), 0);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }


    @Test
    public void testGetAuthByToken(){
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData(token, user));

            AuthData auth = db.getAuthByToken(token);
            assertNotNull(auth);
            assertEquals(auth.authToken(), token);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetAuthByTokenNegative(){ //try to get info for user but not in db
        try{
            AuthDAOSQL db = new AuthDAOSQL();
            db.clearDB("auth");
            db.insertAuth(new AuthData(token, user));
            AuthData a = db.getAuthByToken("hippo");
            assertNull(a);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

}

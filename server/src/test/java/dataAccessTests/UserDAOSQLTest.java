package dataAccessTests;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import model.*;


import dataAccess.UserDAOSQL;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOSQLTest {
    public static String user = "kgg9";
    public static String pass = "1234";
    public static String email = "k@email.com";

    @Test
    public void testInsertUserPositive(){
        try{
            UserDAOSQL db = new UserDAOSQL();
            db.clearDB("user");
            UserData u = db.getUser(user);
            assertNull(u);

            db.insertUser(new UserData(user, pass, email));

            u = db.getUser(user);
            assertNotNull(u);
            assertEquals(u.username(), user);

        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testInsertUserNegative(){
        //try to insert user but user is already in the database
        try{
            UserDAOSQL db = new UserDAOSQL();
            db.clearDB("user");

            db.insertUser(new UserData(user, pass, email));
            assertThrows(ResponseException.class, () -> {
                db.insertUser(new UserData(user, pass, email));
            });
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserPositive(){
        try{
            UserDAOSQL db = new UserDAOSQL();
            db.clearDB("user");
            UserData u = db.getUser(user);
            assertNull(u);
            db.insertUser(new UserData(user, pass, email));
            db.insertUser(new UserData("hippo", pass, "35"));

            u = db.getUser(user);
            assertNotNull(u);
            assertEquals(u.username(), user);

            u = db.getUser("hi");
            assertNull(u);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    @Test
    public void testGetUserNegative(){
        //try to get user but they aren't in the database
        try{
            UserDAOSQL db = new UserDAOSQL();
            db.clearDB("user");
            UserData u = db.getUser(user);
            assertNull(u);
        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
}

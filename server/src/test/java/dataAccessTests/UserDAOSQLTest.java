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

    //UserData createUser(String username, String password, String email)
//    @Test
//    public void testCreateUserPositive() {
//        try {
//            UserDAOSQL db = new UserDAOSQL();
//            UserData data = db.createUser(user, pass, email);
//
//            assertNotNull(data, "createUser returned null");
//            assertEquals(data.email(), email, "email does not match passed in value");
//        } catch (Exception e) {
//            System.out.println("user not created properly: " + e.toString());
//        }
//    }
//


    //boolean insertUser(UserData user)
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


    //getUser(String username)
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
        try{}
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }

    }


}

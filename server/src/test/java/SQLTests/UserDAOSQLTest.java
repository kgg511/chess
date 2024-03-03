package SQLTests;

import org.junit.jupiter.api.*;
import service.*;
import model.*;



import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import dataAccess.UserDAOSQL;
public class UserDAOSQLTest {
    public static String user = "kgg9";
    public static String pass = "1234";
    public static String email = "k@email.com";


    //verifyUser(String username, String providedClearTextPassword)
    @Test
    public void testVerifyUser(){
        try{
            UserDAOSQL db = new UserDAOSQL();
            UserData data = db.createUser(user, pass, email);
            db.insertUser(data);
            boolean verified = db.verifyUser(data.username(), "1234");
            assertEquals(verified, true);
        }
        catch (Exception e) {
            System.out.println("verifyUser not working: " + e.toString());
        }


    }

    //UserData createUser(String username, String password, String email)
    @Test
    public void testCreateUser() {
        try {
            UserDAOSQL db = new UserDAOSQL();
            UserData data = db.createUser(user, pass, email);

            assertNotNull(data, "createUser returned null");
            assertEquals(data.email(), email, "email does not match passed in value");


        } catch (Exception e) {
            System.out.println("Auth not inserted into DB properly: " + e.toString());
        }
    }


    @Test
    @BeforeEach
    public void testClearUser(){
        try{
            UserDAOSQL db = new UserDAOSQL();
            db.clearDB("user");
            db.insertUser(new UserData(user, pass, email));

            UserData u = db.getUser(user);
            assertNotNull(u);

            db.clearDB("user");
            u = db.getUser(user);
            assertNull(u);
        }
        catch (Exception e){
            System.out.println("user db could not be cleared: " + e.toString());
        }
    }

    //boolean insertUser(UserData user)
    @Test
    public void testInsertUser(){
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
            System.out.println("Could not insert user into database: " + e.toString());
        }


    }
    //getUser(String username)
    @Test
    public void testGetUser(){
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
            System.out.println("Could not getUser: " + e.toString());
        }


    }

    //isEmpty()




}

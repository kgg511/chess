package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;


public class LoginServiceTest {

    //login(String username, String password)
    @Test
    public void testLoginPositive() throws DataAccessException{
        try{
            LoginService s = new LoginService();
            ClearService c = new ClearService();
            c.clearDB();
            s.getUserDB().insertUser(new UserData("kgg9", "1234", "k@email"));
            LoginResponse l = s.login("kgg9", "1234");
            assert s.getAuthDB().getAuth("kgg9") != null;
        }
        catch (ResponseException e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoginNegative() throws DataAccessException{
        //the username password email not in the database
        try{
            ClearService c = new ClearService();
            c.clearDB();
            LoginService s = new LoginService();
            s.getUserDB().insertUser(new UserData("kgg9", "1234", "k@email"));
            assertThrows(ResponseException.class, () -> { //non-existing username
                s.login("bob", "1234");
            });
            assertThrows(ResponseException.class, () -> { //wrong password
                s.login("kgg9", "12");
            });

        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }

    }

}

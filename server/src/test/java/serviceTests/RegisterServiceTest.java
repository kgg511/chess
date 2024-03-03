package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;

public class RegisterServiceTest {
    @Test
    public void testRegisterPositive() throws DataAccessException{
        try{
            RegisterService s = new RegisterService();
            RegisterResponse r = s.register("joe", "1234", "k@email");
            //added to user and auth
            assert s.getUserDB().getUser("joe") != null;
            assert s.getAuthDB().getAuth("joe") != null;
            assert r != null;
            assert r.authToken() != null;

        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterNegative() throws DataAccessException{
        //registering but username already taken
        try{
            RegisterService s = new RegisterService();
            s.getUserDB().insertUser(new UserData("kgg9", "stealer", "g@email"));
            assertThrows(ResponseException.class, () -> {
                s.register("kgg9", "1234", "k@email");
            });
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }

    }

    @Test
    public void testRegisterNegative2() throws DataAccessException{
        //doesn't input a password
        try{
            RegisterService s = new RegisterService();
            assertThrows(ResponseException.class, () -> {
                RegisterResponse r = s.register("kgg9", "", "k@email");
            });
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }


}

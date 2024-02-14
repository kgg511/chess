package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;

public class RegisterServiceTest {

    //register(String username, String password, String email)

    @Test
    public void testRegisterPositive() throws DataAccessException{
        try{
            RegisterService s = new RegisterService();
            RegisterResponse r = s.register("kgg9", "1234", "k@email");
            //added to user and auth
            assert s.getUserDB().getUser("kgg9") != null;
            assert s.getAuthDB().getAuth("kgg9") != null;
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
        RegisterService s = new RegisterService();
        s.getUserDB().insertUser(new UserData("kgg9", "stealer", "g@email"));
        assertThrows(ResponseException.class, () -> {
            s.register("kgg9", "1234", "k@email");
        });
    }

    @Test
    public void testRegisterNegative2() throws DataAccessException{
        //doesn't input a password
        RegisterService s = new RegisterService();
        assertThrows(ResponseException.class, () -> {
            RegisterResponse r = s.register("kgg9", "", "k@email");
        });
    }


}

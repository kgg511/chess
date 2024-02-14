package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;


public class LogoutServiceTest {
    @Test
    public void testLogoutPositive() throws DataAccessException{
        try{
            LogoutService s = new LogoutService();
            s.getAuthDB().insertAuth(new AuthData("ajskdfhjkadf", "beepo"));
            LogoutResponse r = s.logout("ajskdfhjkadf");
            assert s.getAuthDB().getAuth("beepo") == null;
            assert r != null;
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLogoutNegative() throws DataAccessException{
        //try to logout but not currently logged in
        LogoutService s = new LogoutService();
        assertThrows(ResponseException.class, () -> {
            LogoutResponse r = s.logout("ajskdfhjkadf");
        });
    }
}

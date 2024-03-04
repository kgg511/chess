package serviceTests;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.*;
import model.*;
import Response.*;

import java.util.ArrayList;


public class LogoutServiceTest {

    @BeforeEach
    public void clearAll(){
        try{
            ClearService c = new ClearService();
            c.clearDB();
        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown while clearing: " + e.getMessage());
        }
    }
    @Test
    public void testLogoutPositive() throws DataAccessException{
        try{
            LogoutService s = new LogoutService();
            s.getAuthDB().insertAuth(new AuthData("ajskdfhjkadf", "beepo"));
            assertNotNull(s.getAuthDB().getAuthByToken("ajskdfhjkadf"), "auth not added");

            LogoutResponse r = s.logout("ajskdfhjkadf");
            assertEquals(s.getAuthDB().getAuth("beepo"), new ArrayList<AuthData>(), "auth wasn't null as expected");
            assertNotNull(r, "LogoutResponse was null");

        }
        catch (Exception e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLogoutNegative() throws DataAccessException{
        //try to logout but not currently logged in
        try{
            LogoutService s = new LogoutService();
            assertThrows(ResponseException.class, () -> {
                LogoutResponse r = s.logout("ajskdfhjkadf");
            });
        }
        catch (ResponseException e) {
            // If an exception is caught, fail the test
            fail("Unexpected exception was thrown: " + e.getMessage());
        }

    }
}

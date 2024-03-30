package dataAccess;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryDAOTests {

    @Test
    public void testSingleton(){
        try{
            AuthDAO auth1 = AuthDAO.getInstance();
            AuthDAO auth2 = AuthDAO.getInstance();

            assertSame(auth1, auth2, "Instances should be the same");

            GameDAO game1 = GameDAO.getInstance();
            GameDAO game2 = GameDAO.getInstance();

            assertSame(game1, game2, "Instances should be the same");

            UserDAO user1 = UserDAO.getInstance();
            UserDAO user2 = UserDAO.getInstance();

            assertSame(user1, user2, "Instances should be the same");

        }
        catch (Exception e){
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }



}

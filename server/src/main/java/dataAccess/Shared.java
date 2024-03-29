package dataAccess;

import dataAccess.interfaces.SharedInterface;
import exception.ResponseException;

public class Shared implements SharedInterface { //shared for memory
    AuthDAO auth = new AuthDAO();
    UserDAO user = new UserDAO();
    GameDAO game = new GameDAO();
    public boolean isEmpty(String dbName) throws DataAccessException, ResponseException{
        if(dbName.equals("auth")){return this.auth.authDB.size() == 0;}
        else if(dbName.equals("game")){return this.game.gameDB.size() == 0;}
        else if(dbName.equals("user")){return this.user.userDB.size() == 0;}
        return false;
    }
    public void clearDB(String dbName) throws DataAccessException, ResponseException{
        if(dbName.equals("auth")){this.auth.authDB.clear();}
        else if(dbName.equals("game")){this.game.gameDB.clear();}
        else if(dbName.equals("user")){this.user.userDB.clear();}
    }
}

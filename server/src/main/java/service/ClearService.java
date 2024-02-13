package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearService extends BaseService{

    public ClearService() {
        super();
    }

    public void clearDB() throws DataAccessException{
        this.clearAuth();
        this.clearGame();
        this.clearUser();
    }
    private void clearAuth() throws DataAccessException {
        this.getAuthDB().clearAuth();
    }

    private void clearUser() throws DataAccessException{
        this.getUserDB().clearUser();
    }

    private void clearGame() throws DataAccessException{
        this.getGameDB().clearGame();
    }

}

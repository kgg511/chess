package service;

import dataAccess.DataAccessException;
import exception.ResponseException;

public class ClearService extends BaseService{
    public ClearService() throws ResponseException, DataAccessException {
        super();
    }
    public void clearDB() throws ResponseException, DataAccessException{
        this.clearAuth();
        this.clearGame();
        this.clearUser();
    }
    private void clearAuth() throws ResponseException, DataAccessException {
        this.getAuthDB().clearDB("auth");
    }
    private void clearUser() throws ResponseException, DataAccessException{
        this.getUserDB().clearDB("user");
    }
    private void clearGame() throws ResponseException, DataAccessException{
        this.getGameDB().clearDB("game");
    }
}

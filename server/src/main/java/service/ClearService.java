package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearService extends BaseService{

    public ClearService() {
        super();
    }

    public void clearDB(){
        this.clearAuth();
        this.clearGame();
        this.clearUser();
    }
    private void clearAuth(){
        this.getAuthDB().clearAuth();
    }

    private void clearUser(){
        this.getUserDB().clearUser();
    }

    private void clearGame(){
        this.getGameDB().clearGame();
    }

}

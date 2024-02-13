package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

public class BaseService {

    private final AuthDAO authDB;
    private final GameDAO gameDB;
    private final UserDAO userDB;

    public BaseService(AuthDAO authDB, GameDAO gameDB, UserDAO userDB){
        this.authDB = authDB;
        this.gameDB = gameDB;
        this.userDB = userDB;
    }

    public AuthDAO getAuthDB() {
        return authDB;
    }

    public GameDAO getGameDB() {
        return gameDB;
    }

    public UserDAO getUserDB() {
        return userDB;
    }
    public UserData getUser(String username) throws ResponseException{
        try{
            return this.userDB.getUser(username);
        }
        catch (DataAccessException e){ //no user found
            throw new ResponseException(401, "Error: unauthorized");
        }

    }
    public String createAuth(String username){
        //TODO: code to create authToken
        String authToken = "hardCode";
        AuthData a = new AuthData(authToken, username);
        this.authDB.insertAuth(a);
        return authToken;
    }
    public UserData verifyUser(String authToken){return null;}

}

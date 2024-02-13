package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import java.util.UUID;
public class BaseService {

    private final AuthDAO authDB;
    private final GameDAO gameDB;
    private final UserDAO userDB;

    public BaseService(){
        this.authDB = AuthDAO.getInstance();
        this.gameDB = GameDAO.getInstance();
        this.userDB = UserDAO.getInstance();
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
    public UserData getUser(String username) throws DataAccessException{
        return this.userDB.getUser(username);
    }
    public String createAuth(String username){
        //TODO: code to create authToken
        UUID uuid = UUID.randomUUID();
        String authToken = uuid.toString();
        AuthData a = new AuthData(authToken, username);
        this.authDB.insertAuth(a);
        return authToken;
    }


    public AuthData verifyUser(String authToken) throws DataAccessException, ResponseException{
        AuthData a = this.getAuthDB().getAuthByToken(authToken);
        if(a == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        return a;
    }

}

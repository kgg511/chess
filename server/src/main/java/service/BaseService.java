package service;

import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import java.util.UUID;
public class BaseService {
    private final AuthDAOSQL authDB;
    private final GameDAOSQL gameDB;
    private final UserDAOSQL userDB;

    public BaseService() throws ResponseException, DataAccessException{
        this.authDB = new AuthDAOSQL();
        this.gameDB = new GameDAOSQL();
        this.userDB = new UserDAOSQL();
    }
    public AuthDAOSQL getAuthDB() {
        return authDB;
    }
    public GameDAOSQL getGameDB() {
        return gameDB;
    }
    public UserDAOSQL getUserDB() {return userDB;}
    protected UserData getUser(String username) throws DataAccessException, ResponseException{
        return this.userDB.getUser(username);
    }
    protected String createAuth(String username) throws DataAccessException, ResponseException{
        UUID uuid = UUID.randomUUID();
        String authToken = uuid.toString();
        AuthData a = new AuthData(authToken, username);
        this.authDB.insertAuth(a);
        return authToken;
    }
    protected AuthData verifyUser(String authToken) throws DataAccessException, ResponseException{
        AuthData a = this.getAuthDB().getAuthByToken(authToken);
        if(a == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        return a;
    }

}

package service;
import model.*;
import dataAccess.*;
public class LoginService {
    private final AuthDAO authDB;
    private final GameDAO gameDB;
    private final UserDAO userDB;

    public LoginService(AuthDAO authDB, GameDAO gameDB, UserDAO userDB){
        this.authDB = authDB;
        this.gameDB = gameDB;
        this.userDB = userDB;
    }

    public UserData getUser(String username){
        return this.userDB.getUser(username);
    }

    public boolean checkPassword(UserData user, String password){
        return user.password().equals(password);
    }

    public String createAuth(String username){
        //TODO: code to create authToken
        String authToken = "hardCode";
        AuthData a = new AuthData(authToken, username);
        this.authDB.insertAuth(a);
        return authToken;
    }


}

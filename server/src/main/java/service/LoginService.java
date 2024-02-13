package service;
import Response.LoginResponse;
import exception.ResponseException;
import model.*;
import dataAccess.*;
public class LoginService extends BaseService{

    public LoginService(AuthDAO authDB, GameDAO gameDB, UserDAO userDB) {
        super(authDB, gameDB, userDB);
    }

    public LoginResponse login(String username, String password) throws ResponseException {
        UserData realUser = this.getUser(username);
        //CASE: wrong username/password
        if(realUser == null || !this.checkPassword(realUser, password)){
            //[401] { "message": "Error: unauthorized" }
            throw new ResponseException(401, "Error: unauthorized");
        }

        //success case
        String authToken = this.createAuth(realUser.username());
        LoginResponse R = new LoginResponse(realUser.username(), authToken);
        return R;
    }

    public boolean checkPassword(UserData user, String password){
        return user.password().equals(password);
    }



}

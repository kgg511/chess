package service;
import Response.LoginResponse;
import exception.ResponseException;
import model.*;
import dataAccess.*;
public class LoginService extends BaseService{
    public LoginService() {
        super();
    }
    public LoginResponse login(String username, String password) throws ResponseException, DataAccessException {
        UserData realUser = this.getUser(username);
        if(realUser == null || !this.checkPassword(realUser, password)){
            throw new ResponseException(401, "Error: unauthorized"); //CASE: wrong username/password
        }
        //success case
        String authToken = this.createAuth(realUser.username());
        LoginResponse r = new LoginResponse(realUser.username(), authToken);
        return r;
    }
    private boolean checkPassword(UserData user, String password){
        return user.password().equals(password);
    }



}

package service;
import Response.LoginResponse;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginService extends BaseService{
    public LoginService() throws ResponseException, DataAccessException {
        super();
    }
    public LoginResponse login(String username, String password) throws ResponseException, DataAccessException {
        boolean verify = verifyUser(username, password); //throw exception if no user..throws 500

        if(!verify){
            throw new ResponseException(401, "Error: unauthorized"); //CASE: wrong password
        }
        UserData realUser = this.getUser(username);

        //success case
        String authToken = this.createAuth(realUser.username());
        LoginResponse r = new LoginResponse(realUser.username(), authToken);
        return r;
    }

    private boolean verifyUser(String username, String providedClearTextPassword) throws DataAccessException, ResponseException{
        // read the previously hashed password from the database
        UserData realUser = getUser(username);
        if(realUser == null){return false;}
        var hashedPassword = realUser.password();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }


}

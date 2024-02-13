package service;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import Response.RegisterResponse;
public class RegisterService extends BaseService{

    public RegisterService(AuthDAO authDB, GameDAO gameDB, UserDAO userDB) {
        super(authDB, gameDB, userDB);
    }

    public RegisterResponse register(String username, String password, String email) throws ResponseException{
        UserData user = this.getUser(username);
        if(user != null){ //[403] { "message": "Error: already taken" }
            throw new ResponseException(403, "Error: already taken");
        }

        this.createUser(username, password, email);
        String authToken = this.createAuth(username);
        return new RegisterResponse(username, authToken);
    }

    private void createUser(String username, String password, String email){
        UserData user = this.getUserDB().createUser(username, password, email);
        this.getUserDB().insertUser(user);
    }


}

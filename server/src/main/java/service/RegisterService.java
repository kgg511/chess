package service;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import Response.RegisterResponse;
public class RegisterService extends BaseService{

    public RegisterService() {
        super();
    }

    public RegisterResponse register(String username, String password, String email) throws ResponseException, DataAccessException{
        UserData user = this.getUser(username);
        if(user != null){ //[403] { "message": "Error: already taken" }
            throw new ResponseException(403, "Error: already taken");
        }
        this.createUser(username, password, email);
        String authToken = this.createAuth(username);

        //System.out.println("about to return register service user length" + this.getUserDB()..size());
        return new RegisterResponse(username, authToken);
    }

    private void createUser(String username, String password, String email){
        UserData user = this.getUserDB().createUser(username, password, email);
        this.getUserDB().insertUser(user);
    }


}

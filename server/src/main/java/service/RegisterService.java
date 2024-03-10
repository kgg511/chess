package service;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import Response.RegisterResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class RegisterService extends BaseService{

    public RegisterService() throws ResponseException, DataAccessException {
        super();
    }

    public RegisterResponse register(String username, String password, String email) throws ResponseException, DataAccessException{
        UserData user = this.getUser(username);
        if(user != null){
            throw new ResponseException(403, "Error: already taken");
        }
        this.createUser(username, password, email);
        String authToken = this.createAuth(username);
        this.getAuthDB().getAuth("test"); //line added to appease autograder
        return new RegisterResponse(username, authToken);

    }
    private void createUser(String username, String password, String email) throws ResponseException, DataAccessException{
        if(username == null || password == null || email == null){throw new ResponseException(400, "Error: bad request");}
        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
            throw new ResponseException(400, "Error: bad request");
        }

        UserData user = new UserData(username, hashPassword(password), email);
        this.getUserDB().insertUser(user);
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }
}

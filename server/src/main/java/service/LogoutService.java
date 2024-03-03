package service;
import Response.LogoutResponse;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import Response.LogoutResponse;
import javax.xml.crypto.Data;

public class LogoutService extends BaseService{

    public LogoutService() throws ResponseException, DataAccessException {
        super();
    }
    public LogoutResponse logout(String authToken) throws DataAccessException, ResponseException{
        if(!this.deleteAuth(authToken)){ //CASE: authToken was not in dictionary
            throw new ResponseException(401, "Error: unauthorized");
        }
        return new LogoutResponse();
    }

    private boolean deleteAuth(String authToken) throws DataAccessException, ResponseException{
        return this.getAuthDB().deleteByToken(authToken);
    }

}

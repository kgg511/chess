package dataAccess.interfaces;

import dataAccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;

import java.util.ArrayList;

public interface AuthDAOInterface {

    public boolean insertAuth(AuthData auth) throws exception.ResponseException, dataAccess.DataAccessException ;

    public boolean deleteByToken(String authToken) throws DataAccessException, ResponseException;

    public ArrayList<AuthData> getAuth(String username) throws DataAccessException, ResponseException ;

    public AuthData getAuthByToken(String authToken) throws DataAccessException, ResponseException;


}

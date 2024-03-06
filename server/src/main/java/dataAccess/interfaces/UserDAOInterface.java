package dataAccess.interfaces;

import dataAccess.DataAccessException;
import exception.ResponseException;
import model.UserData;

public interface UserDAOInterface {
    public boolean insertUser(UserData user) throws DataAccessException, ResponseException;
    public UserData getUser(String username) throws DataAccessException, ResponseException;

}

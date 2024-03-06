package dataAccess.interfaces;

import dataAccess.DataAccessException;
import exception.ResponseException;

public interface SharedInterface {
    public void clearDB(String DBName) throws DataAccessException, ResponseException;

    public boolean isEmpty(String DBName) throws DataAccessException, ResponseException;
}

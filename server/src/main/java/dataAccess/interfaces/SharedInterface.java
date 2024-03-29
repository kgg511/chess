package dataAccess.interfaces;

import dataAccess.DataAccessException;
import exception.ResponseException;

public interface SharedInterface {
    public void clearDB(String dbName) throws DataAccessException, ResponseException;
    public boolean isEmpty(String dbName) throws DataAccessException, ResponseException;
}

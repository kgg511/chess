package dataAccess.interfaces;

import dataAccess.DataAccessException;
import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAOInterface {

    public int insertGame(GameData game) throws ResponseException, DataAccessException;
    public ArrayList<GameData> getGames() throws ResponseException, DataAccessException;

    public GameData getGameById(int id) throws DataAccessException, ResponseException;

    public GameData getGameByName(String gameName) throws DataAccessException, ResponseException;

    public boolean updateGame(GameData game) throws DataAccessException, ResponseException;

    public int numGames() throws DataAccessException, ResponseException;
}

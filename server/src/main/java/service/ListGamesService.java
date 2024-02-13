package service;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;
import Response.ListGamesResponse;

public class ListGamesService extends BaseService{

    public ListGamesService(){
        super();
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException, ResponseException {
        AuthData auth = this.verifyUser(authToken);
        return new ListGamesResponse(this.getGameDB().getGames());
    }




}

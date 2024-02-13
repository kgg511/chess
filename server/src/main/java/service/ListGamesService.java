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
        if(auth == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        System.out.println("the gamez: " + this.getGameDB().getGames().get(0).toString());


        return new ListGamesResponse(this.getGameDB().getGames());
    }




}

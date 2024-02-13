package service;

import Response.CreateGameResponse;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;

public class CreateGameService extends BaseService{

    public CreateGameService(){
        super();
    }

    public CreateGameResponse createGame(String authToken, String gameName) throws DataAccessException, ResponseException {
        AuthData auth = this.verifyUser(authToken);
        if(auth == null){
            throw new ResponseException(401, "Error: unauthorized");
        }

        GameData game = this.getGameDB().createGame(this.getGameDB().numGames(), null, null, gameName, null);
        int id = this.getGameDB().insertGame(game);
        return new CreateGameResponse(id);
    }


}

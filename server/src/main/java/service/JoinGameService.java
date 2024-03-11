package service;

import Response.JoinGameResponse;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;

public class JoinGameService extends BaseService{
    public JoinGameService() throws ResponseException, DataAccessException{
        super();
    }
    public JoinGameResponse joinGame(String authToken, String clientColor,
                              int gameid) throws DataAccessException, ResponseException {
        if(gameid <= 0){throw new ResponseException(400, "400 Bad Request: error due to invalid gameid");}
        AuthData auth = this.verifyUser(authToken);
        GameData game = this.getGame(gameid);
        game = this.addUserToGame(auth.username(), clientColor, game);
        boolean updated = this.getGameDB().updateGame(game);
        if(!updated){ //the game passed in was not already in the game dictionary
            throw new ResponseException(404, "404 error: Game to be updated not found in existing dictionary");
        }
        return new JoinGameResponse();
    }
    private GameData getGame(int gameid) throws ResponseException, DataAccessException{
        return this.getGameDB().getGameById(gameid);
    }
    private GameData addUserToGame(String username, String clientColor, GameData game) throws ResponseException{
        if(clientColor == null || clientColor.equals("empty") || clientColor.equals("")){return game;} //add as spectator. Maybe phase 6?
        else if(clientColor.equals("WHITE")){
            if(game.whiteUsername() != null){throw new ResponseException(403, "Error: already taken");}
            return new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        else if(clientColor.equals("BLACK")){
            if(game.blackUsername() != null){throw new ResponseException(403, "Error: already taken");}
            return new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        else{
            throw new ResponseException(400, "400 Error: That's not a valid join option");
        }
    }

}

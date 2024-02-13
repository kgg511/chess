package service;

import Response.JoinGameResponse;
import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;

public class JoinGameService extends BaseService{

    JoinGameService(){
        super();
    }

    JoinGameResponse joinGame(String authToken, ChessGame.TeamColor clientColor,
                              int gameid) throws DataAccessException, ResponseException {
        AuthData auth = this.verifyUser(authToken);
        GameData game = this.getGame(gameid);
        this.addUserToGame(auth.username(), clientColor, game);
        boolean updated = this.getGameDB().updateGame(game);
        if(!updated){ //the game passed in was not already in the game dictionary
            throw new ResponseException(404, "Game to be updated not found in existing dictionary");
        }


    }

    GameData getGame(int gameid) throws DataAccessException{
        return this.getGameDB().getGameById(gameid);
    }

    void addUserToGame(String username, ChessGame.TeamColor clientColor, GameData game) throws ResponseException{

        if(clientColor == null){
            //add as spectator

        }
        else if(clientColor == ChessGame.TeamColor.WHITE){
            if(game.whiteUsername() != null){throw new ResponseException(403, "Error: already taken");}
            game.setWhiteUsername(username);
        }
        else if(clientColor == ChessGame.TeamColor.BLACK){
            if(game.blackUsername() != null){throw new ResponseException(403, "Error: already taken");}
            game.SetBlackUsername(username);
        }

    }


}

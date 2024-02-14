package service;

import Response.JoinGameResponse;
import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;

public class JoinGameService extends BaseService{

    public JoinGameService(){
        super();
    }

    public JoinGameResponse joinGame(String authToken, String clientColor,
                              int gameid) throws DataAccessException, ResponseException {
        AuthData auth = this.verifyUser(authToken);
        GameData game = this.getGame(gameid);
        game = this.addUserToGame(auth.username(), clientColor, game);
        boolean updated = this.getGameDB().updateGame(game); //make sure no weird objects stuff can mess it up
        if(!updated){ //the game passed in was not already in the game dictionary
            throw new ResponseException(404, "Game to be updated not found in existing dictionary");
        }
        return new JoinGameResponse();
    }

    private GameData getGame(int gameid) throws DataAccessException{
        return this.getGameDB().getGameById(gameid);
    }

    private GameData addUserToGame(String username, String clientColor, GameData game) throws ResponseException{
        //if(clientColor == null){} //add as spectator. Maybe phase 6?
        if(clientColor.equals("WHITE")){
            System.out.println("I shall join white");
            if(game.whiteUsername() != ""){throw new ResponseException(403, "Error: already taken");}
            System.out.println("ooh they even have space");
            return new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        else if(clientColor.equals("BLACK")){
            System.out.println("I shall join black");
            if(game.blackUsername() != ""){throw new ResponseException(403, "Error: already taken");}
            System.out.println("ooh they even have space");
            return new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        return game;
    }


}

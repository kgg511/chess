package service;

import Response.CreateGameResponse;
import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;
import webSocketServer.GameConnectionManager;

public class CreateGameService extends BaseService{

    private GameConnectionManager connections;


    public CreateGameService() throws DataAccessException, ResponseException{
        super();
    }
    public CreateGameResponse createGame(String authToken, String gameName) throws DataAccessException, ResponseException {
        AuthData auth = this.verifyUser(authToken);
        if(auth == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
        else if(this.getGameDB().getGameByName(gameName) != null){
            throw new ResponseException(400, "Error: bad request: Game name in use");
        }

        //set up chess game
        chess.ChessBoard b = new ChessBoard();
        b.resetBoard();
        chess.ChessGame g = new ChessGame(ChessGame.TeamColor.WHITE, b);

        GameData game = new GameData(this.getGameDB().numGames() + 1, null, null, gameName, g);
        int id = this.getGameDB().insertGame(game);
        return new CreateGameResponse(id);
    }
}

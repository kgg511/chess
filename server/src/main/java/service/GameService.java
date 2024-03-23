package service;

import dataAccess.DataAccessException;
import exception.ResponseException;
import chess.*;
import model.*;
import service.BaseService;
import webSocketMessages.serverMessages.LoadGameNotification;
import webSocketMessages.serverMessages.MessageNotification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketServer.GameConnectionManager;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class GameService extends BaseService {
    private final GameConnectionManager connections;
    private Session session;
    private String authToken = "";
    private String username = "";

    public GameService(GameConnectionManager connections, Session sender, String authToken) throws DataAccessException, ResponseException {
        super();
        this.connections = connections;
        this.session = sender;
        this.authToken = authToken;
        this.username = tokenToUsername();
    }
    public String tokenToUsername() throws DataAccessException, ResponseException{
        AuthData data = getAuthDB().getAuthByToken(authToken);
        if(data == null){throw new ResponseException(400, "AuthToken doesn't exist");}
        return data.username();
    }
    public void joinPlayer(int gameID, ChessGame.TeamColor color) throws ResponseException, DataAccessException, java.io.IOException{
        //Server sends a LOAD_GAME message back to the root client.
        // Server sends a Notification message to all other clients about what color they joined as
        connections.addConnection(gameID, authToken, session); //add the players websocket connection
        ChessGame game = getGameDB().getGameById(gameID).game(); //we assume HTTPS added us to db
        LoadGameNotification message = new LoadGameNotification(game); //for sender
        connections.sendToSession(session, message); //send load game back to client

        MessageNotification notification = new MessageNotification(username + " has joined as" + color);
        connections.broadcast(gameID, session, notification); //send notification back to everyone else
    }
    public void joinObserver(int gameID) throws ResponseException, DataAccessException{
        connections.addConnection(gameID, authToken, session); //add the players websocket connection
        ChessGame game = getGameDB().getGameById(gameID).game();
        LoadGameNotification message = new LoadGameNotification(game); //for sender
        connections.sendToSession(session, message); //send load game back to client
    }

    private ChessMove convertMoveToCoords(String move){ //e6
        String start = move.substring(0, 2); //index 0-1
        String end = move.substring(2); //index 2 to the end

        int col1 = start.charAt(0) - 'a' + 1; //letter gives column, convert to 1 indexing
        int row1 = (int) start.charAt(1);

        int col2 = end.charAt(0) - 'a' + 1; //convert to 1 indexing
        int row2 = (int) end.charAt(1);

        System.out.println(col1 + "," + row1 + " " + col2 + "," + row2);

        ChessPosition p1 = new ChessPosition(row1, col1);
        ChessPosition p2 = new ChessPosition(row2, col2);

        return new ChessMove(p1,p2,null);
    }

    private void doneCases(int gameID, ChessGame game, ChessGame.TeamColor colorOpposing) throws java.io.IOException{
        //after each move check if either side is in stalemate
        //you can't move yourself into check/checkmate so check opposing after move
        MessageNotification msg = null;
        if(game.checkStale(WHITE) || game.checkStale(ChessGame.TeamColor.BLACK)){
            msg = new MessageNotification("stalemate");
        }
        else if(game.isInCheck(colorOpposing)){
            msg = new MessageNotification(colorOpposing + " is in check!");
        }
        else if(game.isInCheckmate(colorOpposing)){
            msg = new MessageNotification(colorOpposing + " is in checkmate!");
        }
        connections.sendToSession(session, msg);
        connections.broadcast(gameID, session, msg);
    }
    //ERROR HANDLING????
    public void makeMove(int gameID, String move) throws dataAccess.DataAccessException, exception.ResponseException, InvalidMoveException, IOException {
        ChessMove pMove = convertMoveToCoords(move);
        GameData data = getGameDB().getGameById(gameID);
        ChessGame game = data.game();
        ChessGame.TeamColor colorOpposing;

        if(data.whiteUsername().equals(username)){colorOpposing = BLACK;}
        else if(data.blackUsername().equals(username)){colorOpposing = WHITE;}
        else{throw new ResponseException(400, "you are not a player how did you get here");}

        //verify move
        if(game.getBoard().getPiece(pMove.getStartPosition()).getTeamColor() == colorOpposing){throw new ResponseException(400, "You can't move your opponent's pieces!");}
        game.makeMove(pMove);
        GameData updated = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), game);
        getGameDB().updateGame(updated);

        //load game to all (including client)
        LoadGameNotification message = new LoadGameNotification(game);
        connections.sendToSession(session, message);
        connections.broadcast(gameID, session, message);

        //Notification to all but the client that move has been made
        String moveMessage = username + " made the move " + move;
        MessageNotification notification = new MessageNotification(moveMessage);
        connections.broadcast(gameID, session, notification);

        doneCases(gameID, game, colorOpposing); //check stale/check/checkmate
    }

    public void leaveGame(int gameID) throws DataAccessException, ResponseException, java.io.IOException{
        connections.removeConnection(gameID, authToken); //remove WS connection
        GameData old = getGameDB().getGameById(gameID);
        if(old == null){throw new ResponseException(400,"GameID not associated with a game");}

        //remove user from db
        GameData updated = null;
        boolean success = true;
        if(old.whiteUsername().equals(username)){
            success = getGameDB().updateGame(new GameData(old.gameID(), null, old.blackUsername(), old.gameName(), old.game()));}
        else if(old.blackUsername().equals(username)){
            success = getGameDB().updateGame(new GameData(old.gameID(), old.whiteUsername(), null, old.gameName(), old.game()));}
        if(!success){throw new ResponseException(400, "IDK but leaveGame not working?");}

        //A player left the game. The notification message should include the player’s name
         MessageNotification message = new MessageNotification(username + " has left the game");
         connections.broadcast(gameID, session, message); //tell other users

    }

    public void resignGame(int gameID) throws DataAccessException, ResponseException, java.io.IOException{
        MessageNotification message = new MessageNotification(username + " has resigned and the game is over");
        connections.broadcast(gameID, session, message); //tell ALL
        connections.sendToSession(session, message);

        boolean success = getGameDB().deleteByGameID(gameID); //remove from database
        connections.removeGameConnections(gameID); //remove all their connections

        //TODO: how can we exit players from the game? I guess they could leave
    }
}

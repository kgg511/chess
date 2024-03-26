package service;

import dataAccess.DataAccessException;
import exception.ResponseException;
import chess.*;
import model.*;
import webSocketMessages.serverMessages.LoadGameNotification;
import webSocketMessages.serverMessages.MessageNotification;
import webSocketServer.GameConnectionManager;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class GameService extends BaseService {
    private GameConnectionManager connections;
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
    public void joinPlayer(int gameID, ChessGame.TeamColor color, Session session) throws ResponseException, DataAccessException, java.io.IOException{
        //verify this person was added to db as this color
        GameData data = getGameDB().getGameById(gameID);
        if (data == null){throw new ResponseException(400, "Invalid Game ID");}
        else if(color == WHITE && !username.equals(data.whiteUsername())){throw new ResponseException(400, "Color taken by different user");}
        else if(color == BLACK && !username.equals(data.blackUsername())){throw new ResponseException(400, "Color taken by different user");}
        // Server sends a Notification message to all other clients about what color they joined as
        connections.addConnection(gameID, authToken, session); //add the players websocket connection

        LoadGameNotification message = new LoadGameNotification(data.game()); //for sender
        connections.sendToSession(session, message); //send load game back to client

        MessageNotification notification = new MessageNotification(username + " has joined as" + color);
        connections.broadcast(gameID, session, notification); //send notification back to everyone else
    }
    public void joinObserver(int gameID) throws ResponseException, DataAccessException, IOException{
        GameData data = getGameDB().getGameById(gameID);
        if (data == null){throw new ResponseException(400, "Invalid Game ID");}
        ChessGame game = data.game();
        connections.addConnection(gameID, authToken, session); //add the players websocket connection
        LoadGameNotification message = new LoadGameNotification(game); //for sender
        connections.sendToSession(session, message); //send load game back to client

        MessageNotification notification = new MessageNotification(username + " has joined as an observer");
        connections.broadcast(gameID, session, notification);
    }

    private MessageNotification doneCases(int gameID, ChessGame game, ChessGame.TeamColor colorOpposing) throws java.io.IOException{
        //after each move check if either side is in stalemate
        //you can't move yourself into check/checkmate so check opposing after move
        MessageNotification msg = null;
        if(game.isInCheckmate(colorOpposing)){
            msg = new MessageNotification(colorOpposing + " is in checkmate! " + colorOpposing + " LOSES. Type 'Leave' to return to lobby.");
        }
        else if(game.checkStale(WHITE) || game.checkStale(ChessGame.TeamColor.BLACK)){
            msg = new MessageNotification("Stalemate. No one wins.");
        }
        return msg;
    }
    public void makeMove(int gameID, ChessMove move) throws dataAccess.DataAccessException, exception.ResponseException, InvalidMoveException, IOException {
        GameData data = getVerifyGame(gameID);
        ChessGame game = data.game();
        ChessGame.TeamColor colorOpposing;

        if(data.whiteUsername().equals(username)){colorOpposing = BLACK;}
        else if(data.blackUsername().equals(username)){colorOpposing = WHITE;}
        else{throw new ResponseException(400, "you are not a player how did you get here");}

        //verify move
        if(game.getBoard().getPiece(move.getStartPosition()).getTeamColor() == colorOpposing){throw new ResponseException(400, "You can't move your opponent's pieces!");}

        game.makeMove(move);
        GameData updated = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), game);
        getGameDB().updateGame(updated);

        //load game to all (including client)
        LoadGameNotification message = new LoadGameNotification(game);
        connections.sendToSession(session, message);
        connections.broadcast(gameID, session, message);

        //Notification to all but the client that move has been made
        String moveMessage = username + " made the move " + move + "\n";

        MessageNotification notification = new MessageNotification(moveMessage);
        connections.broadcast(gameID, session, notification);

        //check if game is over
        MessageNotification msg = doneCases(gameID, game, colorOpposing); //check stale/checkmate
        if(msg != null){
            endGame(gameID, msg);
        }

        //see if it's just check
        if(game.isInCheck(colorOpposing)){
            MessageNotification check = new MessageNotification(colorOpposing + " is in check!");
            connections.broadcast(gameID, session, check); //tell ALL
            connections.sendToSession(session, check);
        }
    }

    public void leaveGame(int gameID) throws DataAccessException, ResponseException, java.io.IOException{
        connections.removeConnection(gameID, authToken); //remove WS connection
        GameData old = getVerifyGame(gameID);
        //remove user from db
        GameData updated = null;
        boolean success = true;
        if(old.whiteUsername() != null && old.whiteUsername().equals(username)){
            success = getGameDB().updateGame(new GameData(old.gameID(), null, old.blackUsername(), old.gameName(), old.game()));}
        else if(old.blackUsername() != null && old.blackUsername().equals(username)){
            success = getGameDB().updateGame(new GameData(old.gameID(), old.whiteUsername(), null, old.gameName(), old.game()));}
        if(!success){throw new ResponseException(400, "Could not leave game");}

        //A player left the game. The notification message should include the player’s name
         MessageNotification message = new MessageNotification(username + " has left the game");
         connections.broadcast(gameID, session, message); //tell other users
    }


    public GameData getVerifyGame(int gameID) throws ResponseException, DataAccessException{
        GameData data = getGameDB().getGameById(gameID);
        if(data == null){throw new ResponseException(400,"GameID not associated with a game");}
        return data;
    }
    public void verifyPlayer(int gameID) throws ResponseException, DataAccessException{
        GameData data = getVerifyGame(gameID);
        if(!data.whiteUsername().equals(username) && !data.blackUsername().equals(username)){
            throw new ResponseException(400, "Observers cannot perform this action");
        }
    }

    public void endGame(int gameID, MessageNotification msg) throws DataAccessException, ResponseException, java.io.IOException{
        connections.broadcast(gameID, session, msg); //tell ALL
        connections.sendToSession(session, msg);

        boolean success = getGameDB().deleteByGameID(gameID); //remove game from database
        connections.removeGameConnections(gameID); //remove all their connections
    }

    public void resignGame(int gameID) throws DataAccessException, ResponseException, java.io.IOException{
        //verifies game exists & verifies that person is a player
        verifyPlayer(gameID);

        //resigning game actions: tell all, clean up
        MessageNotification message = new MessageNotification(username + " has resigned and the game is over");
        endGame(gameID, message);
    }
}

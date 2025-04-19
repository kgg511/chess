package webSocketServer;

import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.IOException;
import chess.InvalidMoveException;
import webSocketMessages.serverMessages.ErrorNotification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import service.GameService;

@WebSocket
public class WebSocketHandler {
    private GameConnectionManager connections = new GameConnectionManager();
    private Session session = null;
    private GameService service = null;
    private ServerMessage msg = null; //used for error handling

    @OnWebSocketConnect
    public void onConnect(Session session) {
        // Store the session object for future use
        this.session = session;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try{
            this.session=session;
            UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class); //joinPlayerCommand, etc
            String authToken = cmd.getAuthString();
            service = new GameService(connections, session, authToken);

            if(cmd ==null){ System.out.println("what..you didn't pass in a command");}
            UserGameCommand.CommandType commandType = cmd.getCommandType();
            switch (commandType) {
                case UserGameCommand.CommandType.JOIN_PLAYER:
                    JoinPlayerCommand join = new Gson().fromJson(message, JoinPlayerCommand.class);
                    joinPlayer(join.getGameID(), join.getPlayerColor(), session);
                    break;
                case UserGameCommand.CommandType.JOIN_OBSERVER:
                    JoinObserverCommand observe = new Gson().fromJson(message, JoinObserverCommand.class);
                    joinObserver(observe.getGameID(), session);
                    break;
                case UserGameCommand.CommandType.MAKE_MOVE:
                    MakeMoveCommand move = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(move.getGameID(), move.getMove());
                    break;
                case UserGameCommand.CommandType.LEAVE:
                    LeaveCommand leaveC = new Gson().fromJson(message, LeaveCommand.class);
                    leave(leaveC.getGameID());
                    break;
                case UserGameCommand.CommandType.RESIGN:
                    ResignCommand resignC = new Gson().fromJson(message, ResignCommand.class);
                    resign(resignC.getGameID());
                    break;
            }
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("500 server onMessage DB exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
        catch(ResponseException e){
            msg = new ErrorNotification(e.statusCode() + " server onMessage exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
    }

    private void joinPlayer(int gid, chess.ChessGame.TeamColor playerColor, Session session) throws java.io.IOException {
        try{
            service.joinPlayer(gid, playerColor, session);
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("500 joinPlayer DB exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
        catch(ResponseException e){
            msg = new ErrorNotification(e.statusCode() + " joinPlayer exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
    }

    private void joinObserver(int gid, Session session) throws IOException{
        try{
            service.joinObserver(gid);
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("500 joinPlayer DB exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
        catch(ResponseException e){
            msg = new ErrorNotification(e.statusCode() + " joinObserver exception:" + e.toString());
            connections.sendToSession(session, msg);
        }

    }
    private void makeMove(int gid, ChessMove move) throws IOException{
        ServerMessage msg = null;
        try{service.makeMove(gid, move);}
        catch(InvalidMoveException e){
            msg = new ErrorNotification("Not a valid chess move:" + e.toString());
            connections.sendToSession(session, msg); //only tell user
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("500 makeMove DB exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
        catch(ResponseException e){
            msg = new ErrorNotification(e.statusCode() + " makeMove exception:" + e.toString());
            connections.sendToSession(session, msg);
        }

    }

    private void leave(int gid) throws IOException{
        try{
            service.leaveGame(gid);
            //remove the session stuff
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("500 leave DB exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
        catch(ResponseException e){
            msg = new ErrorNotification(e.statusCode() + " leave exception:" + e.toString());
            connections.sendToSession(session, msg);
        }

    }
    private void resign(int gid) throws java.io.IOException{
        try{
            service.resignGame(gid);
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("500 leave DB exception:" + e.toString());
            connections.sendToSession(session, msg);
        }
        catch(ResponseException e){
            msg = new ErrorNotification(e.statusCode() + " leave exception:" + e.toString());
            connections.sendToSession(session, msg);
        }

    }

}

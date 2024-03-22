package webSocketServer;

import Response.ExceptionResponse;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.Server;
import service.JoinGameService;

import java.io.IOException;
import java.util.Timer;
import chess.InvalidMoveException;
import spark.Request;
import spark.Response;
import webSocketMessages.serverMessages.ErrorNotification;
import webSocketMessages.serverMessages.LoadGameNotification;
import webSocketMessages.serverMessages.MessageNotification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketServer.*; //whyd i have to impor this
import webSocketMessages.userCommands.*;
import service.GameService;

import javax.xml.crypto.Data;

//how websocket takes client messages and then sends back different messages to the appropriate clients
@WebSocket
public class WebSocketHandler {
    private final GameConnectionManager connections = new GameConnectionManager();
    private Session session = null;
    private GameService service = null;
            //new GameService(GameConnectionManager connections, sender, authToken);
    //Actions typically refer to messages or events sent from the client to the server.
    //this should send ACTIONS, aka usergame commands
    //Notifications, on the other hand, usually describe messages or events sent from the server to the client.

    @OnWebSocketConnect
    public void onConnect(Session session){
        //hmmmmm add connection?
        //websocket created when they join a game
        //it would need to add them to the game...oh i could do that here?

        //or, set other stuff?
    }


    @OnWebSocketClose
    public void onClose(Session session){
        //uhh delete from session?
        //IDK
    }


    @OnWebSocketError
    public void onError(Session session){
        //maybe it sends an error?
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String authToken, String message) throws ResponseException, IOException {
        try{
            session = session;
            service = new GameService(connections, session, authToken);
            UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class); //joinPlayerCommand, etc
            if(cmd ==null){ System.out.println("what");}
            UserGameCommand.CommandType commandType = cmd.getCommandType();
            switch (commandType) {
                case UserGameCommand.CommandType.JOIN_PLAYER:
                    JoinPlayerCommand join = (JoinPlayerCommand) cmd;
                    joinPlayer(join.getGameID(), join.getAuthString(), join.getColor(), session);
                    break;
                case UserGameCommand.CommandType.JOIN_OBSERVER:
                    JoinObserverCommand observe = (JoinObserverCommand) cmd;
                    joinObserver(observe.getGameID(), observe.getAuthString(), session);
                    break;
                case UserGameCommand.CommandType.MAKE_MOVE:
                    MakeMoveCommand move = (MakeMoveCommand) cmd;
                    makeMove(move.getGameID(), move.getMove());
                    break;
                case UserGameCommand.CommandType.LEAVE:
                    LeaveCommand leaveC = (LeaveCommand) cmd;
                    leave(leaveC.getGameID());
                    break;
                case UserGameCommand.CommandType.RESIGN:
                    ResignCommand resignC = (ResignCommand) cmd;
                    resign(resignC.getGameID());
                    break;
            }
        }

        catch (DataAccessException e){

        }
    }

    private void convertAndCall(UserGameCommand cmd, object type, ){

    }


    //we receive ACTIONS from the client, and then we call methods which broadcast notifications
    //Integer gameID, ChessGame.TeamColor playerColor
    private void joinPlayer(int gid, String authToken, chess.ChessGame.TeamColor playerColor, Session session) throws ResponseException, java.io.IOException {
        //Server sends a LOAD_GAME message back to the root client.
        // Server sends a Notification message to all other clients in that game informing them what color
        //the root client is joining as.
        ServerMessage msg = null;
        try{
            service.joinPlayer(gid, playerColor);
        }
        catch(DataAccessException e){
            msg = new ErrorNotification("joinPlayer 500 DB exception:" + e.toString());
            connections.sendToSession(session, msg);
            connections.broadcast(gid, session, msg);
        }



    }

    private void joinObserver(int gid, String authToken, Session session) throws IOException{
        try{
            service.joinObserver(gid);
        }

    }
    private void makeMove(int gid, String move) throws IOException, ResponseException{
        ServerMessage msg = null;
        try{service.makeMove(gid, move);}
        catch(InvalidMoveException e){
            msg = new ErrorNotification("Not a valid chess move:" + e.toString());
            connections.sendToSession(session, msg); //only tell user
        }
        catch(ResponseException e){

        }

    }

    private void sendError(String message) throws IOException {
        String msg = new ErrorNotification(message);
        connections.sendToSession(session, msg); //only tell user


    }


    private void leave(int gid){
        service.leaveGame(gid);

    }
    private void resign(int gid){

    }

}

package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataaccess.DataAccess;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import service.JoinGameService;

import java.io.IOException;
import java.util.Timer;

import webSocketMessages.serverMessages.LoadGameNotification;
import webSocketMessages.serverMessages.MessageNotification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketServer.*; //whyd i have to impor this
import webSocketMessages.userCommands.*;

//how websocket takes client messages and then sends back different messages to the appropriate clients
@WebSocket
public class WebSocketHandler {
    private final GameConnectionManager connections = new GameConnectionManager();
    private Session session = null;
    //Actions typically refer to messages or events sent from the client to the server.
    //this should send ACTIONS, aka usergame commands
    //Notifications, on the other hand, usually describe messages or events sent from the server to the client.
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        session = session;
        UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class); //joinPlayerCommand, etc
        if(cmd ==null){ System.out.println("what");}
        UserGameCommand.CommandType commandType = cmd.getCommandType();
        switch (commandType) { //enter(action.visitorName(), session)
            case UserGameCommand.CommandType.JOIN_PLAYER -> joinPlayer(cmd.playerName, session); //these are ACTIONs
            case UserGameCommand.CommandType.JOIN_OBSERVER -> joinObserver();
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove();
            case UserGameCommand.CommandType.LEAVE -> leave();
            case UserGameCommand.CommandType.RESIGN -> resign();
        }
    }

    //we receive ACTIONS from the client, and then we call methods which broadcast notifications
    //Integer gameID, ChessGame.TeamColor playerColor
    private void joinPlayer(int gid, String authToken, chess.ChessGame.TeamColor playerColor, Session session) throws IOException {
        //Server sends a LOAD_GAME message back to the root client.
        // Server sends a Notification message to all other clients in that game informing them what color
        //the root client is joining as.
        //they'll just send the authToken using their request

        connections.addConnection(gid, authToken, session); //add the players websocket connection
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME); //for sender

        ServerMessage notification = new MessageNotification("Player has joined as" + playerColor);

        connections.sendToSession(session, message); //send load game back to client
        connections.broadcast(gid, session, message); //send notification back to everyone else

    }
    private void joinObserver(int gid, String authToken, Session session) throws IOException{
        //Integer gameID
        //Integer gameID, ChessMove move
        connections.addConnection(gid, authToken, session); //add the players websocket connection
        ServerMessage message = new LoadGameNotification(); //for sender
        //how would i have the game..use gid to get it from the database
        //

        ServerMessage notification = new MessageNotification("Player has joined as an observer");

        connections.sendToSession(session, message); //send load game back to client
        connections.broadcast(gid, session, message); //send notification back to everyone else
    }
    private void makeMove(int gid, ChessMove move) throws IOException{
//        Server verifies the validity of the move.
//                Game is updated to represent the move. Game is updated in the database.
//        Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
//        Server sends a Notification message to all other clients in that game informing them what move was made.

        //yeah so it needs to update the UI for the other player hmpgh

    }
    private void leave(int gid) throws IOException{

    }
    private void resign(int gid) throws IOException{

    }





//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }


}

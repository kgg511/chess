package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import webSocketMessages.Action;
import webSocketMessages.Notification;

import java.io.IOException;
import java.util.Timer;

import webSocketMessages.serverMessages.ServerMessage;
import webSocketServer.*; //whyd i have to impor this
import webSocketMessages.userCommands.*;

//how websocket takes client messages and then sends back different messages to the appropriate clients
@WebSocket
public class WebSocketHandler {
    private final GameConnectionManager connections = new GameConnectionManager();
    //Actions typically refer to messages or events sent from the client to the server.
    //this should send ACTIONS, aka usergame commands
    //Notifications, on the other hand, usually describe messages or events sent from the server to the client.
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
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
    private void joinPlayer(int gid, String playerName, Session session) throws IOException{
        //session is our sender, playerName for uI?
        //Integer gameID, ChessGame.TeamColor playerColor
        connections.addConnection(gid, session); //add the players websocket connection
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME); //for sender

        Server
        connections.broadcast(gid, session, message); //load_game
        //(int gid, Session senderSession, Notification notification)

//        Server sends a LOAD_GAME message back to the root client.
//        Server sends a Notification message to all other clients in that game informing them what color
//        the root client is joining as.
    }
    private void joinObserver() throws IOException{
        //Integer gameID
        //Integer gameID, ChessMove move

    }
    private void makeMove() throws IOException{}
    private void leave() throws IOException{}
    private void resign() throws IOException{}





//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }


}

package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.Action;
import webSocketMessages.Notification;

import java.io.IOException;
import java.util.Timer;
import webSocketServer.*; //whyd i have to impor this
import webSocketMessages.userCommands.*;
@WebSocket
public class WebSocketHandler {
    //JOIN_PLAYER,
    //        JOIN_OBSERVER,
    //        MAKE_MOVE,
    //        LEAVE,
    //        RESIGN
    private final GameConnectionManager connections = new GameConnectionManager();
    //Actions typically refer to messages or events sent from the client to the server.
    //this should send ACTIONS, aka usergame commands
    //Notifications, on the other hand, usually describe messages or events sent from the server to the client.
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Action action = new Gson().fromJson(message, Action.class);
        switch (action.type()) { //enter(action.visitorName(), session)
            case UserGameCommand.CommandType.JOIN_PLAYER -> joinPlayer(action.playerName, session); //these are ACTIONs
            case UserGameCommand.CommandType.JOIN_OBSERVER -> joinObserver();
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove();
            case UserGameCommand.CommandType.LEAVE -> leave();
            case UserGameCommand.CommandType.RESIGN -> resign();
        }
    }

    //we receive ACTIONS from the client, and then we call methods which broadcast notifications
    private void joinPlayer(String playerName, Session session) throws IOException{
        //session is our sender, playerName for uI?
    }
    private void joinObserver() throws IOException{}
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

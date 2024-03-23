package clientStuff.webSocketClient;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint{
    Session session;
    DoMessage doMessage = new DoMessage();
    //we don't return strings, we send the messages to websocket then when we hear a response back
    //the repsonse is the output
    public WebSocketCommunicator(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer(); //create websocket
            this.session = container.connectToServer(this, socketURI); //connect to websocket
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()) {
                        case LOAD_GAME:
                            //convert to load game, use the game
                            LoadGameNotification load = (LoadGameNotification) msg;
                            doMessage.drawGame(load.getMessage());
                        case NOTIFICATION:
                            MessageNotification notification = (MessageNotification) msg;
                            doMessage.messageUser(notification.getMessage());
                        case ERROR:
                            //IDK unpack the error and print it to the console I guess
                            ErrorNotification error = (ErrorNotification) msg;
                            doMessage.giveError(error.getMessage());
                    }
                }
            });
        }
        catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        // Implement your logic when the WebSocket connection is closed
        System.out.println("WebSocket connection closed.");
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("WebSocket connection opened.");
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException{
        try{
            LeaveCommand cmd = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws ResponseException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        try{
            JoinPlayerCommand cmd = new JoinPlayerCommand(authToken, gameID, playerColor); //send JOIN_PLAYER
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws ResponseException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        try{
            JoinObserverCommand cmd = new JoinObserverCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void makeMove(String authToken, int gameID, String move) throws ResponseException{ //actually send to websocket
        try{
            MakeMoveCommand cmd = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void resignGame(String authToken, int gameID) throws ResponseException{
        try{
            ResignCommand cmd = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

    }
}

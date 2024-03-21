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
import webSocketMessages.userCommands.*;

//only send messages to the server
public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String>{
    Session session;
    //TODO: put each function in a try except so we always throw REPSONSEEXCEPTIONS
    //NotificationHandler notificationHandler;//notification handler is just to print ws output differently
    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer(); //create websocket
            this.session = container.connectToServer(this, socketURI); //connect to websocket
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                //function called when the client sends a message
                @Override
                public void onMessage(String message) { //
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    //notify
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        // Implement your logic when the WebSocket connection is closed
        System.out.println("WebSocket connection closed.");
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leaveGame(String authToken) throws java.io.IOException{

        this.session.getBasicRemote().sendText(authToken + " left the game.");
        //sends WS stuff
        //the server will

    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws java.io.IOException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        JoinPlayerCommand cmd = new JoinPlayerCommand(authToken, gameID, playerColor); //send JOIN_PLAYER
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    }

    public void joinObserver(String authToken, int gameID) throws java.io.IOException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        JoinObserverCommand cmd = new JoinObserverCommand(authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    }

    private String makeMove(String authToken, int gameID, String move) throws java.io.IOException{ //actually send to websocket
        MakeMoveCommand cmd = new MakeMoveCommand(authToken, gameID, move);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        return authToken + " made the move " + move;

    } //TODO: is this chessmove object?

    private String resignGame(String authToken, int gameID) throws java.io.IOException{
        ResignCommand cmd = new ResignCommand(authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        return authToken + " left the game";

    }

//    public void enterPetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.ENTER, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

//    public void leavePetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.EXIT, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//            this.session.close();
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }


}

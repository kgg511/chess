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

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String>{
    Session session;
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

    public void leaveGame(){

        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        //sends WS stuff

    }

    public void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws java.io.IOException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        JoinPlayerCommand cmd = new JoinPlayerCommand(authToken, gameID, playerColor);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        //IS there a response?? All it does is send messages
    }

    public void joinObserver(String authToken, Integer gameID) throws java.io.IOException{ //this sends the message to the websocket
        //yes this is in loggedinclient but it uses WS so it goes here
        JoinPlayerCommand cmd = new JoinPlayerCommand(authToken, gameID, playerColor);
        this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        //IS there a response?? All it does is send messages
    }

    private String makeMove(){ //actually send to websocket
        return "";

    } //TODO: is this chessmove object?

    private String resignGame(){
        return "";

    }

    private String highlightLegal(){
        return "";

    }

    public void enterPetShop(String visitorName) throws ResponseException {
        try {
            var action = new Action(Action.Type.ENTER, visitorName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leavePetShop(String visitorName) throws ResponseException {
        try {
            var action = new Action(Action.Type.EXIT, visitorName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


}
